package com.community.server.dto.minecraft;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.Collections.singletonMap;
import static java.util.Optional.empty;
import static java.util.Optional.of;

public enum TextureType {
    SKIN(character -> of(singletonMap("model", character.getModel().getModelName()))),
    CAPE,
    ELYTRA;

    private Function<MinecraftUser, Optional<Map<?, ?>>> metadataFunc;

    TextureType() {
        this(dummy -> empty());
    }

    TextureType(Function<MinecraftUser, Optional<Map<?, ?>>> metadataFunc) {
        this.metadataFunc = metadataFunc;
    }

    public Optional<Map<?, ?>> getMetadata(MinecraftUser character) {
        return metadataFunc.apply(character);
    }
}
