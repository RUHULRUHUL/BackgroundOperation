
package com.example.backgroundoperation.model.imageSave;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SaveImageResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<SaveImageData> data = null;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SaveImageData> getData() {
        return data;
    }

    public void setData(List<SaveImageData> data) {
        this.data = data;
    }

//    public class Data implements Serializable
//    {
//
//        @SerializedName("id")
//        @Expose
//        private long id;
//        @SerializedName("file_name")
//        @Expose
//        private String fileName;
//        @SerializedName("original_url")
//        @Expose
//        private String originalUrl;
//        @SerializedName("small_url")
//        @Expose
//        private String smallUrl;
//        @SerializedName("medium_url")
//        @Expose
//        private String mediumUrl;
//        @SerializedName("large_url")
//        @Expose
//        private String largeUrl;
//        private final static long serialVersionUID = 3969576472930286611L;
//
//        public long getId() {
//            return id;
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//
//        public String getFileName() {
//            return fileName;
//        }
//
//        public void setFileName(String fileName) {
//            this.fileName = fileName;
//        }
//
//        public String getOriginalUrl() {
//            return originalUrl;
//        }
//
//        public void setOriginalUrl(String originalUrl) {
//            this.originalUrl = originalUrl;
//        }
//
//        public String getSmallUrl() {
//            return smallUrl;
//        }
//
//        public void setSmallUrl(String smallUrl) {
//            this.smallUrl = smallUrl;
//        }
//
//        public String getMediumUrl() {
//            return mediumUrl;
//        }
//
//        public void setMediumUrl(String mediumUrl) {
//            this.mediumUrl = mediumUrl;
//        }
//
//        public String getLargeUrl() {
//            return largeUrl;
//        }
//
//        public void setLargeUrl(String largeUrl) {
//            this.largeUrl = largeUrl;
//        }
//
//    }
//
//    public class UploadImgResponse implements Serializable
//    {
//
//        @SerializedName("success")
//        @Expose
//        private boolean success;
//        @SerializedName("code")
//        @Expose
//        private long code;
//        @SerializedName("data")
//        @Expose
//        private Data data;
//        private final static long serialVersionUID = -9137231224110904075L;
//
//        public boolean isSuccess() {
//            return success;
//        }
//
//        public void setSuccess(boolean success) {
//            this.success = success;
//        }
//
//        public long getCode() {
//            return code;
//        }
//
//        public void setCode(long code) {
//            this.code = code;
//        }
//
//        public Data getData() {
//            return data;
//        }
//
//        public void setData(Data data) {
//            this.data = data;
//        }
//
//    }


}
