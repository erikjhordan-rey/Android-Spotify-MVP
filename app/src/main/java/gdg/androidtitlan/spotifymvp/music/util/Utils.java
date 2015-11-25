package gdg.androidtitlan.spotifymvp.music.util;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Jhordan on 22/11/15.
 */
public class Utils {


    public static boolean isAudioPlayerServiceRunning(Class<?> serviceClass, Context context) {
        boolean serviceState = false;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                serviceState = true;
            }
        }
        return serviceState;
    }
}
