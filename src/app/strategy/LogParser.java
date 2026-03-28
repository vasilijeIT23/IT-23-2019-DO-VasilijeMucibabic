// app/strategy/LogParser.java
package app.strategy;

import app.geometry.*;
import app.geometry.Point;
import app.geometry.Rectangle;
import app.geometry.Shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LogParser {

    public static Shape add(String line) {
        if (line.startsWith("Added Point")) {
            int x = extractInt(line, "at (", ",");
            int y = extractInt(line, ", ", ")");
            Color color = extractColor(line, "color=");
            return new Point(x, y, color);
        }
        if (line.startsWith("Added Line")) {
            int x1 = extractInt(line, "from (", ",");
            int y1 = extractInt(line, "from (" + x1 + ", ", ")");
            int x2 = extractInt(line, "to (", ",");
            int y2 = extractInt(line, "to (" + x2 + ", ", ")");
            Color color = extractColor(line, "color=");
            return new Line(new Point(x1, y1), new Point(x2, y2), color);
        }
        if (line.startsWith("Added Rectangle")) {
            int x = extractInt(line, "at (", ",");
            int y = extractInt(line, ", ", ")");
            int w = extractInt(line, "w=", " ");
            int h = extractInt(line, "h=", " ");
            Color border = extractColor(line, "border=");
            Color fill   = extractColor(line, "fill=");
            return new Rectangle(new Point(x, y), h, w, border, fill);
        }
        if (line.startsWith("Added Donut")) {
            int x     = extractInt(line, "at (", ",");
            int y     = extractInt(line, ", ", ")");
            int inner = extractInt(line, "inner=", " ");
            int outer = extractInt(line, "outer=", " ");
            Color border = extractColor(line, "border=");
            Color fill   = extractColor(line, "fill=");
            return new Donut(new Point(x, y), outer, inner, border, fill);
        }
        if (line.startsWith("Added Hexagon")) {
            int x = extractInt(line, "at (", ",");
            int y = extractInt(line, ", ", ")");
            int r = extractInt(line, "r=", " ");
            Color border = extractColor(line, "border=");
            Color fill   = extractColor(line, "fill=");
            return new HexagonAdapter(x, y, r, border, fill);
        }
        if (line.startsWith("Added Circle")) {
            int x = extractInt(line, "at (", ",");
            int y = extractInt(line, ", ", ")");
            int r = extractInt(line, "r=", " ");
            Color border = extractColor(line, "border=");
            Color fill   = extractColor(line, "fill=");
            return new Circle(new Point(x, y), r, border, fill);
        }
        return null;
    }

    // returns true if line was handled as a modify
    public static ArrayList<Shape> modify(String line, List<Shape> shapes) {
        Shape original = null;
        Shape new_shape = null;
        if (line.startsWith("Modified Point")) {
            // Modified Point from (ox, oy) to (nx, ny) color=rgb()
            int ox = extractInt(line, "from (", ",");
            int oy = extractInt(line, "from (" + ox + ", ", ")");
            int nx = extractInt(line, "to (", ",");
            int ny = extractInt(line, "to (" + nx + ", ", ")");
            Color color = extractColor(line, "color=");

            original = findPoint(shapes, ox, oy);
            new_shape = new Point(nx, ny, color);

        }
        if (line.startsWith("Modified Line")) {
            // Modified Line from (ox1,oy1)-(ox2,oy2) to (nx1,ny1)-(nx2,ny2) color=rgb()
            int ox1 = extractInt(line, "from (", ",");
            int oy1 = extractInt(line, "from (" + ox1 + ", ", ")");
            int ox2 = extractInt(line, ")-(", ",");
            int oy2 = extractInt(line, ")-(" + ox2 + ", ", ")");
            int nx1 = extractInt(line, "to (", ",");
            int ny1 = extractInt(line, "to (" + nx1 + ", ", ")");
            int nx2 = extractInt(line, "to (" + nx1 + ", " + ny1 + ")-(", ",");
            int ny2 = extractInt(line, "to (" + nx1 + ", " + ny1 + ")-(" + nx2 + ", ", ")");
            Color color = extractColor(line, "color=");

            original = findLine(shapes, ox1, oy1, ox2, oy2);
            new_shape = new Line(new Point(nx1, ny1), new Point(nx2, ny2), color);
        }
        if (line.startsWith("Modified Rectangle")) {
            // Modified Rectangle from (ox, oy) w=OW h=OH to (nx, ny) w=NW h=NH border=rgb() fill=rgb()
            int ox = extractInt(line, "from (", ",");
            int oy = extractInt(line, "from (" + ox + ", ", ")");
            int ow = extractInt(line, "w=", " ");
            int oh = extractInt(line, "h=", " ");
            int nx = extractInt(line, "to (", ",");
            int ny = extractInt(line, "to (" + nx + ", ", ")");
            int nw = extractInt(line, "to (" + nx + ", " + ny + ") w=", " ");
            int nh = extractInt(line, "to (" + nx + ", " + ny + ") w=" + nw + " h=", " ");
            Color border = extractColor(line, "border=");
            Color fill   = extractColor(line, "fill=");

            original = findRectangle(shapes, ox, oy, ow, oh);
            new_shape = new Rectangle(new Point(nx, ny), nh, nw, border, fill);
        }
        if (line.startsWith("Modified Donut")) {
            // Modified Donut from (ox, oy) inner=OI outer=OO to (nx, ny) inner=NI outer=NO border=rgb() fill=rgb()
            int ox    = extractInt(line, "from (", ",");
            int oy    = extractInt(line, "from (" + ox + ", ", ")");
            int oInner = extractInt(line, "inner=", " ");
            int oOuter = extractInt(line, "outer=", " ");
            int nx    = extractInt(line, "to (", ",");
            int ny    = extractInt(line, "to (" + nx + ", ", ")");
            int nInner = extractInt(line, "to (" + nx + ", " + ny + ") inner=", " ");
            int nOuter = extractInt(line, "to (" + nx + ", " + ny + ") inner=" + nInner + " outer=", " ");
            Color border = extractColor(line, "border=");
            Color fill   = extractColor(line, "fill=");

            original = findDonut(shapes, ox, oy, oInner, oOuter);
            new_shape = new Donut(new Point(nx, ny), nOuter, nInner, border, fill);
        }
        if (line.startsWith("Modified Hexagon")) {
            int ox = extractInt(line, "from (", ",");
            int oy = extractInt(line, "from (" + ox + ", ", ")");
            int or_ = extractInt(line, "r=", " ");
            int nx = extractInt(line, "to (", ",");
            int ny = extractInt(line, "to (" + nx + ", ", ")");
            int nr = extractInt(line, "to (" + nx + ", " + ny + ") r=", " ");
            Color border = extractColor(line, "border=");
            Color fill   = extractColor(line, "fill=");

            original = findHexagon(shapes, ox, oy, or_);
            new_shape = new HexagonAdapter(nx, ny, nr, border, fill);
        }
        if (line.startsWith("Modified Circle")) {
            int ox = extractInt(line, "from (", ",");
            int oy = extractInt(line, "from (" + ox + ", ", ")");
            int or_ = extractInt(line, "r=", " ");
            int nx = extractInt(line, "to (", ",");
            int ny = extractInt(line, "to (" + nx + ", ", ")");
            int nr = extractInt(line, "to (" + nx + ", " + ny + ") r=", " ");
            Color border = extractColor(line, "border=");
            Color fill   = extractColor(line, "fill=");

            original = findCircle(shapes, ox, oy, or_);
            new_shape = new Circle(new Point(nx, ny), nr, border, fill);
        }
        ArrayList<Shape> old_new = new ArrayList<>();
        old_new.add(original);
        old_new.add(new_shape);
        return old_new;
    }

    public static Shape delete(String line, List<Shape> shapes) {
        Shape original = null;

        if (line.startsWith("Deleted Point")) {
            int ox = extractInt(line, "at (", ",");
            int oy = extractInt(line, "at (" + ox + ", ", ")");
            original = findPoint(shapes, ox, oy);
        }
        if (line.startsWith("Deleted Line")) {
            int ox1 = extractInt(line, "from (", ",");
            int oy1 = extractInt(line, "from (" + ox1 + ", ", ")");
            int ox2 = extractInt(line, ")-(", ",");
            int oy2 = extractInt(line, ")-(" + ox2 + ", ", ")");
            original = findLine(shapes, ox1, oy1, ox2, oy2);
        }
        if (line.startsWith("Deleted Rectangle")) {
            int ox = extractInt(line, "at (", ",");
            int oy = extractInt(line, "at (" + ox + ", ", ")");
            int ow = extractInt(line, "w=", " ");
            int oh = extractInt(line, "h=", " ");
            original = findRectangle(shapes, ox, oy, ow, oh);
        }
        if (line.startsWith("Deleted Donut")) {
            int ox    = extractInt(line, "at (", ",");
            int oy    = extractInt(line, "at (" + ox + ", ", ")");
            int oInner = extractInt(line, "inner=", " ");
            int oOuter = extractInt(line, "outer=", " ");
            original = findDonut(shapes, ox, oy, oInner, oOuter);
        }
        if (line.startsWith("Deleted Hexagon")) {
            int ox  = extractInt(line, "at (", ",");
            int oy  = extractInt(line, "at (" + ox + ", ", ")");
            int or_ = extractInt(line, "r=", " ");
            original = findHexagon(shapes, ox, oy, or_);
        }
        if (line.startsWith("Deleted Circle")) {
            int ox  = extractInt(line, "at (", ",");
            int oy  = extractInt(line, "at (" + ox + ", ", ")");
            int or_ = extractInt(line, "r=", " ");
            original = findCircle(shapes, ox, oy, or_);
        }

        return original;
    }
    // ===== Finders =====

    private static Shape findPoint(List<Shape> shapes, int x, int y) {
        for (Shape s : shapes)
            if (s instanceof Point p && p.getX() == x && p.getY() == y)
                return s;
        return null;
    }

    private static Shape findLine(List<Shape> shapes, int x1, int y1, int x2, int y2) {
        for (Shape s : shapes)
            if (s instanceof Line l &&
                    l.getA().getX() == x1 && l.getA().getY() == y1 &&
                    l.getB().getX() == x2 && l.getB().getY() == y2)
                return s;
        return null;
    }

    private static Shape findRectangle(List<Shape> shapes, int x, int y, int w, int h) {
        for (Shape s : shapes)
            if (s instanceof Rectangle r &&
                    r.getUpperLeftPoint().getX() == x && r.getUpperLeftPoint().getY() == y &&
                    r.getWidth() == w && r.getHeight() == h)
                return s;
        return null;
    }

    private static Shape findDonut(List<Shape> shapes, int x, int y, int inner, int outer) {
        for (Shape s : shapes)
            if (s instanceof Donut d &&
                    d.getCenter().getX() == x && d.getCenter().getY() == y &&
                    d.getInnerRadius() == inner && d.getRadius() == outer)
                return s;
        return null;
    }

    private static Shape findHexagon(List<Shape> shapes, int x, int y, int r) {
        for (Shape s : shapes)
            if (s instanceof HexagonAdapter h &&
                    h.getX() == x && h.getY() == y && h.getRadius() == r)
                return s;
        return null;
    }

    private static Shape findCircle(List<Shape> shapes, int x, int y, int r) {
        for (Shape s : shapes)
            if (s instanceof Circle c && !(s instanceof Donut) &&
                    c.getCenter().getX() == x && c.getCenter().getY() == y && c.getRadius() == r)
                return s;
        return null;
    }

    // ===== Extractors =====

    private static int extractInt(String line, String after, String before) {
        int start = line.indexOf(after) + after.length();
        int end = line.indexOf(before, start);
        return Integer.parseInt(line.substring(start, end).trim());
    }

    private static Color extractColor(String line, String tag) {
        int start = line.indexOf(tag) + tag.length() + 4; // skip "rgb("
        int end = line.indexOf(")", start);
        String[] parts = line.substring(start, end).split(",");
        return new Color(
                Integer.parseInt(parts[0].trim()),
                Integer.parseInt(parts[1].trim()),
                Integer.parseInt(parts[2].trim())
        );
    }
}