package lab5.modules;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import lab5.modules.shape_editor.MyEditorSingleton;
import lab5.modules.shape_editor.MyView;
import lab5.modules.shape_editor.Type;
import lab5.modules.shape_editor.shapes.CircleEndedLineShape;
import lab5.modules.shape_editor.shapes.CubeShape;
import lab5.modules.shape_editor.shapes.EllipseShape;
import lab5.modules.shape_editor.shapes.LineShape;
import lab5.modules.shape_editor.shapes.PointShape;
import lab5.modules.shape_editor.shapes.RectangleShape;
import lab5.modules.shape_editor.shapes.Shape;

// Створення класу MainActivity, що представляє головний екран додатку
public class MainActivity extends AppCompatActivity implements CallBack {

    // Ініціалізація об'єктів меню та класу ShapeObjectsEditor
    private MyEditorSingleton mMyEditorSingleton;
    private MyView myView;
    private Menu menu;
    private LinearLayout toolbar;
    private Table table;
    private List<File> listFiles;
    final boolean[] flag = {true};
    private LinearLayout mainLayout;
    private View tableView;



    MenuItem[] items;

    // Створюємо інтерфейс екрану
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        myView = findViewById(R.id.shape_editing);
        mMyEditorSingleton = MyEditorSingleton.getInstance(myView, this);
        // Наповнення toolBar
        toolbar = findViewById(R.id.tool_bar);
        createToolBar(toolbar);
        // Додавання таблиці
        LayoutInflater inflater = getLayoutInflater();
        mainLayout = findViewById(R.id.main_layout);
        tableView = inflater.inflate(R.layout.table, mainLayout, false);
        mainLayout.addView(tableView);
        TableLayout tableLayout = findViewById(R.id.table);
        table = new Table(this, tableLayout);
        table.callback = this;
        ImageButton buttonUp = findViewById(R.id.buttonUp);
        buttonUp.setOnClickListener(v -> {
            if (!flag[0]) {
                mainLayout.addView(tableView);
                flag[0] = true;
            }
        });
        ImageButton buttonDown = findViewById(R.id.buttonDown);
        buttonDown.setOnClickListener(v -> {
            if (flag[0]) {
                mainLayout.removeView(tableView);
                flag[0] = false;
            }
        });
        setListFiles();
    }

    // Створюємо меню, за розміткою з файлу R.menu.menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        init();
        return true;
    }

    // Створюємо обробник натиску елемента меню
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(!item.isChecked()) item.setChecked(true);
        switch(id){
            case R.id.point:
                mMyEditorSingleton.start(new PointShape());
                chooseOption(0);
                return true;
            case R.id.line:
                mMyEditorSingleton.start(new LineShape());
                chooseOption(1);
                return true;
            case R.id.rectangle:
                mMyEditorSingleton.start(new RectangleShape());
                chooseOption(2);
                return true;
            case R.id.ellipse:
                mMyEditorSingleton.start(new EllipseShape());
                chooseOption(3);
                return true;
            case R.id.circle_ended_line:
                mMyEditorSingleton.start(new CircleEndedLineShape());
                chooseOption(4);
                return true;
            case R.id.cube:
                mMyEditorSingleton.start(new CubeShape());
                chooseOption(5);
                return true;
            case R.id.save_as_file:
                writeToFile();
                return true;
            case R.id.load_as_file:
                if(listFiles.isEmpty()){
                    final AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                    builder1.setTitle(R.string.info_dialog_title1)
                            .setMessage(R.string.info_dialog_message1)
                            .setNeutralButton(R.string.info_dialog_button, (dialogInterface, i) -> dialogInterface.cancel());
                    AlertDialog dialogInfo1 = builder1.create();
                    dialogInfo1.show();
                }else{
                    String[] strings = new String[listFiles.size()];
                    for (int i = 0; i < listFiles.size(); i++) {
                        String[] data = String.valueOf(listFiles.get(i)).split("/");
                        strings[i] = data[data.length-1];
                    }
                    final File[] selected = new File[1]; // Змінна, що зберігатиме обраний файл
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                    builder2.setTitle(R.string.info_dialog_title1)
                            .setPositiveButton(R.string.action_positive, (dialogInterface, i) -> {
                                try {
                                    MyView myEditorView = mMyEditorSingleton.getMyView();
                                    myEditorView.eraseAll(null);
                                    loadFile(selected[0]);
                                    myEditorView.invalidate();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }) // Встановлення функції, що обробляє натиснення кнопки "Так"
                            .setNegativeButton(R.string.info_dialog_button, (dialogInterface, i) -> {
                                dialogInterface.cancel();
                                selected[0] = null;
                            }) // Встановлення функції, що обробляє натиснення кнопки "Відміна"
                            .setSingleChoiceItems(strings, -1, (dialogInterface, i) -> selected[0] = listFiles.get(i)); // Встановлення функції, що обробляє вибір групи користувача
                    AlertDialog dialog = builder2.create();
                    dialog.show();
                }
                return true;
            case R.id.info:
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.info_dialog_title)
                        .setMessage(R.string.info_dialog_message)
                        .setNeutralButton(R.string.info_dialog_button, (dialogInterface, i) -> dialogInterface.cancel());
                AlertDialog dialogInfo = builder.create();
                dialogInfo.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Функція, що визначає масив елементів меню. Для забезпечення синхронізації
    // вибору фігури введення
    private void init() {
        items = new MenuItem[]{menu.findItem(R.id.point), menu.findItem(R.id.line),
                menu.findItem(R.id.rectangle), menu.findItem(R.id.ellipse),
                menu.findItem(R.id.circle_ended_line), menu.findItem(R.id.cube)};
    }

    // Функції створення toolbar
    private void createToolBar(LinearLayout toolbar) {
        // Створення загальних спільних параметрів кнопок
        LinearLayout.LayoutParams imageLayoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.FILL_PARENT);

        // Створення кнопки для крапки
        ImageButton tableButton = new ImageButton(this);
        tableButton.setImageResource(R.drawable.table_icon);
        tableButton.setLayoutParams(imageLayoutParams);
        tableButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                if (flag[0]) {
                    mainLayout.removeView(tableView);
                    flag[0] = false;
                } else {
                    mainLayout.addView(tableView);
                    flag[0] = true;
                }
            }
        });
        tableButton.setOnLongClickListener(new OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                if (flag[0]) {
                    mainLayout.removeView(tableView);
                    flag[0] = false;
                } else {
                    mainLayout.addView(tableView);
                    flag[0] = true;
                }
                showToolTip(view, R.string.tableName);
                return true;
            }
        });

        // Створення кнопки для крапки
        ImageButton pointButton = new ImageButton(this);
        pointButton.setImageResource(R.drawable.dot_icon);
        pointButton.setLayoutParams(imageLayoutParams);
        pointButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                mMyEditorSingleton.start(new PointShape());
                updateOption(Type.POINT);
            }
        });
        pointButton.setOnLongClickListener(new OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                mMyEditorSingleton.start(new PointShape());
                updateOption(Type.POINT);
                showToolTip(view, R.string.point);
                return true;
            }
        });

        // Створення кнопки для лінії
        ImageButton lineButton = new ImageButton(this);
        lineButton.setImageResource(R.drawable.line_icon);
        lineButton.setLayoutParams(imageLayoutParams);
        lineButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                mMyEditorSingleton.start(new LineShape());
                updateOption(Type.LINE);
            }
        });
        lineButton.setOnLongClickListener(new OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                mMyEditorSingleton.start(new LineShape());
                updateOption(Type.LINE);
                showToolTip(view, R.string.line);
                return true;
            }
        });


        // Створення кнопки для прямокутника
        ImageButton rectButton = new ImageButton(this);
        rectButton.setImageResource(R.drawable.rect_icon);
        rectButton.setLayoutParams(imageLayoutParams);
        rectButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                mMyEditorSingleton.start(new RectangleShape());
                updateOption(Type.RECTANGLE);
            }
        });
        rectButton.setOnLongClickListener(new OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                mMyEditorSingleton.start(new RectangleShape());
                updateOption(Type.RECTANGLE);
                showToolTip(view, R.string.rectangle);
                return true;
            }
        });

        // Створення кнопки для еліпсу
        ImageButton ellipseButton = new ImageButton(this);
        ellipseButton.setImageResource(R.drawable.ellipse_icon);
        ellipseButton.setLayoutParams(imageLayoutParams);
        ellipseButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                mMyEditorSingleton.start(new EllipseShape());
                updateOption(Type.ELLIPSE);
            }
        });
        ellipseButton.setOnLongClickListener(new OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                mMyEditorSingleton.start(new EllipseShape());
                updateOption(Type.ELLIPSE);
                showToolTip(view, R.string.ellipse);
                return true;
            }
        });

        // Створення кнопки для стирання
        ImageButton eraserButton = new ImageButton(this);
        eraserButton.setImageResource(R.drawable.eraser_icon);
        eraserButton.setLayoutParams(imageLayoutParams);
        eraserButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                MyView myEditorView = mMyEditorSingleton.getMyView();
                myEditorView.eraseLast(MainActivity.this);
            }
        });
        eraserButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MyView myEditorView = mMyEditorSingleton.getMyView();
                myEditorView.eraseAll(MainActivity.this);
                showToolTip(view, R.string.erase);
                return true;
            }
        });

        // Створення кнопки для лінії з кружечками на кінцях
        ImageButton circleEndedLineButton = new ImageButton(this);
        circleEndedLineButton.setImageResource(R.drawable.circle_ended_line_icon);
        circleEndedLineButton.setLayoutParams(imageLayoutParams);
        circleEndedLineButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                mMyEditorSingleton.start(new CircleEndedLineShape());
                updateOption(Type.LINE_WITH_CIRCLES);
            }
        });
        circleEndedLineButton.setOnLongClickListener(new OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                mMyEditorSingleton.start(new CircleEndedLineShape());
                updateOption(Type.LINE_WITH_CIRCLES);
                showToolTip(view, R.string.circle_ended_line);
                return true;
            }
        });

        // Створення кнопки для куба
        ImageButton cubeButton = new ImageButton(this);
        cubeButton.setImageResource(R.drawable.cube_icon);
        cubeButton.setLayoutParams(imageLayoutParams);
        cubeButton.setOnClickListener(new OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                mMyEditorSingleton.start(new CubeShape());
                updateOption(Type.CUBE);
            }
        });
        cubeButton.setOnLongClickListener(new OnLongClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onLongClick(View view) {
                mMyEditorSingleton.start(new CubeShape());
                updateOption(Type.CUBE);
                showToolTip(view, R.string.cube);
                return true;
            }
        });
        
        // Усі кнопки реагують на короткі та довгі натиски
        // Саме при довгому натиску відображається підказка
        // Додавання кнопок до toolBar
        toolbar.addView(pointButton);
        toolbar.addView(lineButton);
        toolbar.addView(rectButton);
        toolbar.addView(ellipseButton);
        toolbar.addView(circleEndedLineButton);
        toolbar.addView(cubeButton);
        toolbar.addView(eraserButton);
        toolbar.addView(tableButton);
    }

    // Функцій, що реалізує показ підказок до кнопок
    private void showToolTip(View view, int string) {
        Toast toast = Toast.makeText(view.getContext(), string, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    // Функція, що оновлює опцію обрану користувачем
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updateOption(Type type) {
        switch (type) {
            case POINT:
                    chooseOption(0);
                break;
            case LINE:
                    chooseOption(1);
                break;
            case RECTANGLE:
                    chooseOption(2);
                break;
            case ELLIPSE:
                    chooseOption(3);
                break;
            case LINE_WITH_CIRCLES:
                    chooseOption(4);
                break;
            case CUBE:
                    chooseOption(5);
                break;
        }
    }

    // Функція, що обробляє обрання користувачем опції
    private void chooseOption(int index) {
        for (MenuItem item : items) {
            item.setChecked(false);
        }
        items[index].setChecked(true);
        for (int i = 0; i < 6; i++) {
            toolbar.getChildAt(i).getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
        }
        toolbar.getChildAt(index).getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
    }

    // Функція оберненого виклику для додавання фігури
    @Override
    public void addCallBack(Shape shape) {
        String name = getNameOfShape(shape);
        if (shape instanceof PointShape) {
            table.addRow(name, String.valueOf((int) shape.endX),String.valueOf((int) shape.endY),
                    String.valueOf((int) shape.endX), String.valueOf((int) shape.endY), Color.BLACK);
        } else {
            table.addRow(name, String.valueOf((int) shape.startX),String.valueOf((int) shape.startY),
                    String.valueOf((int) shape.endX), String.valueOf((int) shape.endY), Color.BLACK);
        }
    }

    // Функція оберненого виклику для видалення фігури
    @Override
    public void deleteCallBack(int index) {
        table.deleteRow(index);
    }

    // Функція запису намальованого в файл
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void writeToFile() {
        try {
            MyView myEditorView = mMyEditorSingleton.getMyView();
            List<String> lines = new ArrayList<>();
            String dirPath = this.getApplicationInfo().dataDir;
            StringBuilder fileName = new StringBuilder("output");
            fileName.append(listFiles.size());
            fileName.append(".txt");
            File file = new File(dirPath, String.valueOf(fileName));
            file.setWritable(true);
            file.setReadable(true);
            FileWriter writer = new FileWriter(file);
            for (Shape shape : myEditorView.showedShapes) {
                StringBuilder line = new StringBuilder(getNameOfShape(shape));
                if (shape instanceof PointShape) {
                    line.append("\t").append((int) shape.endX);
                    line.append("\t").append((int) shape.endY);
                } else {
                    line.append("\t").append((int) shape.startX);
                    line.append("\t").append((int) shape.startY);
                }
                line.append("\t").append((int) shape.endX);
                line.append("\t").append((int) shape.endY);
                line.append("\n");
                lines.add(String.valueOf(line));
                writer.append(line);
            }
            writer.flush();
            writer.close();
            Toast toast = Toast.makeText(this, "Успішно збережено в"+file.getAbsolutePath(), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            listFiles.add(file);
        } catch (IOException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(this, "Щось пішло не так", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }

    }

    // Функція завантаження намальованого з файлу
    public void loadFile(File file) throws FileNotFoundException {
        Scanner scan = new Scanner(file);
        final String[] names = new String[]{
                "Точка","Лінія","Прямокутник",
                "Еліпс","Лінія з кружечками","Куб"};
        String[] input = scan.nextLine().split("\t");
        boolean flag = false;
        for (String name: names) {
            if (name.equals(input[0])) {
                flag = true;
                break;
            }
        }
        if (flag) {
            if (input[0].equals(names[0])) {
                createShapeToEditArray(Type.POINT, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                        Integer.parseInt(input[3]), Integer.parseInt(input[4]));
            } else if (input[0].equals(names[1])) {
                createShapeToEditArray(Type.LINE, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                        Integer.parseInt(input[3]), Integer.parseInt(input[4]));
            } else if (input[0].equals(names[2])) {
                createShapeToEditArray(Type.RECTANGLE, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                        Integer.parseInt(input[3]), Integer.parseInt(input[4]));
            } else if (input[0].equals(names[3])) {
                createShapeToEditArray(Type.ELLIPSE, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                        Integer.parseInt(input[3]), Integer.parseInt(input[4]));
            } else if (input[0].equals(names[4])) {
                createShapeToEditArray(Type.LINE_WITH_CIRCLES, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                        Integer.parseInt(input[3]), Integer.parseInt(input[4]));
            } else {
                createShapeToEditArray(Type.CUBE, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                        Integer.parseInt(input[3]), Integer.parseInt(input[4]));
            }
            while (scan.hasNextLine()) {
                input = scan.nextLine().split("\t");
                if (input[0].equals(names[0])) {
                    createShapeToEditArray(Type.POINT, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                            Integer.parseInt(input[3]), Integer.parseInt(input[4]));
                } else if (input[0].equals(names[1])) {
                    createShapeToEditArray(Type.LINE, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                            Integer.parseInt(input[3]), Integer.parseInt(input[4]));
                } else if (input[0].equals(names[2])) {
                    createShapeToEditArray(Type.RECTANGLE, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                            Integer.parseInt(input[3]), Integer.parseInt(input[4]));
                } else if (input[0].equals(names[3])) {
                    createShapeToEditArray(Type.ELLIPSE, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                            Integer.parseInt(input[3]), Integer.parseInt(input[4]));
                } else if (input[0].equals(names[4])) {
                    createShapeToEditArray(Type.LINE_WITH_CIRCLES, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                            Integer.parseInt(input[3]), Integer.parseInt(input[4]));
                } else {
                    createShapeToEditArray(Type.CUBE, Integer.parseInt(input[1]), Integer.parseInt(input[2]),
                            Integer.parseInt(input[3]), Integer.parseInt(input[4]));
                }
           }
        } else {
            Toast toast = Toast.makeText(this, "Обраний файл \nне відповідає вимогам", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        scan.close();
    }

    // Функція, що перетворює дані з файлу
    public void createShapeToEditArray(Type type, int sx, int sy, int ex, int ey) {
        MyView myEditorView = mMyEditorSingleton.getMyView();
        Shape shape = null;
        switch (type) {
            case POINT:
                shape = new PointShape();
                shape.type = Type.POINT;
                break;
            case LINE:
                shape = new LineShape();
                shape.type = Type.LINE;
                break;
            case RECTANGLE:
                shape = new RectangleShape();
                shape.type = Type.RECTANGLE;
                break;
            case ELLIPSE:
                shape = new EllipseShape();
                shape.type = Type.ELLIPSE;
                break;
            case LINE_WITH_CIRCLES:
                shape = new CircleEndedLineShape();
                shape.type = Type.LINE_WITH_CIRCLES;
                break;
            case CUBE:
                shape = new CubeShape();
                shape.type = Type.CUBE;
                break;
        }
        assert shape != null;
        shape.startX = sx;
        shape.startY = sy;
        shape.endX = ex;
        shape.endY = ey;
        myEditorView.showedShapes.add(shape);
    }


    // Фунція видалення за індексом
    @Override
    public void deleteByIndex(int index) {

        MyView myEditorView = mMyEditorSingleton.getMyView();
        myEditorView.eraseByIndex(index);
    }

    // Функція, що забезпечує особливе відображення фігури при обранні певного рядка таблиці
    @Override
    public void chooseFigure(int index) {
        MyView myEditorView = mMyEditorSingleton.getMyView();
        Shape shape = myEditorView.showedShapes.get(index-1);
        switch (shape.type) {
            case POINT:
                Paint pointPaint = getPaintStroke();
                pointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                ((PointShape) shape).pointPaint = pointPaint;
                myEditorView.canvas.drawColor(Color.WHITE);
                myEditorView.invalidate();
                break;
            case LINE:
                ((LineShape) shape).linePaint = getPaintStroke();
                myEditorView.canvas.drawColor(Color.WHITE);
                myEditorView.invalidate();
                break;
            case RECTANGLE:
                ((RectangleShape) shape).rectPaint1 = getPaintFill();
                ((RectangleShape) shape).rectPaint2 = getPaintStroke();
                myEditorView.canvas.drawColor(Color.WHITE);
                myEditorView.invalidate();
                break;
            case ELLIPSE:
                ((EllipseShape) shape).ellipsePaint = getPaintStroke();
                myEditorView.canvas.drawColor(Color.WHITE);
                myEditorView.invalidate();
                break;
            case LINE_WITH_CIRCLES:
                ((CircleEndedLineShape) shape).linePaint = getPaintStroke();
                ((CircleEndedLineShape) shape).circlesPaint1 = getPaintFill();
                ((CircleEndedLineShape) shape).circlesPaint2 = getPaintFill();
                myEditorView.canvas.drawColor(Color.WHITE);
                myEditorView.invalidate();
                break;
            case CUBE:
                ((CubeShape) shape).linePaint = getPaintStroke();
                ((CubeShape) shape).rectPaint = getPaintStroke();
                myEditorView.canvas.drawColor(Color.WHITE);
                myEditorView.invalidate();
                break;
        }
    }

    // Повернення стилю фігури до початкового стану
    @Override
    public void unChooseFigure(int index) {
        MyView myEditorView = mMyEditorSingleton.getMyView();
        myEditorView.showedShapes.get(index-1).setPaint();
        myEditorView.canvas.drawColor(Color.WHITE);
        myEditorView.invalidate();
    }

    public String getNameOfShape(Shape shape) {
        String name = "";
        switch (shape.type) {
            case POINT:
                name = "Точка";
                break;
            case LINE:
                name = "Лінія";
                break;
            case RECTANGLE:
                name = "Прямокутник";
                break;
            case ELLIPSE:
                name = "Еліпс";
                break;
            case LINE_WITH_CIRCLES:
                name = "Лінія з кружечками";
                break;
            case CUBE:
                name = "Куб";
                break;
        }
        return name;
    }

    private Paint getPaintFill() {
        Paint paint = new Paint(Paint.DITHER_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#ffefcc"));
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
        return paint;
    }

    private Paint getPaintStroke() {
        Paint paint = new Paint(Paint.DITHER_FLAG);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#eba434"));
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(5);
        return paint;
    }

    // Функція, що повертає список попередньо збережених файлів користувачем
    private void setListFiles() {
        final String dir = this.getApplicationInfo().dataDir;
        File file = new File(dir);
        List<File> listOfAllFiles = Arrays.asList(Objects.requireNonNull(file.listFiles()));
        listFiles = new ArrayList<>();
        for (File file1 : listOfAllFiles) {
            String[] data = String.valueOf(file1).split("/");
            String name = data[data.length-1];
            if (!(name.equals("cache") || name.equals("code_cache") || name.equals("databases"))) {
                listFiles.add(file1);
            }

        }
    }
}
