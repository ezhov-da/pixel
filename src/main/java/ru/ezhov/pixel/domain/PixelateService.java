package ru.ezhov.pixel.domain;

import java.awt.image.BufferedImage;

public interface PixelateService {
    BufferedImage pixelate(BufferedImage imageToPixelate, int pixelSize);
}
