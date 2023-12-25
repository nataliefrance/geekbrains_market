package ru.shipova.market.utils;

import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.shipova.market.entities.Product;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
//Для каждой сессии будет своя корзина, а не одна на всех пользователей
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private Map<Product, Integer> products;

    @PostConstruct //после настройки бина
    public void init() {
        products = new HashMap<>();
    }
    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            Integer totalQuantity = products.get(product) + 1;
            products.put(product, totalQuantity);
        } else {
            products.put(product, 1);
        }
    }
}
