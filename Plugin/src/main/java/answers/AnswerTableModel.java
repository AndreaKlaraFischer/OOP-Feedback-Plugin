package answers;

import gui.MailBoxScreen;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

//http://www2.hawaii.edu/~takebaya/ics111/jtable_custom/jtable_custom.html
public class AnswerTableModel extends AbstractTableModel {
    private String[] columnNames = {"Antwort", "Tutor", "Datum"};
    private ArrayList<Answer> rowList;

    public AnswerTableModel(AnswerList answerList) {
        rowList = answerList.getAnswerList();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        int size;
        if (rowList == null) {
            size = 0;
        } else {
            size = rowList.size();
        }
        return size;
    }



    @Override
    public Object getValueAt(int row, int col) {
        Object temp = null;
        if (col == 0) {
            temp = rowList.get(row).getAnswerMessage();
        }
        else if (col == 1) {
            temp = rowList.get(row).getTutorName();
        }
        else if (col == 2) {
            temp = rowList.get(row).getAnswerDate();
        }
        return temp;
    }


    public Answer getAnswerAt(int row)
    {
        return rowList.get(row);
    }

    // needed to show column names in JTable
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /*public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }*/


}
