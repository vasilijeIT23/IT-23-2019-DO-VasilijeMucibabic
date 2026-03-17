package app.drawing.components;

import app.drawing.enums.FieldTypes;

public record FieldSpecs(String key, String label, FieldTypes type, Object defaultValue, Integer min, Integer max,
                         boolean required) {

}
