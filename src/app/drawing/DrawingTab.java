package app.drawing;

import app.drawing.enums.Modes;

import javax.swing.*;
import java.awt.*;

public class DrawingTab extends JPanel {

    private final JLabel lblStatus = new JLabel(" ");

    public DrawingTab() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        PnlDrawing drawing = new PnlDrawing();
        add(drawing, BorderLayout.CENTER);

        // ===== Toolbar =====
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JToggleButton btnPoint = new JToggleButton("Point");
        JToggleButton btnLine = new JToggleButton("Line");
        JToggleButton btnRectangle = new JToggleButton("Rectangle");
        JToggleButton btnCircle = new JToggleButton("Circle");
        JToggleButton btnDonut = new JToggleButton("Donut");
        JToggleButton btnModify = new JToggleButton("Modify");
        JToggleButton btnDelete = new JToggleButton("Delete");
        JToggleButton btnSelect = new JToggleButton("Select");

        ButtonGroup group = new ButtonGroup();
        group.add(btnPoint);
        group.add(btnLine);
        group.add(btnRectangle);
        group.add(btnCircle);
        group.add(btnDonut);
        group.add(btnModify);
        group.add(btnDelete);
        group.add(btnSelect);

        drawing.setMode(Modes.POINT);

        btnPoint.addActionListener(e -> {
            drawing.setMode(Modes.POINT);
            setStatus("Tool: POINT | Click to place a point.");
        });

        btnLine.addActionListener(e -> {
            drawing.setMode(Modes.LINE);
            setStatus("Tool: LINE | Click start, then click end.");
        });

        btnRectangle.addActionListener(e -> {
            drawing.setMode(Modes.RECTANGLE);
            setStatus("Tool: RECTANGLE | Click upper left point of the rectangle.");
        });

        btnCircle.addActionListener( e -> {
            drawing.setMode(Modes.CIRCLE);
            setStatus("Tool: CIRCLE | Point click the center of the circle");
        });

        btnDonut.addActionListener( e -> {
            drawing.setMode(Modes.DONUT);
            setStatus("Tool: DONUT | Point click the center of the donut");
        });

        btnModify.addActionListener( e -> {
            drawing.setMode(Modes.MODIFY);
            setStatus("Tool: DONUT | Point click the center of the donut");
        });
        btnDelete.addActionListener( e -> {
            drawing.setMode(Modes.DELETE);
            setStatus("Tool: DONUT | Point click the center of the donut");
        });
        btnSelect.addActionListener( e -> {
            drawing.setMode(Modes.SELECT);
            setStatus("Tool: DONUT | Point click the center of the donut");
        });



//        JButton btnUndo = new JButton("Undo");
//        btnUndo.addActionListener(e -> {
//            drawing.undo();
//            setStatus("Undo.");
//        });
//
//        JButton btnClear = new JButton("Clear");
//        btnClear.addActionListener(e -> {
//            drawing.clearAll();
//            setStatus("Canvas cleared.");
//        });

        toolBar.add(new JLabel("Tool: "));
        toolBar.add(btnPoint);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnLine);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnRectangle);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnCircle);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnDonut);
        toolBar.addSeparator(new Dimension(54, 0));
        toolBar.add(btnModify);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnDelete);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnSelect);
//        toolBar.add(btnUndo);
//        toolBar.add(btnClear);

        add(toolBar, BorderLayout.NORTH);

        // ===== Status bar =====
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        lblStatus.setText("Tool: POINT | Click to place a point.");
        statusBar.add(lblStatus, BorderLayout.WEST);

        add(statusBar, BorderLayout.SOUTH);
    }

    private void setStatus(String text) {
        lblStatus.setText(text);
    }
}
