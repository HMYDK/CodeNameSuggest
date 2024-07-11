package com.hmydk.codenamesuggest.config;


import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.ui.components.JBPasswordField;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ApiKeyConfigurable implements Configurable {
    private JBPasswordField apiKeyField;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "English Naming Suggestion";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        apiKeyField = new JBPasswordField();
        LabeledComponent<JBPasswordField> component = LabeledComponent.create(apiKeyField, "API Key:");
        component.setLabelLocation(BorderLayout.WEST);
        return component;
    }

    @Override
    public boolean isModified() {
        String storedApiKey = ApiKeySettings.getInstance().getApiKey();
        return !String.valueOf(apiKeyField.getPassword()).equals(storedApiKey);
    }

    @Override
    public void apply() throws ConfigurationException {
        ApiKeySettings.getInstance().setApiKey(String.valueOf(apiKeyField.getPassword()));
    }

    @Override
    public void reset() {
        String storedApiKey = ApiKeySettings.getInstance().getApiKey();
        apiKeyField.setText(storedApiKey);
    }
}
