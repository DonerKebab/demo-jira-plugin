package com.cmcglobal.plugin;

import com.atlassian.jira.bc.project.ProjectCreationData;
import com.atlassian.jira.bc.project.ProjectService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.config.IssueTypeManager;
import com.atlassian.jira.issue.CustomFieldManager;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfigScheme;
import com.atlassian.jira.issue.fields.screen.*;
import com.atlassian.jira.issue.issuetype.IssueType;
import com.atlassian.jira.project.Project;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.cmcglobal.plugin.constant.CommonConstant;
import com.cmcglobal.plugin.issue.IssueTypeInstallation;
import com.cmcglobal.plugin.issue.IssueTypeModel;
import com.cmcglobal.plugin.issue.IssueTypeSchemeModel;
import com.cmcglobal.plugin.utils.IssueTypeConfigReader;
import org.apache.log4j.Logger;
import org.ofbiz.core.entity.GenericEntityException;
import com.cmcglobal.plugin.customfield.CustomFieldModel;
import com.cmcglobal.plugin.customfield.CustomFieldInstallation;
import com.cmcglobal.plugin.utils.CustomFieldConfigReader;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@ExportAsService({PluginComponent.class})
@Component("componentListener")
public class PluginComponent implements InitializingBean, DisposableBean
{
    final static Logger logger = Logger.getLogger(PluginComponent.class);
    private ArrayList<CustomField> installedCustomField;
    private ArrayList<IssueType> installedIssueType;
    private IssueTypeInstallation issueTypeInstallation;
    private CustomFieldInstallation customFieldInstallation;

    @ComponentImport
    private CustomFieldManager customFieldManager;

    @ComponentImport
    private IssueTypeManager issueTypeManager;

    @ComponentImport
    private ProjectService projectService;

    @ComponentImport
    private FieldScreenFactory fieldScreenFactory;

    @ComponentImport
    private FieldScreenManager fieldScreenManager;

    @Inject
    public PluginComponent(CustomFieldManager customFieldManager, IssueTypeManager issueTypeManager, ProjectService projectService, FieldScreenFactory fieldScreenFactory, FieldScreenManager fieldScreenManager) {
        this.customFieldManager = customFieldManager;
        this.issueTypeManager = issueTypeManager;
        this.projectService = projectService;
        this.fieldScreenFactory = fieldScreenFactory;
        this.fieldScreenManager = fieldScreenManager;

        this.issueTypeInstallation = new IssueTypeInstallation(this.issueTypeManager);
        this.customFieldInstallation = new CustomFieldInstallation(this.customFieldManager, this.issueTypeInstallation);

        installedCustomField = new ArrayList<>();
        installedIssueType = new ArrayList<>();
    }

    @Override
    public void destroy() throws Exception {
        // remove custom field
        for (CustomField customField : installedCustomField) {
            this.customFieldManager.removeCustomField(customField);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        // create custome fields
        try {
            this.installCustomField();

            this.installIssueType();

            this.installProject();
        } catch (GenericEntityException e) {
            logger.error(e);
        }
    }


    private void installProject() throws IOException {
        IssueTypeConfigReader issueTypeConfigReader = new IssueTypeConfigReader();
        ArrayList<IssueTypeSchemeModel> issueTypeSchemeModels = issueTypeConfigReader.getListIssueTypeScheme();
        FieldConfigScheme issueSchemeModell;

        if(issueTypeSchemeModels.isEmpty()) {
            logger.info("CMCPLUGIN_ERROR: Issue type scheme config field is empty");
        }

        for (IssueTypeSchemeModel issueSchemeModel: issueTypeSchemeModels) {
            FieldConfigScheme issueTypeScheme = this.issueTypeInstallation.createIssueTypeSchemeOrGetIfExist(issueSchemeModel);
            issueSchemeModell = issueTypeScheme;
        }

        FieldScreen adCreateScreen = fieldScreenFactory.createScreen();
        adCreateScreen.setName("ADS Access Card Created");
        adCreateScreen.setDescription("ADS Access Card Created");
        adCreateScreen.store();

        FieldScreen defaultScreen = this.fieldScreenManager.getFieldScreen(FieldScreen.DEFAULT_SCREEN_ID);
        List<FieldScreenLayoutItem> defaultFields = defaultScreen.getTab(0).getFieldScreenLayoutItems();

        FieldScreenTab adCreatedTab = adCreateScreen.addTab("Create tab");
        for(FieldScreenLayoutItem fieldScreenLayoutItem: defaultFields) {
            adCreatedTab.addFieldScreenLayoutItem(fieldScreenLayoutItem.getOrderableField().getId());
        }
        adCreatedTab.store();

        ApplicationUser admin = ComponentAccessor.getUserManager().getUserByName(CommonConstant.ADMIN_USER_NAME);
        ProjectCreationData.Builder pcBuilder = new ProjectCreationData.Builder();
        pcBuilder.withName("IT Service")
                .withType("Software")
                .withLead(admin)
                .withKey("ITS")
                .withDescription("IT services");
        ProjectService.CreateProjectValidationResult result = this.projectService.validateCreateProject(admin , pcBuilder.build());
        if(result.isValid()) {
            Project itProject = projectService.createProject(result);
        }
    }

    private void installIssueType() throws IOException {
        IssueTypeConfigReader issueTypeConfigReader = new IssueTypeConfigReader();
        ArrayList<IssueTypeModel> issueTypeModels;

        issueTypeModels = issueTypeConfigReader.getListIssueType();

        if(issueTypeModels.isEmpty()) {
            logger.info("CMCPLUGIN_ERROR: Issue type config field is empty");
        }

        for (IssueTypeModel issueTypeModel: issueTypeModels) {
            IssueType issueType = this.issueTypeInstallation.createIssueType(issueTypeModel);
            installedIssueType.add(issueType);
        }
    }

    private void installCustomField() throws GenericEntityException, IOException {
        CustomFieldConfigReader customFieldConfigReader = new CustomFieldConfigReader();
        ArrayList<CustomFieldModel> customFieldModels;

        customFieldModels = customFieldConfigReader.getListCustomFiled();


        if(customFieldModels.isEmpty()) {
            logger.info("CMCPLUGIN_ERROR: Custom field config field is empty");
        }

        for(CustomFieldModel customField: customFieldModels){
                CustomField createdCustomField = this.customFieldInstallation.createCustomField(customField);
                this.installedCustomField.add(createdCustomField);
        }

    }

}