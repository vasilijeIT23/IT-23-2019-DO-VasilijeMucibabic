package app.view;

import app.controller.DrawingController;
import app.drawing.enums.Modes;
import app.strategy.LoadDrawingStrategy;
import app.strategy.LoadLogStrategy;
import app.strategy.SaveDrawingStrategy;
import app.strategy.SaveLogStrategy;

import javax.swing.*;
import java.awt.*;

public class DrawingView extends JPanel {
    private DrawingController controller;
    private final PnlDrawing canvas;

    private final JButton btnUndo = new JButton("Undo");
    private final JButton btnRedo = new JButton("Redo");
    private final JToggleButton btnModify = new JToggleButton("Modify");
    private final JToggleButton btnDelete = new JToggleButton("Delete");

    public void setController(DrawingController controller) {
        this.controller = controller;
        canvas.setController(controller); // propagate to canvas

        controller.addSelectionObserver(count -> {
            btnDelete.setEnabled(count > 0);
            btnModify.setEnabled(count == 1);
        });
    }
    private final JLabel lblStatus = new JLabel(" ");
    private final LogPanel logPanel = new LogPanel();

    public DrawingView() {
        btnDelete.setEnabled(false);
        btnModify.setEnabled(false);

        canvas = new PnlDrawing();
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                canvas,
                logPanel
        );

        splitPane.setResizeWeight(0.8); // 80% canvas, 20% log
        splitPane.setDividerSize(4);
        add(splitPane, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JToggleButton btnPoint = new JToggleButton("Point");
        JToggleButton btnLine = new JToggleButton("Line");
        JToggleButton btnRectangle = new JToggleButton("Rectangle");
        JToggleButton btnCircle = new JToggleButton("Circle");
        JToggleButton btnDonut = new JToggleButton("Donut");
        JToggleButton btnHexagon = new JToggleButton("Hexagon");

        JToggleButton btnSelect = new JToggleButton("Select");
        JButton btnSaveLog = new JButton("Save Log");
        JButton btnSaveDrawing = new JButton("Save Drawing");
        JButton btnLoadLog = new JButton("Load Log");
        JButton btnLoadDrawing = new JButton("Load Drawing");
        JButton btnToFront = new JButton("↑");
        JButton btnToBack = new JButton("↓");
        JButton btnBringToFront = new JButton("⇈");
        JButton btnBringToBack  = new JButton("⇊");

        ButtonGroup group = new ButtonGroup();
        group.add(btnPoint);
        group.add(btnLine);
        group.add(btnRectangle);
        group.add(btnCircle);
        group.add(btnDonut);
        group.add(btnHexagon);
        group.add(btnModify);
        group.add(btnDelete);
        group.add(btnSelect);

        btnPoint.addActionListener(e  -> controller.onActionSelected(Modes.POINT));
        btnLine.addActionListener(e  -> controller.onActionSelected(Modes.LINE));
        btnRectangle.addActionListener(e  -> controller.onActionSelected(Modes.RECTANGLE));
        btnCircle.addActionListener(e  -> controller.onActionSelected(Modes.CIRCLE));
        btnDonut.addActionListener(e  -> controller.onActionSelected(Modes.DONUT));
        btnHexagon.addActionListener(e  -> controller.onActionSelected(Modes.HEXAGON));

        btnSelect.addActionListener(e  -> controller.onActionSelected(Modes.SELECT));
        btnModify.addActionListener(e  -> controller.onActionSelected(Modes.MODIFY));
        btnDelete.addActionListener(e  -> controller.onActionSelected(Modes.DELETE));

        btnUndo.addActionListener(e -> controller.undo());
        btnRedo.addActionListener(e -> controller.redo());

        btnSaveLog.addActionListener(e -> {
            controller.setSaveStrategy(new SaveLogStrategy());
            controller.save();
        });

        btnSaveDrawing.addActionListener(e -> {
            controller.setSaveStrategy(new SaveDrawingStrategy(controller.getShapes()));
            controller.save();
        });

        btnLoadLog.addActionListener(e -> {
            controller.setLoadStrategy(new LoadLogStrategy(controller.getShapes()));
            controller.load(canvas);
        });

        btnLoadDrawing.addActionListener(e -> {
            controller.setLoadStrategy(new LoadDrawingStrategy(controller.getShapes()));
            controller.load(canvas); // reuses same load flow
        });

        btnToFront.addActionListener(e -> controller.toFront());
        btnToBack.addActionListener(e -> controller.toBack());
        btnBringToFront.addActionListener(e -> controller.bringToFront());
        btnBringToBack.addActionListener(e -> controller.bringToBack());

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
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnHexagon);
        toolBar.addSeparator(new Dimension(54, 0));
        toolBar.add(btnModify);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnDelete);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnSelect);

        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnUndo);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnRedo);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnSaveLog);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnSaveDrawing);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnLoadDrawing);

        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnLoadLog);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnToFront);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnToBack);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnBringToFront);
        toolBar.addSeparator(new Dimension(18, 0));
        toolBar.add(btnBringToBack);

        btnUndo.setEnabled(false);
        btnRedo.setEnabled(false);

        add(toolBar, BorderLayout.NORTH);

        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        lblStatus.setText("Tool: POINT | Click to place a point.");
        statusBar.add(lblStatus, BorderLayout.WEST);

        add(statusBar, BorderLayout.SOUTH);

    }

    public void setStatus(String text) {
        lblStatus.setText(text);
    }

    public void refreshButtons() {
        btnUndo.setEnabled(controller.canUndo());
        btnRedo.setEnabled(controller.canRedo());
    }

    public void refreshLog() {
        logPanel.refresh(controller.getLog());
    }

    public void refresh() {
        repaint();
        refreshLog();
        refreshButtons();
    }

}
