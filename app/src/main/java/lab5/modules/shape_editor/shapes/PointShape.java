package lab5.modules.shape_editor.shapes;

import android.graphics.Color;
import android.graphics.Paint;

import lab5.modules.shape_editor.Type;

// клас, що визначає точку
public class PointShape extends Shape{
    public Paint pointPaint;

    public PointShape() {
        setPaint();
        type = Type.POINT;
    }

    @Override
    public void show() {
        if (endX >= 4 || endY >= 4) {
            canvas.drawPoint(endX, endY, pointPaint);
        }
    }

    @Override
    public void setPaint() {
        pointPaint = new Paint(Paint.DITHER_FLAG);
        pointPaint.setAntiAlias(true);
        pointPaint.setDither(true);
        pointPaint.setColor(Color.BLACK);
        pointPaint.setStrokeWidth(8);
    }
}
