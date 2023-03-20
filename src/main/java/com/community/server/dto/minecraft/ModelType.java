package com.community.server.dto.minecraft;

public enum ModelType {
    STEVE("default"),
    ALEX("slim"),
    CUSTOM("custom");

    private String modelName;

    ModelType(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }
}
