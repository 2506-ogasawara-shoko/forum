package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    // 日付による投稿絞り込み
    // public List<Report> findByCreatedDateBetween(Data startDate, Data endDate);

    // ID降順
    public List<Report> findAllByOrderByIdDesc();
}
