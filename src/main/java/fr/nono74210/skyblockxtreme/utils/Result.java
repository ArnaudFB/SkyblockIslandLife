package fr.nono74210.skyblockxtreme.utils;

public class Result extends ResultBase {

    public Result() {
        super();
    }

    public Result(String message) {
        super(message);
    }

    public static Result success() {
        return new Result();
    }

    public static Result error(String message) {
        return new Result(message);
    }

    public static Result of(ResultT<?> result) {
        if (result.isSuccess())
            return Result.success();

        return new Result(result.getErrorMessage());
    }
}