
package com.tariqaliiman.tariqaliiman.Models.FirstLevel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FirstLevel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("book_id")
    @Expose
    private Integer bookId;
    @SerializedName("parent_level_id")
    @Expose
    private Integer parentLevelId;
    @SerializedName("title_ar")
    @Expose
    private String titleAr;
    @SerializedName("title_en")
    @Expose
    private Object titleEn;
    @SerializedName("description_ar")
    @Expose
    private Object descriptionAr;
    @SerializedName("description_en")
    @Expose
    private Object descriptionEn;
    @SerializedName("is_end_level")
    @Expose
    private String isEndLevel;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getParentLevelId() {
        return parentLevelId;
    }

    public void setParentLevelId(Integer parentLevelId) {
        this.parentLevelId = parentLevelId;
    }

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public Object getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(Object titleEn) {
        this.titleEn = titleEn;
    }

    public Object getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(Object descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public Object getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(Object descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getIsEndLevel() {
        return isEndLevel;
    }

    public void setIsEndLevel(String isEndLevel) {
        this.isEndLevel = isEndLevel;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

}
