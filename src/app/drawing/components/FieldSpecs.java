package app.drawing.components;

import app.drawing.enums.FieldTypes;

import java.awt.*;

public final class FieldSpecs {

    public final String key;
    public final String label;
    public final FieldTypes type;
    public final Object defaultValue;
    public final Integer min;
    public final Integer max;
    public final boolean required;

    public FieldSpecs(String key, String label, FieldTypes type,
                      Object defaultValue, Integer min, Integer max, boolean required) {
        this.key = key;
        this.label = label;
        this.type = type;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.required = required;
    }
}
