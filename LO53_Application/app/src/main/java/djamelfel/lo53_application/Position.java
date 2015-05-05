package fr.utbm.myapplication;

import java.util.Date;
/**
 * Created by djamel on 05/05/15.
 */
public class Position {
    private int x;
    private int y;
    private Date timestamp;

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
