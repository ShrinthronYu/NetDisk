package com.wavebigfish.operation;

import javax.annotation.Resource;


import com.wavebigfish.operation.delete.Deleter;
import com.wavebigfish.operation.delete.product.LocalStorageDeleter;
import com.wavebigfish.operation.download.Downloader;
import com.wavebigfish.operation.download.product.LocalStorageDownloader;
import com.wavebigfish.operation.upload.Uploader;
import com.wavebigfish.operation.upload.product.LocalStorageUploader;

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
