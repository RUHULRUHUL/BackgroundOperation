package com.example.backgroundoperation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.backgroundoperation.model.Slider;
import com.example.backgroundoperation.model.SliderResponse;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ConetivityInternetInterface, FileUploadCustom.ProgressListener {

    private static final String CHANNEL_ID = "Notification_Channel_1";
    private String LogTag = "OnCreate";
    private BroadCastTest broadCastTest;
    private NetworkConnectivityCheckReciver networkConnectivityCheckReciver;
    private AppCompatButton choseButton;
    private File file;
    ApiServices apiServices;

    private ProgressDialog progressDialog;
    private NotificationManagerCompat notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        choseButton = findViewById(R.id.choseBtn);


        if (broadCastTest == null) {
            broadCastTest = new BroadCastTest();
        }
        if (networkConnectivityCheckReciver == null) {
            networkConnectivityCheckReciver = new NetworkConnectivityCheckReciver(this::isConnected);
        }


        apiServices = RetrofitInstance.getRetrofit().create(ApiServices.class);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {

                Log.d(LogTag, "Thread Name " + Thread.currentThread().getName());
                System.out.println("" + Thread.currentThread().getName());

                apiServices.getSliders().enqueue(new Callback<SliderResponse>() {
                    @Override
                    public void onResponse(Call<SliderResponse> call, Response<SliderResponse> response) {
                        if (response.isSuccessful()) {

                            assert response.body() != null;
                            if (response.body().getData() != null) {
                                for (Slider s : response.body().getData()) {
                                    Log.d(LogTag, "Slider -service " + s.getImage());

                                }

                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<SliderResponse> call, Throwable t) {

                    }
                });
            }
        });

        choseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // openChose();
                //showNotification();
                ForegroundSupportLongTask();

            }
        });


    }

    private void openChose() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkConnectivityCheckReciver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkConnectivityCheckReciver);
    }

    @Override
    public void isConnected(boolean isConnected) {
        if (isConnected) {
            Toast.makeText(this, "Network Connected true", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ClipData clipData = data.getClipData();
        List<Uri> imageList = new ArrayList<>();
        for (int i = 0; i < clipData.getItemCount(); i++) {
            imageList.add(clipData.getItemAt(i).getUri());

        }


        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String path = data.getData().getPath();
            file = new File(path);

            PrepareFileUpload(file);
        }
    }

    private void fileUpload(File file) {
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setMessage("file Uploading..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FileUploadCustom fileUploadCustom = new FileUploadCustom(file, MainActivity.this, this::fileTransfer);
        MultipartBody.Part body = MultipartBody.Part.createFormData("Media", file.getName(), fileUploadCustom);

    }

    @Override
    public void fileTransfer(long transfer) {
        progressDialog.setProgress((int) transfer);

    }

    private void PrepareFileUpload(File file) {
        MultipartBody.Part filePart = filePart("Image", file);
        apiServices.uploadImage(
                "",
                "",
                filePart
        );

    }

    private MultipartBody.Part filePart(String image, File file) {

        RequestBody requestBody = RequestBody.create(MediaType.parse(getMimiType(Uri.fromFile(file))), file);

        return MultipartBody.Part.createFormData(image, file.getName(), requestBody);


    }

    private String getMimiType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = MainActivity.this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;

    }

    private void ForegroundSupportLongTask() {
        WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(FileUploadFourground.class).build();
        WorkManager.getInstance(this).enqueue(uploadWorkRequest);

    }

    private void showNotification() {

        notificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_google)
                .setContentTitle("My notification")
                .setContentText("File upload ..")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                .setAllowSystemGeneratedContextualActions(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        // Do the "lengthy" operation 20 times
                        for (int i = 0; i <= 100; i++) {
                            // Sets the progress indicator to a max value, the current completion percentage and "determinate" state
                            builder.setProgress(100, i, false);
                            // Displays the progress bar for the first time.
                            notificationManager.notify(1, builder.build());
                            // Sleeps the thread, simulating an operation
                            try {
                                // Sleep for 1 second
                                Thread.sleep(1 * 1000);
                            } catch (InterruptedException e) {
                                Log.d("TAG", "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        builder.setContentText("Download completed")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        notificationManager.notify(1, builder.build());
                    }
                }
                // Starts the thread by calling the run() method in its Runnable
        ).start();

       // notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "this is for Test Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}