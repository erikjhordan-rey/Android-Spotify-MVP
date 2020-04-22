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
package gdg.androidtitlan.spotifymvp.example.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import gdg.androidtitlan.spotifymvp.R;
import gdg.androidtitlan.spotifymvp.databinding.ActivityTracksBinding;
import gdg.androidtitlan.spotifymvp.example.data.api.client.SpotifyClient;
import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import gdg.androidtitlan.spotifymvp.example.data.model.Track;
import gdg.androidtitlan.spotifymvp.example.interactor.TracksInteractor;
import gdg.androidtitlan.spotifymvp.example.presenter.TracksPresenter;
import gdg.androidtitlan.spotifymvp.example.view.adapter.TracksAdapter;
import gdg.androidtitlan.spotifymvp.example.view.fragment.PlayerFragment;
import gdg.androidtitlan.spotifymvp.example.view.utils.BlurEffectUtils;
import java.lang.reflect.Type;
import java.util.List;

public class TracksActivity extends AppCompatActivity implements TracksPresenter.View, AppBarLayout.OnOffsetChangedListener {

    public static final String EXTRA_REPOSITORY = "EXTRA_ARTIST";
    public static final String EXTRA_TRACK_POSITION = "EXTRA_TRACK_POSITION";
    public static final String EXTRA_TRACKS = "EXTRA_TRACKS";

    private ActivityTracksBinding binding;

    private TracksPresenter tracksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTracksBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar();
        setupRecyclerView();
        setupPresenter();
        Artist artist = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        initializeViews(artist);
        tracksPresenter.onSearchTracks(artist.id);
    }

    @Override
    public void showLoading() {
        binding.pvTracks.setVisibility(View.VISIBLE);
        binding.ivTracks.setVisibility(View.GONE);
        binding.txtLineTracks.setVisibility(View.GONE);
        binding.rvTracks.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        binding.pvTracks.setVisibility(View.GONE);
        binding.rvTracks.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTracksNotFoundMessage() {
        binding.pvTracks.setVisibility(View.GONE);
        binding.txtLineTracks.setVisibility(View.VISIBLE);
        binding.ivTracks.setVisibility(View.VISIBLE);
        binding.txtLineTracks.setText(getString(R.string.error_tracks_not_found));
        binding.ivTracks.setImageDrawable(ContextCompat.getDrawable(context(), R.mipmap.ic_not_found));
    }

    @Override
    public void showConnectionErrorMessage() {
        binding.pvTracks.setVisibility(View.GONE);
        binding.txtLineTracks.setVisibility(View.VISIBLE);
        binding.ivTracks.setVisibility(View.VISIBLE);
        binding.txtLineTracks.setText(getString(R.string.error_internet_connection));
        binding.ivTracks.setImageDrawable(ContextCompat.getDrawable(context(), R.mipmap.ic_not_internet));
    }

    @Override
    public void renderTracks(List<Track> tracks) {
        TracksAdapter adapter = (TracksAdapter) binding.rvTracks.getAdapter();
        adapter.setTracks(tracks);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void launchTrackDetail(List<Track> tracks, Track track, int position) {
        PlayerFragment.newInstance(setTracks(tracks), position).show(getSupportFragmentManager(), "");
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        onOffsetChangedState(appBarLayout, verticalOffset);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onOffsetChangedState(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            hideAndShowTitleToolbar(View.GONE);
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            hideAndShowTitleToolbar(View.VISIBLE);
        } else {
            hideAndShowTitleToolbar(View.GONE);
        }
    }

    private void hideAndShowTitleToolbar(int visibility) {
        binding.txtLineTracks.setVisibility(visibility);
        binding.txtSubtitleArtist.setVisibility(visibility);
    }

    private void setupPresenter() {
        tracksPresenter = new TracksPresenter(new TracksInteractor(new SpotifyClient()));
        tracksPresenter.setView(this);
    }

    private void setupRecyclerView() {
        TracksAdapter adapter = new TracksAdapter();
        adapter.setItemClickListener((tracks, track, position) -> tracksPresenter.launchArtistDetail(tracks, track, position));
        binding.rvTracks.setAdapter(adapter);
        binding.appbarArtist.addOnOffsetChangedListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void initializeViews(Artist artist) {

        if (artist.artistImages.size() > 0) {
            Picasso.with(this)
                    .load(artist.artistImages.get(0).url)
                    .transform(new BlurEffectUtils(this, 20))
                    .into(binding.ivCollapsingArtist);
            Picasso.with(this).load(artist.artistImages.get(0).url).into(binding.civArtist);
        } else {
            final String imageHolder =
                    "http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png";
            binding.civArtist.setVisibility(View.GONE);
            Picasso.with(this)
                    .load(imageHolder)
                    .transform(new BlurEffectUtils(this, 20))
                    .into(binding.ivCollapsingArtist);
        }

        binding.txtTitleArtist.setText(artist.name);
        binding.txtSubtitleArtist.setText(artist.name);
        String totalFollowers = getResources().getQuantityString(R.plurals.numberOfFollowers,
                artist.followers.totalFollowers, artist.followers.totalFollowers);
        binding.txtFollowersArtist.setText(totalFollowers);
    }

    private String setTracks(List<Track> tracks) {
        Gson gson = new GsonBuilder().create();
        Type trackType = new TypeToken<List<Track>>() {
        }.getType();
        return gson.toJson(tracks, trackType);
    }

    @Override
    public Context context() {
        return TracksActivity.this;
    }
}
