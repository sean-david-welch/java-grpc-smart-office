package client;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class StyledButtonUI extends BasicButtonUI {
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JButton button = (JButton) c;
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        JButton b = (JButton) c;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = b.getWidth();
        int height = b.getHeight();

        Shape border = new RoundRectangle2D.Float(0, 0, width, height, 40, 40);
        g2.setColor(b.getBackground());
        g2.fill(border);

        g2.setColor(b.getForeground());
        g2.setFont(b.getFont());
        FontMetrics fm = g2.getFontMetrics();
        Rectangle stringBounds = fm.getStringBounds(b.getText(), g2).getBounds();
        int textX = (width - stringBounds.width) / 2;
        int textY = (height - stringBounds.height) / 2 + fm.getAscent();
        g2.drawString(b.getText(), textX, textY);

        g2.dispose();
    }

        static JButton getjButton(String text) {
        JButton button = new JButton(text);
        button.setUI(new StyledButtonUI());
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 60, 60));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }
}
