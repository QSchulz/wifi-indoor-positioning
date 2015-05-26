package djamelfel.lo53_application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.loopj.android.http.RequestParams;
import java.util.Date;


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
        Date date;
        long timeDifference;

        final ImageView imageView = (ImageView)findViewById(R.id.batiment_h);
        switch (vue.getId()) {
            /**
             * Draw position was clicked
             */
            case R.id.drawPosition:
                /**
                 * Check if data are update
                 */
                date = new Date();
                timeDifference = date.getTime() - _path.get_lastUpdate().getTime();
                if ( _path.isEmpty() ||  timeDifference < 10000) {
                    /**
                     * If data are not updated Request HTTP
                     */
                    _server.sendRequest("/test", null, new Callback() {
                        @Override
                        public void callbackFunction(String resp) {
                            /**
                             * Save data
                             */
                            _path.setPath(resp);

                            /**
                             * Draw position on the maps
                             */
                            Position position = _path.getLastPosition();
                            _draw.drawPoint(position.getX(), position.getY(), 30, imageView);
                        }
                    });
                }
                else {
                    /**
                     * Draw position on the maps
                     */
                    Position position = _path.getLastPosition();
                    _draw.drawPoint(position.getX(), position.getY(), 30, imageView);
                }

                break;
            /**
             * Draw path was clicked
             */
            case R.id.drawPath:
                /**
                 * Check if data are update
                 */
                date = new Date();
                timeDifference = date.getTime() - _path.get_lastUpdate().getTime();

                if (_path.isEmpty()) {
                    /**
                     * If data are not updated Request HTTP
                     */
                    _server.sendRequest("/test", null, new Callback() {
                        @Override
                        public void callbackFunction(String resp) {
                            /**
                             * Save data
                             */
                            _path.setPath(resp);

                            /**
                             * Draw data on the maps
                             */
                            _draw.drawPath(_path.getPath(false), _draw, imageView);
                        }
                    });
                }
                else if (timeDifference < 10000){
                    /**
                     * If data are not updated Request HTTP
                     */
                    RequestParams params = new RequestParams();
                    params.put("timestamp", _path.get_lastUpdate().toString());
                    _server.sendRequest("/test", null, new Callback() {
                        @Override
                        public void callbackFunction(String resp) {
                            /**
                             * Save data
                             */
                            _path.setPath(resp);

                            /**
                             * Draw data on the maps
                             */
                            _draw.drawPath(_path.getPath(false), _draw, imageView);
                        }
                    });
                }
                else {
                    /**
                     * Draw data on the maps
                     */
                    _draw.drawPath(_path.getPath(false), _draw, imageView);
                }
                break;
        }
    }

    @Override
    public void callbackFunction(String resp) {
    }
}
