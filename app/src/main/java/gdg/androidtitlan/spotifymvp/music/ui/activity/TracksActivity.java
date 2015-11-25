package gdg.androidtitlan.spotifymvp.music.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import gdg.androidtitlan.spotifymvp.R;
import gdg.androidtitlan.spotifymvp.music.api.model.Artist;
import gdg.androidtitlan.spotifymvp.music.api.model.Track;
import gdg.androidtitlan.spotifymvp.music.presenter.TracksPresenter;
import gdg.androidtitlan.spotifymvp.music.ui.adapter.TracksAdapter;
import gdg.androidtitlan.spotifymvp.music.ui.fragment.AudioPlayerFragment;
import gdg.androidtitlan.spotifymvp.music.util.BlurEffect;
import gdg.androidtitlan.spotifymvp.music.view.TracksMvpView;


import android.support.design.widget.AppBarLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jhordan on 21/11/15.
 */

public class TracksActivity extends AppCompatActivity implements TracksMvpView, AppBarLayout.OnOffsetChangedListener {

    public static final String EXTRA_REPOSITORY = "EXTRA_ARTIST";
    public static final String EXTRA_TRACK_POSITION = "EXTRA_TRACK_POSITION";
    public static final String EXTRA_TRACKS = "EXTRA_TRACKS";


    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.appbar_artist) AppBarLayout appbar_artist;
    @Bind(R.id.iv_collapsing_artist) ImageView iv_collapsing_artist;
    @Bind(R.id.civ_artist) CircleImageView civ_artist;
    @Bind(R.id.txt_title_artist) TextView txt_title_artist;
    @Bind(R.id.txt_title_tracks) TextView txt_title_tracks;
    @Bind(R.id.txt_followers_artist) TextView txt_followers_artist;
    @Bind(R.id.txt_subtitle_artist) TextView txt_subtitle_artist;
    @Bind(R.id.rv_tracks) RecyclerView rv_tracks;
    @Bind(R.id.pv_tracks) ProgressBar pv_tracks;
    @Bind(R.id.iv_tracks) ImageView iv_tracks;
    @Bind(R.id.txt_line_tracks) TextView txt_line_tracks;

    private TracksPresenter tracksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);

        ButterKnife.bind(this);
        setupToolbar();
        setupRecyclerView();

        tracksPresenter = new TracksPresenter();
        tracksPresenter.attachedView(this);


        Artist artist = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        initializeViews(artist);

        tracksPresenter.onSearchTracks(artist.id);

    }

    @Override public void showLoading() {
        pv_tracks.setVisibility(View.VISIBLE);
        iv_tracks.setVisibility(View.GONE);
        txt_line_tracks.setVisibility(View.GONE);
        rv_tracks.setVisibility(View.GONE);
    }

    @Override public void hideLoading() {
        pv_tracks.setVisibility(View.GONE);
        rv_tracks.setVisibility(View.VISIBLE);
    }

    @Override public void showTracksNotFoundMessage() {
        pv_tracks.setVisibility(View.GONE);
        txt_line_tracks.setVisibility(View.VISIBLE);
        iv_tracks.setVisibility(View.VISIBLE);
        txt_line_tracks.setText(getString(R.string.error_tracks_not_found));
        iv_tracks.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_not_found));
    }

    @Override public void showConnectionErrorMessage() {
        pv_tracks.setVisibility(View.GONE);
        txt_line_tracks.setVisibility(View.VISIBLE);
        iv_tracks.setVisibility(View.VISIBLE);
        txt_line_tracks.setText(getString(R.string.error_internet_connection));
        iv_tracks.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_not_internet));
    }

    @Override public void renderTracks(List<Track> tracks) {
        TracksAdapter adapter = (TracksAdapter)rv_tracks.getAdapter();
        adapter.setTracks(tracks);
        adapter.notifyDataSetChanged();
    }

    @Override public void launchTrackDetail(List<Track> tracks,Track track, int position) {
        AudioPlayerFragment.newInstance(setTracks(tracks), position).show(getSupportFragmentManager(), "");
    }

    @Override public Context getContext() {
        return this;
    }


    @Override public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
      onOffsetChangedState(appBarLayout, verticalOffset);
    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void onOffsetChangedState(AppBarLayout appBarLayout, int verticalOffset){
        State state = null;

        if (verticalOffset == 0) {
            if (state != State.EXPANDED)
                hideAndShowTitleToolbar(View.GONE);

            state = State.EXPANDED;
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
            if (state != State.COLLAPSED)
                hideAndShowTitleToolbar(View.VISIBLE);

            state = State.COLLAPSED;
        } else {
            if (state != State.IDLE)
                hideAndShowTitleToolbar(View.GONE);

            state = State.IDLE;
        }

    }

    private enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private void hideAndShowTitleToolbar(int visibility){
        txt_title_tracks.setVisibility(visibility);
        txt_subtitle_artist.setVisibility(visibility);

    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);
        rv_tracks.setLayoutManager(linearLayoutManager);
        TracksAdapter adapter = new TracksAdapter();
        adapter.setItemClickListener((List<Track> tracks,Track track, int position) -> tracksPresenter.launchArtistDetail(tracks,track,position));
        rv_tracks.setAdapter(adapter);

        //initialize listener for app bar
        appbar_artist.addOnOffsetChangedListener(this);

    }

    private void setupToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setShowHideAnimationEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    private void initializeViews(Artist artist){

        if(artist.artistImages.size() > 0 ){
            Picasso.with(this)
                    .load(artist.artistImages.get(0).url)
                    .transform(new BlurEffect(this, 20))
                    .into(iv_collapsing_artist);
            Picasso.with(this)
                    .load(artist.artistImages.get(0).url)
                    .into(civ_artist);
        }else{
            final String IMAGE_HOLDER = "http://d2c87l0yth4zbw-2.global.ssl.fastly.net/i/_global/open-graph-default.png";
            civ_artist.setVisibility(View.GONE);
            Picasso.with(this)
                    .load(IMAGE_HOLDER)
                    .transform(new BlurEffect(this, 20))
                    .into(iv_collapsing_artist);
        }

        txt_title_artist.setText(artist.name);
        txt_subtitle_artist.setText(artist.name);
        String totalFollowers =
                getResources().getQuantityString(R.plurals.numberOfFollowers,
                        artist.followers.totalFollowers, artist.followers.totalFollowers);
        txt_followers_artist.setText(totalFollowers);
    }


    private String setTracks(List<Track> tracks){
        Gson gson = new GsonBuilder().create();
        Type trackType = new TypeToken<List<Track>>(){}.getType();
        return gson.toJson(tracks,trackType);
    }


}