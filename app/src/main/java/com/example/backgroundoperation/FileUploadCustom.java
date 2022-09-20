package com.example.backgroundoperation;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class FileUploadCustom extends RequestBody {
    private File file;
    private ProgressListener progressListener;
    private static int DefaultBufferSize = 4096;
    private Context context;

    public FileUploadCustom(File file, Context context, ProgressListener progressListener) {
        this.file = file;
        this.context = context;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(getMimiType(Uri.fromFile(file)));
    }

    private String getMimiType(Uri fromFile) {

        String mimeType = null;
        if (fromFile.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(fromFile);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fromFile
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;

    }


    @Override
    public long contentLength() throws IOException {
        return super.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileSize = file.length();
        byte[] buffer = new byte[DefaultBufferSize];
        FileInputStream in = new FileInputStream(file);
        long uploaded = 0;

        try {
            int read;
            Handler handler = new Handler(Looper.getMainLooper());

            while ((read = in.read(buffer)) != -1) {
                handler.post(new ProgressUpdater(uploaded, fileSize));
                uploaded = uploaded + read;
                sink.write(buffer, 0, read);

            }

        } finally {
            in.close();
        }

    }

    public interface ProgressListener {
        void fileTransfer(long transfer);
    }

    private class ProgressUpdater implements Runnable {

        private long uploaded;
        private long fileLength;

        public ProgressUpdater(long uploaded, long fileLength) {
            this.uploaded = uploaded;
            this.fileLength = fileLength;

        }

        @Override
        public void run() {
            progressListener.fileTransfer((int) 100 * uploaded / fileLength);

        }
    }
}
