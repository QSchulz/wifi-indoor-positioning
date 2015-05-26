package djamelfel.lo53_application;


import java.util.*;


public class Position {
    private int _x;
    private int _y;
    private Date _timestamp;

    public Position(int x, int y, Date timestamp) {
        _x = x;
        _y = y;
        _timestamp = timestamp;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public Date getTimestamp() {
        return _timestamp;
    }
}
