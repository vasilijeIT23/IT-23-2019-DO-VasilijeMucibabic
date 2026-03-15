package app.stack;

import app.geometry.Point;
import app.geometry.Rectangle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StackTab extends JPanel {

    private final Stack stack;
    private final DefaultTableModel tableModel;

    public StackTab(Stack stack) {
        this.stack = stack;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== Table =====
        tableModel = new DefaultTableModel(new String[]{"#", "X", "Y", "Width", "Height", "Area"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== Controls =====
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JTextField txtX      = new JTextField(4);
        JTextField txtY      = new JTextField(4);
        JTextField txtWidth  = new JTextField(4);
        JTextField txtHeight = new JTextField(4);

        controls.add(new JLabel("X:"));      controls.add(txtX);
        controls.add(new JLabel("Y:"));      controls.add(txtY);
        controls.add(new JLabel("Width:"));  controls.add(txtWidth);
        controls.add(new JLabel("Height:")); controls.add(txtHeight);

        JButton btnPush = new JButton("Push");
        JButton btnPop  = new JButton("Pop");

        btnPush.addActionListener(e -> {
            try {
                int x      = Integer.parseInt(txtX.getText().trim());
                int y      = Integer.parseInt(txtY.getText().trim());
                int width  = Integer.parseInt(txtWidth.getText().trim());
                int height = Integer.parseInt(txtHeight.getText().trim());

                stack.push(new Rectangle(new Point(x, y), height, width));
                refresh();

                txtX.setText("");
                txtY.setText("");
                txtWidth.setText("");
                txtHeight.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers.", "Invalid input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnPop.addActionListener(e -> {
            if (stack.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Stack is empty.", "Nothing to pop", JOptionPane.WARNING_MESSAGE);
                return;
            }
            stack.pop();
            refresh();
        });

        controls.add(btnPush);
        controls.add(btnPop);

        add(controls, BorderLayout.SOUTH);
    }

    private void refresh() {
        tableModel.setRowCount(0);
        var all = stack.getAll();
        for (int i = 0; i < all.size(); i++) {
            Rectangle r = all.get(i);
            tableModel.addRow(new Object[]{
                    i + 1,
                    r.getUpperLeftPoint().getX(),
                    r.getUpperLeftPoint().getY(),
                    r.getWidth(),
                    r.getHeight(),
                    r.getWidth() * r.getHeight()
            });
        }
    }
}