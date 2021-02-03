package framework.util;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class Try<R> {

    public enum Status {
        SUCCESS, CAUGHT
    }

    public static <T, R> Function<T, Try<R>> to(ThrowingFunction<T, R> fct) {
        return arg -> {
            try {
                return Try.of(fct.apply(arg));
            } catch (RuntimeException ex) {
                throw ex;
            } catch (Exception ex) {
                return Try.caught(ex);
            }
        };
    }

    public static <R, M> Function<Try<R>, Try<M>> map(Function<R, M> fct) {
        return try_arg -> try_arg.flatMap(fct);
    }

    public static <T> Collector<Try<T>, ?, Map<Status, List<Try<T>>>> groupingByStatus() {
        return Collectors.groupingBy(Try::status,
                () -> new EnumMap<>(Status.class),
                toList());
    }

    private static <R> Try<R> of(R result) {
        return new Success<>(result);
    }

    private static <R> Try<R> caught(Exception ex) {
        return new Caught<>(ex);
    }

    public abstract Status status();

    public abstract R get();

    public abstract Exception exception();

    protected abstract <M> Try<M> flatMap(Function<R, M> fct);

    private static class Success<R> extends Try<R> {
        private final R wrapped;

        public Success(R wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Status status() {
            return Status.SUCCESS;
        }

        @Override
        public R get() {
            return wrapped;
        }

        @Override
        public Exception exception() {
            return null;
        }

        @Override
        protected <M> Try<M> flatMap(Function<R, M> fct) {
            return new Success<>(fct.apply(wrapped));
        }
    }

    private static class Caught<R> extends Try<R> {
        private final Exception wrapped;

        public Caught(Exception wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public Status status() {
            return Status.CAUGHT;
        }

        @Override
        public R get() {
            return null;
        }

        @Override
        public Exception exception() {
            return wrapped;
        }

        @Override
        protected <M> Try<M> flatMap(Function<R, M> fct) {
            return (Try<M>) this;
        }
    }
}
