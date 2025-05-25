package top.sboxm.blog.constants;

/**
 * Redis常量类
 */
public class RedisConstants {
    
    /**
     * 文章阅读数前缀
     */
    public static final String ARTICLE_VIEW_COUNT_PREFIX = "article:viewCount:";
    
    /**
     * 文章评论数前缀
     */
    public static final String ARTICLE_COMMENT_COUNT_PREFIX = "article:commentCount:";
    
    /**
     * 热门文章数量前缀
     */
    public static final String ARTICLE_POPULAR_COUNT_KEY = "article:count:popular";
    
    /**
     * 精选文章数量前缀
     */
    public static final String ARTICLE_SELECTED_COUNT_KEY = "article:count:selected";
    
    /**
     * 文章数量缓存锁前缀
     */
    public static final String ARTICLE_COUNT_LOCK_PREFIX = "article:count:lock:";
    
    /**
     * 缓存过期时间（24小时）
     */
    public static final long CACHE_EXPIRE_TIME = 24;
    
    /**
     * 文章数量缓存刷新时间（10分钟）
     */
    public static final long ARTICLE_COUNT_REFRESH_TIME = 10;
}