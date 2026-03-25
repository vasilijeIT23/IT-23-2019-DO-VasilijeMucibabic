package app.view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LogPanel extends JPanel {

    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> list = new JList<>(listModel);

    public LogPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Command Log"));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public void refresh(List<String> entries) {
        listModel.clear();
        for (String entry : entries) {
            listModel.addElement(entry);
        }
        if (!listModel.isEmpty()) {
            list.ensureIndexIsVisible(listModel.getSize() - 1); // scroll to latest
        }
    }
}