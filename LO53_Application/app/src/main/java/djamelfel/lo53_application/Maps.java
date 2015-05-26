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

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Maps extends Activity implements View.OnClickListener, Callback {
    private Bitmap _bitmap;
    private Server _server;
    private Path _path;


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
        _server = new Server(intent.getStringExtra("setIPAddress"), manager);

        /**
         * Set Path object
         */
        _path = new Path();

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
        final Draw _draw = new Draw(_bitmap, Color.BLUE);

        final EditText e1 = (EditText) findViewById(R.id.editText);
        final EditText e2 = (EditText) findViewById(R.id.editText2);
        e1.setHint(_draw.getCanvas().getWidth() + "");
        e2.setHint(_draw.getCanvas().getHeight() + "");

        final ImageView imageView = (ImageView)findViewById(R.id.batiment_h);
        switch (vue.getId()) {
            /**
             * Draw position was clicked
             */
            case R.id.drawPosition:
                /**
                 * Check if data are update
                 */

                /**
                 * If data are not updated Request HTTP
                 */
                _server.sendRequest("/test", null, new Callback() {
                    @Override
                    public void callbackFunction(String resp) {
                        Log.d("Callback", resp);
                        /**
                         * Save data
                         */
                        try {
                            JSONObject jsonObject = new JSONObject(resp);
                            JSONArray jsonArray = jsonObject.getJSONArray("path");
                            for (int i=0; i<jsonArray.length(); i++) {
                                _path.setPath(jsonArray.getJSONObject(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /**
                         * Draw data on the maps
                         */
                        _draw.drawPoint(Integer.parseInt(e1.getText().toString()),
                                Integer.parseInt(e2.getText().toString()),
                                30, imageView);
                    }
                });

                break;
            /**
             * Draw path was clicked
             */
            case R.id.drawPath:
                RequestParams params = new RequestParams();
                params.put("key", "value");

                _draw.setColor(Color.RED);
                _draw.drawPath(Integer.parseInt(e1.getText().toString()),
                        Integer.parseInt(e2.getText().toString()),
                        20, imageView);
                break;
        }
    }

    @Override
    public void callbackFunction(String response) {
    }
}
