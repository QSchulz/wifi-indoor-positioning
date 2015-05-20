package djamelfel.lo53_application;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;


public class Draw {
    private Paint _paint;
    private Bitmap _bitmap;
    private Canvas _canvas;

    public Draw(Bitmap bitmap, int color) {
        Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
        _bitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        _canvas = new Canvas(_bitmap);
        _paint = new Paint();
        this.setColor(color);
    }

    public void setColor(int color) {
        _paint.setColor(color);
    }

    public Canvas getCanvas() {
        return _canvas;
    }

    public void drawPoint(int cx, int cy, int radius, ImageView image) {
        _canvas.drawCircle(cx, cy, radius, _paint);
        image.setImageBitmap(_bitmap);
    }

    public void drawPath(int cx, int cy, int radius, ImageView image) {
        _paint.setStrokeWidth(radius);
        _canvas.drawLine(0, 0, cx, cy, _paint);
        image.setImageBitmap(_bitmap);
    }


    public void cleanDraw(ImageView image) {
        image.setImageBitmap(_bitmap);
    }
}
