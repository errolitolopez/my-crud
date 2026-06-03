package com.errolito.mycrud.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
@Builder
public class FileProxyResponse {
    private InputStream inputStream;
    private String contentType;
    private String filename;
    private String etag;
}