package com.example.contenthub.common.exception;

public class UserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserException(String message) {
        super(message);
    }

    public static UserException duplicateUserException() {
        return new UserException("이미 사용 중인 아이디입니다.");
    }

    public static UserException invalidUserException() {
        return new UserException("잘못된 아이디 또는 비밀번호입니다.");
    }

    public static UserException unauthorizedException() {
        return new UserException("인증되지 않은 사용자입니다.");
    }
}
