package com.baogutang.frame.uuid.worker.dao;

import com.baogutang.frame.uuid.worker.entity.WorkerNodeEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WorkerNodeDao extends BaseMapper<WorkerNodeEntity> {


    /**
     * Get {@link WorkerNodeEntity} by node host
     *
     * @param host
     * @param port
     * @return
     */
    WorkerNodeEntity getWorkerNodeByHostPort(@Param("host") String host, @Param("port") String port);

    /**
     * Add {@link WorkerNodeEntity}
     *
     * @param workerNodeEntity
     */
    void addWorkerNode(WorkerNodeEntity workerNodeEntity);
}
