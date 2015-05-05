package fr.utbm.myapplication;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by djamel on 05/05/15.
 */
public class Path extends Position{
    private List<Position> path;
    private Date lastUpdate;

    public Path(){
        this.path = new ArrayList<Position>();
        this.lastUpdate = null;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public List<Position> getPath(boolean smooth) {
    }

    public List<Position> getInterval(Date begin, Date end, boolean smooth) {
    }

    public List<Position> getSmooth(List<Position> path) {
    }

    public List<Position> getLastPosition() {
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }
}
