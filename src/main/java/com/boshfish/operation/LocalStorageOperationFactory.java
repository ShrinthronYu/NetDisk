package com.boshfish.operation;

import javax.annotation.Resource;


import com.boshfish.operation.delete.Deleter;
import com.boshfish.operation.delete.product.LocalStorageDeleter;
import com.boshfish.operation.download.Downloader;
import com.boshfish.operation.download.product.LocalStorageDownloader;
import com.boshfish.operation.upload.Uploader;
import com.boshfish.operation.upload.product.LocalStorageUploader;

import org.springframework.stereotype.Component;

@Component
public class LocalStorageOperationFactory implements FileOperationFactory {

    @Resource
    LocalStorageUploader localStorageUploader;
    @Resource
    LocalStorageDownloader localStorageDownloader;
    @Resource
    LocalStorageDeleter localStorageDeleter;

    @Override
    public Uploader getUploader() {
        return localStorageUploader;
    }

    @Override
    public Downloader getDownloader() {
        return localStorageDownloader;
    }

    @Override
    public Deleter getDeleter() {
        return localStorageDeleter;
    }


}
