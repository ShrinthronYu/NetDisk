package com.boshfish.operation.delete.product;

import java.io.File;

import com.boshfish.operation.delete.Deleter;
import com.boshfish.operation.delete.domain.DeleteFile;
import com.boshfish.util.FileUtil;
import com.boshfish.util.PathUtil;

import org.springframework.stereotype.Component;

@Component
public class LocalStorageDeleter extends Deleter {
    @Override
    public void delete(DeleteFile deleteFile) {
        File file = new File(PathUtil.getStaticPath() + deleteFile.getFileUrl());
        if (file.exists()) {
            file.delete();
        }

        if (FileUtil.isImageFile(FileUtil.getFileExtendName(deleteFile.getFileUrl()))) {
            File minFile = new File(PathUtil.getStaticPath() + deleteFile.getFileUrl().replace(deleteFile.getTimeStampName(), deleteFile.getTimeStampName() + "_min"));
            if (minFile.exists()) {
                minFile.delete();
            }
        }
    }
}