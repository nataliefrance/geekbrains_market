package ru.shipova.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
                               @RequestParam(name = "max", required = false) Integer max,
                               @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
        Specification<Product> spec = Specification.where(null);
        StringBuilder filtersBuilder = new StringBuilder();
        if (word != null && !word.isEmpty()) {
            spec = spec.and(ProductSpecifications.titleContains(word));
            filtersBuilder.append("&word=" + word);
        }
        if (min != null) {
            spec = spec.and(ProductSpecifications.priceGreaterThanOrEq(min));
            filtersBuilder.append("&min=" + min);
        }
        if (max != null) {
            spec = spec.and(ProductSpecifications.priceLesserThanOrEq(max));
            filtersBuilder.append("&max=" + max);
        }
        if (pageNumber == null) {
            pageNumber = 1;
        }

        //добавляем параметры в model, чтобы они не сбрасывались на странице
        model.addAttribute("word", word);
        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("filters", filtersBuilder.toString());

        Page<Product> page = productsService.findAllByPagingAndFiltering(spec,
                PageRequest.of(pageNumber - 1, 2, Sort.Direction.ASC, "id"));//2 элементов на страницу, Sort.Direction.ASC, "id" - сортировка по id
        model.addAttribute("page", page);
        return "products";
    }

    @GetMapping("/edit/{id}")
    //на страницу нужно отправить объект, который мы редактируем, поэтому отправляем туда Model
    //@PathVariable - достаём id из пути
    public String showEditForm(Model model, @PathVariable (name = "id") Long id) {
        Product product = productsService.findById(id);
        model.addAttribute("product", product);
        return "edit_product";
    }

    @PostMapping("/edit") //id не нужен, т.к. он уже будет зашит в сам объект
    public String saveModifiedProduct(@ModelAttribute (name = "product") Product product) {
        productsService.save(product);
        return "redirect:/products"; //редиректим на products, чтобы увидеть, что продукт сохранился
    }
}

