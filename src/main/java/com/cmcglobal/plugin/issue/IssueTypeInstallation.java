package com.cmcglobal.plugin.issue;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.issue.fields.config.FieldConfigScheme;
import com.atlassian.jira.issue.fields.config.manager.IssueTypeSchemeManager;
import com.atlassian.jira.issue.issuetype.IssueType;

import java.util.ArrayList;

public class IssueTypeInstallation {

    private IssueTypeManager issueTypeManager;

    public IssueTypeInstallation(IssueTypeManager issueTypeManager) {
        this.issueTypeManager = issueTypeManager;
    }

    public IssueType createIssueType(IssueTypeModel issueTypeModel) {
        IssueType issueType =  issueTypeManager.getIssueType(issueTypeModel.getName());

        if (issueType == null) {
            try{
                issueType = issueTypeManager.createIssueType(issueTypeModel.getName(), issueTypeModel.getName(), issueTypeModel.getAvatarId());
            }catch (IllegalStateException e) {
                System.out.println("Issue type already exist");
            }

        }

        return issueType;
    }

    public ArrayList<IssueType> getListIssueTypeByName(ArrayList<String> issueNameList) {

        ArrayList<IssueType> issueTypeList = new ArrayList<>();
        IssueType issueType;
        for (String issueName: issueNameList) {

            // get Issue type by id not by name
            issueType = issueTypeManager.getIssueType(issueName);

            if(issueType != null){
                issueTypeList.add(issueType);
            }
        }

        return issueTypeList;
    }

    public ArrayList<String> getListIssueTypeIds (ArrayList<String> issueNameList) {
        ArrayList<String> issueTypeIds = new ArrayList<>();
        IssueType issueType;

        if(issueNameList == null || issueNameList.isEmpty()) {
            return new ArrayList<>();
        }
        for (String issueName : issueNameList) {
            issueType = issueTypeManager.getIssueType(issueName);
            issueTypeIds.add(issueType.getId());
        }
        return issueTypeIds;
    }

    public FieldConfigScheme createIssueTypeSchemeOrGetIfExist(IssueTypeSchemeModel issueScheme) {
        IssueTypeSchemeManager issueTypeSchemeManager = ComponentAccessor.getIssueTypeSchemeManager();
        FieldConfigScheme fieldConfigScheme;
        for(FieldConfigScheme fieldConfig : issueTypeSchemeManager.getAllSchemes()) {
            if(issueScheme.getName().equals(fieldConfig.getName())) {
                return fieldConfig;
            }
        }

        ArrayList<String> issueTypeIds = this.getListIssueTypeIds(issueScheme.getIssueTypeIds());
        fieldConfigScheme = issueTypeSchemeManager.create(issueScheme.getName(), issueScheme.getDescription(), issueTypeIds );
        return fieldConfigScheme;
    }

}
