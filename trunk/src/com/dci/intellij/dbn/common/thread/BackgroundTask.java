package com.dci.intellij.dbn.common.thread;

import com.dci.intellij.dbn.common.Constants;
import com.dci.intellij.dbn.common.LoggerFactory;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BackgroundTask extends Task.Backgroundable {
    private static final Logger LOGGER = LoggerFactory.createLogger();

    private static PerformInBackgroundOption START_IN_BACKGROUND = new PerformInBackgroundOption() {
        public boolean shouldStartInBackground() { return true;}
        public void processSentToBackground() {}
    };

    private static PerformInBackgroundOption DO_NOT_START_IN_BACKGROUND = new PerformInBackgroundOption() {
        public boolean shouldStartInBackground() { return false;}
        public void processSentToBackground() {}
    };

    public void run(@NotNull ProgressIndicator progressIndicator) {
        try {
            execute(progressIndicator);
        } catch (InterruptedException e) {
            // no action required here
        } catch (Exception e) {
            LOGGER.error("Error executing background operation.", e);
        } finally {
            /*if (progressIndicator.isRunning()) {
                progressIndicator.stop();
            }*/
        }
    }

    public abstract void execute(@NotNull ProgressIndicator progressIndicator) throws InterruptedException;

    public BackgroundTask(@Nullable Project project, @NotNull String title, boolean startInBackground, boolean canBeCancelled) {
        super(project, Constants.DBN_TITLE_PREFIX + "" + title, canBeCancelled, startInBackground ? START_IN_BACKGROUND : DO_NOT_START_IN_BACKGROUND);
    }

    public BackgroundTask(@Nullable Project project, @NotNull String title, boolean startInBackground) {
        this(project, title, startInBackground, false);
    }

    public void start() {
        final ProgressManager progressManager = ProgressManager.getInstance();
        final BackgroundTask task = BackgroundTask.this;
        Application application = ApplicationManager.getApplication();

        if (application.isDispatchThread()) {
            progressManager.run(task);
        } else {
            Runnable runnable = new Runnable() {
                public void run() {
                    progressManager.run(task);
                }
            };
            application.invokeLater(runnable, ModalityState.NON_MODAL);
        }
    }

    public void initProgressIndicator(final ProgressIndicator progressIndicator, final boolean indeterminate) {
        initProgressIndicator(progressIndicator, indeterminate, null);
    }

    public void initProgressIndicator(final ProgressIndicator progressIndicator, final boolean indeterminate, @Nullable final String text) {
        new ConditionalLaterInvocator() {
            @Override
            public void run() {
                if (progressIndicator.isRunning()) {
                    progressIndicator.setIndeterminate(indeterminate);
                    if (text != null) progressIndicator.setText(text);
                }
            }
        }.start();
    }

}