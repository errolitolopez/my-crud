package com.errolito.mycrud.dto;

import com.errolito.mycrud.validator.ValidFile;
import io.github.uncaughterrol.commons.utils.SmartStringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class FileRequest {
    private String slug;
    private String name;

    @ValidFile
    @Schema(type = "string", format = "binary")
    private MultipartFile file;

    public String getSlug() {
        if (!StringUtils.hasText(slug)) {
            slug = "files";
        }
        return SmartStringUtils.toSnakeCase(slug.trim());
    }

    public String getName() {
        String fileExtension = "";
        String filename = "";

        if (file != null) {
            fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            filename = StringUtils.stripFilenameExtension(file.getOriginalFilename());
        }

        if (!StringUtils.hasText(name)) {
            name = filename;
        }

        return SmartStringUtils.toSnakeCase(name) + "." + fileExtension;
    }
}