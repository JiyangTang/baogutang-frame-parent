package com.baogutang.frame.auth.common.header;

public class AllFailVerify implements Verify {

    public static final AllFailVerify INSTANCE = new AllFailVerify();

    @Override
    public int validate(String value) {
        return ErrorCode.NA.NA.code();
    }
}
