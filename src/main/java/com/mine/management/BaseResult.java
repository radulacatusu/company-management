package com.mine.management;

import java.util.ArrayList;
import java.util.List;

/**
 * @param <T>
 */
public class BaseResult<T> {
    private List<String> errors;
    private T result;

    public BaseResult() {
        errors = new ArrayList<>();
    }

    public List<String> getErrors() {
        return errors;
    }

    public void addError(String error) {
        errors.add(error);
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
