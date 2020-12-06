package lab5.modules.shape_editor;

import android.view.MotionEvent;

import lab5.modules.shape_editor.shapes.Shape;

// Інтерфейс класу MyEditor
public interface MyEditorInterface {
    void start(Shape shape);
    boolean onTouchEvent(MotionEvent event);
}
