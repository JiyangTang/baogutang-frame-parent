package com.baogutang.frame.common.component;

import lombok.Getter;

/**
 * @param <F>
 * @param <S>
 * @author N1KO
 */
@Getter
public class Tuple2<F, S> {

    public F first;

    public S second;

    public Tuple2(F f, S s) {
        this.first = f;
        this.second = s;
    }

    public Tuple2() {
    }

    public Tuple2<F, S> first(F f) {
        this.first = f;
        return this;
    }

    public Tuple2<F, S> second(S s) {
        this.second = s;
        return this;
    }

    public static <F, S> Tuple2<F, S> of(F f, S s) {
        return new Tuple2<>(f, s);
    }

    public static <F, S> Tuple2<F, S> of() {
        return new Tuple2<>();
    }
}
