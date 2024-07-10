package com.hmydk.codenamesuggest.util;


/**
 * Test
 *
 * @author hmydk
 */
public class Test {


    private static String formatSuggestionAsHtml(String suggestion) {
        // Convert Markdown to HTML
        String html = convertMarkdownToHtml(suggestion);

        // Wrap in HTML body
        return "<html><body style='font-family: Arial; font-size: 12pt;'>" + html + "</body></html>";
    }

    private static String convertMarkdownToHtml(String markdown) {
        // Simulate simple Markdown to HTML conversion
        return markdown
                .replaceAll("^## (.*)$", "<h3>$1</h3>") // Convert ## Header to <h3>Header</h3>
                .replaceAll("^\\*\\*([^:]*):\\*\\* (.*)$", "<p><strong>$1:</strong> $2</p>") // Convert **Key:** Value to <p><strong>Key:</strong> Value</p>
                .replaceAll("^\\*\\*([^:]*)\\*\\*$", "<p><strong>$1</strong></p>") // Convert **Key** to <p><strong>Key</strong></p>
                .replaceAll("^\\* (.*)$", "<li>$1</li>") // Convert * List item to <li>List item</li>
                .replaceAll("^\\*\\*([^:]*)\\*\\*$", "<p><strong>$1</strong></p>") // Convert **Key** to <p><strong>Key</strong></p>
                .replaceAll("^\\*\\*([^:]*)\\*\\*$", "<p><strong>$1</strong></p>");
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
