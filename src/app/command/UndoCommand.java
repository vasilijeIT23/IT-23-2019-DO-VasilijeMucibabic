package app.command;

public class UndoCommand implements Command {

    private final Command undoCommand;

    public UndoCommand(Command command) {
        this.undoCommand = command;
    }

    @Override
    public void execute() {
        undoCommand.undo();
    }

    @Override
    public void undo() {
        undoCommand.execute();
    }

    @Override
    public String getDescription() {
        return "Undo: " + undoCommand.getDescription();
    }
}