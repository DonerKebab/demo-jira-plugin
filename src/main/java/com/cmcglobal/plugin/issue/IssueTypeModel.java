package com.cmcglobal.plugin.issue;

import java.util.ArrayList;

public class IssueTypeModel {

    private String name;
    private String avatarId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAvatarId() {
        return Long.parseLong(avatarId);
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }
}
