package top.sboxm.blog.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.sboxm.blog.pojo.dto.ArticleDTO;
import top.sboxm.common.result.RestResult;

@FeignClient(value = "edge-service", path = "/audit")
public interface EdgeClient {
    /**
     * 提交文章审核
     * @param articleDTO 文章信息
     * @return 响应结果
     */
    @PostMapping
    RestResult<Void> submitArticleAudit(@RequestBody ArticleDTO articleDTO);
}
