package com.baogutang.frame.uuid.worker;

import com.baogutang.frame.uuid.utils.ValuedEnum;

/**
 * WorkerNodeType
 * <li>CONTAINER: Such as Docker
 * <li>ACTUAL: Actual machine
 * 
 * @author N1KO
 */
public enum WorkerNodeType implements ValuedEnum<Integer> {

    CONTAINER(1), ACTUAL(2);

    /**
     * Lock type
     */
    private final Integer type;

    /**
     * Constructor with field of type
     */
    private WorkerNodeType(Integer type) {
        this.type = type;
    }

    @Override
    public Integer value() {
        return type;
    }

}
