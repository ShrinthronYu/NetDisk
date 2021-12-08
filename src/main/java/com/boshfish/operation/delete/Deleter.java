package com.boshfish.operation.delete;

import com.boshfish.operation.delete.domain.DeleteFile;

public abstract class Deleter {
    public abstract void delete(DeleteFile deleteFile);
}
