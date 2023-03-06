package az.ingress.book_user_store.security;

import az.ingress.book_user_store.domain.enumeration.RoleName;
import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@AllArgsConstructor
@Import(SecurityProblemSupport.class)
public class WebSecurity {

    private final SecurityProblemSupport securityProblemSupport;
    private final TokenProvider tokenProvider;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.
                authorizeHttpRequests()
                .dispatcherTypeMatchers(HttpMethod.POST, DispatcherType.valueOf("/authentication")).permitAll()
                .dispatcherTypeMatchers(HttpMethod.PUT, DispatcherType.valueOf("/users/{id}/add_publisher_role")).hasRole(RoleName.ADMIN.name())
                .dispatcherTypeMatchers(HttpMethod.POST, DispatcherType.valueOf("/books")).hasRole(RoleName.PUBLISHER.name())
                .dispatcherTypeMatchers(HttpMethod.GET, DispatcherType.valueOf("/books/published_by_me")).hasRole(RoleName.PUBLISHER.name())
                .dispatcherTypeMatchers(HttpMethod.valueOf("/h2-console/**")).permitAll()
                .dispatcherTypeMatchers(HttpMethod.valueOf("/actuator/**")).permitAll()
                .anyRequest().authenticated();

        http
                .addFilterBefore(new JWTFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport);

        http
                .formLogin().disable()
                .headers().frameOptions().disable();
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
