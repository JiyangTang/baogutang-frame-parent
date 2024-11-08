package com.baogutang.frame.uuid.utils;

/**
 * {@code ValuedEnum} defines an enumeration which is bounded to a value, you
 * may implements this interface when you defines such kind of enumeration, that
 * you can use {@link EnumUtils} to simplify parse and valueOf operation.
 *  
 * @author N1KO
 */
public interface ValuedEnum<T> {
    T value();
}
