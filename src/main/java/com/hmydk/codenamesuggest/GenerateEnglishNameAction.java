package com.hmydk.codenamesuggest;

import com.hmydk.codenamesuggest.config.ApiKeySettings;
import com.hmydk.codenamesuggest.util.AIRequestUtil;
import com.hmydk.codenamesuggest.util.MarkdownToHtmlConverter;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * GenerateEnglishNameAction
 * <p>
 * An IntelliJ IDEA plugin action to suggest English names based on selected text.
 *
 * @author hmydk
 */
public class GenerateEnglishNameAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        String selectedText = editor.getSelectionModel().getSelectedText();
        if (selectedText != null && !selectedText.trim().isEmpty()) {
            String codeSuggestion = generateAICodeName(selectedText);
            codeSuggestion = MarkdownToHtmlConverter.convertToHtml(codeSuggestion);
            showSuggestionDialog(e.getProject(), codeSuggestion);
        } else {
            JOptionPane.showMessageDialog(null, "Please select some text first.", "No Text Selected", JOptionPane.WARNING_MESSAGE);
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

    private String generateAICodeName(String input) {
        String apiKey = ApiKeySettings.getInstance().getApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please configure your Google API key first.", "No API Key Configured", JOptionPane.WARNING_MESSAGE);
            return "";
        }
        return AIRequestUtil.getAIResponse(apiKey, input.replace(' ', '_'));
    }

    private void showSuggestionDialog(Project project, String suggestion) {
        SuggestionDialog dialog = new SuggestionDialog(project, suggestion);
        dialog.show();
    }

    private static class SuggestionDialog extends DialogWrapper {
        private final String suggestion;

        public SuggestionDialog(@Nullable Project project, String suggestion) {
            super(project);
            this.suggestion = suggestion;
            init();
            setTitle("AI CodeName Suggest");
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JPanel dialogPanel = new JPanel(new BorderLayout());

            JEditorPane editorPane = new JEditorPane();
            editorPane.setContentType("text/html");
            editorPane.setText(suggestion);
            editorPane.setEditable(false);

            // Set up scrolling
            JBScrollPane scrollPane = new JBScrollPane(editorPane);
            scrollPane.setPreferredSize(new Dimension(900, 600));
            dialogPanel.add(scrollPane, BorderLayout.CENTER);

            return dialogPanel;
        }
    }
}