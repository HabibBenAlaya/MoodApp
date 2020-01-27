package com.soprahr.moodapp.entities;

public class SondageEntity {
    private String id,titre,categorie,date_d,date_f,imageSondage;

    public SondageEntity() {
    }

    public SondageEntity(String id, String titre, String categorie, String date_d, String date_f, String imageSondage) {
        this.id = id;
        this.titre = titre;
        this.categorie = categorie;
        this.date_d = date_d;
        this.date_f = date_f;
        this.imageSondage = imageSondage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getDate_d() {
        return date_d;
    }

    public void setDate_d(String date_d) {
        this.date_d = date_d;
    }

    public String getDate_f() {
        return date_f;
    }

    public void setDate_f(String date_f) {
        this.date_f = date_f;
    }

    public String getImageSondage() {
        return imageSondage;
    }

    public void setImageSondage(String imageSondage) {
        this.imageSondage = imageSondage;
    }

    @Override
    public String toString() {
        return "NotificationEntity{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", categorie='" + categorie + '\'' +
                ", date_d='" + date_d + '\'' +
                ", date_f='" + date_f + '\'' +
                ", imageSondage='" + imageSondage + '\'' +
                '}';
    }
}
