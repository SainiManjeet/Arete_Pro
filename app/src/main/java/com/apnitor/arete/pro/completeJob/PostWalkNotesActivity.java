package com.apnitor.arete.pro.completeJob;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;
import com.apnitor.arete.pro.R;
import com.apnitor.arete.pro.api.request.CompleteJReq;
import com.apnitor.arete.pro.api.request.ImageUrlReq;
import com.apnitor.arete.pro.api.response.ExtraCleaningRes;
import com.apnitor.arete.pro.api.response.GetJobRes;
import com.apnitor.arete.pro.createjob.ImageAdapterPost;
import com.apnitor.arete.pro.databinding.ActivityPostwalkNotesBinding;
import com.apnitor.arete.pro.interfaces.ListItemCancelCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.service.UploadImagesToAwsService;
import com.apnitor.arete.pro.util.PermissionUtitlity;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.util.UploadImage;
import com.apnitor.arete.pro.util.Validation;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PostWalkNotesActivity extends BaseActivity implements ListItemCancelCallback {
    public static final String LOG_TAG = "PostWalkNotesActivity";
    String mPreNotes;
    private ActivityPostwalkNotesBinding binding;
    private int GALLERY = 1, REQUEST_TAKE_PHOTO = 2;
    private UploadImage mUploadImage;
    private int addImage = 0;
    private GetJobRes mJobReq;
    private JobSpecViewModel mJobSpecViewModel;
    private ArrayList<ImageUrlReq> mImageListPost;
    private ImageAdapterPost mImageAdapterPost;
    int position =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = bindContentView(R.layout.activity_postwalk_notes);
        //
        setToolBar(getResources().getString(R.string.complete_job));
        //
        mJobSpecViewModel = getViewModel(JobSpecViewModel.class);
        //
        mJobReq = getIntent().getParcelableExtra("completeJob");
        mPreNotes = getIntent().getStringExtra("preNotes");
        position=getIntent().getIntExtra("position",-1);
        mUploadImage = new UploadImage(this);
        //
        Log.d(LOG_TAG, " PreNotes are: " + mPreNotes);
        Log.d(LOG_TAG, " CleaningType=" + mJobReq.cleaningType + mJobReq.jobType);

        binding.imgAddPost.setOnClickListener(v -> {
            addImage = 1;
            showPictureDialog();
        });

        // Set post image adapter
        LinearLayoutManager manager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.imgRecyclerPost.setLayoutManager(manager2);
        binding.imgRecyclerPost.setNestedScrollingEnabled(false);
        mImageListPost = new ArrayList<>();
        mImageAdapterPost = new ImageAdapterPost(PostWalkNotesActivity.this, mImageListPost, this);
        binding.imgRecyclerPost.setAdapter(mImageAdapterPost);

        binding.btnCompleteJob.setOnClickListener(v -> {
            if (Validation.isValidatedPostWalkJob(PostWalkNotesActivity.this, Objects.requireNonNull(binding.etPostNotes.getText()).toString().trim())) {
                CompleteJReq completeJReq = new CompleteJReq(getPrefHelper().getAuthToken(), mJobReq.jobId, mJobReq.cleaningType, mJobReq.bedrooms, mJobReq.bathrooms, mJobReq.kitchen, mJobReq.others, mJobReq.sqft,
                        mJobReq.date, mJobReq.time, mJobReq.howOften, mJobReq.when, mJobReq.priceType, mJobReq.specialNotesForProvider, mJobReq.images, (ArrayList<ExtraCleaningRes>) mJobReq.extraCleaning, mJobReq.estTime,
                        mJobReq.estPrice, mJobReq.price, mJobReq.providerId, mJobReq.description, mJobReq.jobType, mImageListPost, binding.etPostNotes.getText().toString(), mImageListPost, mPreNotes);
                // 1. static method
                if (Double.isNaN(completeJReq.sqft)) {
                    completeJReq.sqft = 0.0d;
                }
                mJobSpecViewModel.completeJob(completeJReq);
            }
        });

        mJobSpecViewModel.completeJobLiveData().observe(this, createJobRes -> {
            Toast.makeText(this, "Your job completed successfully.", Toast.LENGTH_SHORT).show();
            //
            Intent intent = new Intent();
            intent.putExtra("post_walk",binding.etPostNotes.getText().toString());
            intent.putExtra("position",position);
            setResult(RequestCodes.REQ_CODE_ADDED_POST_WALK, intent);
            //
            Intent in = new Intent(PostWalkNotesActivity.this, ReviewActivity.class);
            in.putExtra("type", "provider");
            in.putExtra("to", mJobReq.clientId);
            in.putExtra("post_walk",binding.etPostNotes.getText().toString());
            in.putExtra("jobId", mJobReq.jobId);
            in.putExtra("position",position);
            startActivityForResult(in,RequestCodes.REQ_CODE_ADDED_POST_WALK);
            //
            if (mImageListPost!=null &&mImageListPost .size() > 0) {
                Intent mServiceIntent = new Intent(this, UploadImagesToAwsService.class);
                mServiceIntent.putExtra("type", "POST_WALK");
                mServiceIntent.putExtra("jobId", mJobReq.jobId);
                mServiceIntent.putExtra("authToken", getPrefHelper().getAuthToken());
                mServiceIntent.putParcelableArrayListExtra("mImageList", mImageListPost);
                if (Build.VERSION.SDK_INT > 25) {
                    startForegroundService(mServiceIntent);
                } else {
                    startService(mServiceIntent);
                }
            }
            //
            finish();
        });
    }


    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(PostWalkNotesActivity.this);
        pictureDialog.setTitle("Select Action");
       /* String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};*/
        String[] pictureDialogItems = {
                "Select photo from gallery",
        };
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (PermissionUtitlity.checkReadStoragePermission(PostWalkNotesActivity.this)) {
                                choosePhotoFromGallary();
                            }
                            break;
                        case 1:
                            if (PermissionUtitlity.checkPermissionCamera(PostWalkNotesActivity.this)) {
                                dispatchTakePictureIntent();
                            }
                            break;
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = mUploadImage.createImageFile();
                if (!photoFile.exists()) {
                    photoFile.createNewFile();
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Log.e("filepath", photoFile.toString());
                Uri photoURI = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtitlity.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    choosePhotoFromGallary();
                }
                break;
            case PermissionUtitlity.CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        Uri mUri;
        if (requestCode == GALLERY) {
            if (data != null) {

                // openCropActivity(mUri);
                try {

                    /*TODO set image in list to show on recycler view Item*/

                    if (data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for (int i = 0; i < count; i++) {
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            ImageUrlReq mUrlReq = new ImageUrlReq();
                            mUrlReq.imageUrl = mUploadImage.saveImage(imageUri);
                            mImageListPost.add(mUrlReq);
                            mImageAdapterPost.notifyDataSetChanged();

                        }

                    } else if (data.getData() != null) {
                        mUri = data.getData();
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        ImageUrlReq mUrlReq = new ImageUrlReq();
                        mUrlReq.imageUrl =  mUploadImage.saveImage(mUri);
                        mImageListPost.add(mUrlReq);
                        mImageAdapterPost.notifyDataSetChanged();
                    }

                    // mImageAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PostWalkNotesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            try {
                mUri = Uri.fromFile(new File(mUploadImage.mCurrentPhotoPath));
                /*TODO set cropped image in list to show on recycler view Item*/
                ImageUrlReq mUrlReq = new ImageUrlReq();

                mUrlReq.imageUrl = mUri.toString();
                mImageListPost.add(mUrlReq);
                mImageAdapterPost.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(PostWalkNotesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onListCancelWithPositionClick(Object object, int position) {
        mImageListPost.remove(position);
        mImageAdapterPost.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("pre_walk",mPreNotes);
        intent.putExtra("position", position);
        setResult(RequestCodes.REQ_CODE_ADDED_PRE_WALK, intent);
    }
}
