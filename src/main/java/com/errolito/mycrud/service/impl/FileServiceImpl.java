package com.errolito.mycrud.service.impl;

import com.errolito.mycrud.dto.FileQuery;
import com.errolito.mycrud.entity.File;
import com.errolito.mycrud.repository.FileRepository;
import com.errolito.mycrud.service.FileService;
import com.errolito.mycrud.shared.BaseCrudServiceImpl;
import com.errolito.mycrud.shared.SpecBuilder;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Supplier;

@Service
public class FileServiceImpl
        extends BaseCrudServiceImpl<Integer, FileQuery, File, FileRepository>
        implements FileService {

    protected FileServiceImpl(FileRepository repository) {
        super(repository);
    }

    @Override
    protected Specification<File> buildLikeSpec(FileQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andLike("slug", query.getSlug())
                        .andLike("name", query.getName())
                        .build();
    }

    @Override
    protected Specification<File> buildEqualSpec(FileQuery query) {
        return (root, criteriaQuery, builder) ->
                SpecBuilder.of(root, builder)
                        .andEqual("slug", query.getSlug())
                        .andEqual("name", query.getName())
                        .build();
    }

    @Override
    protected Supplier<RuntimeException> notFoundException() {
        return () -> ExceptionFactory.notFound("File not found");
    }

    @Override
    @Transactional
    public Optional<File> findBySlugAndName(String slug, String name) {
        return repository.findBySlugAndName(slug, name);
    }
}