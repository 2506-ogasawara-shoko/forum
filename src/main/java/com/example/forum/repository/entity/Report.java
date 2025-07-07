package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Table(name = "report")
@Getter
@Setter
public class Report {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String content;

    // このカラムはinsert/updateのSQLに含まない
    @Column(name = "created_date", insertable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    private Date createdDate;
}

