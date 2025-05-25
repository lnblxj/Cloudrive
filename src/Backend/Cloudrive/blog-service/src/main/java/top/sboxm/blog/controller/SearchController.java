package top.sboxm.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sboxm.blog.pojo.dto.SearchDTO;
import top.sboxm.blog.service.ArticleSearchService;
import top.sboxm.blog.pojo.vo.ArticleSearchVO;
import top.sboxm.common.result.RestResult;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final ArticleSearchService articleSearchService;

    @PostMapping("/articles")
    public Object searchArticles(@RequestBody SearchDTO dto) {
        return RestResult.ok(articleSearchService.search(dto));
    }
}
