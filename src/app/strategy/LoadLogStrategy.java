package app.strategy;

import app.geometry.Shape;
import app.strategy.LoadStrategy;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

// LoadLogStrategy — only reads and parses the file
public class LoadLogStrategy implements LoadStrategy {

    private final List<Shape> shapes;

    public LoadLogStrategy(List<Shape> shapes) {
        this.shapes = shapes;
    }

    @Override
    public List<String> load(JPanel parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load command log");

        int result = chooser.showOpenDialog(parent);
        if (result != JFileChooser.APPROVE_OPTION) return null;

        File file = chooser.getSelectedFile();
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) lines.add(line);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, "Failed to load log: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return lines;
    }
}