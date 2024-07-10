package com.hmydk.codenamesuggest;

import com.hmydk.codenamesuggest.util.AIRequestUtil;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * EnglishNamingAction
 *
 * @author hmydk
 */
public class EnglishNamingAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        String selectedText = editor.getSelectionModel().getSelectedText();

        if (selectedText != null && !selectedText.isEmpty()) {
            String englishSuggestion = generateEnglishNaming(selectedText);
            Messages.showInfoMessage(englishSuggestion, "Code Naming Suggestion");
        } else {
            Messages.showWarningDialog("Please select some text first.", "No Text Selected");
        }
    }

    @Override
    public void update(AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabledAndVisible(
                editor != null && editor.getSelectionModel().hasSelection()
        );
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }

    private String generateEnglishNaming(String input) {
        // This could involve using a translation API, a custom algorithm, or a combination of both
        return "Suggested English Name: " + AIRequestUtil.getAIResponse(input.replace(' ', '_'));
    }
}
