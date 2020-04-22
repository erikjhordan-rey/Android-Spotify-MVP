package gdg.androidtitlan.spotifymvp.example.data.api.retrofit.deserializer;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import gdg.androidtitlan.spotifymvp.example.data.api.Constants;
import java.lang.reflect.Type;
import java.util.List;

public class ArtistsDeserializer<T> implements ListDeserializer<T> {

    @Override
    public List<T> deserialize(JsonElement je, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject artistJsonObject =
                je.getAsJsonObject().get(Constants.Deserializer.ARTISTS).getAsJsonObject();
        JsonElement artistJsonArray = artistJsonObject.getAsJsonArray(Constants.Deserializer.ITEMS);
        return new Gson().fromJson(artistJsonArray, typeOfT);
    }
}
