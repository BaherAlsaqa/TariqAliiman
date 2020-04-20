
package com.tariqaliiman.tariqaliiman.Models.books;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name_ar")
    @Expose
    private String nameAr;
    @SerializedName("name_en")
    @Expose
    private Object nameEn;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("author_ar")
    @Expose
    private String authorAr;
    @SerializedName("author_en")
    @Expose
    private Object authorEn;
    @SerializedName("description_ar")
    @Expose
    private String descriptionAr;
    @SerializedName("description_en")
    @Expose
    private Object descriptionEn;
    @SerializedName("pdf_ar")
    @Expose
    private String pdfAr;
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

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public Object getNameEn() {
        return nameEn;
    }

    public void setNameEn(Object nameEn) {
        this.nameEn = nameEn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAuthorAr() {
        return authorAr;
    }

    public void setAuthorAr(String authorAr) {
        this.authorAr = authorAr;
    }

    public Object getAuthorEn() {
        return authorEn;
    }

    public void setAuthorEn(Object authorEn) {
        this.authorEn = authorEn;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public Object getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(Object descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getPdfAr() {
        return pdfAr;
    }

    public void setPdfAr(String pdfAr) {
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
