// Generated by RSD - Do not modify
package at.bestsolution.quti.client.dto;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.Objects;
import java.util.Optional;

public interface BaseDTO {
	public static class Either<T, U> {
		private T left;
		private U right;

		private Either(T left, U right) {
			this.left = left;
			this.right = right;
		}

		public static <T, U> Either<T, U> left(T left) {
			Objects.requireNonNull(left);
			return new Either<T, U>(left, null);
		}

		public static <T, U> Either<T, U> right(U right) {
			Objects.requireNonNull(right);
			return new Either<T, U>(null, right);
		}

		public Optional<U> isLeftPresent(Consumer<T> consumer) {
			if (left == null) {
				return Optional.of(right);
			}
			consumer.accept(left);
			return Optional.empty();
		}

		public void accept(Consumer<T> consumerLeft, Consumer<U> consumerRight) {
			if (left != null) {
				consumerLeft.accept(left);
			} else {
				consumerRight.accept(right);
			}
		}

		public <R> R apply(Function<T, R> transformLeft, Function<U, R> transformRight) {
			if (left != null) {
				return transformLeft.apply(left);
			}
			return transformRight.apply(right);
		}
	}

	public interface Builder {
		public BaseDTO build();
	}
}
