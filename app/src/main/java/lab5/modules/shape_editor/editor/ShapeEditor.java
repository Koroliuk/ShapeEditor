package lab5.modules.shape_editor.editor;

import android.annotation.SuppressLint;
import android.view.MotionEvent;

import lab5.modules.shape_editor.MyEditorSingleton;
import lab5.modules.shape_editor.MyView;

// задання класу для редагування фігур
@SuppressLint("ViewConstructor")
public class ShapeEditor extends Editor {

    public MyEditorSingleton myEditorSingleton;
    public MyView myView;

    public ShapeEditor(MyEditorSingleton myEditorSingleton) {
        this.myEditorSingleton = myEditorSingleton;
        this.myView = myEditorSingleton.getMyView();
    }

    // Функція обробник дотику
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
    }
}
