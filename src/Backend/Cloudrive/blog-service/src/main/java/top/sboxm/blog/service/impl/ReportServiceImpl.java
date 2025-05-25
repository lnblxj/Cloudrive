package top.sboxm.blog.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.sboxm.blog.exceptions.ArticleException;
import top.sboxm.blog.mapper.ReportMapper;
import top.sboxm.blog.pojo.dto.CreateReportDTO;
import top.sboxm.blog.pojo.po.Report;
import top.sboxm.blog.service.ReportService;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.utils.SecurityContextUtil;

import java.sql.Timestamp;

/**
 * 举报服务实现类
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

    @Override
    public Report createReport(CreateReportDTO dto) {
        // 参数校验
        if (dto == null || dto.getBlogId() == null || dto.getReportType() == null) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        }

        try {
            // 获取当前用户ID
            String userId = SecurityContextUtil.getUserId();
            if (userId == null) {
                throw new ArticleException(RestResultEnum.NEED_LOGIN);
            }

            // 创建举报记录
            Report report = new Report();
            report.setId(IdUtil.getSnowflake().nextId());
            report.setBlogId(Long.parseLong(dto.getBlogId()));
            report.setReportUser(Long.parseLong(userId));
            report.setReportType(dto.getReportType());
            report.setReportContent(dto.getReportContent());
            report.setEvidence(dto.getEvidence());
            report.setStatus(0);
            report.setDelFlag(0);
            report.setReportTime(new Timestamp(System.currentTimeMillis()));

            // 保存举报记录
            this.save(report);
            return report;
        } catch (NumberFormatException e) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        } catch (Exception e) {
            throw new ArticleException(RestResultEnum.OPERATION_FAILED);
        }
    }

    @Override
    public void handleReport(String id, Integer status, String handleRemark) {
        if (id == null || status == null) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        }
        if (status < 0 || status > 2) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        }

        try {
            String userId = SecurityContextUtil.getUserId();
            if (userId == null) {
                throw new ArticleException(RestResultEnum.NEED_LOGIN);
            }
            Report report = this.getById(Long.parseLong(id));
            if (report == null) {
                throw new ArticleException(RestResultEnum.ARTICLE_NOT_FOUND);
            }
            report.setStatus(status);
            report.setHandler(Long.parseLong(userId));
            report.setHandleTime(new Timestamp(System.currentTimeMillis()));
            report.setHandleRemark(handleRemark);
            this.updateById(report);
        } catch (NumberFormatException e) {
            throw new ArticleException(RestResultEnum.INVALID_PARAM);
        } catch (Exception e) {
            throw new ArticleException(RestResultEnum.OPERATION_FAILED);
        }
    }
} 