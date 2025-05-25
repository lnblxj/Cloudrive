package top.sboxm.edge.service;

import top.sboxm.edge.pojo.po.Post;

public interface ArticleAuditService {
    /**
     * 审核文章内容
     * @param post 文章信息
     */
    /**
     * 审核文章内容
     * @param articleId 文章ID
     */
    void auditArticle(Long articleId);
}