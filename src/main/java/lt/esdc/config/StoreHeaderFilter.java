package lt.esdc.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StoreHeaderFilter implements Filter {

    private final PotionStoreProperties storeProperties;

    public StoreHeaderFilter(PotionStoreProperties storeProperties) {
        this.storeProperties = storeProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("X-Store-Name", storeProperties.getName());
        chain.doFilter(request, response);
    }
}