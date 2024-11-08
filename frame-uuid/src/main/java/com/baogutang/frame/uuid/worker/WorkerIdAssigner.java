package com.baogutang.frame.uuid.worker;

import com.baogutang.frame.uuid.impl.DefaultUidGenerator;

/**
 * Represents a worker id assigner for {@link DefaultUidGenerator}
 * 
 * @author yutianbao
 */
public interface WorkerIdAssigner {

    /**
     * Assign worker id for {@link DefaultUidGenerator}
     * 
     * @return assigned worker id
     */
    long assignWorkerId();

}
