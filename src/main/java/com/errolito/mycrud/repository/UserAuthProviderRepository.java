package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.UserAuthProvider;
import com.errolito.mycrud.enums.AuthProvider;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserAuthProviderRepository
        extends JpaRepository<UserAuthProvider, Integer>, JpaSpecificationExecutor<UserAuthProvider> {

    @EntityGraph(attributePaths = "user")
    Optional<UserAuthProvider> findByProviderAndProviderId(AuthProvider provider, String providerId);
}
