package ru.shipova.market.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.shipova.market.entities.Product;

import java.util.List;

@Repository
public interface ProductsRepository extends CrudRepository<Product, Long>, PagingAndSortingRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    //Product save(Product product);

    //Product findById(Long id);

    //List<Product> findAll();
}
