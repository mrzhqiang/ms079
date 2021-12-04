package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "famelog")
public class DFameLog extends Model {

    @Id
    Integer id;
    @NotNull
    @OneToOne
    @JoinColumn(name = "character_id")
    DCharacter character;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid_to")
    DCharacter to;
    @NotNull
    @Column(name = "when")
    LocalDateTime when;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DCharacter getCharacter() {
        return character;
    }

    public void setCharacter(DCharacter character) {
        this.character = character;
    }

    public DCharacter getTo() {
        return to;
    }

    public void setTo(DCharacter to) {
        this.to = to;
    }

    public LocalDateTime getWhen() {
        return when;
    }

    public void setWhen(LocalDateTime when) {
        this.when = when;
    }
}
