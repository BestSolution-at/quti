package at.bestsolution.quti.service;

import at.bestsolution.quti.service.model._Base;

public class _RSDException extends RuntimeException {
	public static class RSDMessageException extends _RSDException {
		public final String message;

		public RSDMessageException(String message) {
			this.message = message;
		}
	}

	public static class RSDStructuredDataException extends _RSDException {
		public final _Base.BaseData data;

		public RSDStructuredDataException(_Base.BaseData data) {
			this.data = data;
		}
	}
}
