package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "dueypackages")
public class DDueyPackage {

    @Id
    @Column(name = "PackageId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer packageId;

    @Column(name = "RecieverId", nullable = false)
    private Integer recieverId;

    @Column(name = "SenderName", nullable = false)
    private String senderName;

    @Column(name = "Mesos")
    private Integer mesos;

    @Column(name = "TimeStamp")
    private Long timeStamp;

    @Column(name = "Checked")
    private Integer checked;

    @Column(name = "Type", nullable = false)
    private Integer type;

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setRecieverId(Integer recieverId) {
        this.recieverId = recieverId;
    }

    public Integer getRecieverId() {
        return recieverId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setMesos(Integer mesos) {
        this.mesos = mesos;
    }

    public Integer getMesos() {
        return mesos;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    @Override
    public String toString() {
        return "DDueyPackage{" +
                "packageId=" + packageId + '\'' +
                "recieverId=" + recieverId + '\'' +
                "senderName=" + senderName + '\'' +
                "mesos=" + mesos + '\'' +
                "timeStamp=" + timeStamp + '\'' +
                "checked=" + checked + '\'' +
                "type=" + type + '\'' +
                '}';
    }
}
