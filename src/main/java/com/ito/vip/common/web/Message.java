package com.ito.vip.common.web;

import java.util.HashMap;


@SuppressWarnings("unused")
public class Message {

    private final static Integer OK = 200;
    private final static Integer ERROR = 500;

    public static abstract class MessageEntry {

        private Integer statusCode;

        public MessageEntry(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public Integer getStatusCode() {
            return statusCode;
        }
    }

    private static class SuccessMessage extends MessageEntry {

        private Object data;

        public SuccessMessage(Integer statusCode, Object data) {
            super(statusCode);
            this.data = data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }
    }

    private static class SuccessSyncMessage extends MessageEntry {

        private Object data;
        private Long syncTime;

        public SuccessSyncMessage(Integer statusCode, Object data, Long syncTime) {
            super(statusCode);
            this.data = data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Object getData() {
            return data;
        }
    }

    private static class ErrorMessage extends MessageEntry {

        private String errorCode;

        private String errorMessage;

        public ErrorMessage(Integer statusCode, String errorCode, String message) {
            super(statusCode);
            this.errorCode = errorCode;
            this.errorMessage = message;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorMessage(String message) {
            this.errorMessage = message;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    private static class ErrorMessageWithOutCode extends MessageEntry {

        private String errorMessage;

        public ErrorMessageWithOutCode(Integer statusCode, String message) {
            super(statusCode);
            this.errorMessage = message;
        }


        public void setErrorMessage(String message) {
            this.errorMessage = message;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public static MessageEntry ok(Object data){
        if(data==null){
            data = new HashMap<String, String>();
        }
        MessageEntry messageEntry = new SuccessMessage(OK, data);
        return messageEntry;
    }

    public static MessageEntry ok(){
        return Message.ok(null);
    }

    public static MessageEntry error(String errorCode, String message){
        MessageEntry messageEntry = new ErrorMessage(ERROR, errorCode, message);
        return messageEntry;
    }

    public static MessageEntry error(String message){
        MessageEntry messageEntry = new ErrorMessageWithOutCode(ERROR, message);
        return messageEntry;
    }

    public static MessageEntry ok(Object data, Long syncTime) {
        if(data == null){
            data = new HashMap<String, String>();
        }
        MessageEntry messageEntry = new SuccessSyncMessage(OK, data, syncTime);
        return messageEntry;
    }
}
