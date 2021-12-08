package com.boshfish.operation;

import com.boshfish.operation.delete.Deleter;
import com.boshfish.operation.download.Downloader;
import com.boshfish.operation.upload.Uploader;

public interface FileOperationFactory {
    Uploader getUploader();

    Downloader getDownloader();

    Deleter getDeleter();
}