package lab5.modules.shape_editor.shapes;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;

import androidx.annotation.RequiresApi;

import lab5.modules.shape_editor.Type;

// клас, що визначає лінії з кружечками на кінцях
public class CircleEndedLineShape extends Shape implements LineShapeInterface, EllipseShapeInterface {
    public Paint linePaint;
    public Paint circlesPaint1;
    public Paint circlesPaint2;


    public CircleEndedLineShape() {
        setPaint();
        type = Type.LINE_WITH_CIRCLES;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void show() {
        showLine(startX, startY, endX, endY);
        showEllipse(startX, startY, endX, endY);
    }

    @Override
    public void setPaint() {
        setLinePaint();
        setEllipsePaint();
    }

    @Override
    public void setEllipsePaint() {
        circlesPaint1 = new Paint(Paint.DITHER_FLAG);
        circlesPaint1.setAntiAlias(true);
        circlesPaint1.setDither(true);
        circlesPaint1.setColor(Color.LTGRAY);
        circlesPaint1.setStyle(Paint.Style.FILL);
        circlesPaint1.setStrokeJoin(Paint.Join.ROUND);
        circlesPaint1.setStrokeCap(Paint.Cap.ROUND);
        circlesPaint1.setStrokeWidth(5);
        circlesPaint2 = new Paint(Paint.DITHER_FLAG);
        circlesPaint2.setAntiAlias(true);
        circlesPaint2.setDither(true);
        circlesPaint2.setStyle(Paint.Style.STROKE);
        circlesPaint2.setColor(Color.BLACK);
        circlesPaint2.setStrokeJoin(Paint.Join.ROUND);
        circlesPaint2.setStrokeCap(Paint.Cap.ROUND);
        circlesPaint2.setStrokeWidth(5);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void showEllipse(float startX, float startY, float endX, float endY) {
        canvas.drawCircle(startX, startY, 30, circlesPaint1);
        canvas.drawCircle(startX, startY, 30, circlesPaint2);
        canvas.drawCircle(endX, endY, 30, circlesPaint1);
        canvas.drawCircle(endX, endY, 30, circlesPaint2);
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
