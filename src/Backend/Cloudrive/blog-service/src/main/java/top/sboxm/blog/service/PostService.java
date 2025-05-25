package top.sboxm.blog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import top.sboxm.blog.pojo.dto.CreatePostDTO;
import top.sboxm.blog.pojo.dto.PostListDTO;
import top.sboxm.blog.pojo.po.Post;
import top.sboxm.blog.pojo.vo.PostCountVO;
import top.sboxm.blog.pojo.vo.PostDetailVO;
import top.sboxm.blog.pojo.vo.PostListVO;
import top.sboxm.blog.pojo.vo.UserPostListVO;

import java.util.List;

/**
 * 文章服务接口
 */
public interface PostService {
    /**
     * 分页查询文章列表
     * @param dto 查询参数
     * @return 分页结果
     */
    IPage<PostListVO> pageList(PostListDTO dto);

    /**
     * 创建文章
     * @param dto 创建文章参数
     * @return 创建的文章
     */
    Post createPost(CreatePostDTO dto);

    /**
     * 获取用户文章列表
     * @return 用户文章列表
     */
    List<UserPostListVO> getUserPostList();

    /**
     * 获取文章详情
     * @param id 文章ID
     * @return 文章详情
     */
    PostDetailVO getPostDetail(String id);

    /**
     * 删除文章
     * @param id 文章ID
     */
    void deletePost(String id);

    /**
     * 修改文章状态
     * @param id 文章ID
     * @param status 新状态
     */
    void updateStatus(String id, Integer status);
    
    /**
     * 获取热门和精选文章数量
     * @return 热门和精选文章数量
     */
    PostCountVO getPostCount();
}
