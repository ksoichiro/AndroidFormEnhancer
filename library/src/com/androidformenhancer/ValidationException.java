
package com.androidformenhancer;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -922236539358176883L;

    public ValidationException() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @param detailMessage
     * @param throwable
     */
    public ValidationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param detailMessage
     */
    public ValidationException(String detailMessage) {
        super(detailMessage);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param throwable
     */
    public ValidationException(Throwable throwable) {
        super(throwable);
        // TODO Auto-generated constructor stub
    }

}
