package djamelfel.lo53_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Login extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText ipAddress = (EditText)findViewById(R.id.text_ipAddress);

        Button validateIPAddress = (Button) findViewById(R.id.button_ipAddress);
        validateIPAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vue) {
                Intent intent = new Intent(Login.this, Maps.class);
                intent.putExtra("setIPAddress", ipAddress.getText().toString());
                startActivity(intent);
            }
        });
    }
}
