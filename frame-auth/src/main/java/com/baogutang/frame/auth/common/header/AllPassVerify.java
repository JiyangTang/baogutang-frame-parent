package com.baogutang.frame.auth.common.header;

public class AllPassVerify implements Verify {

    public static final AllPassVerify INSTANCE = new AllPassVerify();

    @Override
    public int validate(String value) {
        return 0;
    }
}
