package io.github.erikjhordanrey.mvp.data.api.retrofit;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static io.github.erikjhordanrey.mvp.data.api.Constants.ACCESS_TOKEN;
import static io.github.erikjhordanrey.mvp.data.api.Constants.API_KEY;

class ApiInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder().header(API_KEY, ACCESS_TOKEN).method(original.method(), original.body()).build();
        return chain.proceed(request);
    }
}
