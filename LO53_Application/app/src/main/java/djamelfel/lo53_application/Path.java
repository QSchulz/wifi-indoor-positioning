package djamelfel.lo53_application;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Path {
    private List<Position> _position;
    private Date _lastUpdate;

    public Path(){
        _position = new ArrayList<Position>();
        _lastUpdate = new Date(0);
    }

    public void setPath(String resp) {
        try {
            JSONObject jsonObject = new JSONObject(resp);
            JSONArray jsonArray = jsonObject.getJSONArray("positions");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonPosition = jsonArray.getJSONObject(i);
                int x = Integer.parseInt(jsonPosition.getString("x"));
                int y = Integer.parseInt(jsonPosition.getString("y"));
                Date timestamp = null;

                try {
                    SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                    timestamp = format.parse(jsonPosition.getString("timestamp"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                _position.add(new Position(x, y, timestamp));

                if (_lastUpdate.before(timestamp))
                    _lastUpdate.setTime(timestamp.getTime());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Date get_lastUpdate() {
        return _lastUpdate;
    }

    public List<Position> getPath(boolean smooth) {
        if(smooth)
            return getSmooth(_position);
        return _position;
    }

    public List<Position> getInterval(Date begin, Date end, boolean smooth) {
        List<Position> path_tmp = new ArrayList<Position>();
        Date timestamp;
        Iterator itr = _position.iterator();
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
        Iterator itr = _position.iterator();
        Position position = (Position)itr.next();
        while(itr.hasNext()) {
            position = (Position)itr.next();
        }
        return position;
    }

    public boolean isEmpty() {
        return _position.isEmpty();
    }
}