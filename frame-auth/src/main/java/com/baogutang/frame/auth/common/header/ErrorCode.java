package com.baogutang.frame.auth.common.header;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author N1KO
 */
public interface ErrorCode {

    class ErrorWrapper {
        protected int code;
        protected String message;

        public ErrorWrapper() {
        }

        public ErrorWrapper(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public ErrorWrapper setCode(int code) {
            this.code = code;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public ErrorWrapper setMessage(String message) {
            this.message = message;
            return this;
        }
    }

    class ErrorListWrapper {

        final List<ErrorWrapper> errors;

        public ErrorListWrapper() {
            this(4);
        }

        public ErrorListWrapper(int size) {
            errors = new ArrayList<>(size);
        }

        public ErrorListWrapper push(final ErrorCode errorCode) {
            errors.add(new ErrorWrapper(errorCode.code(), errorCode.message()));
            return this;
        }

        public static ErrorListWrapper newErrorList() {
            return new ErrorListWrapper(4);
        }

        public static ErrorListWrapper newErrorListWithSize(final int size) {
            return new ErrorListWrapper(size);
        }
    }

    Logger logger = LoggerFactory.getLogger(ErrorCode.class);

    int GLOBAL_ERROR_CODE_PREFIX = 10_000;

    enum GlobalErrorCode implements ErrorCode {
        UN_AUTH(401, "Customer Auth Token Not passed or not validated"),
        NOT_HTTP_REQUEST(601, "Not qualify http request"),
        UNKNOWN_ERROR(500, "Unknown system error"),
        KYC_MISSED(901, "Kyc limit resource access fail"),
        VENDOR_ID_MISSED(902, "Vendor Id missed"),
        VENDOR_ID_NOT_5_LENGTH(903, "Vendor Id length must be 5 length"),
        ;

        protected final int code;
        protected final String message;

        GlobalErrorCode(int code, String message) {
            this.code = GLOBAL_ERROR_CODE_PREFIX + code;
            this.message = message;
        }

        @Override
        public int code() {
            return code;
        }

        @Override
        public String message() {
            return message;
        }

        @Override
        public int getPrefix() {
            return GLOBAL_ERROR_CODE_PREFIX;
        }
    }

    enum NA implements ErrorCode {
        NA;

        @Override
        public int code() {
            return getPrefix();
        }

        @Override
        public String message() {
            return "Un-known Exception";
        }

        @Override
        public int getPrefix() {
            return 9_00_000;
        }
    }

    static <T extends Enum<T> & ErrorCode> T safeValueOf(Class<T> clazz, String name) {

        try {
            return Enum.valueOf(clazz, name);
        } catch (Throwable e) {
            logger.error("FAIL_MAPPER_{}_ERROR_CODE {},{}", clazz.getSimpleName().toUpperCase(), name, e);
            return (T) NA.NA;
        }
    }

    static String swaggerResponse(ErrorCode... errorCodes) {
        if (errorCodes == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("\t@ApiResponses({\n");
        String sp = "";
        for (ErrorCode errorCode : errorCodes) {
            builder
                    .append(sp)
                    .append("\t\t@ApiResponse(code = ")
                    .append(errorCode.code())
                    .append(",message=\"")
                    .append(errorCode.message())
                    .append("\")");
            sp = ",\n";
        }
        builder.append("\n\t})");
        return builder.toString();
    }

    int code();

    String message();

    int getPrefix();

    default String debug() {
        return "{code:" + getPrefix() + code() + ",message:" + message() + "}";
    }

    class ErrorCode500 implements ErrorCode {
        private String message;

        public ErrorCode500(String message) {
            this.message = message;
        }

        public ErrorCode500() {
        }

        public ErrorCode500 setMessage(String message) {
            this.message = message;
            return this;
        }

        @Override
        public int code() {
            return 500;
        }

        @Override
        public String message() {
            return message;
        }

        @Override
        public int getPrefix() {
            return 0;
        }
    }
}
