package app.drawing.components;

import java.util.List;

public record FormSpecs(
        String title,
        List<FieldSpecs> fields,
        FormValidator validator // can be null
) {
    public FormSpecs(String title, List<FieldSpecs> fields) {
        this(title, fields, null);
    }
}