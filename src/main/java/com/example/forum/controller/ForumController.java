package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    @Autowired
    CommentService commentService;

    @Autowired
    HttpSession session;

    /* (Topページ)投稿・コメント内容表示処理 */
    @GetMapping
    // required=false:日付の入力が無くてもOK
    public ModelAndView top(@RequestParam(name = "start", required = false) String start, @RequestParam(name = "end", required = false) String end) {
        ModelAndView mav = new ModelAndView();
        // 投稿・コメントを全件取得
        List<ReportForm> contentData = reportService.searchReport(start, end);
        List<CommentForm> textData = commentService.findAllComment();
        // 画面遷移先
        mav.setViewName("/top");
        // 投稿・コメントデータオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("texts", textData);
        // 空のCommentFormを用意
        mav.addObject("commentFormModel", new CommentForm());
        // sessionが空でなければエラーメッセージ表示
        setErrorMessage(mav);
        return mav;
    }

    /* ↓ 投稿(Report)の処理 */

    /* 新規投稿画面(newページへの移動) */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        setErrorMessage(mav);
        return mav;
    }

    /* 新規投稿処理 */
    @PostMapping("/add")
    public ModelAndView addContent(@Validated @ModelAttribute("formModel") ReportForm reportForm, BindingResult result) {
        // バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "投稿内容を入力してください");
            return new ModelAndView("redirect:/new");
        }
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /* 投稿編集画面 */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // キーに該当するレコードを取得
        ReportForm report = reportService.editReport(id);
        // 準備した空のFormを保管
        mav.addObject("formModel", report);
        setErrorMessage(mav);
        mav.setViewName("/edit");
        return mav;
    }

    /* 投稿編集処理 */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@PathVariable Integer id, @Validated @ModelAttribute("formModel") ReportForm reportForm, BindingResult result) {
        // バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "投稿内容を入力してください");
            return new ModelAndView("redirect:/edit/{id}");
        }
        // UrlParameterのidを更新するentityにセット
        reportForm.setId(id);
        // 更新日時取得
        Timestamp updatedDate = new Timestamp(System.currentTimeMillis());
        reportForm.setUpdatedDate(updatedDate);
        // 投稿をテーブルに格納(新規投稿と同じメソッドへ)
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /* 投稿削除処理 */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 削除する投稿をテーブルに格納
        reportService.deleteReport(id);
        return new ModelAndView("redirect:/");
    }

    /* ↓ コメント(Comment)の処理 */

    /*　新規コメント */
    @PostMapping("/add/{reportId}")
    // バリデーション追加
    public ModelAndView addComment(@PathVariable Integer reportId, @Validated @ModelAttribute("commentFormModel") CommentForm commentForm, BindingResult result) {
        // バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "コメントを入力してください");
            session.setAttribute("reportId", reportId);
            return new ModelAndView("redirect:/");
        }
        // コメントに対応する投稿のIDを取得
        commentForm.setReportId(reportId);
        // 登録処理へ
        saveComment(commentForm);
        return new ModelAndView("redirect:/");
    }

    /* コメント編集画面 */
    @GetMapping("/commentEdit/{id}")
    public ModelAndView editComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        CommentForm comment = commentService.editComment(id);
        mav.addObject("commentFormModel", comment);
        setErrorMessage(mav);
        mav.setViewName("/commentEdit");
        return mav;
    }

    /* コメント更新 */
    @PutMapping("/commentUpdate/{id}")
    public ModelAndView updateComment(@PathVariable Integer id, Integer reportId, @Validated @ModelAttribute("commentFormModel") CommentForm commentForm, BindingResult result) {
        // バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "コメントを入力してください");
            return new ModelAndView("redirect:/commentEdit/{id}");
        }
        commentForm.setId(id);
        commentForm.setReportId(reportId);
        // 更新処理へ
        saveComment(commentForm);
        return new ModelAndView("redirect:/");
    }

    /* コメント登録・更新処理 */
    @Transactional
    private void saveComment(CommentForm commentForm) {
        // 更新日時取得
        Timestamp updatedDate = new Timestamp(System.currentTimeMillis());
        commentForm.setUpdatedDate(updatedDate);
        commentService.saveComment(commentForm);
        // ↓ reportの更新へ
        updateReportDate(commentForm);
        return;
    }

    /* 投稿の更新日時を更新 */
    private void updateReportDate(CommentForm commentForm) {
        ReportForm reportForm = new ReportForm();
        // reportId入れる
        reportForm.setId(commentForm.getReportId());
        // Idからreportの情報取得
        ReportForm report = reportService.editReport(commentForm.getReportId());
        reportForm.setContent(report.getContent());
        // 取得した更新日時を格納
        reportForm.setUpdatedDate(commentForm.getUpdatedDate());
        reportService.saveReport(reportForm);
        return;
    }

    /* コメント削除処理 */
    @DeleteMapping("/commentDelete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        commentService.deleteComment(id);
        return new ModelAndView("redirect:/");
    }

    /* エラーメッセージ */
    private void setErrorMessage(ModelAndView mav) {
        if (session.getAttribute("errorMessages") != null) {
            mav.addObject("errorMessages", session.getAttribute("errorMessages"));
            // コメント新規投稿時のみreportIdを追加
            if (session.getAttribute("reportId") != null) {
                mav.addObject("reportId", session.getAttribute("reportId"));
            }
            // sessionの破棄
            session.invalidate();
        }
    }
}
