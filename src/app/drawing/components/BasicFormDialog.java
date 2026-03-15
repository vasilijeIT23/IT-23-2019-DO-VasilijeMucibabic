package app.drawing.components;

import app.drawing.enums.FieldTypes;

import javax.swing.*;
import java.awt.*;

public class BasicFormDialog {
    public static java.util.Map<String, Object> ask(
            java.awt.Window owner,
            FormSpecs form
    ) {
        return ask(owner, form.title(), form.fields());
    }


    static java.util.Map<String, Object> ask(
            java.awt.Window owner,
            String title,
            java.util.List<FieldSpecs> specs
    ) {
        JPanel panel = new JPanel(new GridLayout(specs.size(), 2, 6, 6));
        java.util.Map<String, JComponent> inputs = new java.util.HashMap<>();

        for (FieldSpecs s : specs) {
            panel.add(new JLabel(s.label));

            JComponent input;
            if (s.type == FieldTypes.INT) {
                String def = s.defaultValue.toString();
                input = new JTextField(def, 8);
            } else {
                Color col = (Color) s.defaultValue;
                input = new ColorPickerRow("Choose color", col);
            }

            inputs.put(s.key, input);
            panel.add(input);
        }

        while (true) {
            int res = JOptionPane.showConfirmDialog(
                    owner, panel, title,
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (res != JOptionPane.OK_OPTION) return null;

            java.util.Map<String, Object> values = new java.util.HashMap<>();

            try {
                for (FieldSpecs s : specs) {
                    JComponent c = inputs.get(s.key);

                    if (s.type == FieldTypes.INT) {
                        String text = ((JTextField) c).getText().trim();

                        if (text.isEmpty() && s.required)
                            throw new IllegalArgumentException(s.label + " is required.");

                        int v = Integer.parseInt(text);

                        if (s.min != null && v < s.min)
                            throw new IllegalArgumentException(
                                    s.label + " must be ≥ " + s.min);

                        if (s.max != null && v > s.max)
                            throw new IllegalArgumentException(
                                    s.label + " must be ≤ " + s.max);

                        values.put(s.key, v);

                    } else { // COLOR
                        Color col = ((ColorPickerRow) c).getColor();
                        values.put(s.key, col);
                    }
                }

                return values;

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        owner, ex.getMessage(),
                        "Validation error",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
}
