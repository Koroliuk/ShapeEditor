package lab5.modules.shape_editor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

import lab5.modules.CallBack;
import lab5.modules.shape_editor.editor.EllipseShapeEditor;
import lab5.modules.shape_editor.editor.LineShapeEditor;
import lab5.modules.shape_editor.editor.PointShapeEditor;
import lab5.modules.shape_editor.editor.RectangleShapeEditor;
import lab5.modules.shape_editor.shapes.CircleEndedLineShape;
import lab5.modules.shape_editor.shapes.CubeShape;
import lab5.modules.shape_editor.shapes.EllipseShape;
import lab5.modules.shape_editor.shapes.LineShape;
import lab5.modules.shape_editor.shapes.PointShape;
import lab5.modules.shape_editor.shapes.RectangleShape;
import lab5.modules.shape_editor.shapes.Shape;

public class MyView extends View {

    // Динамічний масив фігур
    public List<Shape> showedShapes = new ArrayList<>();
    public boolean isDrawing = false;
    public Paint paint; // Задання стилю під час редагування (пунктир)
    public Bitmap bitmap;
    public Canvas canvas;
    public Shape lastEdited;
    public MyEditorSingleton myEditorContext;


    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{60, 40}, 0));
    }

    // Створення полотна
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        draw(canvas);
    }

    // Перевизначення методу, що малює об'єкти
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        for (Shape showedShape : showedShapes) {
            showedShape.canvas = canvas;
            showedShape.show();
        }

        if (isDrawing) {
            lastEdited.canvas = canvas;
            startEditing(lastEdited);
        }
    }

    // Функція, що додає фігуру до масиву фігур
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addShapeToArray(CallBack callback) {
        lastEdited.canvas = canvas;
        lastEdited.setPaint();
        showedShapes.add(lastEdited);
        callback.addCallBack(lastEdited);
        switch (lastEdited.type) {
            case POINT:
                lastEdited = new PointShape();
                break;
            case LINE:
                lastEdited = new LineShape();
                break;
            case RECTANGLE:
                lastEdited = new RectangleShape();
                break;
            case ELLIPSE:
                lastEdited = new EllipseShape();
                break;
            case LINE_WITH_CIRCLES:
                lastEdited = new CircleEndedLineShape();
                break;
            case CUBE:
                lastEdited = new CubeShape();
                break;
        }
        invalidate();
    }

    // Функція редагування введення фігури
    public void startEditing(Shape shape) {
        switch (shape.type) {
            case LINE:
                ((LineShape) shape).linePaint = paint;
                shape.show();
                break;
            case RECTANGLE:
                ((RectangleShape) shape).rectPaint1 = paint;
                ((RectangleShape) shape).rectPaint2 = paint;
                shape.show();
                break;
            case ELLIPSE:
                ((EllipseShape) shape).ellipsePaint = paint;
                shape.show();
                break;
            case LINE_WITH_CIRCLES:
                ((CircleEndedLineShape) shape).linePaint = paint;
                shape.show();
                break;
            case CUBE:
                ((CubeShape) shape).linePaint = paint;
                ((CubeShape) shape).rectPaint = paint;
                shape.show();
                break;
        }
    }

    // Функція обробник дотиків
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (lastEdited == null) {
            return false;
        }
        switch (lastEdited.type) {
            case POINT:
                PointShapeEditor pointShapeEditor = new PointShapeEditor(myEditorContext);
                pointShapeEditor.onTouchEvent(event);
                break;
            case LINE:
            case LINE_WITH_CIRCLES:
            case CUBE:
                LineShapeEditor lineShapeEditor = new LineShapeEditor(myEditorContext);
                lineShapeEditor.onTouchEvent(event);
                break;
            case RECTANGLE:
                RectangleShapeEditor rectangleShapeEditor = new RectangleShapeEditor(myEditorContext);
                rectangleShapeEditor.onTouchEvent(event);
                break;
            case ELLIPSE:
                EllipseShapeEditor ellipseShapeEditor = new EllipseShapeEditor(myEditorContext);
                ellipseShapeEditor.onTouchEvent(event);
                break;
        }
        return true;
    }

    // Функція, що стирає останню фігуру
    public void eraseLast(CallBack callback) {
        canvas.drawColor(Color.WHITE);
        int length = showedShapes.size();
        if (length > 1) {
            callback.deleteCallBack(showedShapes.size());
            showedShapes.remove(length-1);
            invalidate();
        }
        if (length == 1) {
            callback.deleteCallBack(showedShapes.size());
            showedShapes.clear();
            invalidate();
        }
    }

    // Функція, що видаляє за індексом
    public void eraseByIndex(int index) {
        canvas.drawColor(Color.WHITE);
        showedShapes.remove(index-1);
        invalidate();
    }
    // Функція, що стирає всі намальовані фігури
    public void eraseAll(CallBack callback) {
        canvas.drawColor(Color.WHITE);
        if (callback != null) {
            for (int i = showedShapes.size(); i > 0; i--) {
                callback.deleteCallBack(i);
            }
        }
        showedShapes.clear();
        invalidate();
    }
}
