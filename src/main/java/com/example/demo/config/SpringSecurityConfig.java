package com.example.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	  private final AccessDeniedHandler accessDeniedHandler;
	  final DataSource dataSource;
	  
	  @Value("${spring.admin.username}")
	   private String adminUsername;

	  @Value("${spring.admin.password}")
	   private String adminPassword;

	  @Value("${spring.queries.users-query}")
	   private String usersQuery;

	  @Value("${spring.queries.roles-query}")
	   private String rolesQuery;
	  
	  @Autowired
	    public SpringSecurityConfig(AccessDeniedHandler accessDeniedHandler, DataSource dataSource) {
	        this.accessDeniedHandler = accessDeniedHandler;
	        this.dataSource = dataSource;
	    }
	  
	  
	  	// roles admin allow to access /admin/**
	    // roles user allow to access /user/**
	    // custom 403 access denied handler
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {

	        http.csrf().disable()
	                .authorizeRequests()
						//.antMatchers("/", "/home", "/about").permitAll()
	                	.antMatchers("/home", "/registration", "/error").permitAll()
						//.antMatchers("/admin/**").hasAnyRole("ADMIN")
						//.antMatchers("/user/**").hasAnyRole("USER")
						.anyRequest().authenticated()
	                .and()
	                .formLogin()
						.loginPage("/login")
						.defaultSuccessUrl("/home")
						.permitAll()
						.and()
	                .logout()
						.permitAll()
						.and()
	                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	    }
	    
	    /**
	     * Authentication details
	     */
	    @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

	        // Database authentication
	        auth.jdbcAuthentication()
	             .usersByUsernameQuery(usersQuery)
	             .authoritiesByUsernameQuery(rolesQuery)
	             .dataSource(dataSource)
	             .passwordEncoder(passwordEncoder());

	        // In memory authentication
	        auth.inMemoryAuthentication()
	                .withUser(adminUsername).password(adminPassword).roles("ADMIN");
	    }

	    /**
	     * Configure and return BCrypt password encoder
	     */
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
}
