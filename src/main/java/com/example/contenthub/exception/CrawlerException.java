package com.example.contenthub.exception;

public class CrawlerException extends RuntimeException {
    public enum ExceptionMessage {
        TIMEOUT_EXCEPTION("시간 초과가 발생했습니다."),
        UNEXPECTED_EXCEPTION("예기치 않은 예외가 발생했습니다."),
        DATA_EXIST_EXCEPTION("데이터가 이미 존재합니다.");

        private final String message;

        ExceptionMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public CrawlerException(String message) {
        super(message);
    }

    public CrawlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
