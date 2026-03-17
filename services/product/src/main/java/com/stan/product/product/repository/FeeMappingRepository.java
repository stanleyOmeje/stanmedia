package com.stan.product.product.repository;


import com.stan.product.product.entity.FeeMapping;
import com.stan.product.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeeMappingRepository extends JpaRepository<FeeMapping, Long> {
    Optional<FeeMapping> findOneByProduct(Product product);
}
