package app.drawing.dialogs;

import javax.swing.*;
import java.awt.*;

public class DlgDonut {
    public static DonutParams ask(Window owner) {
        JTextField tfR = new JTextField(8);
        JTextField tfIR = new JTextField(8);

        JPanel borderColorBlock = new JPanel();
        borderColorBlock.setPreferredSize(new Dimension(16, 16));
        borderColorBlock.setBackground(Color.BLACK);
        borderColorBlock.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        JPanel innerColorBlock = new JPanel();
        innerColorBlock.setPreferredSize(new Dimension(16, 16));
        innerColorBlock.setBackground(Color.BLACK);
        innerColorBlock.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        DlgColorPicker btnBorderColor = new DlgColorPicker("Choose color", Color.BLACK);
        DlgColorPicker btnInnerColor  = new DlgColorPicker("Choose color", Color.BLACK);

        // live updates
        borderColorBlock.setBackground(btnBorderColor.getColor());
        btnBorderColor.addPropertyChangeListener("color", evt ->
                borderColorBlock.setBackground((Color) evt.getNewValue())
        );

        innerColorBlock.setBackground(btnInnerColor.getColor());
        btnInnerColor.addPropertyChangeListener("color", evt ->
                innerColorBlock.setBackground((Color) evt.getNewValue())
        );

        JPanel borderColorChooserRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        borderColorChooserRow.add(borderColorBlock);
        borderColorChooserRow.add(btnBorderColor);

        JPanel innerColorChooserRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        innerColorChooserRow.add(innerColorBlock);
        innerColorChooserRow.add(btnInnerColor);

        // ✅ 3 rows x 2 cols: Radius / Border / Inner
        JPanel p = new JPanel(new GridLayout(4, 2, 8, 8));
        p.add(new JLabel("Radius:"));
        p.add(tfR);

        p.add(new JLabel("Inner Radius:"));
        p.add(tfIR);

        p.add(new JLabel("Border Color:"));
        p.add(borderColorChooserRow);   // ✅ row (block + button)

        p.add(new JLabel("Inner Color:"));
        p.add(innerColorChooserRow);    // ✅ row (block + button)

        while (true) {
            int res = JOptionPane.showConfirmDialog(
                    owner, p, "Circle properties",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (res != JOptionPane.OK_OPTION) return null;

            try {
                int radius = Integer.parseInt(tfR.getText().trim());
                int innerRadius = Integer.parseInt(tfIR.getText().trim());
                if (radius <= 0 || innerRadius >= radius) throw new NumberFormatException();
                return new DonutParams(radius, innerRadius, btnBorderColor.getColor(), btnInnerColor.getColor());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        owner, "Radius must be positive integer. Inner radius must be less than outer.", "Validation",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }

    public record DonutParams(int radius, int innerRadius, Color borderColor, Color innerColor) {}
}
