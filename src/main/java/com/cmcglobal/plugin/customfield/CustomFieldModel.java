package com.cmcglobal.plugin.customfield;

import java.util.ArrayList;

public class CustomFieldModel {

    private String name;
    private String type;
    private String description;
    private ArrayList<String> options;

    public ArrayList<String> getIssueTypes() {
        return issueTypes;
    }

    public void setIssueTypes(ArrayList<String> issueTypes) {
        this.issueTypes = issueTypes;
    }

    private ArrayList<String> issueTypes;

    public ArrayList<String> getIssueType() {
        return issueType;
    }

    public void setIssueType(ArrayList<String> issueType) {
        this.issueType = issueType;
    }

    private ArrayList<String> issueType;

    public CustomFieldModel() {

    }

    public CustomFieldModel(String name, String type, String description, ArrayList<String> options) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.options = options;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
