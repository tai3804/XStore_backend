package iuh.fit.xstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Config de serve static files tu /uploads directory
 * Frontend co the access anh via: http://localhost:8080/uploads/products/productImage_UUID.jpg
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files từ uploads/ directory
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/")
                .setCachePeriod(3600);  // Cache 1 giờ

        // Serve comment files từ uploads/comments/ directory
        registry
                .addResourceHandler("/comments/**")
                .addResourceLocations("file:uploads/comments/")
                .setCachePeriod(3600);  // Cache 1 giờ

        // Serve avatar files từ uploads/avatars/ directory
        registry
                .addResourceHandler("/avatars/**")
                .addResourceLocations("file:uploads/avatars/")
                .setCachePeriod(3600);  // Cache 1 giờ
    }
}
