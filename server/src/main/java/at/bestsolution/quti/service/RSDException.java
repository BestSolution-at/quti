package at.bestsolution.quti.service;

import at.bestsolution.quti.service.model._Base;

public class RSDException extends RuntimeException {
	public final RSDExceptionType type;

	RSDException(RSDExceptionType type) {
		this.type = type;
	}

	public static class RSDMessageException extends RSDException {
		public final String message;

		public RSDMessageException(RSDExceptionType type, String message) {
			super(type);
			this.message = message;
		}
	}

	public static class RSDStructuredDataException extends RSDException {
		public final _Base.BaseData data;

		public RSDStructuredDataException(RSDExceptionType type, _Base.BaseData data) {
			super(type);
			this.data = data;
		}
	}
}
