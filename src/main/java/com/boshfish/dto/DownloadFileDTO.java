package com.boshfish.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

@Data
@Schema(name = "DownloadFileDTO", required = true)
public class DownloadFileDTO {

    private Long userFileId;
}