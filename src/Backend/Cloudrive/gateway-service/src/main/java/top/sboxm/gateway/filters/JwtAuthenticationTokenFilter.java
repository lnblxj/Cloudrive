package top.sboxm.gateway.filters;

import top.sboxm.common.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationTokenFilter implements GlobalFilter, Ordered {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1.获取token及其载荷
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String authHeader = request.getHeaders().getFirst("Authorization");
        String token = null;
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        if (!StringUtils.hasText(token)){
            return chain.filter(exchange);
            //白名单
//            if (path.contains("login")||path.contains("articleList")||path.contains("register")||path.contains("commentList")||path.contains("linkCommentList")||path.contains("getAllLink")||path.contains("updateViewCount")||path.contains("board")||path.contains("tip")||path.contains("/article/details")||path.contains("/author")||path.contains("/user-service/info")){
//                return chain.filter(exchange);
//            }
        }
        if (!StringUtils.hasText(token)){
            response.setStatusCode(HttpStatusCode.valueOf(401));
            return response.setComplete();

        }
        String userId = JwtUtils.parse(token, "userId").toString();

        //缓存到请求域
        exchange = exchange.mutate()
                .request(
                        request.mutate().header("userId",userId).build()
                ).build();


        //从redis中获取用户信息
//        String loginUserDetailsJson = stringRedisTemplate.opsForValue().get("bloglogin:" + userId);
//        if (!StringUtils.hasText(loginUserDetailsJson)){
//            response.setStatusCode(HttpStatusCode.valueOf(403));
//            return  response.setComplete();
//        }
//
//        LoginUserDetails loginUserDetails = JSONUtil.toBean(loginUserDetailsJson, LoginUserDetails.class);


        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
