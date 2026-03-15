package app.sort;

import app.geometry.Rectangle;
import app.stack.Stack;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SortTab extends JPanel {

    private final Stack stack;
    private final DefaultTableModel tableModel;

    public SortTab(Stack stack) {
        this.stack = stack;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                showSorted(Sort.sortAscending(stack.getAll()));
            }
        });

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

        JButton btnAsc  = new JButton("Sort Ascending");
        JButton btnDesc = new JButton("Sort Descending");

        btnAsc.addActionListener(e  -> showSorted(Sort.sortAscending(stack.getAll())));
        btnDesc.addActionListener(e -> showSorted(Sort.sortDescending(stack.getAll())));

        controls.add(btnAsc);
        controls.add(btnDesc);
        add(controls, BorderLayout.SOUTH);
    }

    private void showSorted(List<Rectangle> rectangles) {
        tableModel.setRowCount(0);
        for (int i = 0; i < rectangles.size(); i++) {
            Rectangle r = rectangles.get(i);
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