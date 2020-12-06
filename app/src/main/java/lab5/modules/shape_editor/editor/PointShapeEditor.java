package lab5.modules.shape_editor.editor;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

import lab5.modules.shape_editor.MyEditorSingleton;

// Клас редагування точки
@SuppressLint("ViewConstructor")
public class PointShapeEditor extends ShapeEditor {

    public PointShapeEditor(MyEditorSingleton context) {
        super(context);
    }

    // Функція обробник дотику
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                myView.isDrawing = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                myView.isDrawing = false;
                myView.lastEdited.endX = x;
                myView.lastEdited.endY = y;
                myView.addShapeToArray(myEditorSingleton.getCallback());
                break;
        }
    }
}
