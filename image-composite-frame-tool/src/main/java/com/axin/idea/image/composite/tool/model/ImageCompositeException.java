package com.axin.idea.image.composite.tool.model;

/**
* @author Axin
* @since 2021-01-20
* @summary 图片合成异常
*/
public class ImageCompositeException extends RuntimeException {

    public ImageCompositeException() {
    }

    public ImageCompositeException(String message) {
        super(message);
    }

    public ImageCompositeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageCompositeException(Throwable cause) {
        super(cause);
    }

    public ImageCompositeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
