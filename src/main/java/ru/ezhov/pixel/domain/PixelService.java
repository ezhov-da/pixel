package ru.ezhov.pixel.domain;

import ru.ezhov.pixel.domain.PixelateService;

import javax.imageio.ImageIO;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

public class PixelService {
    private final PixelateService pixelate;

    public PixelService(PixelateService pixelate) {
        this.pixelate = pixelate;
    }

    public BufferedImage toPixel(File file, int pixelSize) throws Exception {
        BufferedImage img = ImageIO.read(file);

        return toPixel(toBlack(img), pixelSize);
    }

    private BufferedImage toBlack(BufferedImage src) {
        BufferedImage imageBlack = new BufferedImage(
                src.getWidth(),
                src.getHeight(),
                src.getType()
        );
        ColorConvertOp op =
                new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(src, imageBlack);

        return imageBlack;
    }

    private BufferedImage toPixel(BufferedImage src, int pixSize) {
        return pixelate.pixelate(src, pixSize);
    }

    public void writeAsJpg(BufferedImage src, String name) throws IOException {
        ImageIO.write(src, "jpg", new File(name));
    }
}
