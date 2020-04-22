package io.github.erikjhordanrey.mvp.data.api.retrofit.deserializer;

import com.google.gson.JsonDeserializer;
import java.util.List;

interface ListDeserializer<T> extends JsonDeserializer<List<T>> {
}
