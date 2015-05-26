package djamelfel.lo53_application;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.apache.http.Header;
import java.io.UnsupportedEncodingException;



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

    public String getMacAddress() {
        return _macAddress;
    }

    public void connect(final Callback callback) {
        _httpClient.get(_ipServer + "/connect", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] bytes) {
                try {
                    callback.callbackFunction(new String(bytes, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    Log.d("Error", e.toString());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.callbackFunction(error.toString());
            }
        });
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