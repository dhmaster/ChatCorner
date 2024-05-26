package com.dhuer.mallchat.common.common.exception;

import lombok.Data;

/**
 * Description:
 * Author: Jintao Li
 * Date: 2024/5/9
 */
@Data
public class BusinessException extends RuntimeException{
    protected Integer errorCode;
    protected String errorMsg;

    public BusinessException(String errorMsg){
        // 当异常被抛出并被捕获时，通常可以通过调用异常对象的 getMessage()
        // 方法来获取这个错误信息。super(errorMsg) 的调用确保了当 BusinessException
        // 被捕获后，可以通过 getMessage() 方法获取到传递进来的错误信息 errorMsg。
        // 如果没有这行代码，在写入 log 日志时调用 e.getMessage() 读取不到任何信息。
        super(errorMsg);

        this.errorCode = CommonErrorEnum.BUSINESS_ERROR.getErrorCode();
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public BusinessException(ErrorEnum errorEnum) {
        super(errorEnum.getErrorMsg());
        this.errorCode = errorEnum.getErrorCode();
        this.errorMsg = errorEnum.getErrorMsg();
    }
}
