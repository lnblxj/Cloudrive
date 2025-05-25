package top.sboxm.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sboxm.blog.pojo.dto.CommentAddDTO;
import top.sboxm.blog.pojo.dto.CommentUpdateDTO;
import top.sboxm.blog.pojo.po.Comment;
import top.sboxm.blog.pojo.vo.CommentVO;

import java.util.List;

public interface CommentService extends IService<Comment> {
    /**
     * 添加评论
     * @param dto 评论信息
     * @return 评论ID
     */
    Long addComment(CommentAddDTO dto);

    /**
     * 修改评论
     * @param dto 评论信息
     */
    void updateComment(CommentUpdateDTO dto);

    /**
     * 删除评论
     * @param id 评论ID
     */
    void deleteComment(Long id);

    /**
     * 获取文章评论列表
     * @param articleId 文章ID
     * @return 评论列表
     */
    List<CommentVO> getCommentList(Long articleId);
}
