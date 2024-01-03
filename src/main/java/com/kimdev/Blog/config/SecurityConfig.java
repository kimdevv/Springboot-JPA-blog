package com.kimdev.Blog.config;

import com.kimdev.Blog.config.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Security에서 이 아래 3개는 그냥 세트라서... 그냥 등록 해라!
@Configuration // Bean 등록 -> 스프링 컨테이너에서 객체를 관리할 수 있게 한다.
@EnableWebSecurity // '시큐리티' 필터 등록 -> 스프링 시큐리티가 활성화 되어 있는데, 어떤 설정을 해당 파일에서 하겠다는 뜻.
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근하면 권한 및 인증을 먼저 체크하겠다는 것.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean // return하는 저 값을 스프링이 관리!!
    public BCryptPasswordEncoder encodePWD() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티가 대신 로그인하기 위해 password를 가로채기 하는데,
    // password가 어떤 방식으로 해시되어있는지 알아야 같은 해시로 로그인해서 DB에 있는 해시랑 비교할 수 있음.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // csrf 토큰 비활성화 (테스트 시 걸어두는 게 좋음)
                .authorizeRequests() // Request가 들어오면
                .antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**") // 이 쪽으로 들어오는 건
                .permitAll() // 누구나 들어올 수 있다!
                .anyRequest() // 이 주소가 아닌 다른 모든 요청들은
                .authenticated() // 인증이 되어야 함!
            .and()
                .formLogin()
                .loginPage("/auth/loginForm") // 인증이 필요한 곳으로 요청이 오면 얘가 뜰 것.
                .loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청 오는 로그인을 가로채서 대신 로그인 해준다.
                .defaultSuccessUrl("/"); // 로그인 요청이 정상적으로 완료되면 여기로 이동.
                //.failureUrl("/fail"); // 로그인 요청이 잘못되었을 때.

    }
}
