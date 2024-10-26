package com.example.contenthub.exception;

public class UserException extends RuntimeException {
    public UserException(String message) {
        super(message);
    }

    // 유효하지 않은 사용자 이름 예외 생성
    public static UserException duplicateUserException() {
        return new UserException("이미 사용 중인 닉네임입니다.");
    }

    public static UserException invalidUserException(){
        return new UserException("잘못된 아이디 또는 비밀번호입니다.");
    }
}
