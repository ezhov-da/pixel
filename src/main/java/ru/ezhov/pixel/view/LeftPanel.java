package ru.ezhov.pixel.view;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

public class LeftPanel  extends JPanel {
    private File selectedFile = null;
    private Pixeler pixeler;

    public LeftPanel(Pixeler pixeler) {
        this.pixeler = pixeler;

        setLayout(new BorderLayout());

        ImagePanel imagePanel = new ImagePanel();

        JButton open = new JButton("Open");

        open.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int i = fileChooser.showOpenDialog(this);
            if (i == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                selectedFile = file;
                try {
                    imagePanel.setBufferedImage(ImageIO.read(file));

                    LeftPanel.this.repaint();
                    LeftPanel.this.revalidate();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        add(open, BorderLayout.NORTH);


        add(imagePanel, BorderLayout.CENTER);


        JSpinner spinner = new JSpinner(new SpinnerNumberModel(20, 1, 500, 5));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(spinner, BorderLayout.CENTER);

        JButton doButton = new JButton("Create");
        panel.add(doButton, BorderLayout.EAST);

        doButton.addActionListener(e -> {
            if (selectedFile != null) {
                pixeler.toPixel(selectedFile, (Integer) spinner.getModel().getValue());
            }
        });

        add(panel, BorderLayout.SOUTH);
    }
}