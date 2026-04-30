package com.errolito.mycrud.repository;

import com.errolito.mycrud.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Integer>, JpaSpecificationExecutor<File> {
    Optional<File> findBySlugAndName(String slug, String name);
}