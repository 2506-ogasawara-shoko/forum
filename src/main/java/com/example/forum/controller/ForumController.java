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
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        return mav;
    }

    /*
     * 新規投稿画面表示
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
     * 返信内容表示処理
     * (対応する投稿にそれぞれ表示)
     */
    @GetMapping
    public ModelAndView comment(@PathVariable Integer reportId) {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<CommentForm> textData = commentService.findAllComment(reportId);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", textData);
        return mav;
    }

    /*
     * 新規返信画面表示
     */
    @GetMapping("/top")
    public ModelAndView newComment() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        CommentForm commentForm = new CommentForm();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 準備した空のFormを保管
        mav.addObject("formModel", commentForm);
        return mav;
    }

    /*
     * 新規返信処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") CommentForm commentForm) {
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

}
