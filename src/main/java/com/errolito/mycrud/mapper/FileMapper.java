package com.errolito.mycrud.mapper;

import com.errolito.mycrud.dto.FileRequest;
import com.errolito.mycrud.dto.FileResponse;
import com.errolito.mycrud.entity.File;
import com.errolito.mycrud.shared.BaseMapper;
import org.mapstruct.Mapper;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public interface FileMapper extends BaseMapper<FileRequest, File, FileResponse> {
}