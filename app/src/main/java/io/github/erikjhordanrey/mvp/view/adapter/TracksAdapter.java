/**
 * Copyright 2015 Erik Jhordan Rey.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.erikjhordanrey.mvp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import io.github.erikjhordanrey.mvp.data.model.Track;
import io.github.erikjhordanrey.mvp.databinding.ItemTrackBinding;
import java.util.Collections;
import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.TracksViewHolder> {

    private List<Track> tracks;
    private ItemClickListener itemClickListener;

    public TracksAdapter() {
        tracks = Collections.emptyList();
    }

    @NonNull
    @Override
    public TracksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TracksViewHolder(ItemTrackBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(TracksViewHolder holder, int position) {
        Track track = tracks.get(position);
        holder.render(track, position);
        holder.itemView.setOnClickListener((View view) -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(tracks, track, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(List<Track> tracks, Track track, int position);
    }

    static class TracksViewHolder extends RecyclerView.ViewHolder {

        private ItemTrackBinding binding;

        TracksViewHolder(ItemTrackBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void render(Track track, Integer position) {
            final String trackTitle = (position + 1) + "." + track.name;
            binding.txtTrackTitle.setText(trackTitle);
            binding.txtTrackAlbum.setText(track.album.albumName);

            if (track.album.trackImages.size() > 0) {
                binding.ivTrack.setScaleType(ImageView.ScaleType.FIT_XY);
                for (int i = 0; i < track.album.trackImages.size(); i++) {
                    if (track.album.trackImages.get(i) != null) {
                        renderImage(track.album.trackImages.get(0).url);
                    }
                }
            } else {
                renderImage("http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png");
            }
        }

        private void renderImage(String url) {
            Picasso.with(binding.ivTrack.getContext())
                    .load(url)
                    .into(binding.ivTrack);
        }
    }
}
