package lab5.modules;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Клас, що реалізує таблицю
public class Table implements TableInterface {

    private final Context context;
    public TableLayout table;
    public List<TableRow> rowList = new ArrayList<>();
    public TableRow.LayoutParams llpTitle;
    public TableRow.LayoutParams llpRow;
    public CallBack callback;

    public Table(Context context, TableLayout table) {
        this.context = context;
        this.table = table;
        llpTitle = new TableRow.LayoutParams(433, LinearLayout.LayoutParams.MATCH_PARENT);
        llpTitle.setMargins(2, 2, 2, 2);
        llpRow = new TableRow.LayoutParams(150, LinearLayout.LayoutParams.MATCH_PARENT);
        llpRow.setMargins(2, 2, 2, 2);
        addRow("Назва", "x1", "y1", "x2", "y2", Color.RED);
    }

    // Функція, що додає рядок до таблиці
    @Override
    public void addRow(String name, String x1, String y1, String x2, String y2, int textColor) {
        TableRow tr = new TableRow(context);
        tr.setBackgroundColor(Color.BLACK);
        tr.setClickable(true);
        if (!name.equals("Назва")) {
            tr.setOnClickListener(v -> {
                List<LinearLayout> cells = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    cells.add((LinearLayout) tr.getChildAt(i));
                }
                int index = rowList.indexOf(tr);
                int cellColor = ((ColorDrawable) cells.get(0).getBackground()).getColor();
                if (cellColor == Color.WHITE) {
                    for (LinearLayout cell : cells) {
                        cell.setBackgroundColor(Color.MAGENTA);
                    }
                    callback.chooseFigure(index);
                } else {
                    for (LinearLayout cell : cells) {
                        cell.setBackgroundColor(Color.WHITE);
                    }
                    callback.unChooseFigure(index);
                }
            });
            tr.setOnLongClickListener(v -> {
                int index = rowList.indexOf(tr);
                callback.deleteByIndex(index);
                table.removeView(tr);
                rowList.remove(tr);
                return true;
            });
        }

        LinearLayout cell1 = new LinearLayout(context);
        cell1.setBackgroundColor(Color.WHITE);
        cell1.setLayoutParams(llpTitle);
        LinearLayout cell2 = new LinearLayout(context);
        cell2.setBackgroundColor(Color.WHITE);
        cell2.setLayoutParams(llpRow);
        LinearLayout cell3 = new LinearLayout(context);
        cell3.setBackgroundColor(Color.WHITE);
        cell3.setLayoutParams(llpRow);
        LinearLayout cell4 = new LinearLayout(context);
        cell4.setBackgroundColor(Color.WHITE);
        cell4.setLayoutParams(llpRow);
        LinearLayout cell5 = new LinearLayout(context);
        cell5.setBackgroundColor(Color.WHITE);
        cell5.setLayoutParams(llpRow);

        TextView tv1 = new TextView(context);
        tv1.setText(name);
        tv1.setTextSize(18f);
        tv1.setPadding(15, 3, 3, 3);
        tv1.setTextColor(textColor);
        TextView tv2 = new TextView(context);
        tv2.setText(x1);
        tv2.setTextSize(18f);
        tv2.setPadding(15, 3, 3, 3);
        tv2.setTextColor(textColor);
        TextView tv3 = new TextView(context);
        tv3.setText(y1);
        tv3.setTextSize(18f);
        tv3.setPadding(15, 3, 3, 3);
        tv3.setTextColor(textColor);
        TextView tv4 = new TextView(context);
        tv4.setText(x2);
        tv4.setTextSize(18f);
        tv4.setPadding(15, 3, 3, 3);
        tv4.setTextColor(textColor);
        TextView tv5 = new TextView(context);
        tv5.setText(y2);
        tv5.setTextSize(18f);
        tv5.setPadding(15, 3, 3, 3);
        tv5.setTextColor(textColor);

        cell1.addView(tv1);
        cell2.addView(tv2);
        cell3.addView(tv3);
        cell4.addView(tv4);
        cell5.addView(tv5);

        tr.addView(cell1);
        tr.addView(cell2);
        tr.addView(cell3);
        tr.addView(cell4);
        tr.addView(cell5);

        table.addView(tr);
        rowList.add(tr);
    }

    // Функція, що видаляє рядок з таблиці
    @Override
    public void deleteRow(int index) {
        table.removeView(rowList.get(index));
        rowList.remove(index);
    }
}
