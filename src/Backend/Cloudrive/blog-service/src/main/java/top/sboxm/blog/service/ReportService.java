package top.sboxm.blog.service;

import top.sboxm.blog.pojo.dto.CreateReportDTO;
import top.sboxm.blog.pojo.po.Report;

/**
 * 举报服务接口
 */
public interface ReportService {
    /**
     * 创建举报
     * @param dto 举报信息
     * @return 创建的举报记录
     */
    Report createReport(CreateReportDTO dto);

    /**
     * 处理举报
     * @param id 举报ID
     * @param status 处理状态
     * @param handleRemark 处理备注
     */
    void handleReport(String id, Integer status, String handleRemark);
} 