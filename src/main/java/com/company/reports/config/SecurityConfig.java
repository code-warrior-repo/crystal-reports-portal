package com.company.reports.config;

import com.company.reports.security.CurrentUser;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/launch", "/static/**", "/access-denied").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex.accessDeniedPage("/access-denied"))
            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/access-denied"))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/launch", "/reports/*/viewer"));

        return http.build();
    }

    public static void establishSessionAuthentication(CurrentUser user,
                                                       jakarta.servlet.http.HttpServletRequest request) {
        var authorities = user.roles().stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
        var authentication = new UsernamePasswordAuthenticationToken(user, "N/A", authorities);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        request.getSession(true).setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            context
        );
    }
}
