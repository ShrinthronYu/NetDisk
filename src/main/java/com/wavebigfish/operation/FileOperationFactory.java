package com.wavebigfish.operation;

import com.wavebigfish.operation.delete.Deleter;
import com.wavebigfish.operation.download.Downloader;
import com.wavebigfish.operation.upload.Uploader;

public interface FileOperationFactory {
    Uploader getUploader();

    Downloader getDownloader();

    Deleter getDeleter();
}