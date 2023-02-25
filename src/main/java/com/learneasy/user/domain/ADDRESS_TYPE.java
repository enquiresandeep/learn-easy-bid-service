package com.learneasy.user.domain;

public enum ADDRESS_TYPE {
    HOME("1"),
    BILLING("2"),
    SHIPPING("3");

    private final String value;

    ADDRESS_TYPE(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

