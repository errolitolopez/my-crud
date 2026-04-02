package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

//    @EntityGraph(attributePaths = {"userProfile"}) // fix: N+1 query problem
    Page<User> findAll(Specification<User> spec, Pageable pageable);
}