package lab5.modules.shape_editor.shapes;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import lab5.modules.shape_editor.Type;

// клас, що задає лінію, як фігуру
public class LineShape extends Shape implements LineShapeInterface {
    public Paint linePaint;

    public LineShape() {
        setPaint();
        type = Type.LINE;
    }

    @Override
    public void show() {
        showLine(startX, startY, endX, endY);
    }

    @Override
    public void setPaint() {
        setLinePaint();
    }

    @Override
    public void setLinePaint() {
        linePaint = new Paint(Paint.DITHER_FLAG);
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(5);
    }

    @Override
    public void showLine(float startX, float startY, float endX, float endY) {
        float dx = Math.abs(endX - startX);
        float dy = Math.abs(endY - startY);
        if (dx >= 4 || dy >= 4) {
            Path linePath = new Path();
            linePath.moveTo(startX, startY);
            linePath.lineTo(endX, endY);
            canvas.drawPath(linePath, linePaint);
        }
    }
}
