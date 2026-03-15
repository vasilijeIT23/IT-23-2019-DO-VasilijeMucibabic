package app;

import app.drawing.DrawingTab;
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

        // shared list — both tabs operate on the same stack
        Stack sharedStack = new Stack();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Stack",   new StackTab(sharedStack));
        tabs.addTab("Sort",    new SortTab(sharedStack));
        tabs.addTab("Drawing", new DrawingTab());

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }
}