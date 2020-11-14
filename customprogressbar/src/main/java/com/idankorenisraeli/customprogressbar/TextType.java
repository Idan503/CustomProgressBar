package com.idankorenisraeli.customprogressbar;

/**
 * The text of the custom bar can either show data about current bar progression
 * or it can show a custom title that can be set in xml
 */
public enum TextType {
    STATIC, // A custom static string set by "title" attribute
    PERCENTAGE, // Current bar percentage between 0% and 100%
    DECIMAL, // Current bar value between 0.00 to 1.00
}
