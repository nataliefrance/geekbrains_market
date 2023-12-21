package ru.shipova.market.utils;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import ru.shipova.market.entities.Product;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
//Для каждой сессии будет своя корзина, а не одна на всех пользователей
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Cart {
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    @PostConstruct //после настройки бина
    public void init() {
        products = new ArrayList<>();
    }
    public void addProduct(Product product) {
        products.add(product);
    }

}
