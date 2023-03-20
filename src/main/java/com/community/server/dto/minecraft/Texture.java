package com.community.server.dto.minecraft;

import static java.util.Objects.requireNonNull;

public class Texture {

    public final String hash;
    public final byte[] data;
    public final String url;

    public Texture(String hash, byte[] data, String url) {
        this.hash = requireNonNull(hash);
        this.data = requireNonNull(data);
        this.url = requireNonNull(url);
    }
}
