package com.baogutang.frame.auth.common.header;

/**
 * @author N1KO
 */
public enum Scope {
    /**
     * scope
     */
    REGISTER(1),
    CALL(2),
    ANY(3);

    public final int flag;

    Scope(int flag) {
        this.flag = flag;
    }


}
