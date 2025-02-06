package at.bestsolution.quti.rest.model;

import java.util.function.Consumer;
import java.util.function.Function;

import at.bestsolution.quti.service.model._Base;

public class _NillableImpl<T> implements _Base.Nillable<T> {
	private static _NillableImpl<?> UNDEFINED = new _NillableImpl<>(null);
	private static _NillableImpl<?> NULL = new _NillableImpl<>(null);
	private final T value;

	_NillableImpl(T value) {
		this.value = value;
	}

	@Override
	public <X> X apply(Function<T, X> function, X defaultValue) {
		if (this == UNDEFINED) {
			return defaultValue;
		}
		return function.apply(value);
	}

	@Override
	public void accept(Consumer<T> block) {
		if (this != UNDEFINED) {
			block.accept(value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> _Base.Nillable<X> map(Function<T, X> mapper) {
		if (this == UNDEFINED) {
			return (_Base.Nillable<X>) UNDEFINED;
		}
		return of(mapper.apply(value));
	}

	@SuppressWarnings("unchecked")
	public static <T> _Base.Nillable<T> undefined() {
		return (_NillableImpl<T>) UNDEFINED;
	}

	@SuppressWarnings("unchecked")
	public static <T> _Base.Nillable<T> nill() {
		return (_NillableImpl<T>) NULL;
	}

	public static <T> _Base.Nillable<T> of(T value) {
		if (value != null) {
			return new _NillableImpl<>(value);
		}
		return nill();
	}
}
