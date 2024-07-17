package com.hmydk.codenamesuggest;

import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.util.Key;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * WelcomeNotification
 *
 * @author hmydk
 */
public class WelcomeNotification implements ProjectActivity {

    private static final String NOTIFICATION_GROUP_ID = "CodeNameSuggest Notifications";
    private static final String PLUGIN_NAME = "CodeNameSuggest";
    private static final String WELCOME_TITLE = "Welcome to CodeNameSuggest!";
    private static final String WELCOME_CONTENT = "Thank you for installing CodeNameSuggest. " +
            "To get started, please configure your API key in the settings.";
    private static final Key<Boolean> WELCOMED_KEY = Key.create("com.hmydk.codenamesuggest.welcomed");

    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        if (isFirstRun(project)) {
            showWelcomeNotification(project);
            markAsRun(project);
        }
        return Unit.INSTANCE;
    }

    private boolean isFirstRun(@NotNull Project project) {
        Boolean welcomed = project.getUserData(WELCOMED_KEY);
        return welcomed == null || !welcomed;
    }

    private void markAsRun(@NotNull Project project) {
        project.putUserData(WELCOMED_KEY, true);
    }

    private void showWelcomeNotification(@NotNull Project project) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(NOTIFICATION_GROUP_ID)
                .createNotification(WELCOME_TITLE, WELCOME_CONTENT, NotificationType.INFORMATION)
                .setIcon(null)  // You can set a custom icon here if you have one
                .addAction(new ConfigureAction())
                .notify(project);
    }

    private static class ConfigureAction extends com.intellij.openapi.actionSystem.AnAction {
        ConfigureAction() {
            super("Configure");
        }

        @Override
        public void actionPerformed(@NotNull com.intellij.openapi.actionSystem.AnActionEvent e) {
            com.intellij.openapi.options.ShowSettingsUtil.getInstance().showSettingsDialog(e.getProject(), PLUGIN_NAME);
        }
    }
}
