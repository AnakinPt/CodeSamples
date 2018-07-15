package pt.com.hugodias.cardservice.thirdparty;

public class CardDetailsException extends Exception {

	private static final long serialVersionUID = 1L;

	public CardDetailsException() {
		super();
	}

	public CardDetailsException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CardDetailsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CardDetailsException(String message) {
		super(message);
	}

	public CardDetailsException(Throwable cause) {
		super(cause);
	}

	
}
