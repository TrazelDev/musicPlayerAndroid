package com.example.musicplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SongUploadFragment extends Fragment
{
    ImageView userSongImage;
    Button uploadPicture;
    Button uploadSong;
    Button sendSong;
    CheckBox noImageBox;
    TextInputEditText songName;
    Uri imageUri;
    Uri songUri;
    TextView currSongUploaded;
    public SongUploadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View fragmentView = inflater.inflate(R.layout.fragment_song_upload, container, false);

        songUri = null;
        imageUri = null;

        currSongUploaded = fragmentView.findViewById(R.id.CurrentSongUploaded);
        userSongImage = fragmentView.findViewById(R.id.songImage);

        // helper that create an activity gets its URI
        // URI (Uniform Resource Identifier) - similar to a computer file path this is the structure of it:
        // scheme:[//authority]path[?query][#fragment]
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri ->
                {
                    if (uri != null)
                    {
                        // in the case that the uri is of the mp3
                        if(checkUriIsMp3(uri))
                        {
                            songUri = uri;

                            currSongUploaded.setText("Song uploaded: " + getFileNameFromUri(songUri));
                        }
                        else
                        {
                            imageUri = uri;

                            Glide.with(requireContext())
                                    .load(imageUri)
                                    .into(userSongImage);

                            Toast.makeText(requireContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        uploadPicture = fragmentView.findViewById(R.id.uploadPicture);
        uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();

                if(activity != null)
                {
                    mGetContent.launch("image/*"); // accepting picture files
                }
            }
        });

        uploadSong = fragmentView.findViewById(R.id.uploadSong);
        uploadSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();

                if(activity != null)
                {
                    mGetContent.launch("audio/mpeg"); // accepting mp3 files
                }
            }
        });

        sendSong = fragmentView.findViewById(R.id.sendSong);
        sendSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSong();
            }
        });

        noImageBox = fragmentView.findViewById(R.id.songNoWithNoPic);

        songName = fragmentView.findViewById(R.id.songName);

        return fragmentView;
    }

    private boolean checkUriIsMp3(Uri uri)
    {
        ContentResolver contentResolver = requireContext().getContentResolver();
        String fileExtension = getFileExtension(uri);
        String mimeType = contentResolver.getType(uri);

        // Check if the MIME type is audio/mpeg or if the file extension is mp3
        return "audio/mpeg".equals(mimeType) || "audio/mp3".equals(mimeType) || "mp3".equals(fileExtension);
    }

    private static String getFileExtension(Uri uri)
    {
        String extension = null;
        String uriString = uri.toString();
        int lastDotIndex = uriString.lastIndexOf(".");


        if (lastDotIndex != -1)
        {
            extension = uriString.substring(lastDotIndex + 1);
        }
        return extension;
    }

    // suppresses the lint ( lint is an android tool that gives warnings and errors about potential performance issues):
    @SuppressLint("Range")
    public String getFileNameFromUri(Uri uriFile)
    {
        String fileName = null;

        // checking if the the type of the uri is content:
        if(uriFile.getScheme().equals("content"))
        {
            // getting the content resolver that helps access and manage the data of uri, and using it query the data
            // that is associated with the file uri
            Cursor cursor = requireContext().getContentResolver().query(uriFile, null,
                    null, null, null);

            try
            {
                if(cursor != null && cursor.moveToFirst())
                {
                    // getting the file name using DISPLAY_NAME column and the cursor
                    fileName =  cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally
            {
                if(cursor != null)
                {
                    cursor.close();
                }
            }
        }

        // in the cast that the file is not a content type we use a more simple way:
        if(fileName == null)
        {
            fileName = uriFile.getPath();
            int cut = fileName.lastIndexOf('/');
            if(cut != -1)
            {
                fileName = fileName.substring(cut + 1);
            }
        }

        return fileName;
    }

    private void uploadSong()
    {
        if(imageUri == null && !noImageBox.isChecked())
        {
            Toast.makeText(requireContext(), "pls upload an image or check the box for no image", Toast.LENGTH_SHORT).show();
            return;
        }
        if(songUri == null)
        {
            Toast.makeText(requireContext(), "pls upload a song", Toast.LENGTH_SHORT).show();
            return;
        }
        String songsName = String.valueOf(songName.getText());
        if(TextUtils.isEmpty(songsName))
        {
            Toast.makeText(requireContext(), "pls enter the song name", Toast.LENGTH_SHORT).show();
            return;
        }

        Database temp = new Database();
        temp.uploadNewSong(songsName, "0:00", !noImageBox.isChecked(), songUri, imageUri, requireContext());

        /*
        // getting the users email
        String usesEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference fileRef = storageRef.child(usesEmail + "/songs/" + songsName);

        fileRef.putFile(songUri)
                .addOnSuccessListener(taskSnapshot ->
                {
                    Toast.makeText(requireContext(), "uploaded song successfully", Toast.LENGTH_LONG).show();

                    if (!noImageBox.isChecked())
                    {
                        songUri = null;
                        storageRef.child(usesEmail + "/pictures/" + songsName).putFile(imageUri)
                                .addOnSuccessListener(imageUploadTask ->
                                {
                                    imageUri = null;
                                    Toast.makeText(requireContext(), "Uploaded image successfully", Toast.LENGTH_LONG).show();



                                })
                                .addOnFailureListener(imageUploadException -> {
                                    Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                                });
                    }

                })
                .addOnFailureListener(exception -> {
                    Toast.makeText(requireContext(), "failed to upload song", Toast.LENGTH_SHORT).show();
                });



         */

    }
}