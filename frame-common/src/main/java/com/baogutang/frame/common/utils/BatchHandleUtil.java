package com.baogutang.frame.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.stream.IntStream.range;

@Slf4j
public class BatchHandleUtil {
    public static final Integer PAGE_SIZE = 2000;

    public static Integer page(Integer count, Integer pageSize) {
        if (Objects.isNull(count) || count == 0) {
            return 0;
        }
        pageSize = Optional.ofNullable(pageSize).orElse(PAGE_SIZE);
        return count % pageSize == 0 ? count / pageSize : (count / pageSize) + 1;
    }

    /**
     * 功能描述
     *
     * @param count                  总条数
     * @param threadHandleCount      线程数量
     * @param threadPoolTaskExecutor 线程池
     * @param param                  查询条件参数
     * @param condOpr                条件整理
     * @param selectDate             查询数据
     * @param handleDate             处理数据
     * @return void
     * @author
     * @date
     */
    public static <T, P> void batchHandle(Integer count, Integer threadHandleCount, ThreadPoolTaskExecutor threadPoolTaskExecutor,
                                          P param, BiFunction<P, String, P> condOpr,
                                          Function<P, List<T>> selectDate, Consumer<List<T>> handleDate) {
        log.info("batchHandle count:{}, threadHandleCount:{}, maximumPoolSize:{}",
                count, threadHandleCount, threadPoolTaskExecutor.getMaxPoolSize());
        if (Objects.isNull(count) || count <= 0) {
            log.info("count Parameter error!");
            return;
        }
        int page = page(count, threadHandleCount);
        int realMaxPoolSize = Math.min(page, threadPoolTaskExecutor.getMaxPoolSize());
        threadPoolTaskExecutor.setMaxPoolSize(realMaxPoolSize);
        // 单个线程处理数
        int singeThreadSize = count / realMaxPoolSize;
        // 最后一个线程处理数
        int lastThreadSize = singeThreadSize + count % realMaxPoolSize;
        log.info("Processed by {} threads, number of processes by a single thread: {}, number of processes by the last thread:{}",
                realMaxPoolSize, singeThreadSize, lastThreadSize);
        range(0, realMaxPoolSize).forEach(i -> {
            threadPoolTaskExecutor.execute(() -> {
                // 开始的数据
                int start = singeThreadSize * i;
                // 条数
                int singleSize = Optional.of(i).filter(x -> x == realMaxPoolSize - 1)
                        .map(x -> lastThreadSize).orElse(singeThreadSize);
                String limit = "limit " + start + ", " + singleSize;
                P cond = condOpr.apply(param, limit);
                List<T> list = selectDate.apply(cond);
                if (!CollectionUtils.isEmpty(list)) {
                    handleDate.accept(list);
                }
                log.info("Thread {} execution ends, start: {}, number of threads:{}",
                        Thread.currentThread().getName(), start, singleSize);
            });
        });
    }

    public static void shutdown(ExecutorService executorService, long timeout, TimeUnit timeUnit) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(timeout, timeUnit)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(timeout, timeUnit)) {
                    log.info("The thread pool task did not end normally.");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
        }
    }
}
