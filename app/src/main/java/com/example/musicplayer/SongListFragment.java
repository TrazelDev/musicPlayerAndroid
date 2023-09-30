package com.example.musicplayer;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SongListFragment extends Fragment {
    private ListView listView;
    public SongListFragment()
    {

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_song_list, container, false);

        // Initialize the ListView
        listView = fragmentView.findViewById(R.id.songListView);

        ArrayList<Song> songs = new ArrayList<>(); // List to store Song objects
        Database database = new Database();

        database.fetchSongs(new Database.SongFetchListener() {
            @Override
            public void onFetchComplete(List<Song> fetchedSongs) {
                int totalSongs = fetchedSongs.size();
                AtomicInteger songsProcessed = new AtomicInteger(0);

                for (Song fetchedSong : fetchedSongs) {
                    // Create a new Song object for each fetched song
                    Song song = new Song(fetchedSong.getSongId(), fetchedSong.getSongName(), fetchedSong.hasPicture());

                    String songId = song.getSongId();

                    if(!fetchedSong.hasPicture())
                    {
                        songs.add(song);
                        songsProcessed.incrementAndGet();
                        // Check if all songs have been processed
                        if (songsProcessed.get() == totalSongs) {
                            // All image URIs have been fetched, create the adapter
                            adapter = new SongListAdapter(requireContext(), songs);
                            listView.setAdapter(adapter);
                            Toast.makeText(requireContext(), "switch1", Toast.LENGTH_SHORT).show();
                            adapter.setOnImageButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    function();
                                }
                            });
                        }
                    }

                    else {
                        // Retrieve the image URI asynchronously
                        database.retrievePictureFile(songId)
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri imageUri) {
                                        // Image URI retrieved successfully
                                        song.setImageUri(imageUri);

                                        // Add the song to the 'songs' ArrayList
                                        songs.add(song);

                                        // Increment the counter for processed songs
                                        songsProcessed.incrementAndGet();

                                        // Check if all songs have been processed
                                        if (songsProcessed.get() == totalSongs) {
                                            // All image URIs have been fetched, create the adapter
                                            adapter = new SongListAdapter(requireContext(), songs);
                                            listView.setAdapter(adapter);
                                            Toast.makeText(requireContext(), "switch2", Toast.LENGTH_SHORT).show();
                                            adapter.setOnImageButtonClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    function();
                                                }
                                            });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle the error if needed
                                    }
                                });
                    }
                }
            }

            @Override
            public void onFetchError(String error) {
                // Handle the error if needed
            }
        });

        return fragmentView;
    }


    @Override
    public void onResume()
    {
        super.onResume();

        ArrayList<Song> songs = new ArrayList<>(); // List to store Song objects
        Database database = new Database();

        database.fetchSongs(new Database.SongFetchListener() {
            @Override
            public void onFetchComplete(List<Song> fetchedSongs) {
                int totalSongs = fetchedSongs.size();
                AtomicInteger songsProcessed = new AtomicInteger(0);

                for (Song fetchedSong : fetchedSongs) {
                    // Create a new Song object for each fetched song
                    Song song = new Song(fetchedSong.getSongId(), fetchedSong.getSongName(), fetchedSong.hasPicture());

                    String songId = song.getSongId();

                    if(!fetchedSong.hasPicture())
                    {
                        songs.add(song);
                        songsProcessed.incrementAndGet();
                        // Check if all songs have been processed
                        if (songsProcessed.get() == totalSongs) {
                            // All image URIs have been fetched, create the adapter
                            adapter = new SongListAdapter(requireContext(), songs);
                            listView.setAdapter(adapter);
                            Toast.makeText(requireContext(), "switch1", Toast.LENGTH_SHORT).show();
                            adapter.setOnImageButtonClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view)
                                {
                                    function();
                                }
                            });
                        }
                    }

                    else {
                        // Retrieve the image URI asynchronously
                        database.retrievePictureFile(songId)
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri imageUri) {
                                        // Image URI retrieved successfully
                                        song.setImageUri(imageUri);

                                        // Add the song to the 'songs' ArrayList
                                        songs.add(song);

                                        // Increment the counter for processed songs
                                        songsProcessed.incrementAndGet();

                                        // Check if all songs have been processed
                                        if (songsProcessed.get() == totalSongs) {
                                            // All image URIs have been fetched, create the adapter
                                            adapter = new SongListAdapter(requireContext(), songs);
                                            listView.setAdapter(adapter);
                                            Toast.makeText(requireContext(), "switch2", Toast.LENGTH_SHORT).show();
                                            adapter.setOnImageButtonClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    function();
                                                }
                                            });
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Handle the error if needed
                                    }
                                });
                    }
                }
            }

            @Override
            public void onFetchError(String error) {
                // Handle the error if needed
            }
        });

    }


    /*
    private void showPopupMenu(View v) {
        Context wrapper = new ContextThemeWrapper(this, R.style.BottomPopupMenu);
        PopupMenu popupMenu = new PopupMenu(wrapper, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.song_popup_menu, popupMenu.getMenu()); // Replace with your menu resource

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_item1)
                {
                    return true;
                }
                if(item.getItemId() == R.id.menu_item2)
                {
                    return true;
                }

                return false;
            }
        });

        popupMenu.show();
    }
    */

    private void function()
    {
        Toast.makeText(requireContext(), "switch", Toast.LENGTH_SHORT).show();
        //Toast.makeText(requireContext(), "yes", Toast.LENGTH_SHORT).show();
        // Create a Bundle to pass data to the new MainFragment
        //Bundle bundle = new Bundle();
        //bundle.putString("key", "value"); // Replace with your data
        // newMainFragment.setArguments(bundle);

        if (getActivity() instanceof songPlayerAppActivity) {
            songPlayerAppActivity activity = (songPlayerAppActivity) getActivity();
            Fragment currentFragment = activity.getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (currentFragment != null) {
                FragmentManager fragmentManager = currentFragment.getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.songManipulation, new AddSongToPlayListFragment())
                        .addToBackStack(null)
                        .commit();
                Toast.makeText(requireContext(), "switch", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(requireContext(), "failed to access fragment", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(requireContext(), "failed the main activity", Toast.LENGTH_SHORT).show();
        }


        //FragmentManager fragmentManager = mainFragment.getChildFragmentManager();





        // HomePage newMainFragment = new HomePage();
        // FragmentManager fragmentManager = activity.getSupportFragmentManager();
        // FragmentTransaction transaction = fragmentManager.beginTransaction();
        // transaction.replace(R.id.fragment_container, newMainFragment);
        // transaction.addToBackStack(null);
        // transaction.commit();
    }
    SongListAdapter adapter;

}
