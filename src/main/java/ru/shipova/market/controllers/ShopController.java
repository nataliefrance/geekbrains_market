package ru.shipova.market.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shipova.market.entities.Product;
import ru.shipova.market.services.ProductsService;
import ru.shipova.market.utils.ProductsFilter;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private ProductsService productsService;

    @Autowired
    public void setProductsService(ProductsService productsService) {
        this.productsService = productsService;
    }

    //http://localhost:8189/market/shop/
    @GetMapping()
    public String shop(Model model, HttpServletRequest request,
                               //параметры, которые мы ожидаем получить из формы со страницы shop.html
                               @RequestParam(name = "word", required = false) String word,
                               @RequestParam(name = "min", required = false) Integer min,
                               @RequestParam(name = "max", required = false) Integer max,
                               @RequestParam(name = "pageNumber", required = false) Integer pageNumber) {

        ProductsFilter filter = new ProductsFilter(request);

        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }
        //добавляем параметры в model, чтобы они не сбрасывались на странице
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("filters", filter.getFiltersString());
        Page<Product> page = productsService.findAllByPagingAndFiltering(filter.getSpec(),
                PageRequest.of(pageNumber - 1, 10, Sort.Direction.ASC, "id"));//10 элементов на страницу, Sort.Direction.ASC, "id" - сортировка по id
        model.addAttribute("page", page);
        return "shop";
    }
}
