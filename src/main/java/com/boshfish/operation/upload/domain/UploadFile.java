package com.boshfish.operation.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    private String fileName;
    private String fileType;
    private String identifier;
    private String message;
    private String url;
    private String timeStampName;
    private String taskId;
    private long fileSize;
    private long chunkSize;
    private long totalSize;
    private long currentChunkSize;
    private int success;
    private int chunkNumber;
    private int totalChunks;
}