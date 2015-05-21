package djamelfel.lo53_application;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
            Intent intent = new Intent(Login.this, Maps.class);
            intent.putExtra("setIPAddress", myIpString);
            startActivity(intent);
        }
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(Login.this);
            alert
                .setTitle("wrong IP Address")
                .setMessage("write an IP Address int the following form: '192.168.0.1'")
                .setCancelable(false)
                .setNeutralButton("Return", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _ipAddress.setText(null);
                        dialog.cancel();
                    }
                });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();
        }
    }

}
