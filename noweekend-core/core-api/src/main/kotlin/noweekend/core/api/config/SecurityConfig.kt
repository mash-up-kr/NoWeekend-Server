package noweekend.core.api.config

import noweekend.core.api.security.filter.JwtAuthenticationFilter
import noweekend.core.domain.jwt.JwtProperties
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableConfigurationProperties(JwtProperties::class)
@Configuration
class SecurityConfig {

    @Profile(value = ["local", "development"])
    @Bean("permitDenyPath")
    fun permitDenyPathsOnDevelopment() = mapOf(
        "permit" to arrayOf(
            "/health",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/login/**",
            "/test-gen",
        ),
        "deny" to arrayOf(),
    )

    @Profile(value = ["staging", "production"])
    @Bean("permitDenyPath")
    fun permitDenyPathsOnProduction() = mapOf(
        "permit" to arrayOf(
            "/health",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/login/**",
            "/test-gen",
        ),
        "deny" to arrayOf(
            "/example",
        ),
    )

    @Bean
    @Order(1)
    fun publicSecurityFilterChain(
        @Qualifier("permitDenyPath")
        permitDenyPath: Map<String, Array<String>>,
        http: HttpSecurity,
    ): SecurityFilterChain {
        val combinedPath = permitDenyPath["permit"]!! + permitDenyPath["deny"]!!
        val httpSecurity = http.securityMatcher(*combinedPath)
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(*permitDenyPath["deny"]!!).denyAll()
                    .anyRequest().permitAll()
            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }

        return httpSecurity.build()
    }

    @Bean
    @Order(2)
    fun authorizedSecurityFilterChain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
    ): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth.anyRequest().authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java,
            )
            .httpBasic { it.disable() }
            .formLogin { it.disable() }

        return http.build()
    }

    @Bean
    fun jwtAuthorizationFilterRegistration(filter: JwtAuthenticationFilter): FilterRegistrationBean<JwtAuthenticationFilter> {
        return FilterRegistrationBean(filter).apply { isEnabled = false }
    }

    @Bean
    fun authenticationManager(providers: List<AuthenticationProvider>): AuthenticationManager {
        return ProviderManager(providers)
    }
}
