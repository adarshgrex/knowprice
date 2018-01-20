/*
 * (c) Copyright 2001-2015 COMIT AG
 * All Rights Reserved.
 */
package exception;

public class KnowPriceException extends RuntimeException {

    private static final long serialVersionUID = -4103999309724194764L;
    Exception rootCause;

    public KnowPriceException(Exception rootCause) {
        super();
        this.rootCause = rootCause;
    }

    public Exception getRootCause() {
        return rootCause;
    }
}
