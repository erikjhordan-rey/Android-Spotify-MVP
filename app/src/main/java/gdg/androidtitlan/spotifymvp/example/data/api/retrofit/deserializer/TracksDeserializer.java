package gdg.androidtitlan.spotifymvp.example.data.api.retrofit.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import gdg.androidtitlan.spotifymvp.example.data.api.Constants;
import java.lang.reflect.Type;
import java.util.List;

public class TracksDeserializer<T> implements ListDeserializer<T> {

    @Override
    public List<T> deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement trackJsonArray = jsonElement.getAsJsonObject().get(Constants.Deserializer.TRACKS);
        return new Gson().fromJson(trackJsonArray, typeOfT);
    }
}
