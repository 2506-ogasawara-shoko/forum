package com.example.forum.controller.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter

public class CommentForm {
    private int id;

    // バリデーション
    @NotBlank(message = "コメントを入力してください")
    private String text;

    private int reportId;
    private Timestamp createdDate;
    private Timestamp updatedDate;
}
