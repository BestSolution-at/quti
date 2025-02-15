package at.bestsolution.quti.service;

import at.bestsolution.quti.service.model._Base;

public class _RSDException extends RuntimeException {
	public final _ExceptionType type;

	_RSDException(_ExceptionType type) {
		this.type = type;
	}

	public static class RSDMessageException extends _RSDException {
		public final String message;

		public RSDMessageException(_ExceptionType type, String message) {
			super(type);
			this.message = message;
		}
	}

	public static class RSDStructuredDataException extends _RSDException {
		public final _Base.BaseData data;

		public RSDStructuredDataException(_ExceptionType type, _Base.BaseData data) {
			super(type);
			this.data = data;
		}
	}
}
