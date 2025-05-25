package top.sboxm.edge.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sboxm.common.result.RestResult;
import top.sboxm.edge.service.ArticleAuditService;
import top.sboxm.edge.pojo.dto.ArticleDTO;

@RestController
@RequestMapping("/audit")
@RequiredArgsConstructor
public class ArticleAuditController {
    private final ArticleAuditService articleAuditService;

    /**
     * 提交文章审核
     * @param articleDTO 文章DTO
     * @return 响应结果
     */
    @PostMapping
    public Object submitArticleAudit(@RequestBody ArticleDTO articleDTO) {
        articleAuditService.auditArticle(Long.parseLong(articleDTO.getArticleId()));
        return RestResult.ok();
    }
}
