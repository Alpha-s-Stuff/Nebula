package me.alphamode.nebula.utils;

import me.alphamode.nebula.NebulaTabs;

public @interface RegistryInfo {
    NebulaTabs tab() default NebulaTabs.MISC;
}
