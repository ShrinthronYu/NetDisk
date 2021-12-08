package com.boshfish.service.impl;


import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.boshfish.dto.DownloadFileDTO;
import com.boshfish.dto.UploadFileDTO;
import com.boshfish.mapper.FileMapper;
import com.boshfish.mapper.UserFileMapper;
import com.boshfish.model.File;
import com.boshfish.model.UserFile;
import com.boshfish.operation.FileOperationFactory;
import com.boshfish.operation.download.Downloader;
import com.boshfish.operation.download.domain.DownloadFile;
import com.boshfish.operation.upload.Uploader;
import com.boshfish.operation.upload.domain.UploadFile;
import com.boshfish.service.FileTransferService;
import com.boshfish.util.DateUtil;
import com.boshfish.util.PropertiesUtil;

import org.springframework.stereotype.Service;

@Service
public class FileTransferServiceImpl implements FileTransferService {

    @Resource
    FileMapper fileMapper;
    @Resource
    UserFileMapper userFileMapper;

    @Resource
    FileOperationFactory localStorageOperationFactory;

    @Override
    public void uploadFile(HttpServletRequest request, UploadFileDTO uploadFileDto, Long userId) {

        Uploader uploader = null;
        UploadFile uploadFile = new UploadFile();
        uploadFile.setChunkNumber(uploadFileDto.getChunkNumber());
        uploadFile.setChunkSize(uploadFileDto.getChunkSize());
        uploadFile.setTotalChunks(uploadFileDto.getTotalChunks());
        uploadFile.setIdentifier(uploadFileDto.getIdentifier());
        uploadFile.setTotalSize(uploadFileDto.getTotalSize());
        uploadFile.setCurrentChunkSize(uploadFileDto.getCurrentChunkSize());
        String storageType = PropertiesUtil.getProperty("file.storage-type");
        synchronized (FileTransferService.class) {
            if ("0".equals(storageType)) {
                uploader = localStorageOperationFactory.getUploader();
            }
        }

        List<UploadFile> uploadFileList = uploader.upload(request, uploadFile);
        for (UploadFile value : uploadFileList) {
            uploadFile = value;
            File file = new File();

            file.setIdentifier(uploadFileDto.getIdentifier());
            file.setStorageType(Integer.parseInt(storageType));
            file.setTimeStamp(uploadFile.getTimeStampName());
            if (uploadFile.getSuccess() == 1) {
                file.setFileUrl(uploadFile.getUrl());
                file.setFileSize(uploadFile.getFileSize());
                file.setPointCount(1);
                fileMapper.insert(file);
                UserFile userFile = new UserFile();
                userFile.setFileId(file.getFileId());
                userFile.setExtendName(uploadFile.getFileType());
                userFile.setFileName(uploadFile.getFileName());
                userFile.setFilePath(uploadFileDto.getFilePath());
                userFile.setDeleteFlag(0);
                userFile.setUserId(userId);
                userFile.setIsDir(0);
                userFile.setUploadTime(DateUtil.getCurrentTime());
                userFileMapper.insert(userFile);
            }

        }
    }

    @Override
    public void downloadFile(HttpServletResponse httpServletResponse, DownloadFileDTO downloadFileDTO) {
        UserFile userFile = userFileMapper.selectById(downloadFileDTO.getUserFileId());

        String fileName = userFile.getFileName() + "." + userFile.getExtendName();
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        httpServletResponse.setContentType("application/force-download");// 设置强制下载不打开
        httpServletResponse.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名


        File file = fileMapper.selectById(userFile.getFileId());
        Downloader downloader = null;
        if (file.getStorageType() == 0) {
            downloader = localStorageOperationFactory.getDownloader();
        }
        DownloadFile uploadFile = new DownloadFile();
        uploadFile.setFileUrl(file.getFileUrl());
        uploadFile.setTimeStampName(file.getTimeStamp());
        downloader.download(httpServletResponse, uploadFile);
    }

    @Override
    public Long selectStorageSizeByUserId(Long userId) {
        return userFileMapper.selectStorageSizeByUserId(userId);
    }
}