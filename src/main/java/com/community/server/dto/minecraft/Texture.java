package com.community.server.dto.minecraft;

import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
