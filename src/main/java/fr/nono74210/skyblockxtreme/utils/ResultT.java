package fr.nono74210.skyblockxtreme.utils;

public class ResultT<T> extends ResultBase {
    private final T result;

    public ResultT(T result) {
        super();
        this.result = result;
    }

    public ResultT(String message) {
        super(message);
        this.result = null;
    }

    public ResultT(Result result) {
        super(result);
        this.result = null;
    }

    public T getResult() {
        return result;
    }

    public static <T> ResultT<T> success(T result) {
        return new ResultT<>(result);
    }

    public static <T> ResultT<T> error(String message) {
        return new ResultT<>(message);
    }

    public static <T> ResultT<T> of(Result result) {
        return new ResultT<>(result);
    }

    public static <T> ResultT<T> of(ResultT<?> result) {
        if (result.isSuccess())
            throw new RuntimeException("NotPossibleException");

        return new ResultT<>(result.errorMessage);
    }
}