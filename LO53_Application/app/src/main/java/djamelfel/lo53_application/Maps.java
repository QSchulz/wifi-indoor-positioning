package djamelfel.lo53_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class Maps extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        Server server = new Server();
        server.setIpServer(intent.getStringExtra("setIPAddress"));

        Log.d("IP Address", server.getIpServer());
        /*
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.id.maps);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        // Nous allons dessiner nos points par rapport à la résolution de l'écran
        int iWidth = canvas.getWidth(); // Largeur
        int iHeight = canvas.getHeight(); // Hauteur

        Random rand = new Random();

        // Affecter la couleur noir
        paint.setARGB(255, 255, 0, 0);
        // Taille du point
        paint.setStrokeWidth(10);
        // Puis dessiner nos points dans le canevas
        canvas.drawPoint(rand.nextInt(iWidth), rand.nextInt(iHeight), paint);
*/

    }
}
