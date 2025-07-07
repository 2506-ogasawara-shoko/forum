package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    @Autowired
    CommentService commentService;


    /*
     * 投稿・コメント内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(name="start") String start, @RequestParam(name="end") String end) {
        ModelAndView mav = new ModelAndView();
        // 投稿・コメントを全件取得
        // (投稿)日付指定の引数も加える？
        List<ReportForm> contentData = reportService.findAllReport();
        List<CommentForm> textData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿・コメントデータオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("texts", textData);
        // 空のCommentFormを用意
        mav.addObject("commentFormModel", new CommentForm());
        return mav;
    }

    /*
     * 新規投稿画面表示
     * newページへの移動
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm) {
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 新規コメント処理
     */
    @PostMapping("/add/{reportId}")
    // ModelAttribute:リクエストパラメータをオブジェクトにマッピングし、ビューに渡すためのアノテーション
    public ModelAndView addComment(@PathVariable Integer reportId, @ModelAttribute("commentFormModel") CommentForm commentForm) {
        // コメントに対応する投稿のIDを取得
        commentForm.setReportId(reportId);
        // コメントをテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿編集画面表示
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // キーに該当するレコードを取得
        ReportForm report = reportService.editReport(id);
        // 準備した空のFormを保管
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 投稿編集処理
     */
    @PutMapping("/update/{id}")
    // 今回はURLに同じidという名前があるため、引数にidが無くても回ってしまうが、
    // @PathVariable Integer idの表記は必要
    public ModelAndView updateContent(@PathVariable Integer id, @ModelAttribute("formModel") ReportForm reportForm) {
        // UrlParameterのidを更新するentityにセット
        reportForm.setId(id);
        // 投稿をテーブルに格納(新規投稿と同じメソッドへ)
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント編集画面表示
     */
    @GetMapping("/commentEdit/{id}")
    public ModelAndView editComment(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // キーに該当するレコードを取得
        CommentForm comment = commentService.editComment(id);
        // 準備した空のFormを保管
        mav.addObject("commentFormModel", comment);
        // 画面遷移先を指定
        mav.setViewName("/commentEdit");
        return mav;
    }

    /*
     * コメント編集処理
     */
    @PutMapping("/commentUpdate/{id}")
    public ModelAndView updateComment(@PathVariable Integer id, @ModelAttribute("commentFormModel") CommentForm commentForm) {
        // UrlParameterのidを更新するentityにセット
        commentForm.setId(id);
        // コメントをテーブルに格納(新規作成時と同じメソッドへ)
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }


    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 削除する投稿をテーブルに格納
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント削除処理
     */
    @DeleteMapping("/commentDelete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // 削除するコメントをテーブルに格納
        commentService.deleteComment(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

}
