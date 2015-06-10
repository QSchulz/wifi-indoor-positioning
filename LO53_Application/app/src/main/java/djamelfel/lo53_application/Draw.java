package djamelfel.lo53_application;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;
import java.util.Iterator;
import java.util.List;


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

    public void drawPoint(int x, int y, int radius, ImageView image) {
        _canvas.drawCircle(x, y, radius, _paint);
        image.setImageBitmap(_bitmap);
    }

    public void drawPath(List<Position> positionList, Draw draw, ImageView image) {
        Iterator itr = positionList.iterator();
        Position positionPrev = (Position)itr.next();
        Position position;
        Log.d("drawPath", "TRUE");
        while(itr.hasNext()) {
            position = (Position)itr.next();
            draw.drawPath(positionPrev.getX(), positionPrev.getY(), position.getX(), position.getY(), 20, image);
            positionPrev = position;
        }
    }

    private void drawPath(int x1, int y1, int x2, int y2, int radius, ImageView image) {
        Log.d("drowPoint", "TRUE");
        _paint.setStrokeWidth(radius);
        _canvas.drawLine(x1, y1, x2, y2, _paint);
        image.setImageBitmap(_bitmap);
    }
}
