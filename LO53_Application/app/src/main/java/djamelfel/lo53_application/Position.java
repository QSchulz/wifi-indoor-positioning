package djamelfel.lo53_application;

import java.util.*;
/**
 * Created by djamel on 05/05/15.
 */
public class Position {
    private int x;
    private int y;
    private Date timestamp;

    public Position(int x, int y, Date timestamp) {
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
    }

    public Position(Position position) {
        this.x = position.x;
        this.y = position.y;
        this.timestamp = position.timestamp;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return this.x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }
}
