package ru.shipova.market.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.shipova.market.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) //возможность защищать отдельные методы аннотацией secure
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserService userService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //для аутентификации пользователя исп-ем бин DaoAuthenticationProvider
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    //настройки для защиты приложения
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //"/admin/**" - по этому адресу могут заходить только ADMIN и MANAGER
                .antMatchers("/admin/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/products/**").hasAnyRole("ADMIN", "MANAGER")
                .antMatchers("/admin/users/**").hasRole("ADMIN")
                //user, который оформляет заказ должен пройти аутентификацию
                .antMatchers("/shop/order/**").authenticated()
                .antMatchers("/profile/**").authenticated()
                .anyRequest().permitAll()
                .and()
                //указываем, каким образом пользователь будет логиниться на нашем сайте
                .formLogin()
                //форма для входа нах-ся по адресу /login
                .loginPage("/login")
                //когда user вводит догин, пароль, его отправляют на адрес /authenticateTheUser
                .loginProcessingUrl("/authenticateTheUser")
                .permitAll()
                .and()
                //Если пользователю на страницу вход запрещён, его перенаправляют на /accessDenied
                //.exceptionHandling().accessDeniedPage("/accessDenied")
                //в случае logout пользователя перекидывают на /shop
                .logout()
                .logoutSuccessUrl("/shop")
                .permitAll();
    }

    @Bean
    //Стандартный passwordEncoder
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    //Dao - пользователь будет жить в БД, мы его оттуда вытаскиваем
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService); //даём auth ссылку на userService, чтобы он мог работать с пользователями
        auth.setPasswordEncoder(passwordEncoder());//пароль, кот. введёт user, нужно прогнать через BCryptPasswordEncoder
        return auth;
    }

}
