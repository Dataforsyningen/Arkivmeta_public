package dk.dataforsyningen.arkivmeta.caching;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

@Configuration
@EnableCaching
public class CachingConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    WebContentInterceptor interceptor = new WebContentInterceptor();
    interceptor.addCacheMapping(CacheControl.maxAge(7, TimeUnit.DAYS), "/*");
    registry.addInterceptor(interceptor);
  }

  @Bean
  public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
    FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean
        = new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
    filterRegistrationBean.addUrlPatterns("/*");
    filterRegistrationBean.setName("etagFilter");
    return filterRegistrationBean;
  }

  /**
   * Use softValues() https://stackoverflow.com/questions/299659/whats-the-difference-between-softreference-and-weakreference-in-java
   *
   * @return
   */
  @Bean
  public Caffeine caffeineConfig() {
    return Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).softValues();
  }

  /**
   * Use soft
   *
   * @param caffeine
   * @return
   */
  @Bean
  public CacheManager cacheManager(Caffeine caffeine) {
    CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
    caffeineCacheManager.setCaffeine(caffeine);
    caffeineCacheManager.setCacheNames(
        List.of("kortgrupper", "daekningsomraader", "maalestokke", "kort",
            "dokument", "dokumentsamling", "sognenavn", "herredsnavn"));
    return caffeineCacheManager;
  }
}