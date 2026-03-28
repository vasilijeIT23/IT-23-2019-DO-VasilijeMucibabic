package app.command;

import java.util.ArrayList;
import java.util.List;

public class CompositeCommand implements Command {

    private final List<Command> commands;
    private final String description;

    public CompositeCommand(List<Command> commands, String description) {
        this.commands = new ArrayList<>(commands);
        this.description = description;
    }

    @Override
    public void execute() {
        commands.forEach(Command::execute);
    }

    @Override
    public void undo() {
        // reverse order — last command undone first
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }

    @Override
    public String getDescription() {
        return description;
    }

    public List<Command> getCommands() {
        return commands;
    }
}