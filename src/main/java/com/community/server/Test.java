package com.community.server;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("resources/skins/default.png"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] bytes = baos.toByteArray();
        System.out.println();
    }
}
