package ru.shipova.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shipova.market.entities.Product;
import ru.shipova.market.services.ProductsService;
import ru.shipova.market.utils.Cart;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/cart")
public class CartController {
    private ProductsService productsService;

    private Cart cart;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    @Autowired
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @GetMapping("")
    public String show(Model model) {
        model.addAttribute("products", cart.getProducts());
        return "cart";
    }

    @GetMapping("/add")
    public void addProductToCart(@RequestParam (name = "id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = productsService.findById(id);
        cart.addProduct(product);
        //Когда приходит запрос (request), в нём есть отдельный header - referer, в нём хранится ссылка, откуда мы сюда пришли
        response.sendRedirect(request.getHeader("referer"));
    }
}
