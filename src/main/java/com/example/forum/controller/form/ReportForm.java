package com.example.forum.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class ReportForm {

    private int id;
    private String content;
    private Date createdDate;
}
