package app.strategy;

import javax.swing.*;
import java.util.List;

public interface LoadStrategy {
    List<String> load(JPanel parent);
}