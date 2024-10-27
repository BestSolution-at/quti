package at.bestsolution.quti.service;

public record Result<T>(ResultType type, T value, String message) {
	public enum ResultType {
		OK,
		NOT_FOUND,
		INVALID_CONTENT,
		INVALID_PARAMETER
	}

	public static Result<Void> OK = ok(null);

	public Result<Void> toVoid() {
		return new Result<Void>(type, null, message);
	}

	public <X> Result<X> toAny() {
		if( ! isOk() ) {
			return new Result<X>(type, null, message);
		}
		throw new UnsupportedOperationException("You can only convert a failure to any result");
	}

	public boolean isOk() {
		return type == ResultType.OK;
	}

	public boolean isNotOk() {
		return ! isOk();
	}

	public static <T> Result<T> ok(T value) {
		return new Result<T>(ResultType.OK, value, null);
	}

	public static <T> Result<T> notFound(String message, Object... args) {
		return new Result<T>(ResultType.NOT_FOUND, null, String.format(message, args));
	}

	public static <T> Result<T> invalidContent(String message, Object... args) {
		return new Result<T>(ResultType.INVALID_CONTENT, null, String.format(message, args));
	}

	public static <T> Result<T> invalidParameter(String message, Object... args) {
		return new Result<T>(ResultType.INVALID_PARAMETER, null, String.format(message, args));
	}
}
