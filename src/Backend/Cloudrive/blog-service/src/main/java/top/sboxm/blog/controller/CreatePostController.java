package top.sboxm.blog.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.sboxm.blog.pojo.dto.CreatePostDTO;
import top.sboxm.blog.pojo.po.Post;
import top.sboxm.blog.service.PostService;
import top.sboxm.common.result.RestResult;

/**
 * 文章创建控制器
 */
@RestController
@RequestMapping("/post")
public class CreatePostController {

    @Resource
    private PostService postService;

    /**
     * 创建文章
     * @param dto 创建文章参数
     * @return 创建结果
     */
    @PostMapping("/create")
    public Object createPost(@RequestBody CreatePostDTO dto) {
        Post post = postService.createPost(dto);
        return RestResult.ok().setData(post);
    }
}
