package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "famelog")
public class DFameLog {

    @Id
    @Column(name = "famelogid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer famelogid;

    @Column(name = "characterid", nullable = false)
    private Integer characterid;

    @Column(name = "characterid_to", nullable = false)
    private Integer characteridTo;

    @Column(name = "when", nullable = false)
    private Date when;

    public void setFamelogid(Integer famelogid) {
        this.famelogid = famelogid;
    }

    public Integer getFamelogid() {
        return famelogid;
    }

    public void setCharacterid(Integer characterid) {
        this.characterid = characterid;
    }

    public Integer getCharacterid() {
        return characterid;
    }

    public void setCharacteridTo(Integer characteridTo) {
        this.characteridTo = characteridTo;
    }

    public Integer getCharacteridTo() {
        return characteridTo;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public Date getWhen() {
        return when;
    }

    @Override
    public String toString() {
        return "DFameLog{" +
                "famelogid=" + famelogid + '\'' +
                "characterid=" + characterid + '\'' +
                "characteridTo=" + characteridTo + '\'' +
                "when=" + when + '\'' +
                '}';
    }
}
