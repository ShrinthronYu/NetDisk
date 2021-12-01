package com.wavebigfish.operation.delete;

import com.wavebigfish.operation.delete.domain.DeleteFile;

public abstract class Deleter {
    public abstract void delete(DeleteFile deleteFile);
}
