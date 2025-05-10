package com.example.cameraintentsample;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //保存された画像のURI
    private Uri _imageUri;
    //Cameraアクティビティを起動するためのランチャーオブジェクト。
    ActivityResultLauncher<Intent> _cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallbackFromCamera());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //画像部分がタップされたときの処理メソッド。
    public void onCameraImageClick(View view){
        //日時データを「yyyyMMddHHmmss」の形式に整形するフォーマッタの生成。
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        //現在の日時データを取得。
        Date now = new Date(System.currentTimeMillis());
        //取得した日時データを「yyyyMMddHHmmss」形式に整形した文字列を生成。
        String nowStr = dataFormat.format(now);
        //ストレージに格納する画像のファイル名を生成。ファイル名の一意を確保するためにタイムスタンプの値を利用。
        String fileName = "CameraIntentSamplePhoto_" + nowStr + ".jpg";


        //ContentValueオブジェクトを生成。
        ContentValues values = new ContentValues();
        //画像ファイル名を設定。
        values.put(MediaStore.Images.Media.TITLE,fileName);
        //画像ファイルの種類を設定。
        values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
        //ContentResolverオブジェクトを生成。
        ContentResolver resolver = getContentResolver();
        //ContentResolverを使ってURIオブジェクトを生成。
        _imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //Intentオブジェクトを生成。
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Extra情報として_imageUriを設定。
        intent.putExtra(MediaStore.EXTRA_OUTPUT,_imageUri);
        //アクティビティを起動。
        _cameraLauncher.launch(intent);
    }

    //Cameraアクティビティから戻ってきたときの処理が記述されたコールバッククラス。
    private class ActivityResultCallbackFromCamera implements ActivityResultCallback<ActivityResult>{
        @Override
        public void onActivityResult(ActivityResult result){
            //カメラアプリで撮影成功の場合
            if(result.getResultCode() == RESULT_OK){
                //画像を表示するImageViewを取得。
                ImageView ivCamera = findViewById(R.id.ivCamera);
                //フィールドの画像URIをImageViewに設定。
                ivCamera.setImageURI(_imageUri);
            }
        }
    }
}