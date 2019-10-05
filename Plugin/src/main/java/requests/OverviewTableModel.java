package requests;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

//http://www2.hawaii.edu/~takebaya/ics111/jtable_custom/jtable_custom.html
public class OverviewTableModel extends AbstractTableModel {
    private String[] columnNames = {"Antwort", "Tutor", "Datum"};
    private ArrayList<Answer> rowList;

    public OverviewTableModel() {
        //TODO: closedIssueList = rows!
        //rowList =  GitHubModel.closedIssueList;
    }

    @Override
    public int getRowCount() {
        int size;
        if (rowList == null) {
            size = 0;
            //TODO: Hier dann noch irgendwie eine Ausgabe, dass es noch keine Antworten gibt.
        } else {
            size = rowList.size();
        }
        return size;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    //! Hier wird´s spannend!
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

    // needed to show column names in JTable
    public String getColumnName(int col) {
        return columnNames[col];
    }
}
