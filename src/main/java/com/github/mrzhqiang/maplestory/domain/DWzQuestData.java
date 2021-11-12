package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "wz_questdata")
public class DWzQuestData extends Model {

    @Id
    @Column(name = "questid")
    public Integer id;
    @NotNull
    @Column(name = "name")
    public String name;
    @NotNull
    @Column(name = "autoStart")
    public Boolean autoStart;
    @NotNull
    @Column(name = "autoPreComplete")
    public Boolean autoPreComplete;
    @NotNull
    @Column(name = "viewMedalItem")
    public Integer viewMedalItem;
    @NotNull
    @Column(name = "selectedSkillID")
    public Integer selectedSkillID;
    @NotNull
    @Column(name = "blocked")
    public Boolean blocked;
    @NotNull
    @Column(name = "autoAccept")
    public Boolean autoAccept;
    @NotNull
    @Column(name = "autoComplete")
    public Boolean autoComplete;

    @OneToMany(mappedBy = "questData")
    public List<DWzQuestReqData> reqDataList;
    @OneToMany(mappedBy = "questData")
    public List<DWzQuestActData> actDataList;
    @OneToMany(mappedBy = "questData")
    public List<DWzQuestPartyData> partyDataList;
}
