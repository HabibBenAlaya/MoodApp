package com.soprahr.moodapp.entities;

public class QuestionEntity {
    private String id,titre,categorie,type,periodicite,activation,id_sondage,rep1,rep2,rep3,rep4,rep5,affichage;

    public QuestionEntity() {
    }

    public QuestionEntity(String id, String titre, String categorie, String type, String periodicite, String activation, String id_sondage, String rep1, String rep2, String rep3, String rep4, String rep5, String affichage) {
        this.id = id;
        this.titre = titre;
        this.categorie = categorie;
        this.type = type;
        this.periodicite = periodicite;
        this.activation = activation;
        this.id_sondage = id_sondage;
        this.rep1 = rep1;
        this.rep2 = rep2;
        this.rep3 = rep3;
        this.rep4 = rep4;
        this.rep5 = rep5;
        this.affichage = affichage;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public String getActivation() {
        return activation;
    }

    public void setActivation(String activation) {
        this.activation = activation;
    }

    public String getId_sondage() {
        return id_sondage;
    }

    public void setId_sondage(String id_sondage) {
        this.id_sondage = id_sondage;
    }

    public String getRep1() {
        return rep1;
    }

    public void setRep1(String rep1) {
        this.rep1 = rep1;
    }

    public String getRep2() {
        return rep2;
    }

    public void setRep2(String rep2) {
        this.rep2 = rep2;
    }

    public String getRep3() {
        return rep3;
    }

    public void setRep3(String rep3) {
        this.rep3 = rep3;
    }

    public String getRep4() {
        return rep4;
    }

    public void setRep4(String rep4) {
        this.rep4 = rep4;
    }

    public String getRep5() {
        return rep5;
    }

    public void setRep5(String rep5) {
        this.rep5 = rep5;
    }

    public String getAffichage() {
        return affichage;
    }

    public void setAffichage(String affichage) {
        this.affichage = affichage;
    }

    @Override
    public String toString() {
        return "QuestionEntity{" +
                "id='" + id + '\'' +
                ", titre='" + titre + '\'' +
                ", categorie='" + categorie + '\'' +
                ", type='" + type + '\'' +
                ", periodicite='" + periodicite + '\'' +
                ", activation='" + activation + '\'' +
                ", id_sondage='" + id_sondage + '\'' +
                ", rep1='" + rep1 + '\'' +
                ", rep2='" + rep2 + '\'' +
                ", rep3='" + rep3 + '\'' +
                ", rep4='" + rep4 + '\'' +
                ", rep5='" + rep5 + '\'' +
                ", affichage='" + affichage + '\'' +
                '}';
    }
}
