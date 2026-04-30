package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.FileQuery;
import com.errolito.mycrud.dto.FileRequest;
import com.errolito.mycrud.dto.FileResponse;
import com.errolito.mycrud.facade.FileFacade;
import com.errolito.mycrud.shared.BaseCrudController;
import io.github.uncaughterrol.commons.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/files")
public class FileController extends BaseCrudController<Integer, FileQuery, FileRequest, FileResponse> {

    protected FileController(FileFacade facade) {
        super(facade);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileResponse>> create(@Valid @ModelAttribute FileRequest request) {
        return success(facade.save(request));
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<FileResponse>> update(@PathVariable Integer id, @Valid @ModelAttribute  FileRequest request) {
        return success(facade.update(id, request));
    }
}