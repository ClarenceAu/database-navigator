package com.dci.intellij.dbn.browser.ui;

import com.dci.intellij.dbn.browser.model.BrowserTreeNode;
import com.dci.intellij.dbn.common.ui.DBNFormImpl;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

public abstract class DatabaseBrowserForm extends DBNFormImpl {
    private Project project;

    protected DatabaseBrowserForm(Project project) {
        this.project = project;
    }

    public Project getProject() {
        return project;
    }

    @Nullable
    public abstract DatabaseBrowserTree getBrowserTree();

    public abstract void selectElement(BrowserTreeNode treeNode, boolean requestFocus);

    public abstract void updateTree();

    public abstract void rebuild();

    @Override
    public void dispose() {
        super.dispose();
        project = null;
    }
}
