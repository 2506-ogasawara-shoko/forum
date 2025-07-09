package com.example.forum.repository;

import com.example.forum.repository.entity.Report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    // 日付による投稿絞り込み&更新日時降順
    public List<Report> findByCreatedDateBetweenOrderByUpdatedDateDesc(Date startDate, Date endDate);
}
