package com.classroom.init

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
class WebSecurityConfig {

    @Bean
    fun bcryptPasswordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    open fun filterChain(http: HttpSecurity,introspect: HandlerMappingIntrospector): SecurityFilterChain? {
        val mvcMatcherBuilder = MvcRequestMatcher.Builder(introspect)
        for (paths in getExclusionPaths()) {
            http.authorizeHttpRequests { auth ->
                auth.requestMatchers(mvcMatcherBuilder.pattern(paths)).permitAll()
            }
        }
        http.authorizeHttpRequests { auth -> auth.anyRequest().authenticated() }
        http.formLogin {
            formLogin -> formLogin.loginPage("/login").permitAll()
            formLogin.defaultSuccessUrl("/", true)
        }
        return http.build()
    }

    private fun getExclusionPaths(): Array<String> {
        return arrayOf(
            "/resources/**",
            "/css/**",
            "/js/**",
            "/img/**",
            "/user-photos/**",
            "/assets/**",
            "/registration"
        )
    }

}