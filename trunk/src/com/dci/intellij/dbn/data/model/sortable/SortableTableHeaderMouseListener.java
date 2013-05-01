package com.dci.intellij.dbn.data.model.sortable;

import com.dci.intellij.dbn.data.sorting.SortDirection;
import com.dci.intellij.dbn.data.ui.table.sortable.SortableTable;

import javax.swing.table.JTableHeader;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SortableTableHeaderMouseListener extends MouseAdapter {
    private SortableTable table;

    public SortableTableHeaderMouseListener(SortableTable table) {
        this.table = table;
    }

    public void mouseClicked(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            Point mousePoint = event.getPoint();
            mousePoint.setLocation(mousePoint.getX() - 4, mousePoint.getX());
            JTableHeader tableHeader = table.getTableHeader();
            int columnIndex = tableHeader.columnAtPoint(mousePoint);
            Rectangle colRect = tableHeader.getHeaderRect(columnIndex);
            boolean isEdgeClick = colRect.getMaxX() - 8 < mousePoint.getX();
            if (isEdgeClick) {
                if (event.getClickCount() == 2) {
                    table.accommodateColumnSize(columnIndex, 20);
                }
            } else {
                table.sort(columnIndex, SortDirection.INDEFINITE);
            }
        }
        table.requestFocus();
        //event.consume();
    }
}
