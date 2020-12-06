package lab5.modules.shape_editor.editor;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

import lab5.modules.shape_editor.MyEditorSingleton;
import lab5.modules.shape_editor.MyView;

// Клас редагування прямокутника
@SuppressLint("ViewConstructor")
public class RectangleShapeEditor extends ShapeEditor {
    public RectangleShapeEditor(MyEditorSingleton context) {
        super(context);
    }

    // Функція обробник дотику
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                myView.isDrawing = true;
                myView.lastEdited.startX = x;
                myView.lastEdited.startY = y;
                myView.lastEdited.endX = x;
                myView.lastEdited.endY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                myView.lastEdited.endX = x;
                myView.lastEdited.endY = y;
                myView.invalidate();
                break;
            case MotionEvent.ACTION_UP:
                myView.isDrawing = false;
                myView.addShapeToArray(myEditorSingleton.getCallback());
                break;
        }
    }
}
