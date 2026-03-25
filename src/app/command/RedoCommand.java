package app.command;

public class RedoCommand implements Command {

    private final Command redoCommand;

    public RedoCommand(Command command) {
        this.redoCommand = command;
    }

    @Override
    public void execute() {
        redoCommand.execute();
    }

    @Override
    public void undo() {
        redoCommand.undo();
    }

    @Override
    public String getDescription() {
        return "Redo: " + redoCommand.getDescription();
    }
}