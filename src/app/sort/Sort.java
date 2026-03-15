package app.sort;

import app.geometry.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sort {

    public static List<Rectangle> sortAscending(List<Rectangle> rectangles) {
        List<Rectangle> sorted = new ArrayList<>(rectangles);
        sorted.sort(Comparator.comparingInt(r -> r.getWidth() * r.getHeight()));
        return sorted;
    }

    public static List<Rectangle> sortDescending(List<Rectangle> rectangles) {
        List<Rectangle> sorted = new ArrayList<>(rectangles);
        sorted.sort((a, b) -> (b.getWidth() * b.getHeight()) - (a.getWidth() * a.getHeight()));
        return sorted;
    }
}