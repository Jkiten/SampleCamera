package com.example.samplecamera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {
    ImageView imageView;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageview);

        Button button =findViewById(R.id.button);
        button.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        takePickture();
                    }
                }
        );
        AutoPermissions.Companion.loadAllPermissions(this,101);
    }
    public void takePickture(){
        if(file ==null){
            file = createFile();
        }
        Log.d("getAbsolutePath",file.getAbsolutePath());
        Uri fileUri = FileProvider.getUriForFile(this,"com.example.samplecamera.provider",file);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        if(intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent,101);
        }
    }

    private File createFile() {
        Date date= new Date();
        String fileName = "capture"+date.getTime()+".jpg";
        //File storageDir = Environment.getExternalStorageDirectory();
        File storageDir = getFilesDir();
        Log.d("storageDir", storageDir.toString());
        File outFile = new File(storageDir,fileName);
        return outFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult",requestCode+"/"+resultCode);
         if(requestCode == 101 && resultCode==RESULT_OK){

             BitmapFactory.Options options = new BitmapFactory.Options();
             options.inSampleSize = 8;
             Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
             Log.d("bitmap!!!",file.getAbsolutePath());
             imageView.setImageBitmap(bitmap);
         }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
        Toast.makeText(this,"permissions denied : "+strings.length,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this,"permissions Granted : "+strings.length,Toast.LENGTH_LONG).show();
    }
}
