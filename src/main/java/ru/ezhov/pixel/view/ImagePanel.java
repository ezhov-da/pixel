package ru.ezhov.pixel.view;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage bufferedImage = null;

    public ImagePanel() {
        setLayout(null);
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics2D = (Graphics2D) g;

        if (bufferedImage != null) {
            graphics2D.drawImage(bufferedImage, 0, 0, null);
        }
    }
}