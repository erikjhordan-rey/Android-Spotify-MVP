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

package gdg.androidtitlan.spotifymvp.example.view.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import gdg.androidtitlan.spotifymvp.R;
import gdg.androidtitlan.spotifymvp.databinding.FragmentArtistsBinding;
import gdg.androidtitlan.spotifymvp.example.data.api.client.SpotifyClient;
import gdg.androidtitlan.spotifymvp.example.data.model.Artist;
import gdg.androidtitlan.spotifymvp.example.interactor.ArtistsInteractor;
import gdg.androidtitlan.spotifymvp.example.presenter.ArtistsPresenter;
import gdg.androidtitlan.spotifymvp.example.view.activity.TracksActivity;
import gdg.androidtitlan.spotifymvp.example.view.adapter.ArtistsAdapter;
import java.util.List;

public class ArtistsFragment extends Fragment implements ArtistsPresenter.View, SearchView.OnQueryTextListener {

    public static ArtistsFragment newInstance() {
        return new ArtistsFragment();
    }

    private ArtistsPresenter artistsPresenter;

    private FragmentArtistsBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentArtistsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupPresenter();
        setupToolbar();
        setupRecyclerView();
    }

    @Override
    public void onDestroy() {
        artistsPresenter.terminate();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_music, menu);
        setupSearchView(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_github) {
            startActivityActionView();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        artistsPresenter.onSearchArtist(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public void showLoading() {
        binding.pvArtists.setVisibility(View.VISIBLE);
        binding.ivArtists.setVisibility(View.GONE);
        binding.txtLineArtists.setVisibility(View.GONE);
        binding.txtSublineArtists.setVisibility(View.GONE);
        binding.rvArtists.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        binding.pvArtists.setVisibility(View.GONE);
        binding.rvArtists.setVisibility(View.VISIBLE);
    }

    @Override
    public void showArtistNotFoundMessage() {
        binding.pvArtists.setVisibility(View.GONE);
        binding.txtLineArtists.setVisibility(View.VISIBLE);
        binding.ivArtists.setVisibility(View.VISIBLE);
        binding.txtLineArtists.setText(getString(R.string.error_artist_not_found));
        binding.ivArtists.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.ic_not_found));
    }

    @Override
    public void showConnectionErrorMessage() {
        binding.pvArtists.setVisibility(View.GONE);
        binding.txtLineArtists.setVisibility(View.VISIBLE);
        binding.ivArtists.setVisibility(View.VISIBLE);
        binding.txtLineArtists.setText(getString(R.string.error_internet_connection));
        binding.ivArtists.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_not_internet));
    }

    @Override
    public void showServerError() {
        binding.pvArtists.setVisibility(View.GONE);
        binding.txtLineArtists.setVisibility(View.VISIBLE);
        binding.ivArtists.setVisibility(View.VISIBLE);
        binding.txtLineArtists.setText(getString(R.string.error_server_internal));
        binding.ivArtists.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.mipmap.ic_not_found));
    }

    @Override
    public void renderArtists(List<Artist> artists) {
        ArtistsAdapter adapter = (ArtistsAdapter) binding.rvArtists.getAdapter();
        adapter.setArtists(artists);
        adapter.notifyDataSetChanged();
    }

    private void setupSearchView(Menu menu) {
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setMaxWidth(binding.toolbar.toolbar.getWidth());
        searchView.setOnQueryTextListener(this);
    }

    private void setupPresenter() {
        artistsPresenter = new ArtistsPresenter(new ArtistsInteractor(new SpotifyClient()));
        artistsPresenter.setView(this);
    }

    private void setupToolbar() {
        getParentActivity().setSupportActionBar(binding.toolbar.toolbar);
        ActionBar actionBar = getParentActivity().getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_action_navigation_menu);
        }
    }

    private void setupRecyclerView() {
        ArtistsAdapter adapter = new ArtistsAdapter();
        adapter.setItemClickListener((Artist artist, int position) -> artistsPresenter.launchArtistDetail(artist));
        binding.rvArtists.setAdapter(adapter);
    }

    @Override
    public void launchArtistDetail(Artist artist) {
        Intent intent = new Intent(getContext(), TracksActivity.class);
        intent.putExtra(TracksActivity.EXTRA_REPOSITORY, artist);
        startActivity(intent);
    }

    private void startActivityActionView() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/erikjhordan-rey/Android-Spotify-MVP")));
    }

    @Override
    public Context context() {
        return null;
    }

    private AppCompatActivity getParentActivity() {
        return ((AppCompatActivity) getActivity());
    }
}
