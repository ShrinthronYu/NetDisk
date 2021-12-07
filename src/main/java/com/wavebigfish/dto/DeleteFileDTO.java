package com.wavebigfish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(name = "DeleteFileDTO", required = true)
public class DeleteFileDTO {

    @Schema(description = "UserFileId")
    private Long userFileId;

    @Schema(description = "filePath")
    @Deprecated
    private String filePath;

    @Schema(description = "文件名")
    @Deprecated
    private String fileName;

    @Schema(description = "是否是目录")
    @Deprecated
    private Integer isDir;

}