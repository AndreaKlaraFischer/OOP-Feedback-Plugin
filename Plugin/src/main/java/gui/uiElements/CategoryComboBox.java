package gui.uiElements;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.intellij.openapi.ui.ComboBox;

public class CategoryComboBox implements ActionListener {
    //TODO: String Array for Categories (oder Enum?)
    // uneditable Combobox
    public CategoryComboBox() {
        String[] categoryList = { "A", "B", "C"};
        //Create the combo box, select the item at index 0.
        ComboBox categories = new ComboBox(categoryList);
        categories.setSelectedIndex(0);
        categories.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
