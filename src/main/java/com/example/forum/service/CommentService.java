package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    /* レコード全件取得処理 */
    public List<CommentForm> findAllComment() {
        // reportIdをキーに情報をとってくる
        List<Comment> results = commentRepository.findAllByOrderByUpdatedDateDesc();
        List<CommentForm> comments = setCommentForm(results);
        return comments;
    }

    /* DBから取得したデータをFormに設定 */
    private List<CommentForm> setCommentForm(List<Comment> results) {
        List<CommentForm> comments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            CommentForm comment = new CommentForm();
            Comment result = results.get(i);
            comment.setId(result.getId());
            comment.setText(result.getText());
            comment.setReportId(result.getReportId());
            comment.setUpdatedDate(result.getUpdatedDate());
            comments.add(comment);
        }
        return comments;
    }

    /* レコード追加・更新 */
    public void saveComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentRepository.save(saveComment);
    }

    /* リクエストから取得した情報をEntityに設定 */
    private Comment setCommentEntity(CommentForm reqComment) {
        Comment comment = new Comment();
        comment.setId(reqComment.getId());
        comment.setText(reqComment.getText());
        comment.setReportId(reqComment.getReportId());
        comment.setUpdatedDate(reqComment.getUpdatedDate());
        return comment;
    }

    /*　レコード1件取得 */
    public CommentForm editComment(Integer id) {
        List<Comment> results = new ArrayList<>();
        // findById:該当のレコード取得,取得できなければnullを入れる
        results.add((Comment) commentRepository.findById(id).orElse(null));
        List<CommentForm> comments = setCommentForm(results);
        // idを返す
        return comments.get(0);
    }

    /* レコード削除 */
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }
}
