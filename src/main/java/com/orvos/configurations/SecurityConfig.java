package com.orvos.configurations;

import com.orvos.services.UserDetailsServiceImpl;
import com.orvos.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests().antMatchers("/","/hatoanyag", "/regisztracio", "/static/**", "/assets/**").permitAll()
                .antMatchers("/beteglekerdez", "/beteggyakorisag", "/ujbeteg","/ujbetegseg",
                        "/ujgyogyszer", "/ujnaplo", "/felirtgyogyszer", "/orvosadat", "/idopontkezeles", "/gyogyszervalaszto", "/orvosstatus","/gyogyszerdata/**").hasRole("ADMIN")
                .antMatchers("/betegnaplo", "/betegadatok").hasRole("USER")
                .antMatchers("/jelszocsere").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/", true)
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .permitAll();
        httpSecurity.headers().frameOptions().disable();
        httpSecurity.csrf().ignoringAntMatchers("/","/hatoanyag","/regisztracio","/static/**", "/assets/**");
    }




    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }
}
