package ww.com.core.exception;

import java.io.IOException;

/**
 * sdcard 不存在·或者是内存空间不足
 * 作者: fighter
 * 时间: 2016-04-18 11:50
 */
public class StorageSpaceException extends IOException {
    public StorageSpaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageSpaceException() {
    }

    public StorageSpaceException(Throwable cause) {
        super(cause);
    }

    public StorageSpaceException(String detailMessage) {
        super(detailMessage);
    }
}
