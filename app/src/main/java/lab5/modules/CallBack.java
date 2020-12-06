package lab5.modules;

import lab5.modules.shape_editor.shapes.Shape;

// Інтерфейс для реалізації функцій оберненого виклику
public interface CallBack {
    void addCallBack(Shape shape);
    void deleteCallBack(int index);
    void deleteByIndex(int index);
    void chooseFigure(int index);
    void unChooseFigure(int index);
}
