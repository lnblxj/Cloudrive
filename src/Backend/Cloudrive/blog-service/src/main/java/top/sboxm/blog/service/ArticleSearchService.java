package top.sboxm.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.sboxm.blog.pojo.dto.SearchDTO;
import top.sboxm.blog.pojo.vo.ArticleSearchVO;

public interface ArticleSearchService {
    /**
     * 搜索文章
     * @param dto 搜索参数
     * @return 分页搜索结果
     */
    IPage<ArticleSearchVO> search(SearchDTO dto);
}
