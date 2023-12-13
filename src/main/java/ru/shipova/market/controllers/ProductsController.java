package ru.shipova.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shipova.market.entities.Product;
import ru.shipova.market.services.ProductsService;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private ProductsService productsService;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    //http://localhost:8189/app/products/
    @GetMapping()
    public String showProducts(Model model) {
        Specification<Product> spec = Specification.where(null);
        model.addAttribute("page", productsService.findAllByPagingAndFiltering(spec, PageRequest.of(0, 10))); //10 элементов на страницу
        return "products";
    }
}