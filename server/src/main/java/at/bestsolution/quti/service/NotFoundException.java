package at.bestsolution.quti.service;

public class NotFoundException extends RSDException.RSDMessageException {
	public NotFoundException(String message) {
		super(RSDExceptionType.NotFoundException, message);
	}
}
