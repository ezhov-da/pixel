package ru.ezhov.pixel.view;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class PixelFrame extends JFrame {
    private final RightPanel rightPanel = new RightPanel();
    private final LeftPanel leftPanel = new LeftPanel(rightPanel);

    public PixelFrame() {
//        rightPanel.setPreferredSize(new Dimension(500, 600));
//        leftPanel.setPreferredSize(new Dimension(500, 600));

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);

        splitPane.setDividerLocation(0.5);
        splitPane.setDividerLocation(500);

        add(splitPane, BorderLayout.CENTER);

        setSize(1000, 600);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
