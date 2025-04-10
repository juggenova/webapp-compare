package com.juggenova.doodleclone.model;

public enum ChoiceEnum {
    NO,
    YES,
    MAYBE;

    /**
     * Returns ChoiceEnum values in display order
     */
    public static ChoiceEnum[] getSortedValues() {
        return new ChoiceEnum[] {YES, NO, MAYBE};
    }
}
