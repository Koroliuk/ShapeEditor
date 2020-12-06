package lab5.modules.shape_editor;

import android.os.Build;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import lab5.modules.CallBack;
import lab5.modules.shape_editor.shapes.Shape;

public class MyEditorSingleton implements MyEditorInterface {

    private CallBack callback;
    private MyView myView;

    private static MyEditorSingleton myEditorSingleton;

    // Оголошення конструктору класу private
    private MyEditorSingleton(MyView myView, CallBack callback) {
        this.myView = myView;
        this.myView.myEditorContext = this;
        this.callback = callback;
    }

    // Функція для отримання екземпляру класу
    public static MyEditorSingleton getInstance(MyView myView, CallBack callback) {
        if (myEditorSingleton == null) {
            myEditorSingleton = new MyEditorSingleton(myView, callback);
        }
        return myEditorSingleton;
    }

    // Заборона копіювання об'єкту класу
    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public CallBack getCallback() {
        return callback;
    }

    public void setCallback(CallBack callback) {
        this.callback = callback;
    }

    public MyView getMyView() {
        return myView;
    }

    public void setMyView(MyView myView) {
        this.myView = myView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void start(Shape shape) {

        myView.lastEdited = shape;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
