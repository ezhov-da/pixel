package ru.ezhov.pixel.view;

import ru.ezhov.pixel.domain.NumberService;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        removePixels();

        if (this.paintPixels) {
            paintPixels();
        } else {
            removePixels();
        }

    }

    public void paintNotWhitePixelTo() {
        pixels.forEach(Pixel::fillBlack);

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

    public void changeFontSize(int size) {
        pixels.forEach(p -> {
            Font font = p.getFont();
            Font newFonr = new Font(font.getName(), font.getStyle(), size);
            p.setFont(newFonr);
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
    }

    private void paintImage(Graphics2D graphics2D) {
        if (bufferedImage != null) {
            graphics2D.drawImage(bufferedImage, 0, 0, null);
        }
    }

    private void paintPixels() {
        if (bufferedImage != null) {
            pixels.forEach(this::add);

            revalidate();
            repaint();
        }
    }

    private void removePixels() {
        removeAll();

        revalidate();
        repaint();
    }

    private void createPixels() {
        if (bufferedImage != null) {
            pixels.clear();

            int counterY = 0;

            while (bufferedImage.getHeight() > counterY) {
                int counterX = 0;
                while (bufferedImage.getWidth() > counterX) {

                    int color = bufferedImage.getRGB(counterX + 1, counterY + 1);


                    Pixel label = new Pixel(counterX, counterY, pixelSize, pixelSize, new Color(color));
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseReleased(MouseEvent e) {
                            label.paintPixel();
                            PixelImagePanel.this.repaint();
                        }
                    });

                    pixels.add(label);

                    counterX += pixelSize;
                    maxWidth = counterX;
                }
                counterY += pixelSize;
                maxHeight = counterY;

            }
        }
    }

    public Optional<File> saveImage() throws IOException {
        if (bufferedImage != null) {
            int w = maxWidth;
            int h = maxHeight;
            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bi.createGraphics();

            RenderingHints rh = new RenderingHints(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            rh.add(new RenderingHints(
                    RenderingHints.KEY_ALPHA_INTERPOLATION,
                    RenderingHints.VALUE_COLOR_RENDER_QUALITY));
            rh.add(new RenderingHints(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY));
            g.setRenderingHints(rh);

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
        private final Color originalColor;
        private Color newColor;
        private String text;

        public Pixel(int x,
                     int y,
                     int w,
                     int h,
                     Color originalColor
        ) {
            this.x = x;
            this.y = y;
            this.h = h;
            this.w = w;
            this.originalColor = originalColor;

            initText(this.originalColor);

            setBackground(Color.white);
            setHorizontalAlignment(SwingConstants.CENTER);
            setBounds(x, y, w, h);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }

        private void initText(Color color) {
            NumberService numberService = new NumberService();
            if (Color.WHITE.equals(color)) {
                text = numberService.odd() + "";
            } else {
                text = numberService.even() + "";
            }
            setText(text);
        }

        public void paintPixel() {
            if (newColor == null) {
                newColor = Color.BLACK;
            } else if (Color.WHITE.equals(newColor)) {
                newColor = Color.BLACK;
            } else {
                newColor = Color.WHITE;
            }

            paintNewColor();
        }

        private void paintNewColor() {
            initText(newColor);

            setOpaque(true);
            setBackground(newColor);
            repaint();
        }

        private void fillBlack() {
            if (!Color.WHITE.equals(this.originalColor)) {
                this.newColor = Color.BLACK;
                paintNewColor();
            }
        }
    }

}
