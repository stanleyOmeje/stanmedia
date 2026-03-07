package com.stan.product.product.repository;

import com.stan.product.product.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Optional<Category> findByCode(String code);
}
