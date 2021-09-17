package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reports")
public class DReport {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reporttime", nullable = false)
    private Date reporttime;

    @Column(name = "reporterid", nullable = false)
    private Integer reporterid;

    @Column(name = "victimid", nullable = false)
    private Integer victimid;

    @Column(name = "reason", nullable = false)
    private Integer reason;

    @Column(name = "chatlog", nullable = false)
    private String chatlog;

    @Column(name = "status", nullable = false)
    private String status;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setReporttime(Date reporttime) {
        this.reporttime = reporttime;
    }

    public Date getReporttime() {
        return reporttime;
    }

    public void setReporterid(Integer reporterid) {
        this.reporterid = reporterid;
    }

    public Integer getReporterid() {
        return reporterid;
    }

    public void setVictimid(Integer victimid) {
        this.victimid = victimid;
    }

    public Integer getVictimid() {
        return victimid;
    }

    public void setReason(Integer reason) {
        this.reason = reason;
    }

    public Integer getReason() {
        return reason;
    }

    public void setChatlog(String chatlog) {
        this.chatlog = chatlog;
    }

    public String getChatlog() {
        return chatlog;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "DReport{" +
                "id=" + id + '\'' +
                "reporttime=" + reporttime + '\'' +
                "reporterid=" + reporterid + '\'' +
                "victimid=" + victimid + '\'' +
                "reason=" + reason + '\'' +
                "chatlog=" + chatlog + '\'' +
                "status=" + status + '\'' +
                '}';
    }
}
