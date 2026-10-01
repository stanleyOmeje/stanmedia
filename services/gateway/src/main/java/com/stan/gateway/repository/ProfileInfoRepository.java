package com.stan.gateway.repository;

import com.stan.gateway.entity.ProfileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfileInfoRepository extends JpaRepository<ProfileInfo, Long> {
    boolean existsByEmail(String email);
}
