package app.drawing.components;

import java.util.Map;

public interface FormValidator {
    String validate(Map<String, Object> values); // null = OK
}
