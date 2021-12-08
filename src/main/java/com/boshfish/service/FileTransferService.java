package com.boshfish.service;

import com.boshfish.dto.DownloadFileDTO;
import com.boshfish.dto.UploadFileDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface FileTransferService {
    void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long userId);

    void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO);

    Long selectStorageSizeByUserId(Long userId);
}