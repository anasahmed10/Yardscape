package com.example.yardscapelistingprototype.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yardscapelistingprototype.Listing;
import com.example.yardscapelistingprototype.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment {
    Button createButton, uploadButton;
    EditText create_title;
    EditText create_desc;
    EditText timeView;
    EditText create_date;
    ImageView imagePreview;

    Listing inputListing = new Listing();
    FirebaseAuth mAuth;

    // Firebase Information for Image Uploading
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private Uri filePath;
    String imageName;


    public CreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance() {
        CreateFragment fragment = new CreateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReferenceFromUrl("gs://yardscape-1c1d8.appspot.com/images/");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        create_title = view.findViewById(R.id.create_name);
        create_desc = view.findViewById(R.id.create_description);
        timeView = view.findViewById(R.id.create_time);
        create_date = view.findViewById(R.id.create_date);
        uploadButton = (Button) view.findViewById(R.id.uploadImageButton);
        imagePreview = (ImageView) view.findViewById(R.id.imagePreview);

        // Calls the datePickerFragment when the date input box is clicked on
        create_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment(create_date);
                datePicker.setTargetFragment(CreateFragment.this, 3);
                datePicker.show(getActivity().getSupportFragmentManager(), "datePicker");

            }
        });

        // Calls the TimePickerFragment when the time input box is clicked on
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment(timeView);
                timePicker.setTargetFragment(CreateFragment.this, 4);
                timePicker.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        createButton = (Button) view.findViewById(R.id.createListingButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fieldCheck() == true) {
                    createListing();
                    databaseReference = firebaseDatabase.getReference().child("listings").push();
                    databaseReference.setValue(inputListing);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("openListing", inputListing);
                    Toast.makeText(requireContext(), "Listing Created Successfully", Toast.LENGTH_SHORT).show();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    androidx.fragment.app.Fragment fragment = new OpenListingFragment(bundle);
                    fragmentTransaction.replace(R.id.flContent, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    // Opens the DatePickerDialog and updates the input once the user picks the date
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        EditText dateEdit = (EditText) view.findViewById(R.id.create_date);
        dateEdit.setText(currentDateString, TextView.BufferType.EDITABLE);
    }

    // Opens the TimePickerDialog and updates the input once the user picks the time
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        EditText timeEdit = (EditText) view.findViewById(R.id.create_time);
        timeEdit.setText(String.format("%d:%d", hourOfDay, minute), TextView.BufferType.EDITABLE);
    }

    // If any of the fields are empty, then the user is prompted to complete the fields
    public boolean fieldCheck() {
        boolean isFilled = true;
        if(create_title.getText().toString().trim().equalsIgnoreCase("")){
            create_title.setError("Please enter a title for your listing");
            isFilled = false;
        }

        if(timeView.getText().toString().trim().equalsIgnoreCase("")){
            timeView.setError("Please enter a time for your listing");
            isFilled = false;
        }

        if(create_date.getText().toString().trim().equalsIgnoreCase("")){
            create_date.setError("Please enter a date for your listing");
            isFilled = false;
        }

        return isFilled;
    }

    private void createListing() {
        String input_title = create_title.getText().toString();
        String input_desc = create_desc.getText().toString();
        String dateString = create_date.getText().toString();
        String timeString = timeView.getText().toString();

        inputListing.setTitle(input_title);
        inputListing.setDescription(input_desc);
        inputListing.setListing_date(dateString);
        inputListing.setListing_time(timeString);
        inputListing.setListing_user(mAuth.getCurrentUser().toString());
        if(inputListing.getListing_image_path() == null) {inputListing.setDefaultImage();}

    }

    // Prompts the user for an image when the upload image button is clicked
    private void selectImage() {
        // Asks for permission to access storage before user uploads image
        // This prevents a bug where the image would not upload due to the
        // verifyStoragePermissions(CreateListing.this);

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select Image"), 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If the image is uploaded properly and the resultcode is OK
        // The imagePreview is updated and the image is uploaded
        if(requestCode == 1 || requestCode == 2) {
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                filePath = data.getData();
                try {
                    File f = new File(data.getDataString());
                    imageName = f.getName();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
                    imagePreview.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 282, 159, false));
                    uploadImage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // UploadImage method
// UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference.child(mAuth.getCurrentUser().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            ref.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                @Override
                                public void onSuccess(StorageMetadata storageMetadata) {
                                    inputListing.setListing_image_path("images/" + storageMetadata.getName());
                                }
                            });
                            Toast.makeText(getActivity(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                            }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Failed " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
                                }
                    });
        }
    }
}