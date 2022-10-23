package vn.mellow.ecom.ecommercefloor.base.exception;

import lombok.Data;

@Data
public class ServiceException extends Exception{
    protected String errorCode;
    protected String errorMessage;
    protected String errorDetail;

    public ServiceException(String errorCode, String errorMessage, String errorDetail) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }

    public ServiceException(String message, Throwable cause, String errorCode, String errorMessage, String errorDetail) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }
}
