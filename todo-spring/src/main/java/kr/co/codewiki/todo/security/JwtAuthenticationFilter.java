package kr.co.codewiki.todo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // 리퀘스트에서 토큰 가져오기.
            String token = parseBearerToken(request);
            log.info("Filter is running...");

            // 토큰 검사하기. JWT이므로 인가 서버에 요청 하지 않고도 검증 가능.
            if (token != null && !token.equalsIgnoreCase("null")) {

                // userId 가져오기. 위조 된 경우 예외 처리 된다.
                String userId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated user ID : " + userId );

                // 인증 완료; SecurityContextHolder 에 등록해야 인증된 사용자라고 생각한다.
                // 인증된 사용자의 정보. 문자열이 아니어도 아무거나 넣을 수 있다. 보통은 UserDetails 라는 오브젝트를 넣는데, 여기서는 넣지 않는다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, AuthorityUtils.NO_AUTHORITIES);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);

                /*
                이 오브젝트에 사용자의 인증 정보를 저장하고, SecurityContext 에 인증된 사용자를 등록한다.
                등록하는 이유는,
                요청을 처리하는 과정에서 사용자가 인증됐는지 여부나, 인증된 사용자가 누군지 알아야 할 때가 있기 때문이다.
                 */
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    // 헤더에서 토큰 가져오기.
    private String parseBearerToken(HttpServletRequest request) {
        // Http 리퀘스트의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    // 이렇게 필터를 구현하고 나면 서블릿 컨테이너(톰켓) 가 JwtAuthenticationFilter 를 사용하도록 어딘가에 설정해야 한다.
    // 우리는 스프링 시큐리티를 사용하므로, 스프링 시큐리티에 JwtAuthenticationFilter 를 사용하라고 알려줘야 한다.
    // config 패키지의 WebSecurityConfig 클래스에다가 알려주면 됨

}
