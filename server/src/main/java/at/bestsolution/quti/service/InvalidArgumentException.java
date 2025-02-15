package at.bestsolution.quti.service;

public class InvalidArgumentException extends RSDException.RSDMessageException {
	public InvalidArgumentException(String message) {
		super(RSDExceptionType.InvalidArgumentException, message);
	}
}
