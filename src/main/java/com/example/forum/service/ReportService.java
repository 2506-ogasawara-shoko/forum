package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /* 日付絞り込み処理 */
    public List<ReportForm> searchReport(String start, String end) {
        // フォーマット指定
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // start（end）が入力されたら時刻設定、そうでなければデフォルト値
        // hasTextがisBlankと同じ働きをしている
        if (hasText(start)) {
            start += " 00:00:00";
        } else {
            start = "2020-01-01 00:00:00";
        }

        if (hasText(end)) {
            end += " 23:59:59";
        } else {
            Date date = new Date();
            end = sdFormat.format(date);
        }

        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdFormat.parse(start);
            endDate = sdFormat.parse(end);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        List<Report> results = reportRepository.findByCreatedDateBetweenOrderByUpdatedDateDesc(startDate, endDate);
        return setReportForm(results);
    }

    /* DBから取得したデータをFormに設定 */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            report.setUpdatedDate(result.getUpdatedDate());
            reports.add(report);
        }
        return reports;
    }

    /* レコード追加・更新 */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /* リクエストから取得した情報をEntityに設定 */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        return report;
    }

    /*
     * レコード1件取得
     * findById：引数がid(キー),戻り値がReportForm(Optional<Entity>)
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        // findById:該当のレコード取得,取得できなければnullを入れる
        results.add((Report) reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        // idを返す
        return reports.get(0);
    }

    /* レコード削除 */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

}
