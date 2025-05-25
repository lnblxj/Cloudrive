package top.sboxm.blog.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import top.sboxm.blog.exceptions.SearchException;
import top.sboxm.blog.pojo.dto.SearchDTO;
import top.sboxm.blog.pojo.document.ArticleDocument;
import top.sboxm.blog.service.ArticleSearchService;
import top.sboxm.blog.pojo.vo.ArticleSearchVO;
import top.sboxm.common.enums.RestResultEnum;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleSearchServiceImpl implements ArticleSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public IPage<ArticleSearchVO> search(SearchDTO dto) {
        try {
            // 参数验证
            if (dto == null || dto.getKeyword() == null || dto.getKeyword().trim().isEmpty()) {
                throw new SearchException(RestResultEnum.INVALID_PARAM);
            }

            // 构建多字段模糊查询条件
            Criteria criteria = new Criteria()
                    .or(new Criteria("title").matches(dto.getKeyword()))
                    .or(new Criteria("content").matches(dto.getKeyword()))
                    .or(new Criteria("summary").matches(dto.getKeyword()));

            CriteriaQuery query = new CriteriaQuery(criteria);
            
            // 执行搜索
            SearchHits<ArticleDocument> searchHits;
            try {
                searchHits = elasticsearchOperations.search(query, ArticleDocument.class);
            } catch (Exception e) {
                throw new SearchException(RestResultEnum.SYSTEM_ERROR);
            }
            
            // 转换搜索结果
            List<ArticleSearchVO> records = searchHits.getSearchHits().stream()
                    .map(SearchHit::getContent)
                    .map(doc -> {
                        try {
                            ArticleSearchVO vo = new ArticleSearchVO();
                            vo.setId(String.valueOf(doc.getId()));
                            vo.setTitle(doc.getTitle());
                            vo.setSummary(doc.getSummary());
                            return vo;
                        } catch (Exception e) {
                            throw new SearchException(RestResultEnum.SYSTEM_ERROR);
                        }
                    })
                    .collect(Collectors.toList());

            // 构建分页结果
            Page<ArticleSearchVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
            page.setRecords(records);
            page.setTotal(searchHits.getTotalHits());
            
            return page;
        } catch (SearchException e) {
            throw e;
        } catch (Exception e) {
            throw new SearchException(RestResultEnum.SYSTEM_ERROR);
        }
    }
}
