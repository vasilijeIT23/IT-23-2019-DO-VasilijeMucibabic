package app.strategy;

import app.command.Command;
import java.util.List;

public interface SaveStrategy {
    void save(List<Command> logs);
}