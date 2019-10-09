package com.cmcglobal.plugin.utils;

import com.cmcglobal.plugin.constant.CommonConstant;
import com.cmcglobal.plugin.issue.IssueTypeModel;
import com.cmcglobal.plugin.issue.IssueTypeSchemeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class IssueTypeConfigReader extends ConfigReader {
    private final String CONFIG_FILE = CommonConstant.ISSUE_TYPE_CONFIG;

    private final String ISSUE_SCHEME_FILE = CommonConstant.ISSUE_SCHEME_CONFIG;

    public ArrayList<IssueTypeModel> getListIssueType() throws IOException {

        ArrayList<IssueTypeModel> issueTypes ;
        BufferedReader bufferedReader = this.readFileFromResourceFolder(this.CONFIG_FILE);

        Type customFieldModelListType = new TypeToken<ArrayList<IssueTypeModel>>(){}.getType();
        Gson gson = new Gson();
        issueTypes = gson.fromJson(bufferedReader, customFieldModelListType);

        return issueTypes;
    }

    public ArrayList<IssueTypeSchemeModel> getListIssueTypeScheme() throws IOException {

        ArrayList<IssueTypeSchemeModel> issueTypeSchemes ;
        BufferedReader bufferedReader = this.readFileFromResourceFolder(this.ISSUE_SCHEME_FILE);

        Type customFieldModelListType = new TypeToken<ArrayList<IssueTypeSchemeModel>>(){}.getType();
        Gson gson = new Gson();
        issueTypeSchemes = gson.fromJson(bufferedReader, customFieldModelListType);

        return issueTypeSchemes;
    }
}
