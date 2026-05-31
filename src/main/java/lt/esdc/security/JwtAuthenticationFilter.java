package lt.esdc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 1. Ищем заголовок Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Если заголовка нет или он не начинается с "Bearer ", идем дальше (возможно это публичный эндпоинт)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Достаем токен (отрезаем первые 7 символов: "Bearer ")
        jwt = authHeader.substring(7);
        
        // 3. Извлекаем имя пользователя из токена
        username = jwtService.extractUsername(jwt);

        // 4. Если имя есть, а пользователь еще не аутентифицирован в текущем контексте Spring Security
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // Ищем пользователя в базе (в нашем случае - в памяти)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Если токен валиден (не истек и подпись совпадает)
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                // Создаем объект аутентификации
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Сохраняем аутентификацию в контекст Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Передаем запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}