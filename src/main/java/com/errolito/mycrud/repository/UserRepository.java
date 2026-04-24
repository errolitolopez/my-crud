package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = "userProfile")
    Optional<User> findById(@NonNull Integer integer);

    @Override
    @NonNull
    @EntityGraph(attributePaths = "userProfile")
    Page<User> findAll(@NonNull Specification<User> spec, @NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"userCredential", "roles", "roles.permissions"})
    Optional<User> findByUsername(String username);
}