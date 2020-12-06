package lab5.modules.shape_editor.editor;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.RequiresApi;

import lab5.modules.shape_editor.MyEditorSingleton;

// Клас редагування еліпса
@SuppressLint("ViewConstructor")
public class EllipseShapeEditor extends ShapeEditor {
    public EllipseShapeEditor(MyEditorSingleton context) {
        super(context);
    }

    // Функція обробник дотику
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ClickableViewAccessibility")
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
