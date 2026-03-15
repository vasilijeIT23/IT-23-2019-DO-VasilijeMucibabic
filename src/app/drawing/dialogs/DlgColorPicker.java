package app.drawing.dialogs;

import javax.swing.*;
import java.awt.*;

public class DlgColorPicker extends JButton {

    private Color color;

    public DlgColorPicker(String text, Color initialColor) {
        super(text);
        this.color = initialColor != null ? initialColor : Color.BLACK;

        addActionListener(e -> {
            Color chosen = JColorChooser.showDialog(
                    SwingUtilities.getWindowAncestor(DlgColorPicker.this),
                    "Choose color",
                    color
            );
            if (chosen != null) {
                Color old = this.color;
                this.color = chosen;
                firePropertyChange("color", old, chosen); // ✅ notify listeners
            }
        });
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color c) {
        if (c == null) return;
        Color old = this.color;
        this.color = c;
        firePropertyChange("color", old, c);
    }
}
