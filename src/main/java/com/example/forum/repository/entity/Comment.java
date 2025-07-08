package com.example.forum.repository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "comment")
@Getter
@Setter
public class Comment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String text;

    @Column(name = "reportId", insertable = true, updatable = false)
    private int reportId;

    // このカラムはinsert/updateのSQLに含まない
    @Column(name = "created_date", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Column(name = "updated_date", insertable = false, updatable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp updatedDate;
}
