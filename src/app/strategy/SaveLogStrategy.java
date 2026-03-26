package app.strategy;

import app.command.Command;
import app.model.DrawingModel;

import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class SaveLogStrategy implements SaveStrategy {

    @Override
    public void save(List<Command> logs) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save command log");
        chooser.setSelectedFile(new File("src/logs.txt"));

        int result = chooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Command entry : logs) {
                writer.write(entry.getDescription());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Log saved to " + file.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Failed to save log: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}