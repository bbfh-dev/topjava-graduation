package me.bbfh.graduation.common.error;

import static me.bbfh.graduation.common.error.ErrorType.FORBIDDEN;

public class ForbiddenRequestException extends AppException {
    public ForbiddenRequestException(String msg) {
        super(msg, FORBIDDEN);
    }
}
