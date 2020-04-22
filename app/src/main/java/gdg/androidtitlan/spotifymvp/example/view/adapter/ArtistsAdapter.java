package gdg.androidtitlan.spotifymvp.example.view.adapter;

/**
 * Copyright 2015 Erik Jhordan Rey.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import gdg.androidtitlan.spotifymvp.databinding.ItemArtistBinding;
import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import java.util.Collections;
import java.util.List;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistsViewHolder> {

    private List<Artist> artists;
    private ItemClickListener itemClickListener;

    public ArtistsAdapter() {
        artists = Collections.emptyList();
    }

    @NonNull
    @Override
    public ArtistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistsViewHolder(ItemArtistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistsViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.render(artist);
        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(artist, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(Artist artist, int position);
    }

    static class ArtistsViewHolder extends RecyclerView.ViewHolder {

        private ItemArtistBinding binding;

        ArtistsViewHolder(ItemArtistBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void render(Artist artist) {
            binding.txtArtistName.setText(artist.name);

            if (artist.artistImages.size() > 0) {

                for (int i = 0; i < artist.artistImages.size(); i++) {
                    if (artist.artistImages.get(i) != null) {
                        artist.artistImages.size();
                        renderImage(artist.artistImages.get(0).url);
                    }
                }
            } else {
                final String imageHolder =
                        "http://www.iphonemode.com/wp-content/uploads/2016/07/Spotify-new-logo.jpg";
                renderImage(imageHolder);
            }
        }

        private void renderImage(String url) {
            Picasso.with(binding.imgViewArtistImage.getContext())
                    .load(url)
                    .into(binding.imgViewArtistImage);
        }
    }
}
