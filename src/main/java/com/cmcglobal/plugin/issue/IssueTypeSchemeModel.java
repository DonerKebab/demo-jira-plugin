package com.cmcglobal.plugin.issue;

import java.util.ArrayList;

public class IssueTypeSchemeModel {
    private String name;
    private String description;
    private ArrayList<String> issueTypes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getIssueTypeIds() {
        return issueTypes;
    }

    public void setIssueTypeIds(ArrayList<String> issueTypes) {
        this.issueTypes = issueTypes;
    }
}
