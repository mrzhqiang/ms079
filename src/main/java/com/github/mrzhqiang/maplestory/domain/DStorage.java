package com.github.mrzhqiang.maplestory.domain;

import io.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "storages")
public class DStorage extends Model {

    @Id
    @Column(name = "storageid")
    public Integer id;
    @OneToOne
    @JoinColumn(name = "accountid")
    public DAccount account;
    @Column(name = "slots")
    public Integer slots;
    @Column(name = "meso")
    public Integer meso;

}
