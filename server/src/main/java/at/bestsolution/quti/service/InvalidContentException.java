package at.bestsolution.quti.service;

public class InvalidContentException extends RSDException.RSDMessageException {
	public InvalidContentException(String message) {
		super(RSDExceptionType.InvalidContentException, message);
	}
}
