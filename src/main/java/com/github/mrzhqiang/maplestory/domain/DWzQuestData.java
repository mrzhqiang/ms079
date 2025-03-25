package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wz_questdata")
public class DWzQuestData extends Model {

    @Id
    Integer id;
    @NotNull
    String name;
    @NotNull
    Boolean autoStart;
    @NotNull
    Boolean autoPreComplete;
    @NotNull
    Integer viewMedalItem;
    @NotNull
    Integer selectedSkillId;
    @NotNull
    Boolean blocked;
    @NotNull
    Boolean autoAccept;
    @NotNull
    Boolean autoComplete;

    @OneToMany(mappedBy = "questData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<DWzQuestReqData> reqDataList;
    @OneToMany(mappedBy = "questData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<DWzQuestActData> actDataList;
    @OneToMany(mappedBy = "questData", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<DWzQuestPartyData> partyDataList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    public Boolean getAutoPreComplete() {
        return autoPreComplete;
    }

    public void setAutoPreComplete(Boolean autoPreComplete) {
        this.autoPreComplete = autoPreComplete;
    }

    public Integer getViewMedalItem() {
        return viewMedalItem;
    }

    public void setViewMedalItem(Integer viewMedalItem) {
        this.viewMedalItem = viewMedalItem;
    }

    public Integer getSelectedSkillId() {
        return selectedSkillId;
    }

    public void setSelectedSkillId(Integer selectedSkillId) {
        this.selectedSkillId = selectedSkillId;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public Boolean getAutoAccept() {
        return autoAccept;
    }

    public void setAutoAccept(Boolean autoAccept) {
        this.autoAccept = autoAccept;
    }

    public Boolean getAutoComplete() {
        return autoComplete;
    }

    public void setAutoComplete(Boolean autoComplete) {
        this.autoComplete = autoComplete;
    }

    public List<DWzQuestReqData> getReqDataList() {
        return reqDataList;
    }

    public void setReqDataList(List<DWzQuestReqData> reqDataList) {
        this.reqDataList = reqDataList;
    }

    public List<DWzQuestActData> getActDataList() {
        return actDataList;
    }

    public void setActDataList(List<DWzQuestActData> actDataList) {
        this.actDataList = actDataList;
    }

    public List<DWzQuestPartyData> getPartyDataList() {
        return partyDataList;
    }

    public void setPartyDataList(List<DWzQuestPartyData> partyDataList) {
        this.partyDataList = partyDataList;
    }
}
