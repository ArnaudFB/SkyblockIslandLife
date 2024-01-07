package fr.nono74210.skyblockislandlife.utils;

public abstract class ResultBase {
    protected boolean success;
    protected String errorMessage;

    protected ResultBase() {
        this.success = true;
    }

    protected ResultBase(String message) {
        this.success = false;
        this.errorMessage = message;
    }

    protected ResultBase(ResultBase result) {
        this.success = result.success;
        this.errorMessage = result.errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean inError() {
        return !success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}