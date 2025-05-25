package top.sboxm.blog.controller;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.sboxm.blog.pojo.dto.CommentAddDTO;
import top.sboxm.blog.pojo.dto.CommentUpdateDTO;
import top.sboxm.blog.pojo.vo.CommentVO;
import top.sboxm.blog.service.CommentService;
import top.sboxm.common.result.RestResult;

import java.util.List;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    
    @Resource
    private CommentService commentService;

    /**
     * 添加评论
     * @param dto 评论添加参数
     * @return 添加的评论信息
     */
    @PostMapping
    public Object addComment(@Validated @RequestBody CommentAddDTO dto) {
        return RestResult.ok().setData(commentService.addComment(dto));
    }

    /**
     * 更新评论
     * @param dto 评论更新参数
     * @return 更新结果
     */
    @PutMapping
    public Object updateComment(@Validated @RequestBody CommentUpdateDTO dto) {
        commentService.updateComment(dto);
        return RestResult.ok();
    }

    /**
     * 删除评论
     * @param id 评论ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Object deleteComment(@PathVariable String id) {
        commentService.deleteComment(Long.parseLong(id));
        return RestResult.ok();
    }

    /**
     * 获取文章评论列表
     * @param articleId 文章ID
     * @return 评论列表
     */
    @GetMapping("/list/{articleId}")
    public Object getCommentList(@PathVariable String articleId) {
        return RestResult.ok().setData(commentService.getCommentList(Long.parseLong(articleId)));
    }
}
