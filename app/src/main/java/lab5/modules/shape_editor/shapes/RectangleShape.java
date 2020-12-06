package lab5.modules.shape_editor.shapes;

import android.graphics.Color;
import android.graphics.Paint;

import lab5.modules.shape_editor.Type;


// клас, що задає прямокутник, як фігуру
public class RectangleShape extends Shape implements RectangleShapeInterface{
    public Paint rectPaint1;
    public Paint rectPaint2;

    public RectangleShape() {
        setPaint();
        type = Type.RECTANGLE;
    }

    @Override
    public void show() {
        showRect(startX, startY, endX, endY);
    }

    @Override
    public void setPaint() {
        setRectPaint();
    }

    @Override
    public void setRectPaint() {
        rectPaint1 = new Paint(Paint.DITHER_FLAG);
        rectPaint1.setAntiAlias(true);
        rectPaint1.setDither(true);
        rectPaint1.setColor(Color.YELLOW);
        rectPaint1.setStyle(Paint.Style.FILL);
        rectPaint1.setStrokeJoin(Paint.Join.ROUND);
        rectPaint1.setStrokeCap(Paint.Cap.ROUND);
        rectPaint1.setStrokeWidth(5);
        rectPaint2 = new Paint(Paint.DITHER_FLAG);
        rectPaint2.setAntiAlias(true);
        rectPaint2.setDither(true);
        rectPaint2.setStyle(Paint.Style.STROKE);
        rectPaint2.setColor(Color.BLACK);
        rectPaint2.setStrokeJoin(Paint.Join.ROUND);
        rectPaint2.setStrokeCap(Paint.Cap.ROUND);
        rectPaint2.setStrokeWidth(5);
    }

    @Override
    public void showRect(float startX, float startY, float endX, float endY) {
        float right = Math.max(startX, endX);
        float left = Math.min(startX, endX);
        float bottom = Math.max(startY, endY);
        float top = Math.min(startY, endY);
        canvas.drawRect(left, top , right, bottom, rectPaint1);
        canvas.drawRect(left, top, right, bottom, rectPaint2);
    }
}
