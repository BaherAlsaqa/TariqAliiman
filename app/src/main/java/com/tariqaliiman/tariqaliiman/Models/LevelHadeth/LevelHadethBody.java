
package com.tariqaliiman.tariqaliiman.Models.LevelHadeth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LevelHadethBody {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private LevelHadethData data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public LevelHadethData getData() {
        return data;
    }

    public void setData(LevelHadethData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
