package tv.mopl.common.exception;

public interface ErrorCode {

    int getStatus();

    String getCode();

    String getMessage();
}
