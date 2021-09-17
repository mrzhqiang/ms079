package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wz_questdata")
public class DWzQuestData {

    @Id
    @Column(name = "questid", nullable = false)
    private Integer questid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "autoStart", nullable = false)
    private Integer autoStart;

    @Column(name = "autoPreComplete", nullable = false)
    private Integer autoPreComplete;

    @Column(name = "viewMedalItem", nullable = false)
    private Integer viewMedalItem;

    @Column(name = "selectedSkillID", nullable = false)
    private Integer selectedSkillID;

    @Column(name = "blocked", nullable = false)
    private Integer blocked;

    @Column(name = "autoAccept", nullable = false)
    private Integer autoAccept;

    @Column(name = "autoComplete", nullable = false)
    private Integer autoComplete;

    public void setQuestid(Integer questid) {
        this.questid = questid;
    }

    public Integer getQuestid() {
        return questid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setAutoStart(Integer autoStart) {
        this.autoStart = autoStart;
    }

    public Integer getAutoStart() {
        return autoStart;
    }

    public void setAutoPreComplete(Integer autoPreComplete) {
        this.autoPreComplete = autoPreComplete;
    }

    public Integer getAutoPreComplete() {
        return autoPreComplete;
    }

    public void setViewMedalItem(Integer viewMedalItem) {
        this.viewMedalItem = viewMedalItem;
    }

    public Integer getViewMedalItem() {
        return viewMedalItem;
    }

    public void setSelectedSkillID(Integer selectedSkillID) {
        this.selectedSkillID = selectedSkillID;
    }

    public Integer getSelectedSkillID() {
        return selectedSkillID;
    }

    public void setBlocked(Integer blocked) {
        this.blocked = blocked;
    }

    public Integer getBlocked() {
        return blocked;
    }

    public void setAutoAccept(Integer autoAccept) {
        this.autoAccept = autoAccept;
    }

    public Integer getAutoAccept() {
        return autoAccept;
    }

    public void setAutoComplete(Integer autoComplete) {
        this.autoComplete = autoComplete;
    }

    public Integer getAutoComplete() {
        return autoComplete;
    }

    @Override
    public String toString() {
        return "DWzQuestData{" +
                "questid=" + questid + '\'' +
                "name=" + name + '\'' +
                "autoStart=" + autoStart + '\'' +
                "autoPreComplete=" + autoPreComplete + '\'' +
                "viewMedalItem=" + viewMedalItem + '\'' +
                "selectedSkillID=" + selectedSkillID + '\'' +
                "blocked=" + blocked + '\'' +
                "autoAccept=" + autoAccept + '\'' +
                "autoComplete=" + autoComplete + '\'' +
                '}';
    }
}
