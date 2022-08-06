package com.manning.application.notification.formatters;

import com.manning.application.notification.common.exceptions.NotificationServiceApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ClientsErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        var requestUrl = response.request().url();
        var status = response.status();
        var responseBody = response.body();

        return new NotificationServiceApiException(
                requestUrl,
                status,
                toString(responseBody)
        );
    }

    private static String toString(Response.Body responseBody) {
        try {
            InputStream is = responseBody.asInputStream();
            var buffer = new byte[is.available()];
            new BufferedInputStream(is).read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            return "'No response body found'";
        }
    }
}
