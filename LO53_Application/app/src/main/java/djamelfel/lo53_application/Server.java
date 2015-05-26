package djamelfel.lo53_application;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import org.apache.http.Header;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.loopj.android.http.*;



public class Server {
    private String _ipServer;
    private String _macAddress;
    private AsyncHttpClient _httpClient;

    public Server(String ipServer, WifiManager manager) {
        WifiInfo info = manager.getConnectionInfo();
        _macAddress = info.getMacAddress();
        _ipServer = "http://" + ipServer + ":8888";
        _httpClient = new AsyncHttpClient();
    }

    public String get_ipServer() {
        return this._ipServer;
    }

    public String getMacAddress() {
        return _macAddress;
    }

    public void sendRequest(String host, RequestParams params, final Callback callback) {
        _httpClient.get(_ipServer + host, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    callback.callbackFunction(new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.d("Error", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.d("onFailure", e.toString());
            }
        });
    }
}