package com.circulosiete.minibank.account.api.client;

import codes.domix.fun.Result;
import codes.domix.fun.Try;
import com.circulosiete.minibank.account.api.dto.ApiError;
import java.util.function.Supplier;

public class HttpClientUtil {
    private final Error error = new Error();

    public <T> Result<T, ApiError> invokes(Supplier<T> request) {
        return Try.of(request::get)
            .toResult()
            .mapError(error::getApiError);
    }
}
