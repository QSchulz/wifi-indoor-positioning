package djamelfel.lo53_application;


import java.util.*;
/**
 * Created by djamel on 05/05/15.
 */
public class Path {
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
        if (isEmpty()) {
            return null;
        }
        return lastUpdate;
    }

    public List<Position> getPath(boolean smooth) {
        if (isEmpty()) {
            return null;
        }
        else if(smooth) {
            return getSmooth(this.path);
        }
        return this.path;
    }

    public List<Position> getInterval(Date begin, Date end, boolean smooth) {
        if (isEmpty()) {
            return null;
        }
        List<Position> path_tmp = new ArrayList<Position>();
        Date timestamp;
        Iterator itr = this.path.iterator();
        while(itr.hasNext()) {
           Position position = (Position)itr.next();
           timestamp = position.getTimestamp();
           if (timestamp.getTime() < end.getTime() && timestamp.getTime() > begin.getTime()) {
               path_tmp.add(position);
           }
        }
        if (smooth) {
            return getSmooth(path_tmp);
        }
        return path_tmp;
    }

    public List<Position> getSmooth(List<Position> path) {
        return null;
    }

    public Position getLastPosition() {
        Iterator itr = this.path.iterator();
        Position position = null;
        while(itr.hasNext()) {
            position = new Position((Position)itr.next());
        }
        return position;
    }

    public boolean isEmpty() {
        return path.isEmpty();
    }

}
