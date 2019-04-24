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

package gdg.androidtitlan.spotifymvp.example.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import gdg.androidtitlan.spotifymvp.R;
import gdg.androidtitlan.spotifymvp.example.data.model.Track;

public class TracksAdapter extends RecyclerView.Adapter<TracksAdapter.TracksViewHolder> {

  private List<Track> tracks;
  private ItemClickListener itemClickListener;

  public TracksAdapter() {
    tracks = Collections.emptyList();
  }

  @NonNull
  @Override public TracksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
    return new TracksViewHolder(itemView);
  }

  @Override public void onBindViewHolder(TracksViewHolder holder, int position) {
    Track track = tracks.get(position);

    holder.txt_title_tracks.setText((position + 1) + "." + track.name);
    holder.txt_track_album.setText(track.album.albumName);

    if (track.album.trackImages.size() > 0) {
      holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      for (int i = 0; i < track.album.trackImages.size(); i++) {
        if (track.album.trackImages.get(i) != null && track.album.trackImages.size() > 0) {
          Picasso.with(holder.imageView.getContext())
              .load(track.album.trackImages.get(0).url)
              .into(holder.imageView);
        }
      }
    } else {

      Picasso.with(holder.imageView.getContext())
          .load("http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png")
          .into(holder.imageView);
    }

    holder.itemView.setOnClickListener((View view) -> {
      if (itemClickListener != null) {
        itemClickListener.onItemClick(tracks, track, position);
      }
    });
  }

  @Override public int getItemCount() {
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

    @BindView(R.id.iv_track) ImageView imageView;
    @BindView(R.id.txt_track_title) TextView txt_title_tracks;
    @BindView(R.id.txt_track_album) TextView txt_track_album;

    View itemView;

    TracksViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      this.itemView = itemView;
    }
  }
}
