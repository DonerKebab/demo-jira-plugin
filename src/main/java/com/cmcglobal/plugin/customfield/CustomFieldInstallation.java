package com.cmcglobal.plugin.customfield;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.context.GlobalIssueContext;
import com.atlassian.jira.issue.context.JiraContextNode;
import com.atlassian.jira.issue.customfields.CustomFieldSearcher;
import com.atlassian.jira.issue.customfields.CustomFieldType;
import com.atlassian.jira.issue.customfields.manager.OptionsManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.config.FieldConfigScheme;
import com.atlassian.jira.issue.fields.config.manager.FieldConfigSchemeManager;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.cmcglobal.plugin.constant.CustomFieldConstant;
import com.cmcglobal.plugin.issue.IssueTypeInstallation;
import org.ofbiz.core.entity.GenericEntityException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomFieldInstallation {

    private CustomFieldManager customFieldManager;

    private IssueTypeInstallation issueTypeInstalltion;

    private OptionsManager optionsManager;

    @Inject
    public CustomFieldInstallation(CustomFieldManager customFieldManager, IssueTypeInstallation issueTypeInstalltion) {
        this.customFieldManager = customFieldManager;
        this.issueTypeInstalltion = issueTypeInstalltion;
        this.optionsManager = ComponentAccessor.getOSGiComponentInstanceOfType(OptionsManager.class);
    }

    public CustomField createCustomField(CustomFieldModel customFieldObject) throws GenericEntityException {
        CustomField customField = null;

        // check custom field already exist or not
        if(!isCustomFieldExist(customFieldObject.getName())){
            List<JiraContextNode> contexts = new ArrayList<>();
            contexts.add(GlobalIssueContext.getInstance());

            List<IssueType> issueTypes;
            if (customFieldObject.getIssueType() == null || customFieldObject.getIssueType().isEmpty()) {
                issueTypes = new ArrayList<>();
                issueTypes.add(null);
            } else {
                issueTypes = issueTypeInstalltion.getListIssueTypeByName(customFieldObject.getIssueType());
            }

            switch(customFieldObject.getType()) {
                case "multiselect":
                    customField = this.createMultiSelectField(customFieldObject.getName(), customFieldObject.getDescription(), contexts, issueTypes);
                    this.createOption(customField, customFieldObject.getOptions());
                    break;
                case "select":
                    customField = this.createSelectField(customFieldObject.getName(), customFieldObject.getDescription(), contexts, issueTypes);
                    break;
                case "number":
                    customField = this.createNumberField(customFieldObject.getName(), customFieldObject.getDescription(), contexts, issueTypes);
                    break;
                default:
                    throw new IllegalStateException("Unexpected Custom field type: " + customFieldObject.getType());
            }
        }

        return customField;
    }

    private CustomField createMultiSelectField(String name, String description, List<JiraContextNode> contexts,
                                               List<IssueType> issueTypes) throws GenericEntityException {
        return this.customFieldManager.createCustomField(name, description,
                this.getCustomFieldType(CustomFieldConstant.MULTISELECT_FIELD_TYPE),
                this.getCustomFieldSearcher(CustomFieldConstant.MULTISELECT_SEARCHER),
                contexts,
                issueTypes);
    }

    private CustomField createSelectField(String name, String description, List<JiraContextNode> contexts,
                                               List<IssueType> issueTypes) throws GenericEntityException {
        return this.customFieldManager.createCustomField(name, description,
                this.getCustomFieldType(CustomFieldConstant.SINGLECHOICE_FIELD_TYPE),
                this.getCustomFieldSearcher(CustomFieldConstant.SINGLECHOICE_SEARCHER),
                contexts,
                issueTypes);
    }

    private CustomField createNumberField(String name, String description, List<JiraContextNode> contexts,
                                          List<IssueType> issueTypes) throws GenericEntityException {
        return this.customFieldManager.createCustomField(name, description,
                this.getCustomFieldType(CustomFieldConstant.NUMBER_FIELD_TYPE),
                this.getCustomFieldSearcher(CustomFieldConstant.NUMBERFIELD_SEARCHER),
                contexts,
                issueTypes);
    }

    private boolean isCustomFieldExist(String name) {
        Collection existingCustomFields = this.customFieldManager.getCustomFieldObjectsByName(name);

        if(existingCustomFields.isEmpty()) {
            return false;
        }

        return true;
    }

    private CustomFieldType getCustomFieldType(String fieldTypePackage){
        CustomFieldType fieldType = null;

        fieldType = this.customFieldManager
                .getCustomFieldType(fieldTypePackage);

        return fieldType;
    }

    private CustomFieldSearcher getCustomFieldSearcher(String fieldSearcherPackage){
        CustomFieldSearcher fieldSearcher= null;

        fieldSearcher = this.customFieldManager
                .getCustomFieldSearcher(fieldSearcherPackage);

        return fieldSearcher;
    }

    private void createOption(CustomField customField, ArrayList<String> option) {
        FieldConfigSchemeManager fieldConfigSchemeManager =
                ComponentAccessor.getComponent(FieldConfigSchemeManager.class);
        List<FieldConfigScheme> schemes =
                fieldConfigSchemeManager.getConfigSchemesForField(customField);
        FieldConfigScheme fieldConfigScheme = schemes.get(0);
        FieldConfig config = fieldConfigScheme.getOneAndOnlyConfig();

        this.optionsManager.createOptions(config, null, Long.valueOf(1), option);
    }

}
