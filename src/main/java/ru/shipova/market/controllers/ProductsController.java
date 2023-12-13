package ru.shipova.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shipova.market.entities.Product;
import ru.shipova.market.repositories.specifications.ProductSpecifications;
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
    public String showProducts(Model model,
                               //параметры, которые мы ожидаем получить из формы со страницы products.html
                               @RequestParam(name = "word", required = false) String word,
                               @RequestParam(name = "min", required = false) Integer min,
                               @RequestParam(name = "max", required = false) Integer max) {
        Specification<Product> spec = Specification.where(null);
        if (word != null) {
            spec = spec.and(ProductSpecifications.titleContains(word));
        }
        if (min != null) {
            spec = spec.and(ProductSpecifications.priceGreaterThanOrEq(min));
        }
        if (max != null) {
            spec = spec.and(ProductSpecifications.priceLesserThanOrEq(max));
        }
        //добавляем параметры в model, чтобы они не сбрасывались на странице
        model.addAttribute("word", word);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("page", productsService.findAllByPagingAndFiltering(spec, PageRequest.of(0, 10))); //10 элементов на страницу
        return "products";
    }
}

