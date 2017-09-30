package com.example.springakka.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Ido on 2017/9/22.
 */
@Entity
@Table(name = "tag")
public class Tag implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @Column(length = 20)
    private String name;

    private int relateId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelateId() {
        return relateId;
    }

    public void setRelateId(int relateId) {
        this.relateId = relateId;
    }

}
