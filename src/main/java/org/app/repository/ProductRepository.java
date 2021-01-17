package org.app.repository;

import org.app.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Page<Product> findAllByActiveTrueOrderByName(Pageable pageable);

    Page<Product> findAllByActiveFalseOrderByName(Pageable pageable);

    Product findByName(String name);

}
