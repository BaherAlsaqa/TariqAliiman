
package com.tariqaliiman.tariqaliiman.Models.books;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BooksBody {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private BooksData data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public BooksData getData() {
        return data;
    }

    public void setData(BooksData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
