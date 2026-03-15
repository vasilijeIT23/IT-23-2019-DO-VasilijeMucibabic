package app.drawing.dialogs;

import app.drawing.components.ColorPickerRow;

import javax.swing.*;
import java.awt.*;

public class DlgRect {
    public static RectParams ask(Window owner) {
        JTextField tfW = new JTextField(8);
        JTextField tfH = new JTextField(8);

        ColorPickerRow borderRow = new ColorPickerRow("Choose color", Color.BLACK);
        ColorPickerRow innerRow  = new ColorPickerRow("Choose color", Color.BLACK);

        JPanel p = new JPanel(new GridLayout(4, 2, 6, 6));
        p.add(new JLabel("Width:"));
        p.add(tfW);
        p.add(new JLabel("Height:"));
        p.add(tfH);

        p.add(new JLabel("Border Color:"));
        p.add(borderRow);

        p.add(new JLabel("Inner Color:"));
        p.add(innerRow);


        while (true) {
            int res = JOptionPane.showConfirmDialog(
                    owner, p, "Rectangle size", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (res != JOptionPane.OK_OPTION) return null;

            try {
                int width = Integer.parseInt(tfW.getText().trim());
                int height = Integer.parseInt(tfH.getText().trim());
                if (width <= 0 || height <= 0) throw new NumberFormatException();
                return new RectParams(width, height, borderRow.getColor(), innerRow.getColor());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(owner, "Width/Height must be positive integers.", "Validation",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    public record RectParams(int width, int height, Color borderColor, Color innerColor) {}
}
