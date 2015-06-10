package djamelfel.lo53_application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.conn.util.InetAddressUtils;


public class Login extends Activity implements View.OnClickListener {
    private EditText _ipAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        _ipAddress = (EditText)findViewById(R.id.text_ipAddress);
        Button validateIPAddress = (Button) findViewById(R.id.button_ipAddress);
        validateIPAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View vue) {
        String myIpString = _ipAddress.getText().toString();

        if (InetAddressUtils.isIPv4Address(myIpString)) {
            final Intent intent = new Intent(Login.this, Trace.class);
            intent.putExtra("setIPAddress", myIpString);

            WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            Server server = new Server(myIpString, manager);
            server.connect(new Callback() {
                @Override
                public void callbackFunction(String response) {
                }

                @Override
                public void callbackFunction(int status) {
                    if (status < 300 && status >= 200)
                        startActivity(intent);
                    else
                        Toast.makeText(getApplicationContext(),
                                status, Toast.LENGTH_LONG).show();
                }
            });
        }
        else
            Toast.makeText(getApplicationContext(),
                "Wrong IP Address:\n'" +
                "Please try with an IP Address in the following form: '192.168.0.1'",
                Toast.LENGTH_LONG).show();
    }
}