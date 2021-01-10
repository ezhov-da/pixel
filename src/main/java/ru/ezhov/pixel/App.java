package ru.ezhov.pixel;

import ru.ezhov.pixel.view.PixelFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Throwable ex) {
                //
            }
            PixelFrame pixelFrame = new PixelFrame();
            pixelFrame.setVisible(true);
        });
    }
}