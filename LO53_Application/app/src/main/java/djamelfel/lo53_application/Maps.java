package djamelfel.lo53_application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class Maps extends Activity implements View.OnClickListener {
    private Bitmap _bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /**
         * Get address ip from previous Activity
         */
        Intent intent = getIntent();

        /**
         * Setup Server with MAC Address and IP Address
         */
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        Server server = new Server(intent.getStringExtra("setIPAddress"), manager);

        Log.d("IP Address", server.getMacAddress());
        Log.d("IP Address", server.get_ipServer());

        /**
         * Set listener on button
         */
        Button bDraw = (Button)findViewById(R.id.drawPosition);
        bDraw.setOnClickListener(this);
        Button bClean = (Button)findViewById(R.id.drawPath);
        bClean.setOnClickListener(this);

        _bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.batiment_h);
    }

    @Override
    public void onClick(View vue) {
        Draw _draw = new Draw(_bitmap, Color.BLUE);

        EditText e1 = (EditText) findViewById(R.id.editText);
        EditText e2 = (EditText) findViewById(R.id.editText2);
        e1.setHint(_draw.getCanvas().getWidth() + "");
        e2.setHint(_draw.getCanvas().getHeight() + "");

        ImageView imageView = (ImageView)findViewById(R.id.batiment_h);
        switch (vue.getId()) {
            /**
             * Draw position was clicked
             */
            case R.id.drawPosition:
                _draw.drawPoint(Integer.parseInt(e1.getText().toString()),
                        Integer.parseInt(e2.getText().toString()),
                        30, imageView);
                break;
            /**
             * Draw path was clicked
             */
            case R.id.drawPath:
                _draw.setColor(Color.RED);
                _draw.drawPath(Integer.parseInt(e1.getText().toString()),
                        Integer.parseInt(e2.getText().toString()),
                        20, imageView);
                break;
        }
    }
}
