package com.dci.intellij.dbn.editor.data.ui.table.renderer;

import com.dci.intellij.dbn.common.ui.table.BasicTable;
import com.dci.intellij.dbn.common.ui.table.renderer.BasicTableCellRenderer;
import com.dci.intellij.dbn.data.editor.color.DataGridTextAttributes;
import com.dci.intellij.dbn.editor.data.ui.table.DatasetEditorTable;
import com.dci.intellij.dbn.editor.data.ui.table.model.DatasetEditorModelCell;
import com.dci.intellij.dbn.object.DBColumn;
import com.intellij.openapi.project.Project;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.UIUtil;

import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class DatasetEditorTableCellRenderer extends BasicTableCellRenderer {
    private static final Border CELL_ERROR_BORDER = new LineBorder(Color.RED, 1);

    public DatasetEditorTableCellRenderer(Project project) {
        super(project);
    }

    protected void customizeCellRenderer(JTable table, Object value, boolean isSelected, boolean hasFocus, int rowIndex, int columnIndex) {
        DataGridTextAttributes configTextAttributes = ((BasicTable) table).getConfigTextAttributes();

        DatasetEditorModelCell cell = (DatasetEditorModelCell) value;
        DatasetEditorTable datasetEditorTable = (DatasetEditorTable) table;


        if (cell != null && cell.getRow() != null) {
            boolean isLoading = datasetEditorTable.isLoading();
            boolean isInserting = datasetEditorTable.isInserting();

            boolean isDeletedRow = cell.getRow().isDeleted();
            boolean isInsertRow = cell.getRow().isInsert();
            boolean isCaretRow = table.getCellSelectionEnabled() && table.getSelectedRow() == rowIndex && table.getSelectedRowCount() == 1;


            //DataModelCell cellAtMouseLocation = datasetEditorTable.getCellAtMouseLocation();

            DBColumn column = cell.getColumnInfo().getColumn();
            if (cell.getUserValue() != null) {
                SimpleTextAttributes textAttributes =
                        isSelected ? configTextAttributes.getSelection() :
                        isLoading ? configTextAttributes.getLoadingData() :
                        isDeletedRow ? configTextAttributes.getDeletedData() :
                        column.isPrimaryKey() ? (isCaretRow ? configTextAttributes.getPrimaryKeyAtCaretRow() : configTextAttributes.getPrimaryKey()) :
                        column.isForeignKey() ? (isCaretRow ? configTextAttributes.getForeignKeyAtCaretRow() : configTextAttributes.getForeignKey()) :
                        cell.isModified() ? configTextAttributes.getModifiedData() :
                        cell.isLobValue() ? configTextAttributes.getReadonlyData() :
                                            configTextAttributes.getPlainData();

                writeUserValue(cell, textAttributes, configTextAttributes);

                
            } /*else {
                    append("[null]", SimpleTextAttributes.GRAYED_ATTRIBUTES);
                }*/

            //updateBorder(cell, datasetEditorTable);
            


            if (!isSelected) {
                if (isLoading || !datasetEditorTable.getDataset().getConnectionHandler().isConnected()) {
                    setBackground(configTextAttributes.getLoadingData().getBgColor());
                } else {
                    Color background = null;
                    if (cell.hasError()) {
                        setBorder(CELL_ERROR_BORDER);
                        setBackground(configTextAttributes.getErrorData().getBgColor());
                    }

                    if (isDeletedRow) {
                        setBackground(configTextAttributes.getDeletedData().getBgColor());
                    }

                    if (isInserting && !isInsertRow) {
                        setBackground(configTextAttributes.getReadonlyData().getBgColor());
                    }

                    if (isInsertRow) {
                        setBackground(UIUtil.getTableBackground());
                    }

                    if (isCaretRow) {
                        background = configTextAttributes.getCaretRow().getBgColor();
                    } else if (column.isPrimaryKey()) {
                        background = configTextAttributes.getPrimaryKey().getBgColor();
                    } else if (column.isForeignKey()) {
                        background = configTextAttributes.getForeignKey().getBgColor();
                    }

                    if (background != null) {
                        setBackground(background);
                    }

                }
            }
        }
    }
}
                                                                