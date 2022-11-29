package com.tin;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tin.custom.CustomOAuth2UserService;
import com.tin.custom.CustomUserDetailsService;
import com.tin.custom.OAuthLoginSuccessHandle;
import com.tin.entity.Account;
import com.tin.service.AccountService;
 
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	 	@Autowired
	    AccountService accountService;

	    @Autowired
	    private CustomOAuth2UserService oauth2UserService;

	    @Autowired
	    private OAuthLoginSuccessHandle oauthLoginSuccessHandler;
	    
	    @Bean
	    public UserDetailsService userDetailsService() {
	        return new CustomUserDetailsService();
	    }

	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable();
	        http.authorizeRequests()
			    		.antMatchers("/admin/**","/staff").hasAnyRole("Admin","Staff")
			    		.antMatchers("/order/**","/cart").authenticated()
			            .anyRequest().permitAll()
			            .and()
			            .exceptionHandling().accessDeniedPage("/403");
	        
	        http.formLogin()
	                .loginPage("/security/login")
	                .loginProcessingUrl("/security/login")
	                .defaultSuccessUrl("/security/login/success",false)
	                .failureUrl("/security/login/error")
	                .usernameParameter("username")
	                .passwordParameter("password")
	                .and()
	                .oauth2Login()
	                .loginPage("/security/login")
	                .userInfoEndpoint()
	                .userService(oauth2UserService)
	                .and()
	                .successHandler(oauthLoginSuccessHandler)
//	                .and()
//	                .logout().logoutSuccessUrl("/").permitAll()
	                .and()
	                .exceptionHandling().accessDeniedPage("/403");
	         
	        http.rememberMe();
	        
	        http.logout()
	                .logoutUrl("/security/logoff")
	                .logoutSuccessUrl("/security/logoff/success")
	                .deleteCookies("JSESSIONID");

	    }
	    
	    //thực hiện validate thông tin người dùng
	    @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        //call UserDetailService để xác thực thông tin người dùng.
	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(getPasswordEncoder());
	        return authProvider;
	    }

	    //Lấy thông tin account từ form login
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	    	BCryptPasswordEncoder pe = new BCryptPasswordEncoder();
	        auth.userDetailsService(username -> {
	            try {
	                Account account = accountService.findByUsername(username);
	                String password = account.getPassword();
	                String roles = account.getRole().getName();
	                Boolean isEnable = account.getIs_enable();
		            return User.withUsername(username).password(password).roles(roles).accountExpired(isEnable).build();
	                //return User.withUsername(username).password(password).roles(roles).build();
	            } catch (NoSuchElementException e) {
	                throw new UsernameNotFoundException(username + "Not Found !");
	            }
	        });
	    }

		//	Cơ chế mã hóa mật khẩu
	    @Bean
	    public BCryptPasswordEncoder getPasswordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    //	Cho phép truy xuất REST API từ bên ngoài (domain khác)
	    @Override
	    public void configure(WebSecurity web) throws Exception {
	        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	    }
}
