package com.errolito.mycrud.facade.impl;

import com.errolito.mycrud.cache.FileCacheStore;
import com.errolito.mycrud.dto.FileQuery;
import com.errolito.mycrud.dto.FileRequest;
import com.errolito.mycrud.dto.FileResponse;
import com.errolito.mycrud.entity.File;
import com.errolito.mycrud.facade.FileFacade;
import com.errolito.mycrud.mapper.FileMapper;
import com.errolito.mycrud.service.FileService;
import com.errolito.mycrud.service.S3Service;
import com.errolito.mycrud.shared.BaseCrudFacadeImpl;
import io.github.uncaughterrol.commons.exception.ExceptionFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FileFacadeImpl
        extends BaseCrudFacadeImpl<Integer, FileQuery, FileRequest, File, FileResponse>
        implements FileFacade {

    private final FileService service;
    private final S3Service s3Service;

    protected FileFacadeImpl(
            FileMapper mapper,
            FileService service,
            S3Service s3Service,
            FileCacheStore cacheStore
    ) {
        super(mapper, service, cacheStore);
        this.s3Service = s3Service;
        this.service = service;
    }

    @Override
    public FileResponse save(FileRequest request) {
        String slug = request.getSlug();
        String name = request.getName();

        File file = service.findBySlugAndName(slug, name).orElseGet(File::new);
        mapper.fromRequest(request, file);

        file.setUrl(upload(request));

        service.save(file);
        return mapper.toResponse(file);
    }


    @Override
    public FileResponse update(Integer id, FileRequest request) {
        File file = service.getById(id);

        String slug = request.getSlug();
        String name = request.getName();

        if (!file.getSlug().equalsIgnoreCase(slug) || !file.getName().equalsIgnoreCase(name)) {
            Optional<File> existing = service.findBySlugAndName(slug, name);
            if (existing.isPresent()) {
                delete(request);
                service.delete(existing.get());
            }

            delete(file);
        }

        file.setUrl(upload(request));

        mapper.fromRequest(request, file);
        service.save(file);

        return mapper.toResponse(file);
    }

    @Override
    public void deleteById(Integer id) {
        File file = service.getById(id);
        delete(file);
        service.delete(file);
    }

    private String upload(FileRequest request) {
        try {
            return s3Service.upload(getKeyFromRequest(request), request.getFile());
        } catch (Exception e) {
            log.warn("S3 upload failed: {}", e.getMessage());
            throw ExceptionFactory.internal("File upload failed");
        }
    }

    private void delete(String key) {
        try {
            s3Service.delete(key);

            log.info("S3 delete success: {}", key);
        } catch (Exception e) {
            log.warn("S3 delete failed: {}", e.getMessage());
        }
    }

    private void delete(File file) {
        delete("public/" + file.getSlug() + "/" + file.getName());
    }

    private void delete(FileRequest request) {
        delete(getKeyFromRequest(request));
    }

    private String getKeyFromRequest(FileRequest request) {
        return "public/" + request.getSlug() + "/" + request.getName();
    }
}