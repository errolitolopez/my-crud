package com.errolito.mycrud.service;

import com.errolito.mycrud.dto.FileQuery;
import com.errolito.mycrud.entity.File;
import com.errolito.mycrud.shared.BaseCrudService;

import java.util.Optional;

public interface FileService extends BaseCrudService<Integer, FileQuery, File> {
    Optional<File> findBySlugAndName(String slug, String name);
}
