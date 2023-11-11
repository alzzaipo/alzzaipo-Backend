package com.alzzaipo.common.util;

import com.alzzaipo.common.Uid;
import com.github.f4b6a3.tsid.TsidCreator;

public class UidGenerator {

    public static Uid generate() {
        return new Uid(TsidCreator.getTsid().toLong());
    }
}
