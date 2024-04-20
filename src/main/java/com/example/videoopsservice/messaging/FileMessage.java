package com.example.videoopsservice.messaging;

public class FileMessage {

    private String fileID;
    private String email;

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FileMessage() {
    }

    public FileMessage(String fileID, String email) {
        this.fileID = fileID;
        this.email = email;
    }


}
