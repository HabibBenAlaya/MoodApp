package com.soprahr.moodapp.entities;

public class User {
    private String id,nom,prenom,utilisateur,password,email,role,equipe,admin,genre;
    private byte[] photo;

    public User() {
    }

    public User(String id, String nom, String prenom, String utilisateur, String password, String email, String role, String equipe, String admin, String genre, byte[] photo) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.utilisateur = utilisateur;
        this.password = password;
        this.email = email;
        this.role = role;
        this.equipe = equipe;
        this.admin = admin;
        this.genre = genre;
        this.photo = photo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(String utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEquipe() {
        return equipe;
    }

    public void setEquipe(String equipe) {
        this.equipe = equipe;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", utilisateur='" + utilisateur + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", equipe='" + equipe + '\'' +
                ", admin='" + admin + '\'' +
                ", genre='" + genre + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
