package top.sboxm.blog.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.sboxm.blog.pojo.dto.CreateReportDTO;
import top.sboxm.blog.pojo.po.Report;
import top.sboxm.blog.service.ReportService;
import top.sboxm.common.result.RestResult;

/**
 * 举报控制器
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    /**
     * 创建举报
     * @param dto 举报信息
     * @return 创建结果
     */
    @PostMapping
    public Object createReport(@RequestBody CreateReportDTO dto) {
        Report report = reportService.createReport(dto);
        return RestResult.ok().setData(report);
    }

    /**
     * 处理举报
     * @param id 举报ID
     * @param status 处理状态
     * @param handleRemark 处理备注
     * @return 处理结果
     */
    @PutMapping("/{id}/handle")
    public Object handleReport(
            @PathVariable String id,
            @RequestParam Integer status,
            @RequestParam(required = false) String handleRemark) {
        reportService.handleReport(id, status, handleRemark);
        return RestResult.ok();
    }
} 