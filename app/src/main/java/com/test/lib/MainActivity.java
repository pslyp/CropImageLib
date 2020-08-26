package com.test.lib;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView1, imageView2;
    private Button chooseImageButton, button;

    private static final int PICK_IMAGE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstance();
    }

    private void initInstance() {
        imageView1 = findViewById(R.id.image_view);
        imageView2 = findViewById(R.id.image_view_2);
        chooseImageButton = findViewById(R.id.choose_image_button);
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(chooseImageIntent, PICK_IMAGE);
            }
        });

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_test);
                Bitmap originalBitmap = ((BitmapDrawable) imageView1.getDrawable()).getBitmap();

                Bitmap cutBitmap = Bitmap.createBitmap(originalBitmap.getWidth()/2, originalBitmap.getHeight()/2, Bitmap.Config.RGB_565);

                Canvas canvas = new Canvas(cutBitmap);
                Rect desRect = new Rect(0, 0, originalBitmap.getWidth()/2, originalBitmap.getHeight()/2);
                Rect srcRect = new Rect(originalBitmap.getWidth()/2, 0, originalBitmap.getWidth(), originalBitmap.getHeight());
                canvas.drawBitmap(originalBitmap, srcRect, desRect, null);

                imageView2.setImageBitmap(cutBitmap);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView1.setImageURI(imageUri);
        }
    }
}