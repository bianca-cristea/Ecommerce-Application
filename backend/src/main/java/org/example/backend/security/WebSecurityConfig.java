package org.example.backend.security;

import org.example.backend.models.AppRoles;
import org.example.backend.models.Role;
import org.example.backend.models.User;
import org.example.backend.repositories.RoleRepository;
import org.example.backend.repositories.UserRepository;
import org.example.backend.security.jwt.AuthEntryPointJwt;
import org.example.backend.security.jwt.AuthTokenFilter;
import org.example.backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Set;

@Configuration // spune lui Spring că această clasă conține configurări
@EnableWebSecurity // activează securitatea web în Spring
@EnableMethodSecurity // permite securitate la nivel de metode (ex: @PreAuthorize)
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    // aducem clasa ta care spune cum se încarcă userii din DB

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    // ce se întâmplă dacă userul NU este autorizat (ex: 401 Unauthorized)


    //cream obiectul AuthTokenFilter
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
        // filtru custom care citește JWT din request
    }

    //logica de verificare a autentificării
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        // folosește userDetailsService pentru autentificare

        authProvider.setPasswordEncoder(passwordEncoder());
        // spune cum sunt codate parolele

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
        // managerul principal de autentificare
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // parolele sunt criptate cu BCrypt (foarte important!)
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // dezactivează CSRF (ok pentru API-uri REST cu JWT)
                // CSRF = protecție împotriva request-urilor false trimise în numele userului
                // Dezactivat deoarece folosim JWT (stateless, fără sesiuni)



                .cors(cors -> {})
                // CORS = permite frontend-ului (alt domeniu) să apeleze API-ul
                // Necesare pentru aplicații cu frontend separat (React, Angular etc.)

                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(unauthorizedHandler))
                // dacă nu ești logat → handler custom

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // NU folosește sesiuni → JWT (stateless)

                .authorizeHttpRequests(auth ->
                                auth.requestMatchers("/api/auth/**").permitAll() // login/register → acces liber
                                        .requestMatchers("/v3/api-docs/**").permitAll()
                                        .requestMatchers("/h2-console/**").permitAll() // dev tools → acces liber
                                        //.requestMatchers("/api/admin/**").hasRole("ADMIN") // doar ADMIN
                                        .requestMatchers("/api/seller/**").hasAnyRole("ADMIN","SELLER") // ADMIN sau SELLER
                                        //.requestMatchers("/api/public/**").permitAll() // public
                                        .requestMatchers("/swagger-ui/**").permitAll()
                                        .requestMatchers("/api/test/**").permitAll()
                                        .requestMatchers("/images/**").permitAll()
                                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                        // pentru CORS preflight

                                        .anyRequest().authenticated()
                        // orice alt request → trebuie login
                );

        http.authenticationProvider(authenticationProvider()); // spune ce provider de autentificare folosește
        http.addFilterBefore(authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class);  // adaugă filtrul JWT înainte de filtrul standard
        http.headers(headers ->
                headers.frameOptions(frame -> frame.sameOrigin()));  // permite H2 console în browser

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
        // aceste endpoint-uri sunt complet ignorate de securitate
    }


    // ---------------------------------------------------------------------
    // FIXED: initData using TransactionTemplate
    // ---------------------------------------------------------------------
    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository,
                                      UserRepository userRepository,
                                      PasswordEncoder passwordEncoder,
                                      PlatformTransactionManager platformTransactionManager) { // 1. Inject Transaction Manager
        return args -> {
            // 2. Create a template to control the transaction manually
            // This ensures the fetched Roles stay "attached" to the session when we save the Users.
            TransactionTemplate txTemplate = new TransactionTemplate(platformTransactionManager);

            txTemplate.execute(status -> {
                // Retrieve or create roles
                Role userRole = roleRepository.findByRoleName(AppRoles.ROLE_USER)
                        .orElseGet(() -> roleRepository.save(new Role(AppRoles.ROLE_USER)));

                Role sellerRole = roleRepository.findByRoleName(AppRoles.ROLE_SELLER)
                        .orElseGet(() -> roleRepository.save(new Role(AppRoles.ROLE_SELLER)));

                Role adminRole = roleRepository.findByRoleName(AppRoles.ROLE_ADMIN)
                        .orElseGet(() -> roleRepository.save(new Role(AppRoles.ROLE_ADMIN)));

                Set<Role> userRoles = Set.of(userRole);
                Set<Role> sellerRoles = Set.of(sellerRole);
                Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);

                // Create users if not already present
                if (!userRepository.existsByUsername("user1")) {
                    User user1 = new User("user1", "user1@example.com", passwordEncoder.encode("password1"));
                    user1.setRoles(userRoles);
                    userRepository.save(user1);
                }

                if (!userRepository.existsByUsername("seller1")) {
                    User seller1 = new User("seller1", "seller1@example.com", passwordEncoder.encode("password2"));
                    seller1.setRoles(sellerRoles);
                    userRepository.save(seller1);
                }

                if (!userRepository.existsByUsername("admin")) {
                    User admin = new User("admin", "admin@example.com", passwordEncoder.encode("adminPass"));
                    admin.setRoles(adminRoles);
                    userRepository.save(admin);
                }
                return null; // Transaction callback requires a return value
            });
        };
    }
}