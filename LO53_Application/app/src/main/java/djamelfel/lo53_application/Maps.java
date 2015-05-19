package djamelfel.lo53_application;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


public class Maps extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        Server server = new Server();
        server.setIpServer(intent.getStringExtra("setIPAddress"));

        Log.d("IP Address", server.getIpServer());

        Button bDraw = (Button)findViewById(R.id.draw);
        bDraw.setOnClickListener(this);
        Button bClean = (Button)findViewById(R.id.clean);
        bClean.setOnClickListener(this);
    }

    @Override
    public void onClick(View vue) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.batiment_h);
        final Draw d = new Draw(bitmap);
        d.setColor(Color.BLUE);

        final EditText e1 = (EditText) findViewById(R.id.editText);
        final EditText e2 = (EditText) findViewById(R.id.editText2);
        e1.setHint(d.getCanvas().getWidth() + "");
        e2.setHint(d.getCanvas().getHeight() + "");

        ImageView imageView = (ImageView)findViewById(R.id.maps);
        switch (vue.getId()) {
            case R.id.draw:
                d.drawCircle(Integer.parseInt(e1.getText().toString()),
                        Integer.parseInt(e2.getText().toString()),
                        10, imageView);
                break;
        }
    }
}