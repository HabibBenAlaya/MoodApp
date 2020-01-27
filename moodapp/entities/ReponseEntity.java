package com.soprahr.moodapp.entities;

public class ReponseEntity {
    private String id_sondage,id_question,id_collab,reponse,date,periodicite;

    public ReponseEntity() {
    }

    public ReponseEntity(String id_sondage, String id_question, String id_collab, String reponse, String date, String periodicite) {
        this.id_sondage = id_sondage;
        this.id_question = id_question;
        this.id_collab = id_collab;
        this.reponse = reponse;
        this.date = date;
        this.periodicite = periodicite;
    }

    public String getId_sondage() {
        return id_sondage;
    }

    public void setId_sondage(String id_sondage) {
        this.id_sondage = id_sondage;
    }

    public String getId_question() {
        return id_question;
    }

    public void setId_question(String id_question) {
        this.id_question = id_question;
    }

    public String getId_collab() {
        return id_collab;
    }

    public void setId_collab(String id_collab) {
        this.id_collab = id_collab;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    @Override
    public String toString() {
        return "ReponseEntity{" +
                "id_sondage='" + id_sondage + '\'' +
                ", id_question='" + id_question + '\'' +
                ", id_collab='" + id_collab + '\'' +
                ", reponse='" + reponse + '\'' +
                ", date='" + date + '\'' +
                ", periodicite='" + periodicite + '\'' +
                '}';
    }
}
