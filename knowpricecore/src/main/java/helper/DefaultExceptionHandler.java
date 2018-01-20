package helper;

import exception.KnowPriceException;

public class DefaultExceptionHandler implements ExceptionHandler {

	@Override
	public void handleException(Exception rootCause) {
		rootCause.printStackTrace();
		throw new KnowPriceException(rootCause);
	}
}
