package top.sboxm.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.sboxm.blog.pojo.dto.PostListDTO;
import top.sboxm.blog.pojo.vo.PostCountVO;
import top.sboxm.blog.pojo.vo.PostDetailVO;
import top.sboxm.blog.pojo.vo.PostListVO;
import top.sboxm.blog.pojo.vo.UserPostListVO;
import top.sboxm.blog.service.PostService;
import top.sboxm.common.result.RestResult;

import java.util.List;

/**
 * 文章控制器
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Resource
    private PostService postService;

    /**
     * 分页查询文章列表
     * @param dto 查询参数
     * @return 分页结果
     */
    @GetMapping("/list")
    public Object list(PostListDTO dto) {
        return RestResult.ok().setData(postService.pageList(dto));
    }

    /**
     * 获取用户文章列表
     * @return 用户文章列表
     */
    @GetMapping("/user/list")
    public Object getUserPostList() {
        List<UserPostListVO> list = postService.getUserPostList();
        return RestResult.ok().setData(list);
    }

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    @GetMapping("/{id}")
    public Object getPostDetail(@PathVariable String id) {
        PostDetailVO detail = postService.getPostDetail(id);
        return RestResult.ok().setData(detail);
    }

    /**
     * 删除文章
     * @param id 文章ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Object deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return RestResult.ok();
    }

    /**
     * 修改文章状态（内部调用）
     * @param id 文章ID
     * @param status 新状态
     * @return 修改结果
     */
    @PutMapping("/{id}/status/{status}")
    public Object updateStatus(@PathVariable String id, @PathVariable Integer status) {
        postService.updateStatus(id, status);
        return RestResult.ok();
    }
    
    /**
     * 获取热门和精选文章数量
     * @return 热门和精选文章数量
     */
    @GetMapping("/count")
    public Object getPostCount() {
        PostCountVO countVO = postService.getPostCount();
        return RestResult.ok().setData(countVO);
    }
}
