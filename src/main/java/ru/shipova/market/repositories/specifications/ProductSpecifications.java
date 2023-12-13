package ru.shipova.market.repositories.specifications;


import org.springframework.data.jpa.domain.Specification;
import ru.shipova.market.entities.Product;

public class ProductSpecifications {
    //Поиск по слову
    public static Specification<Product> titleContains(String word) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder)
                -> criteriaBuilder.like(root.get("title"), "%" + word + "%");
    }

    //поиск по цене
    public static Specification<Product> priceGreaterThanOrEq(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value);
        };
    }

    //поиск по цене
    public static Specification<Product> priceLesserThanOrEq(double value) {
        return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> {
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), value);
        };
    }
}
