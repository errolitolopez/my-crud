package com.errolito.mycrud.controller;

import com.errolito.mycrud.dto.FileProxyResponse;
import com.errolito.mycrud.dto.FileQuery;
import com.errolito.mycrud.dto.FileRequest;
import com.errolito.mycrud.dto.FileResponse;
import com.errolito.mycrud.facade.FileFacade;
import com.errolito.mycrud.shared.BaseCrudController;
import io.github.uncaughterrol.commons.model.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping("/api/v1/files")
public class FileController extends BaseCrudController<Integer, FileQuery, FileRequest, FileResponse> {

    private final FileFacade facade;

    protected FileController(FileFacade facade) {
        super(facade);
        this.facade = facade;
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

    @GetMapping("/{id}/proxy")
    public ResponseEntity<InputStreamResource> proxy(@PathVariable Integer id) {
        FileProxyResponse response = facade.getFileProxy(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(response.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + response.getFilename() + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400, immutable")
                .header(HttpHeaders.ETAG, response.getEtag())
                .body(new InputStreamResource(response.getInputStream()));
    }
}