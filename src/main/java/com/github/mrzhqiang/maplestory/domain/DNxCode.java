package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "nxcode")
public class DNxCode {

    @Id
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "valid", nullable = false)
    private Integer valid;

    @Column(name = "user")
    private String user;

    @Column(name = "type", nullable = false)
    private Integer type;

    @Column(name = "item", nullable = false)
    private Integer item;

    @Column(name = "size")
    private Integer size;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }

    public Integer getValid() {
        return valid;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setItem(Integer item) {
        this.item = item;
    }

    public Integer getItem() {
        return item;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "DNxCode{" +
                "code=" + code + '\'' +
                "valid=" + valid + '\'' +
                "user=" + user + '\'' +
                "type=" + type + '\'' +
                "item=" + item + '\'' +
                "size=" + size + '\'' +
                '}';
    }
}
