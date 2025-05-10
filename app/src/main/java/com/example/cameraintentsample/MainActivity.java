package com.example.cameraintentsample;

import android.content.Intent;
import android.graphics.Bitmap;
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

public class MainActivity extends AppCompatActivity {
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
        //Intentオブジェクトを生成。
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //アクティビティを起動。
        _cameraLauncher.launch(intent);
    }

    //Cameraアクティビティから戻ってきたときの処理が記述されたコールバッククラス。
    private class ActivityResultCallbackFromCamera implements ActivityResultCallback<ActivityResult>{
        @Override
        public void onActivityResult(ActivityResult result){
            //カメラアプリで撮影成功の場合
            if(result.getResultCode() == RESULT_OK){
                //撮影された画像のビットマップデータを取得。
                Intent data = result.getData();
                Bitmap bitmap;
                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
                    bitmap = data.getParcelableExtra("data",Bitmap.class);
                }
                else{
                    bitmap = data.getParcelableExtra("data");
                }
                //画像を表示するImageViewを取得。
                ImageView ivCamera = findViewById(R.id.ivCamera);
                //撮影された画像をImageViewに設定。
                ivCamera.setImageBitmap(bitmap);
            }
        }
    }
}