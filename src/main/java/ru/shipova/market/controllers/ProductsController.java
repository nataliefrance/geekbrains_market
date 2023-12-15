package ru.shipova.market.controllers;

import jakarta.servlet.http.HttpServletRequest;
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
import ru.shipova.market.utils.ProductsFilter;

@Controller
@RequestMapping("/products")
public class ProductsController {

    private ProductsService productsService;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    //http://localhost:8189/market/products/
    @GetMapping()
    public String showProducts(Model model, HttpServletRequest request,
                               //параметры, которые мы ожидаем получить из формы со страницы products.html
                               @RequestParam(name = "word", required = false) String word,
                               @RequestParam(name = "min", required = false) Integer min,
                               @RequestParam(name = "max", required = false) Integer max,
                               @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {
        //System.out.println(request.getHeader("referer")); //Покажет с какой url мы сюда попали

        ProductsFilter filter = new ProductsFilter(request);

        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        //добавляем параметры в model, чтобы они не сбрасывались на странице
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("filters", filter.getFiltersString());
        Page<Product> page = productsService.findAllByPagingAndFiltering(filter.getSpec(),
                PageRequest.of(pageNumber - 1, 2, Sort.Direction.ASC, "id"));//2 элементов на страницу, Sort.Direction.ASC, "id" - сортировка по id
        model.addAttribute("page", page);
        return "products";
    }

    //edit используется и для редактирования, и для сохранения продукта
    @GetMapping("/edit")
    //на страницу нужно отправить объект, который мы редактируем, поэтому отправляем туда Model
    //если @PathVariable - достаём id из пути
    //@RequestParam - параметр, который мы ожидаем получить из формы со страницы products.html (th:href="@{'/products/edit?id=' + ${product.id}}")
    public String showEditForm(Model model, @RequestParam (name = "id", required = false) Long id) {
        Product product = null;
        if (id != null) {
            product = productsService.findById(id);
        } else {
            product = new Product();
        }
        model.addAttribute("product", product);
        return "edit_product";
    }

    @PostMapping("/edit") //id не нужен, т.к. он уже будет зашит в сам объект
    public String saveModifiedProduct(@ModelAttribute (name = "product") Product product) {
        productsService.save(product); //если у product будет id, мы его обновим, иначе - создадим новый
        return "redirect:/products"; //редиректим на products, чтобы увидеть, что продукт сохранился
    }
}

