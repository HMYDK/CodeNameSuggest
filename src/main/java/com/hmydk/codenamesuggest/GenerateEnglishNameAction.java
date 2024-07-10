package com.hmydk.codenamesuggest;

import com.hmydk.codenamesuggest.util.AIRequestUtil;
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
        System.out.println("这是选中的文本：" + selectedText);
        if (selectedText != null && !selectedText.trim().isEmpty()) {
            String englishSuggestion = generateEnglishName(selectedText);
            System.out.println("````````````````````````````");
            System.out.println(englishSuggestion);
            showSuggestionDialog(e.getProject(), englishSuggestion);
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

    private String generateEnglishName(String input) {
        return AIRequestUtil.getAIResponse(input.replace(' ', '_'));
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
            setTitle("English Naming Suggestion");
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
            scrollPane.setPreferredSize(new Dimension(500, 300));
            dialogPanel.add(scrollPane, BorderLayout.CENTER);

            return dialogPanel;
        }

        private static String formatSuggestionAsHtml(String suggestion) {
            String[] parts = suggestion.split("\n\n");
            StringBuilder html = new StringBuilder();
            for (String part : parts) {
                if (part.startsWith("Suggested Name:")) {
                    html.append("<h3>").append(part).append("</h3>");
                } else if (part.startsWith("Explanation:")) {
                    html.append("<p><strong>").append(part.split(":")[0]).append(":</strong> ")
                            .append(part.split(":")[1].trim()).append("</p>");
                } else if (part.startsWith("Alternative Suggestions:")) {
                    html.append("<p><strong>Alternative Suggestions:</strong></p><ul>");
                    String[] alternatives = part.split("\n");
                    for (int i = 1; i < alternatives.length; i++) {
                        html.append("<li>").append(alternatives[i]).append("</li>");
                    }
                    html.append("</ul>");
                } else if (part.startsWith("Additional Comments:")) {
                    html.append("<p><strong>").append(part.split(":")[0]).append(":</strong> ")
                            .append(part.split(":")[1].trim()).append("</p>");
                }
            }
            return html.toString();
        }

        public static void main(String[] args) {
            String ss = "## Naming Suggestions for \"逻辑删除\" (Logical Deletion)\n" +
                    "\n" +
                    "**Suggested Name:** isDeleted \n" +
                    "\n" +
                    "**Explanation:** This name clearly indicates the variable's purpose, which is to track whether an object has been logically deleted.  The prefix \"is\" aligns with common conventions for boolean variables.\n" +
                    "\n" +
                    "**Alternative Suggestions:**\n" +
                    "\n" +
                    "**deleted:** This is a concise and straightforward option. It might be preferred if the variable is not explicitly used to indicate a boolean state.\n" +
                    "\n" +
                    "**logicalDeletionFlag:** This option provides a more explicit explanation of the variable's purpose, but it can be overly verbose.\n" +
                    "\n" +
                    "**Additional Comments:**\n" +
                    "\n" +
                    "* The context of the variable is crucial. For example, if it's associated with a specific object, a name like `userIsDeleted` might be more appropriate.\n" +
                    "* If you are using a specific framework or library, check their conventions and best practices for naming boolean variables. Some libraries might have specific naming conventions, such as using a \"has\" prefix. \n";

            String conclusion = formatSuggestionAsHtml(ss);
            System.out.println("---------");
            System.out.println(conclusion);
        }
    }
}