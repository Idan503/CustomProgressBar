package com.idankorenisraeli.customprogressbar;

public enum ColorType {
    SINGLE_STATIC, // A Single color that doesn't change, applied by staticColor attribute
    SINGLE_DYNAMIC, // A Single color that changes depending on % of bar, changes by applied gradient
    GRADIENT // Applied by colorStart, colorCenter (optional), and colorEnd
}
