package com.guillot.go4lunch.Models;

class User {
    private String id;
    private String username;
    private String urlProfilePicture;

    public User(String id, String username,String urlProfilePicture ) {
        this.id = id;
        this.username = username;
        this.urlProfilePicture = urlProfilePicture;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getUrlProfilePicture() { return urlProfilePicture; }

    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setUrlProfilePicture(String urlProfilePicture) { this.urlProfilePicture = urlProfilePicture; }
}
