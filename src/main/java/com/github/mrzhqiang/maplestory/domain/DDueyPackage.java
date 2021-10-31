package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "dueypackages")
public class DDueyPackage extends Model {

    @Id
    @Column(name = "PackageId")
    public Integer id;
    @NotNull
    @Column(name = "RecieverId")
    public Integer recieverId;
    @NotNull
    @Column(name = "SenderName")
    public String senderName;
    @Column(name = "Mesos")
    public Integer mesos;
    @Column(name = "TimeStamp")
    public LocalDateTime timeStamp;
    @Column(name = "Checked")
    public Integer checked;
    @NotNull
    @Column(name = "Type")
    public Integer type;

}
