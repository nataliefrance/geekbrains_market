package ru.shipova.market.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.shipova.market.entities.User;

//UserDetailsService - по имени вытаскивает всю интересующую нас инф-ю о пользователе
public interface UserService extends UserDetailsService {
    User findByUsername(String username);
}
