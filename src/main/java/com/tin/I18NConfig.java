package com.tin;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class I18NConfig implements WebMvcConfigurer {
	@Bean("messageSource")
	public MessageSource getMessageSource() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setDefaultEncoding("utf-8");
		ms.setBasenames("classpath:i18n/global");
		return ms;
	}
	//Duy trì ngôn ngữ
	
		@Bean("localeResolver")
		public LocaleResolver getLocaleResolver() {
			CookieLocaleResolver cooki = new CookieLocaleResolver();
			cooki.setDefaultLocale(new Locale("vi"));
			//tất cả mọi đường dẫn dùng /
			cooki.setCookiePath("/");
			// sử dụng trong bao nhiêu lâu
			cooki.setCookieMaxAge(10*24*60*60);
			return cooki;
		}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//Chỉ ra tham số thay đổi Ngôn ngữ
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		registry.addInterceptor(lci)
		.addPathPatterns("/**")
		.excludePathPatterns("/images/**");
		
	}
	
}
