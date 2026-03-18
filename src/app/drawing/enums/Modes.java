package app.drawing.enums;

public enum Modes {
    POINT("Tool: POINT | Click to place a point."),
    LINE("Tool: LINE | Click start, then click end."),
    RECTANGLE("Tool: RECTANGLE | Click upper left point."),
    CIRCLE("Tool: CIRCLE | Click the center."),
    DONUT("Tool: DONUT | Click the center."),
    HEXAGON("Tool: HEXAGON | Click the start point."),
    MODIFY("Tool: MODIFY | Click a shape to modify."),
    DELETE("Tool: DELETE | Click a shape to delete."),
    SELECT("Tool: SELECT | Click a shape to select.");

    private final String statusMessage;

    Modes(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}