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

    public static final FormSpecs MODIFY_POINT_FIELDS = new FormSpecs(
            "Point Properties",
            List.of(
                    new FieldSpecs("x", "X Axis:", FieldTypes.INT, 400, 1, 2000, true),
                    new FieldSpecs("y", "Y Axis:", FieldTypes.INT, 400, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, Color.BLACK, null, null, false)
            )
    );

    public static final FormSpecs CREATE_LINE_FIELDS = new FormSpecs(
            "Line Properties",
            List.of(
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, Color.BLACK, null, null, false)
            )
    );

    public static final FormSpecs MODIFY_LINE_FIELDS = new FormSpecs(
            "Line Properties",
            List.of(
                    new FieldSpecs("x", "X Axis:", FieldTypes.INT, 400, 1, 2000, true),
                    new FieldSpecs("y", "Y Axis:", FieldTypes.INT, 400, 1, 2000, true),
                    new FieldSpecs("x2", "X Axis:", FieldTypes.INT, 400, 1, 2000, true),
                    new FieldSpecs("y2", "Y Axis:", FieldTypes.INT, 400, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, Color.BLACK, null, null, false)
            )
    );

    public static final FormSpecs CREATE_RECTANGLE_FIELDS = new FormSpecs(
            "Rectangle properties",
            List.of(
                    new FieldSpecs("width", "Width:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("height", "Height:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, NO_COLOR, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, NO_COLOR, null, null, false)
            )
    );

    public static final FormSpecs MODIFY_RECTANGLE_FIELDS = new FormSpecs(
            "Rectangle properties",
            List.of(
                    new FieldSpecs("x", "X Axis:", FieldTypes.INT, 500, 1, 2000, true),
                    new FieldSpecs("y", "Y Axis:", FieldTypes.INT, 500, 1, 2000, true),
                    new FieldSpecs("width", "Width:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("height", "Height:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, Color.BLACK, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, Color.WHITE, null, null, false)
            )
    );

    public static final FormSpecs CREATE_CIRCLE_FIELDS = new FormSpecs(
            "Circle properties",
            List.of(
                    new FieldSpecs("radius", "Radius:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, NO_COLOR, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, NO_COLOR, null, null, false)
            )
    );

    public static final FormSpecs MODIFY_CIRCLE_FIELDS = new FormSpecs(
            "Circle properties",
            List.of(
                    new FieldSpecs("x", "X Axis:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("y", "Y Axis:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("radius", "Radius:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, Color.BLACK, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, Color.WHITE, null, null, false)
            )
    );

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

    public static final FormSpecs MODIFY_DONUT_FIELDS = new FormSpecs(
            "Donut properties",
            List.of(
                    new FieldSpecs("x", "X Axis:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("y", "Y Axis:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("outer", "Outer radius:", FieldTypes.INT, 50, 1, 2000, true),
                    new FieldSpecs("inner", "Inner radius:", FieldTypes.INT, 20, 1, 2000, true),
                    new FieldSpecs("border", "Border color", FieldTypes.COLOR, Color.BLACK, null, null, false),
                    new FieldSpecs("fill", "Inner color", FieldTypes.COLOR, Color.WHITE, null, null, false)
            ),
            values -> {
                int outer = (Integer) values.get("outer");
                int inner = (Integer) values.get("inner");
                return inner < outer ? null : "Inner radius must be smaller than outer radius.";
            }
    );
}
