package com.baogutang.frame.uuid.buffer;

import java.util.List;

/**
 * Buffered UID provider(Lambda supported), which provides UID in the same one second
 *
 * @author N1KO
 */
@FunctionalInterface
public interface BufferedUidProvider {

    /**
     * Provides UID in one second
     *
     * @param momentInSecond
     * @return
     */
    List<Long> provide(long momentInSecond);
}
