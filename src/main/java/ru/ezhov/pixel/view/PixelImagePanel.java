package ru.ezhov.pixel.view;

import ru.ezhov.pixel.domain.NumberService;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PixelImagePanel extends JPanel {
    private int maxWidth = 0;
    private int maxHeight = 0;

    private boolean paintPixels = false;
    private boolean paintImage = true;

    private final List<Pixel> pixels = new ArrayList<>();

    private BufferedImage bufferedImage = null;
    private int pixelSize = 0;

    public PixelImagePanel() {
        setLayout(null);
        setBackground(Color.white);
    }

    public void setBufferedImage(BufferedImage bufferedImage, int pixelSize) {
        this.bufferedImage = bufferedImage;
        this.pixelSize = pixelSize;
        createPixels();
        lines();
    }

    public void setPaintPixels(boolean paintPixels) {
        this.paintPixels = paintPixels;
        lines();
    }

    public void setPaintImage(boolean paintImage) {
        this.paintImage = paintImage;
        repaint();
    }

    private void lines() {
        if (this.paintPixels) {
            paintPixels();
        } else {
            removePixels();
        }

    }

    public void paintNotWhitePixelTo(Color color) {
        pixels.forEach(p -> {
            if (!p.isWhiteColor()) {
                p.setOpaque(true);
                p.setBackground(color);
                p.repaint();
            }
        });

        repaint();
        revalidate();
    }

    public void resetColor() {
        pixels.forEach(p -> {
            p.setOpaque(false);
            p.setBackground(Color.white);
        });

        repaint();
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;

        if (paintImage) {
            paintImage(graphics2D);
        }
        paintLines(graphics2D);
    }

    private void paintImage(Graphics2D graphics2D) {
        if (bufferedImage != null) {
            graphics2D.drawImage(bufferedImage, 0, 0, null);
        }
    }

    private void paintLines(Graphics2D graphics2D) {
        if (bufferedImage != null) {
            int counter = pixelSize;

            while (bufferedImage.getWidth() > counter) {
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawLine(counter, 0, counter, bufferedImage.getHeight());

                maxWidth = counter;
                counter += pixelSize;
            }

            counter = pixelSize;
            while (bufferedImage.getHeight() > counter) {
                graphics2D.setColor(Color.BLACK);
                graphics2D.drawLine(0, counter, bufferedImage.getWidth(), counter);

                maxHeight = counter;
                counter += pixelSize;
            }
        }
    }

    private void paintPixels() {
        if (bufferedImage != null) {
            pixels.forEach(this::add);

            repaint();
            revalidate();
        }
    }

    private void removePixels() {
        removeAll();

        repaint();
        revalidate();
    }

    private void createPixels() {
        if (bufferedImage != null) {
            pixels.clear();

            NumberService numberService = new NumberService();

            int counterY = 0;

            while (bufferedImage.getHeight() > counterY) {
                int counterX = 0;
                while (bufferedImage.getWidth() > counterX) {

                    int color = bufferedImage.getRGB(counterX + 1, counterY + 1);

                    String name;
                    if (Color.WHITE.equals(new Color(color))) {
                        name = numberService.odd() + "";
                    } else {
                        name = numberService.even() + "";
                    }

                    Pixel label = new Pixel(counterX, counterY, pixelSize, pixelSize, name, new Color(color));

                    pixels.add(label);

                    counterX += pixelSize;
                }
                counterY += pixelSize;
            }
        }
    }

    public Optional<File> saveImage() throws IOException {
        if (bufferedImage != null) {
            int w = maxWidth;
            int h = maxHeight;
            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();
            this.paint(g);
            g.dispose();
            File f = new File("result.jpg");
            ImageIO.write(bi, "jpg", f);
            return Optional.of(f);
        }
        return Optional.empty();
    }

    private class Pixel extends JLabel {
        private final int x;
        private final int y;
        private final int w;
        private final int h;
        private final Color color;

        public Pixel(int x,
                     int y,
                     int w,
                     int h,
                     String text,
                     Color color
        ) {
            super(text);
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
            this.color = color;

            setBackground(Color.white);
            setHorizontalAlignment(SwingConstants.CENTER);
            setBounds(x, y, w, h);
        }

        public boolean isWhiteColor() {
            return Color.WHITE.equals(color);
        }
    }

}
