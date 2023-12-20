package ru.shipova.market.utils;

import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import ru.shipova.market.entities.Product;
import ru.shipova.market.repositories.specifications.ProductSpecifications;

import javax.servlet.http.HttpServletRequest;

@Getter
public class ProductsFilter {
    private Specification<Product> spec;
    private final StringBuilder filtersString;

    public ProductsFilter(HttpServletRequest request) {
        this.spec = Specification.where(null);
        this.filtersString = new StringBuilder();

        if (request.getParameter("word") != null && !request.getParameter("word").isEmpty()) {
            spec = spec.and(ProductSpecifications.titleContains(request.getParameter("word")));
            filtersString.append("&word=" + request.getParameter("word"));
        }
        if (request.getParameter("min") != null && !request.getParameter("min").isEmpty()) {
            spec = spec.and(ProductSpecifications.priceGreaterThanOrEq(Double.parseDouble(request.getParameter("min"))));
            filtersString.append("&min=" + request.getParameter("min"));
        }
        if (request.getParameter("max") != null && !request.getParameter("max").isEmpty()) {
            spec = spec.and(ProductSpecifications.priceLesserThanOrEq(Double.parseDouble(request.getParameter("max"))));
            filtersString.append("&max=" + request.getParameter("max"));
        }
    }
}
