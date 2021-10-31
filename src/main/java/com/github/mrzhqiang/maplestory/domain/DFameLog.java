package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "famelog")
public class DFameLog extends Model {

    @Id
    @Column(name = "famelogid")
    public Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid")
    public DCharacter character;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "characterid_to")
    public DCharacter to;
    @NotNull
    @Column(name = "when")
    public LocalDateTime when;

}
