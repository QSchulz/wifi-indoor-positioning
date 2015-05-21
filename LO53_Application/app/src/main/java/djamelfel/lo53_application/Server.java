package djamelfel.lo53_application;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import java.util.*;


public class Server {
    private String _ipServer;
    private String _macAddress;

    public Server(String ipServer, WifiManager manager) {
        _ipServer = ipServer;

        WifiInfo info = manager.getConnectionInfo();
        _macAddress = info.getMacAddress();
    }

    public String get_ipServer() {
        return this._ipServer;
    }

    public Object getCurrentPosition() {
        Object position = null;

        return position;
    }

    public Object getPath() {
        Object path = null ;

        return path;
    }

    public Object updatePath(Date date){
        Object path = null;

        return path;
    }

    public String getMacAddress() {
        return _macAddress;
    }
}
