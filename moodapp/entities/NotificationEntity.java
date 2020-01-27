package com.soprahr.moodapp.entities;

/**
 * Created by Habib on 27/02/2018.
 */

public class NotificationEntity {
    private int id;
    private String titre,description,type,imageNotification;

    public NotificationEntity() {
    }

    public NotificationEntity(int id, String titre, String description, String type, String imageNotification) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.type = type;
        this.imageNotification = imageNotification;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageNotification() {
        return imageNotification;
    }

    public void setImageNotification(String imageNotification) {
        this.imageNotification = imageNotification;
    }

    @Override
    public String toString() {
        return "NotificationEntity{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", imageNotification='" + imageNotification + '\'' +
                '}';
    }
}
