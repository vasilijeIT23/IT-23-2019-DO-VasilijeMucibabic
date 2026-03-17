package app;

import app.controller.DrawingController;
import app.model.DrawingModel;
import app.view.DrawingView;
import app.sort.SortTab;
import app.stack.Stack;
import app.stack.StackTab;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        super("OOIT");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        Stack sharedStack = new Stack();

        DrawingModel model = new DrawingModel();
        DrawingView drawingView = new DrawingView();
        new DrawingController(model, drawingView); // wires everything together

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Stack",   new StackTab(sharedStack));
        tabs.addTab("Sort",    new SortTab(sharedStack));
        tabs.addTab("Drawing", drawingView);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}