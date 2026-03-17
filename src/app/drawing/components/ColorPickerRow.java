package app.drawing.components;

import javax.swing.*;
import java.awt.*;

public class ColorPickerRow extends JPanel {

    private final JPanel block = new JPanel();
    private final DlgColorPicker button;

    public ColorPickerRow(String buttonText, Color initial) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 6, 0));

        block.setPreferredSize(new Dimension(16, 16));
        block.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        button = new DlgColorPicker(buttonText, initial != null ? initial : Color.BLACK);

        block.setBackground(button.getColor());
        button.addPropertyChangeListener("color", evt ->
                block.setBackground((Color) evt.getNewValue())
        );

        add(block);
        add(button);
    }

    public Color getColor() {
        return button.getColor();
    }

    public void setColor(Color c) {
        button.setColor(c);
    }
}
