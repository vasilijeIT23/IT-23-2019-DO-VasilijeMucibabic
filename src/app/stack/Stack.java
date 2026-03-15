package app.stack;

import app.geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Stack {

    private final List<Rectangle> stack = new ArrayList<>();

    public void push(Rectangle rectangle) {
        stack.add(rectangle);
    }

    public Rectangle pop() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty.");
        return stack.remove(stack.size() - 1);
    }

    public Rectangle peek() {
        if (isEmpty()) throw new IllegalStateException("Stack is empty.");
        return stack.get(stack.size() - 1);
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }

    public List<Rectangle> getAll() {
        return new ArrayList<>(stack);
    }
}
