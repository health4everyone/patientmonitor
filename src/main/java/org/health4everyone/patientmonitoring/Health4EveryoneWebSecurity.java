package org.health4everyone.patientmonitoring;

import javax.sql.DataSource;

import org.health4everyone.patientmonitoring.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class Health4EveryoneWebSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private DataSource dataSource;
	
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

	
    @Bean(name = "pwdEncoder")
    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder delPasswordEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories
                .createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        delPasswordEncoder.setDefaultPasswordEncoderForMatches(bcryptPasswordEncoder);
        return delPasswordEncoder;
    }
	
    @Override
    public void configure(HttpSecurity http) throws Exception{

    	http.csrf().disable();
    	// Device APIs
    	http.authorizeRequests().antMatchers("/device/*").permitAll();
    	// These pages does not require login
        http.authorizeRequests().antMatchers("/login", "/logout").permitAll();
        http.authorizeRequests().antMatchers("/register").permitAll();
        http.authorizeRequests().antMatchers("/recover-password").permitAll();
        // Dashboard
        http.authorizeRequests().antMatchers("/","/index","/index.html","/dashboard","/dashboard/*").authenticated();
        // Devices
        http.authorizeRequests().antMatchers("/devices","/devices/","/devices/data","/devices/details").authenticated();
        
        http.authorizeRequests().antMatchers("/userInfo").access("hasAnyRole('ROLE_PATIENT', 'ROLE_PHYSICIAN', 'ROLE_ADMIN')");
 
        http.authorizeRequests().antMatchers("/users").access("hasRole('ROLE_ADMIN')");
        // For ADMIN only.
        http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");
        // For PATIENT only.
        http.authorizeRequests().antMatchers("/patient").access("hasAnyRole('ROLE_PATIENT','ROLE_ADMIN','ROLE_PHYSICIAN')");

        //For PHYSICIAN only
        http.authorizeRequests().antMatchers("/physician").access("hasAnyRole('ROLE_PHYSICIAN','ROLE_ADMIN')");
        
        // When the user has logged in as XX.
        // But access a page that requires role YY,
        // AccessDeniedException will be thrown.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
 
        // Config for Login Form
        http.authorizeRequests().and().formLogin()//
                // Submit URL of login page.
                .loginProcessingUrl("/j_spring_security_check") // Submit URL
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")//
                .failureUrl("/login?error=true")//
                .usernameParameter("username")//
                .passwordParameter("password")
                // Config for Logout Page
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
 
        // Config Remember Me.
        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h    
    }
    
 // Token stored in Memory (Of Web Server).
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
    	JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
    	db.setDataSource(dataSource);
    	return db;
/*        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
        return memory;*/
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
 
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
 
        // Setting Service to find User in the database.
        // And Setting PassswordEncoder
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
 
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery("select email as principal, password as credentails, true from users where email=?")
                .authoritiesByUsernameQuery("select users.email as principal, roles.name as role from user_roles as ur join users on ur.user_id=users.id join roles on ur.role_id=roles.id where users.email=?")
                .passwordEncoder(passwordEncoder()).rolePrefix("ROLE_");
    }
    

}
