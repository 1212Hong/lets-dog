package com.dog.fileupload.common.api;

import com.dog.fileupload.common.error.ErrorCodeIfs;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Api<T> {
    private Result result;
    private T body;

    public static <T> Api<T> ok(T data) {
        var api = new Api<T>();
        api.result = Result.ok();
        api.body = data;
        return api;
    }

    public static Api<Object> error(Result result) {
        var api = new Api<Object>();
        api.result = result;
        return api;
    }

    public static Api<Object> error(ErrorCodeIfs errorCodeIfs) {
        var api = new Api<Object>();
        api.result = Result.error(errorCodeIfs);
        return api;
    }

    public static Api<Object> error(ErrorCodeIfs errorCodeIfs, String description) {
        var api = new Api<Object>();
        api.result = Result.error(errorCodeIfs, description);
        return api;
    }

}
