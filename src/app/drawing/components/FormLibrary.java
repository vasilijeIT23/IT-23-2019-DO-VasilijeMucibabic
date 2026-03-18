package app.drawing.components;

import app.drawing.enums.FieldTypes;

import java.awt.*;
import java.util.List;

public final class FormLibrary {
    public static final Color NO_COLOR = new Color(0,0,0,0);
    private FormLibrary() {}

    public static final FormSpecs CREATE_POINT_FIELDS = new FormSpecs(
            "Point Properties",
            List.of(
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, Color.BLACK, null, null, false)
            )
    );

    public static FormSpecs modifyPointFields(app.geometry.Point p) {
        return new FormSpecs(
                "Point properties",
                List.of(
                        new FieldSpecs("x", "X Axis:", FieldTypes.INT, p.getX(), 1, 2000, true),
                        new FieldSpecs("y", "Y Axis:", FieldTypes.INT, p.getY(), 1, 2000, true),
                        new FieldSpecs("border", "Border color", FieldTypes.COLOR, p.getColor(), null, null, false)
                )
        );
    }

    public static final FormSpecs CREATE_LINE_FIELDS = new FormSpecs(
            "Line Properties",
            List.of(
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, Color.BLACK, null, null, false)
            )
    );

    public static FormSpecs modifyLineFields(app.geometry.Line l) {
        return new FormSpecs(
                "Line properties",
                List.of(
                        new FieldSpecs("x", "X Axis:", FieldTypes.INT, l.getA().getX(), 1, 2000, true),
                        new FieldSpecs("y", "Y Axis:", FieldTypes.INT, l.getA().getY(), 1, 2000, true),
                        new FieldSpecs("x2", "X Axis:", FieldTypes.INT, l.getB().getX(), 1, 2000, true),
                        new FieldSpecs("y2", "Y Axis:", FieldTypes.INT, l.getB().getY(), 1, 2000, true),
                        new FieldSpecs("border", "Border color", FieldTypes.COLOR, l.getColor(), null, null, false)
                )
        );
    }

    public static final FormSpecs CREATE_RECTANGLE_FIELDS = new FormSpecs(
            "Rectangle properties",
            List.of(
                    new FieldSpecs("width", "Width:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("height", "Height:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, NO_COLOR, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, NO_COLOR, null, null, false)
            )
    );

    public static FormSpecs modifyRectangleFields(app.geometry.Rectangle r) {
        return new FormSpecs(
                "Rectangle properties",
                List.of(
                        new FieldSpecs("x",      "X Axis:",      FieldTypes.INT,   r.getUpperLeftPoint().getX(), 1, 2000, true),
                        new FieldSpecs("y",      "Y Axis:",      FieldTypes.INT,   r.getUpperLeftPoint().getY(), 1, 2000, true),
                        new FieldSpecs("width",  "Width:",       FieldTypes.INT,   r.getWidth(),                 1, 2000, true),
                        new FieldSpecs("height", "Height:",      FieldTypes.INT,   r.getHeight(),                1, 2000, true),
                        new FieldSpecs("border", "Border color", FieldTypes.COLOR, r.getColor(),                 null, null, false),
                        new FieldSpecs("fill",   "Inner color",  FieldTypes.COLOR, r.getInnerColor(),            null, null, false)
                )
        );
    }

    public static final FormSpecs CREATE_CIRCLE_FIELDS = new FormSpecs(
            "Circle properties",
            List.of(
                    new FieldSpecs("radius", "Radius:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, NO_COLOR, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, NO_COLOR, null, null, false)
            )
    );

    public static FormSpecs modifyCircleFields(app.geometry.Circle c) {
        return new FormSpecs(
                "Rectangle properties",
                List.of(
                        new FieldSpecs("x", "X Axis:", FieldTypes.INT, c.getCenter().getX(), 1, 2000, true),
                        new FieldSpecs("y", "Y Axis:", FieldTypes.INT, c.getCenter().getY(), 1, 2000, true),
                        new FieldSpecs("radius", "Radius:", FieldTypes.INT, c.getRadius(), 1, 2000, true),
                        new FieldSpecs("border", "Border color", FieldTypes.COLOR, c.getColor(), null, null, false),
                        new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, c.getInnerColor(), null, null, false)
                )
        );
    }

    public static final FormSpecs CREATE_DONUT_FIELDS = new FormSpecs(
            "Donut properties",
            List.of(
                    new FieldSpecs("outer", "Outer radius:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("inner", "Inner radius:", FieldTypes.INT, 20, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, NO_COLOR, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, NO_COLOR, null, null, false)
            ),
            values -> {
                int outer = (Integer) values.get("outer");
                int inner = (Integer) values.get("inner");
                return inner < outer ? null : "Inner radius must be smaller than outer radius.";
            }
    );

    public static FormSpecs modifyDonutFields(app.geometry.Donut d) {
        return new FormSpecs(
                "Rectangle properties",
                List.of(
                        new FieldSpecs("x", "X Axis:", FieldTypes.INT, d.getCenter().getX(), 1, 2000, true),
                        new FieldSpecs("y", "Y Axis:", FieldTypes.INT, d.getCenter().getY(), 1, 2000, true),
                        new FieldSpecs("outer", "Outer radius:", FieldTypes.INT, d.getRadius(), 1, 2000, true),
                        new FieldSpecs("inner", "Inner radius:", FieldTypes.INT, d.getInnerRadius(), 1, 2000, true),
                        new FieldSpecs("border", "Border color", FieldTypes.COLOR, d.getColor(), null, null, false),
                        new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, d.getInnerColor(), null, null, false)
                ),
                values -> {
                    int outer = (Integer) values.get("outer");
                    int inner = (Integer) values.get("inner");
                    return inner < outer ? null : "Inner radius must be smaller than outer radius.";
                }
        );
    }

    public static final FormSpecs CREATE_HEXAGON_FIELDS = new FormSpecs(
            "Hexagon properties",
            List.of(
                    new FieldSpecs("radius", "Radius:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, NO_COLOR, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, NO_COLOR, null, null, false)
            )
    );

    public static FormSpecs modifyHexagonFields(app.geometry.HexagonAdapter h) {
        return new FormSpecs(
                "Rectangle properties",
                List.of(
                        new FieldSpecs("x", "X Axis:", FieldTypes.INT, h.getX(), 1, 2000, true),
                        new FieldSpecs("y", "Y Axis:", FieldTypes.INT, h.getY(), 1, 2000, true),
                        new FieldSpecs("radius", "Radius:", FieldTypes.INT, h.getRadius(), 1, 2000, true),
                        new FieldSpecs("border", "Border color", FieldTypes.COLOR, h.getColor(), null, null, false),
                        new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, h.getInnerColor(), null, null, false)
                )
        );
    }
}

