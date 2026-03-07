package com.stan.product.product.repository;


import com.stan.product.product.entity.FeeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeMappingRepository extends JpaRepository<FeeMapping, Long> {
}
