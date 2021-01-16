package ru.ezhov.pixel.view;

import ru.ezhov.pixel.domain.PixelService;
import ru.ezhov.pixel.infrastructure.CoderoadRuPixelayeService;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class RightPanel extends JPanel implements Pixeler {
    private PixelImagePanel pixelImagePanel = new PixelImagePanel();

    public RightPanel() {
        setLayout(new BorderLayout());

        add(pixelImagePanel, BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JCheckBox checkBoxImage = new JCheckBox("Paint image");

        checkBoxImage.addActionListener(e -> {
            pixelImagePanel.setPaintImage(checkBoxImage.isSelected());
        });

        JCheckBox checkBox = new JCheckBox("Paint pixel");
        checkBox.addActionListener(e -> {
            pixelImagePanel.setPaintPixels(checkBox.isSelected());
        });

        JCheckBox checkBoxPaintToBlack = new JCheckBox("Paint to black");
        checkBoxPaintToBlack.addActionListener(e -> {
            if (checkBoxPaintToBlack.isSelected()) {
                pixelImagePanel.paintNotWhitePixelTo();

            } else {
                pixelImagePanel.resetColor();
            }
        });

        JSpinner spinner = new JSpinner(new SpinnerNumberModel(new JLabel().getFont().getSize(), 5, 500, 1));
        spinner.addChangeListener(e -> {
            int val = (Integer) spinner.getModel().getValue();
            pixelImagePanel.changeFontSize(val);
        });

        JButton saveImage = new JButton("Save image");
        saveImage.addActionListener(e -> {
            try {
                final Optional<File> file = pixelImagePanel.saveImage();
                if (file.isPresent()) {
                    Desktop.getDesktop().open(file.get());
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        panel.add(checkBoxImage);
        panel.add(checkBox);
        panel.add(checkBoxPaintToBlack);
        panel.add(spinner);
        panel.add(saveImage);

        add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void toPixel(File file, int pixelSize) {
        PixelService pixelService = new PixelService(new CoderoadRuPixelayeService());
        try {
            final BufferedImage bufferedImage = pixelService.toPixel(file, pixelSize);
            pixelService.writeAsJpg(bufferedImage, "pixel.jpg");

            pixelImagePanel.setBufferedImage(bufferedImage, pixelSize);

            this.revalidate();
            this.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}