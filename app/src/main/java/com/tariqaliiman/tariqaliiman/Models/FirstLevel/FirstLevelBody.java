
package com.tariqaliiman.tariqaliiman.Models.FirstLevel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirstLevelBody {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private FirstLevelData data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public FirstLevelData getData() {
        return data;
    }

    public void setData(FirstLevelData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
