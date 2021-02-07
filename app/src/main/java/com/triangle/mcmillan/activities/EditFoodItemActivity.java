package com.triangle.mcmillan.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.triangle.mcmillan.R;
import com.triangle.mcmillan.models.FoodItem;
import com.triangle.mcmillan.utilities.UserHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class EditFoodItemActivity extends AppCompatActivity {
    private ImageView imageView;
    private EditText title;
    private EditText description;
    private EditText price;
    private Button addItem;
    private Uri filePath;
    private FoodItem foodItem = new FoodItem();
    private DatabaseReference newItemReference;
    private boolean isEditing = false;
    private Button deleteBtn;

    private final int PICK_IMAGE_REQUEST = 71;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_item);

        initViews();
        Intent intent = getIntent();
        foodItem = (FoodItem) intent.getSerializableExtra("item");
        if (foodItem != null){
            title.setText(foodItem.getName());
            description.setText(foodItem.getDescription());
            price.setText(foodItem.getPrice().toString());
            Glide
                    .with(this)
                    .load(foodItem.getImage())
                    .centerCrop()
                    .placeholder(R.drawable.gif_loading_image)
                    .into(imageView);
            isEditing = true;
            addItem.setText("Save Changes");
            deleteBtn.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        imageView = findViewById(R.id.imageFood);
        title = findViewById(R.id.editTextFoodName);
        description = findViewById(R.id.editTextFoodDescription);
        price = findViewById(R.id.editTextFoodPrice);
        addItem = findViewById(R.id.btnAddItem);
        deleteBtn = findViewById(R.id.btnDeleteItem);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance()
                        .getReference("items")
                        .child(foodItem.getId())
                        .removeValue();
                finish();
            }
        });
    }

    private void addItem() {
        if (title.getText().toString().length() == 0){
            title.setError("Title cannot be empty");
        }
        else if (description.getText().toString().length() == 0){
            description.setError("Description cannot be empty");
        }
        else if (price.getText().toString().length() == 0){
            price.setError("Price cannot be empty");
        }
        else if (filePath == null && !isEditing){
            Snackbar.make(title, "Image cannot be empty", Snackbar.LENGTH_LONG).show();
        }
        else{
            addItem.setVisibility(View.GONE);
            Snackbar.make(price, "Adding new item", Snackbar.LENGTH_LONG).show();
            // create food item
            // Create a new post reference with an auto-generated id
            if (isEditing){
                newItemReference = FirebaseDatabase.getInstance().getReference("items").child(foodItem.getId());
            }
            else{
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("items");
                newItemReference = ref.push();
                foodItem = new FoodItem();
            }
            foodItem.setId(newItemReference.getKey());
            foodItem.setName(title.getText().toString());
            foodItem.setDescription(description.getText().toString());
            foodItem.setPrice(Long.parseLong(price.getText().toString()));

            if (isEditing){
                newItemReference.setValue(foodItem);
                finish();
            }
            else{
                uploadImage(foodItem.getId());
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(String id){
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();

        // Create a reference to 'images/mountains.jpg'
        final StorageReference imageRef = FirebaseStorage.getInstance().getReference().child("images/" +id+ ".jpg");
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                else{
                    addItem.setVisibility(View.VISIBLE);
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    String downloadUrl = task.getResult().toString();
                    foodItem.setImage(downloadUrl);
                    newItemReference.setValue(foodItem);
                    finish();
                }
                else{
                    addItem.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}