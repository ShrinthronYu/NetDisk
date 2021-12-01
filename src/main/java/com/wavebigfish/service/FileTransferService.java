package com.wavebigfish.service;

import com.wavebigfish.dto.DownloadFileDTO;
import com.wavebigfish.dto.UploadFileDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface FileTransferService {
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long userId);

    void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO);

    Long selectStorageSizeByUserId(Long userId);
}