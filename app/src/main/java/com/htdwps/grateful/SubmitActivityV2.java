package com.htdwps.grateful;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.htdwps.grateful.Model.GratefulPost;
import com.htdwps.grateful.Model.User;
import com.htdwps.grateful.Util.FirebaseUtil;
import com.htdwps.grateful.Util.GlideUtil;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by HTDWPS on 4/5/18.
 */
public class SubmitActivityV2 extends AppCompatActivity implements View.OnClickListener {

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    Bitmap bitmap;
    ByteArrayOutputStream byteArrayOutputStream;
    String imageUrlString;

    private DatabaseReference publicReference;
    private DatabaseReference privateReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    TextView tvTagLineHeaderText;
    TextView tvPreviewLabelText;
    EditText etGratefulPostEntryField;
    TextView tvUploadImageButtonBtn;
    TextView tvSubmitGratefulEntryBtn;
    ImageView ivPreviewImage;
    TextView tvPreviewText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_v2);

        Typeface headerTypeface = Typeface.createFromAsset(getAssets(), "fonts/kaushan.ttf");
        Typeface buttonTypeface = Typeface.createFromAsset(getAssets(), "fonts/passion.ttf");

        publicReference = FirebaseUtil.getGratefulPostsRef();
        privateReference = FirebaseUtil.getGratefulPersonalRef();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        tvTagLineHeaderText = findViewById(R.id.tv_submit_message_tagline);
        tvPreviewLabelText = findViewById(R.id.tv_preview_post_text);
        etGratefulPostEntryField = findViewById(R.id.et_grateful_post_entry);
        tvUploadImageButtonBtn = findViewById(R.id.tv_upload_image_button);
        tvSubmitGratefulEntryBtn = findViewById(R.id.tv_submit_button);
        ivPreviewImage = findViewById(R.id.iv_image_preview);
        tvPreviewText = findViewById(R.id.tv_text_preview);

        // Add Typeface to buttons
        tvTagLineHeaderText.setTypeface(headerTypeface);
        tvPreviewLabelText.setTypeface(buttonTypeface);
        tvUploadImageButtonBtn.setTypeface(buttonTypeface);
        tvSubmitGratefulEntryBtn.setTypeface(buttonTypeface);

        tvUploadImageButtonBtn.setOnClickListener(this);
        tvSubmitGratefulEntryBtn.setOnClickListener(this);
        tvSubmitGratefulEntryBtn.setEnabled(false);
        tvSubmitGratefulEntryBtn.setBackgroundColor(getResources().getColor(R.color.color_divider));

        etGratefulPostEntryField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    tvPreviewText.setVisibility(View.VISIBLE);
                    tvPreviewText.setText(charSequence);
                } else {
                    tvPreviewText.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

//                tvPreviewText.setText(editable.toString());

            }
        });

    }

    public void uploadPostToDatabase() {

        String randomPostKey = publicReference.push().getKey();

        User user = FirebaseUtil.getCurrentUser();

        String postString = etGratefulPostEntryField.getText().toString();

        if (postString.equals("") || postString.length() < 1) {

            etGratefulPostEntryField.setError(getApplication().getResources().getString(R.string.submit_error_post_title_text));

        } else {

            GratefulPost gratefulPost = new GratefulPost(user, user.getUserDisplayName(), postString, ServerValue.TIMESTAMP, imageUrlString);

            Map<String, Object> newPost = new HashMap<>();

            newPost.put("grateful_posts_public/" + randomPostKey, gratefulPost);

            newPost.put("grateful_personal_posts/" + user.getUserid() + "/" + randomPostKey, gratefulPost);

            publishToDatabase(newPost);

        }


    }

    private void publishToDatabase(Map<String, Object> newPost) {
        FirebaseUtil.getBaseRef().updateChildren(newPost, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(SubmitActivityV2.this, "Stay Grateful", Toast.LENGTH_SHORT).show();

                    Intent completeIntent = new Intent(SubmitActivityV2.this, ListActivity.class);
                    completeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(completeIntent);
                } else {
                    Toast.makeText(SubmitActivityV2.this, "Sorry try again in a little", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void chooseImageToUpload() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public void submitImageToFirebaseStorage(final byte[] bytes, final Bitmap bitmap) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Timber.i("Submitting to firebase already.");
        String randomPostKey = publicReference.push().getKey();

        StorageReference reference = storageReference.child(firebaseUser.getUid()).child("image" + randomPostKey);
        reference.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageUrlString = taskSnapshot.getDownloadUrl().toString();
//                GlideUtil.loadBitmapImage(bitmap, bytes, mPropertyImageView);

                GlideUtil.loadImage(imageUrlString, ivPreviewImage);
                progressDialog.dismiss();
                Toast.makeText(SubmitActivityV2.this, "Uploaded", Toast.LENGTH_SHORT).show();
                tvSubmitGratefulEntryBtn.setEnabled(true);
                tvSubmitGratefulEntryBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(SubmitActivityV2.this, "Your upload size exceeds the 5MB limit.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage((int) progress + "% Uploaded");
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {

            filePath = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);

                submitImageToFirebaseStorage(byteArrayOutputStream.toByteArray(), bitmap);

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.tv_upload_image_button:

                chooseImageToUpload();

                break;

            case R.id.tv_submit_button:

                uploadPostToDatabase();

                break;

            default:

                break;

        }

    }
}
