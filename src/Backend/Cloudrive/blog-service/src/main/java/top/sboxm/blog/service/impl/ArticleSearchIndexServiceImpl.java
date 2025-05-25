package top.sboxm.blog.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import top.sboxm.blog.pojo.document.ArticleDocument;
import top.sboxm.blog.service.ArticleSearchIndexService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleSearchIndexServiceImpl implements ArticleSearchIndexService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public void deleteArticleIndex(String id) {
        try {
            elasticsearchOperations.delete(id, ArticleDocument.class);
        } catch (Exception e) {
            log.error("Failed to delete article index with id: {}", id);
            throw new RuntimeException("删除文章索引失败", e);
        }
    }
}
