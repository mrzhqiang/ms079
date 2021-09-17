package com.github.mrzhqiang.maplestory.domain;

import javax.persistence.*;

@Entity
@Table(name = "notes")
public class DNote {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "to", nullable = false)
    private String to;

//    @Column(name = "from", nullable = false)
    @Column(name = "from_", nullable = false)
    private String from;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    @Column(name = "gift", nullable = false)
    private Integer gift;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setGift(Integer gift) {
        this.gift = gift;
    }

    public Integer getGift() {
        return gift;
    }

    @Override
    public String toString() {
        return "DNote{" +
                "id=" + id + '\'' +
                "to=" + to + '\'' +
                "from=" + from + '\'' +
                "message=" + message + '\'' +
                "timestamp=" + timestamp + '\'' +
                "gift=" + gift + '\'' +
                '}';
    }
}
