
package com.tariqaliiman.tariqaliiman.Models.LevelHadeth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LevelHadeth {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("book_id")
    @Expose
    private Integer bookId;
    @SerializedName("level_id")
    @Expose
    private Integer levelId;
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
    @SerializedName("page_no_ar")
    @Expose
    private Integer pageNoAr;
    @SerializedName("page_no_en")
    @Expose
    private Object pageNoEn;
    @SerializedName("pdf_ar")
    @Expose
    private Object pdfAr;
    @SerializedName("pdf_en")
    @Expose
    private Object pdfEn;
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

    public Integer getLevelId() {
        return levelId;
    }

    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
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

    public Integer getPageNoAr() {
        return pageNoAr;
    }

    public void setPageNoAr(Integer pageNoAr) {
        this.pageNoAr = pageNoAr;
    }

    public Object getPageNoEn() {
        return pageNoEn;
    }

    public void setPageNoEn(Object pageNoEn) {
        this.pageNoEn = pageNoEn;
    }

    public Object getPdfAr() {
        return pdfAr;
    }

    public void setPdfAr(Object pdfAr) {
        this.pdfAr = pdfAr;
    }

    public Object getPdfEn() {
        return pdfEn;
    }

    public void setPdfEn(Object pdfEn) {
        this.pdfEn = pdfEn;
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
