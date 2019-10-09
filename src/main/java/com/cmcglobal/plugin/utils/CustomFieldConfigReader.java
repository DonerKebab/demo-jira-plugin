package com.cmcglobal.plugin.utils;

import com.cmcglobal.plugin.constant.CommonConstant;
import com.cmcglobal.plugin.customfield.CustomFieldModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomFieldConfigReader extends ConfigReader {

    private final String CONFIG_FILE = CommonConstant.CUSTOM_FIELD_CONFIG;

    public ArrayList<CustomFieldModel> getListCustomFiled() throws IOException {

        ArrayList<CustomFieldModel> customFields ;
        BufferedReader bufferedReader = this.readFileFromResourceFolder(this.CONFIG_FILE);

        Type customFieldModelListType = new TypeToken<ArrayList<CustomFieldModel>>(){}.getType();
        Gson gson = new Gson();
        customFields = gson.fromJson(bufferedReader, customFieldModelListType);

        return customFields;
    }
}
