package com.baogutang.frame.auth.common.header;

/**
 * @author N1KO
 */
@FunctionalInterface
public interface Verify {
    /**
     * validate
     * @param value value
     * @return res
     */
    int validate(final String value);
}
