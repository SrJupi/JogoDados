package cat.itacademy.barcelonactiva.Sulzbach.Lucas.s05.t02.n01.Dados.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
    /*
    https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    https://www.codejava.net/frameworks/spring-boot/fix-websecurityconfigureradapter-deprecated
    https://www.appsdeveloperblog.com/migrating-from-deprecated-websecurityconfigureradapter/
    https://github.com/ericus20/spring-boot-starter
     */

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http.cors();

        http.exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/players").permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/players").permitAll()
                .antMatchers(HttpMethod.DELETE, "/players/**").denyAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager configureAuthentication(){

        return new InMemoryUserDetailsManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
