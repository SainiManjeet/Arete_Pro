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
import com.apnitor.arete.pro.createjob.ImageAdapter;
import com.apnitor.arete.pro.createjob.ImageAdapterPost;
import com.apnitor.arete.pro.databinding.ActivityPrewalkNotesBinding;
import com.apnitor.arete.pro.interfaces.ListItemCancelCallback;
import com.apnitor.arete.pro.profile.BaseActivity;
import com.apnitor.arete.pro.service.UploadImagesToAwsService;
import com.apnitor.arete.pro.util.PermissionUtitlity;
import com.apnitor.arete.pro.util.RequestCodes;
import com.apnitor.arete.pro.util.UploadImage;
import com.apnitor.arete.pro.util.Validation;
import com.apnitor.arete.pro.viewmodel.JobSpecViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;


public class PreWalkNotesActivity extends BaseActivity implements ListItemCancelCallback {
    private ActivityPrewalkNotesBinding binding;
    private int GALLERY = 1, REQUEST_TAKE_PHOTO = 2;
    private UploadImage mUploadImage;
    private ArrayList<ImageUrlReq> mImageList = new ArrayList<>();
    private ArrayList<ImageUrlReq> mImageListPost;
    private ImageAdapter mImageAdapter;
    private ImageAdapterPost mImageAdapterPost;
    private int addImage = 0;
    private GetJobRes mJobReq;
    private JobSpecViewModel mJobSpecViewModel;
    String mPreWalkNotes = "";
    int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = bindContentView(R.layout.activity_prewalk_notes);
        mUploadImage = new UploadImage(this);
        //
        setToolBar(getResources().getString(R.string.Prewalk_notes));
        mJobSpecViewModel = getViewModel(JobSpecViewModel.class);
        mJobReq = getIntent().getParcelableExtra("completeJob");
        position = getIntent().getIntExtra("position", -1);

        binding.imgAddPre.setOnClickListener(v -> {
            addImage = 0;
            showPictureDialog();
        });


        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.imageRecycler.setLayoutManager(manager);
        binding.imageRecycler.setNestedScrollingEnabled(false);
        mImageList = new ArrayList<>();
        mImageAdapter = new ImageAdapter(PreWalkNotesActivity.this, mImageList, null, this);
        binding.imageRecycler.setAdapter(mImageAdapter);

        // Set post image adapter
        LinearLayoutManager manager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.imgRecyclerPost.setLayoutManager(manager2);
        binding.imgRecyclerPost.setNestedScrollingEnabled(false);
        mImageListPost = new ArrayList<>();
        mImageAdapterPost = new ImageAdapterPost(PreWalkNotesActivity.this, mImageListPost, null);
        binding.imgRecyclerPost.setAdapter(mImageAdapterPost);

        binding.btnCompleteJob.setOnClickListener(v -> {
            if (Validation.isValidatedCompleteJob(PreWalkNotesActivity.this, Objects.requireNonNull(binding.etPreNotes.getText()).toString().trim())) {
                CompleteJReq completeJReq = new CompleteJReq(getPrefHelper().getAuthToken(), mJobReq.jobId, mJobReq.cleaningType, mJobReq.bedrooms, mJobReq.bathrooms, mJobReq.kitchen, mJobReq.others, mJobReq.sqft,
                        mJobReq.date, mJobReq.time, mJobReq.howOften, mJobReq.when, mJobReq.priceType, mJobReq.specialNotesForProvider, mJobReq.images, (ArrayList<ExtraCleaningRes>) mJobReq.extraCleaning, mJobReq.estTime,
                        mJobReq.estPrice, mJobReq.price, mJobReq.providerId, mJobReq.description, mJobReq.jobType, mImageList, binding.etPreNotes.getText().toString());
                mJobSpecViewModel.completeJob(completeJReq);
            }
        });

        mJobSpecViewModel.completeJobLiveData().observe(this, createJobRes -> {
            //
            Toast.makeText(this, "Pre Walk notes added successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("pre_walk", binding.etPreNotes.getText().toString());
            intent.putExtra("position", position);
            setResult(RequestCodes.REQ_CODE_ADDED_PRE_WALK, intent);
            //
            if (mImageList.size() > 0) {
                Intent mServiceIntent = new Intent(this, UploadImagesToAwsService.class);
                mServiceIntent.putExtra("type", "PRE_WALK");
                mServiceIntent.putExtra("jobId", mJobReq.jobId);
                mServiceIntent.putExtra("authToken", getPrefHelper().getAuthToken());
                mServiceIntent.putParcelableArrayListExtra("mImageList", mImageList);
                if (Build.VERSION.SDK_INT > 25) {
                    startForegroundService(mServiceIntent);
                } else {
                    startService(mServiceIntent);
                }
            }
            //
            Intent in = new Intent(PreWalkNotesActivity.this, PostWalkNotesActivity.class);
            in.putExtra("completeJob", mJobReq);
            in.putExtra("preNotes", binding.etPreNotes.getText().toString());
            in.putExtra("position", position);
//            in.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(in);
            //
            finish();
            //
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
        });
    }


    public void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(PreWalkNotesActivity.this);
        pictureDialog.setTitle("Select Picture");
       /* String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};*/
        String[] pictureDialogItems = {
                "From gallery",
        };
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            if (PermissionUtitlity.checkReadStoragePermission(PreWalkNotesActivity.this)) {
                                choosePhotoFromGallary();
                                dialog.dismiss();
                            }
                            break;
                        case 1:
                            if (PermissionUtitlity.checkPermissionCamera(PreWalkNotesActivity.this)) {
                                dispatchTakePictureIntent();
                                dialog.dismiss();
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
        addImage = 0;
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
            } catch (Exception ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
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
                            mImageList.add(mUrlReq);
                            mImageAdapter.notifyDataSetChanged();

//                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                            ImageUrlReq mUrlReq = new ImageUrlReq();
//                            mUrlReq.imageUrl = mUploadImage.saveImage(imageUri);
//                            mImageList.add(mUrlReq);


                        }

                    } else if (data.getData() != null) {

                        mUri = data.getData();
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                        ImageUrlReq mUrlReq = new ImageUrlReq();
                        mUrlReq.imageUrl =  mUploadImage.saveImage(mUri);
                        if (addImage == 0) {
                            mImageList.add(mUrlReq);
                            mImageAdapter.notifyDataSetChanged();
                        }



                    }

                    // mImageAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PreWalkNotesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO) {
            try {
                mUri = Uri.fromFile(new File(mUploadImage.mCurrentPhotoPath));
                /* TODO set cropped image in list to show on recycler view Item */
                ImageUrlReq mUrlReq = new ImageUrlReq();
                mUrlReq.imageUrl = mUri.toString();
                mImageList.add(mUrlReq);
                mImageAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(PreWalkNotesActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onListCancelWithPositionClick(Object object, int position) {
        mImageList.remove(position);
        mImageAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent();
//        intent.putExtra("pre_walk",binding.etPreNotes.getText().toString());
//        setResult(RequestCodes.REQ_CODE_ADDED_PRE_WALK, intent);
//        super.onBackPressed();
//    }
}
