package gdg.androidtitlan.spotifymvp.music.api.exception;

import retrofit.HttpException;

/**
 * Created by Jhordan on 22/11/15.
 */
public class HttpNotFound {

    public final static String SERVER_INTERNET_ERROR = "Unable to resolve host \"multimedia.telesurtv.net\": No address associated with hostname";
    public static boolean isHttp404(Throwable error) {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }
}
