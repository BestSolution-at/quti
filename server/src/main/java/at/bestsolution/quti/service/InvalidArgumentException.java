package at.bestsolution.quti.service;

public class InvalidArgumentException extends _RSDException.RSDMessageException {
	public InvalidArgumentException(String message) {
		super(message);
	}
}
