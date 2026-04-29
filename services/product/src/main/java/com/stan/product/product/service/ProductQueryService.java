package com.stan.product.product.service;


import com.stan.product.product.dto.request.ProductPage;
import com.stan.product.product.dto.request.ProductSearchCriteria;
import com.stan.product.product.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class ProductQueryService {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ProductQueryService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<Product> getAllProductWithFilter(ProductPage productPage, ProductSearchCriteria searchCriteria) {
        log.info("Inside get all Product with filter");
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> productRoot = criteriaQuery.from(Product.class);
        Predicate predicate = getPredicate(searchCriteria, productRoot);

        if (Objects.isNull(predicate)) {
            log.info("No predicate found");
        }
        criteriaQuery.where(predicate);
        getSort(productPage, criteriaQuery, productRoot);
        TypedQuery<Product> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(productPage.getPageNo() * productPage.getPageSize());
        typedQuery.setMaxResults(productPage.getPageSize());
        Pageable pageable = getPageable(productPage);
        long productCount = getProductCount(predicate);
        return new PageImpl<>(typedQuery.getResultList(), pageable, productCount);
    }

    Predicate getPredicate(ProductSearchCriteria searchCriteria, Root<Product> productRoot) {
        log.info("Inside get Predicate method");
        List<Predicate> predicates = new ArrayList<>();
        if (Objects.nonNull(searchCriteria.getCategoryCode())) {
            predicates.add(criteriaBuilder.equal(productRoot.get("category").get("code"), searchCriteria.getCategoryCode()));
        }
        if (Objects.nonNull(searchCriteria.getName())) {
            predicates.add(criteriaBuilder.equal(productRoot.get("name"), searchCriteria.getName()));
        }
        if (Objects.nonNull(searchCriteria.getCode())) {
            predicates.add(criteriaBuilder.equal(productRoot.get("code"), searchCriteria.getCode()));
        }
        if (Objects.nonNull(searchCriteria.getDescription())) {
            predicates.add(criteriaBuilder.equal(productRoot.get("description"), searchCriteria.getDescription()));
        }
        if (Objects.nonNull(searchCriteria.getProductType())) {
            predicates.add(criteriaBuilder.equal(productRoot.get("productType"), searchCriteria.getProductType()));
        }
        if (Objects.nonNull(searchCriteria.getBelt())) {
            predicates.add(criteriaBuilder.equal(productRoot.get("belt"), searchCriteria.getBelt()));
        }
        if (Objects.nonNull(searchCriteria.getFeeType())) {
            predicates.add(criteriaBuilder.equal(productRoot.get("fee").get("feeType"), searchCriteria.getFeeType()));
        }
        if (Objects.nonNull(searchCriteria.getPrice())) {
            predicates.add(criteriaBuilder.equal(productRoot.get("fee").get("price"), searchCriteria.getPrice()));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    public void getSort(ProductPage productPage, CriteriaQuery<Product> criteriaQuery, Root<Product> productRoot) {
        log.info("Inside get Sort method");
        if (productPage.getDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(productRoot.get(productPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(productRoot.get(productPage.getSortBy())));
        }
    }

    private Pageable getPageable(ProductPage productPage) {
        log.info("Inside get Pageable method");
        Sort sort = Sort.by(productPage.getDirection(), productPage.getSortBy());
        return PageRequest.of(productPage.getPageNo(), productPage.getPageSize(), sort);
    }

    private long getProductCount(Predicate predicate) {
        log.info("Inside get Product count");
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Product> rootCount = countQuery.from(Product.class);
        countQuery.select(criteriaBuilder.count(rootCount)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
