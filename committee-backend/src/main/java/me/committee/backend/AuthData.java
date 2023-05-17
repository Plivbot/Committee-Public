package me.committee.backend;

public class AuthData {
    boolean correct;
    String name;
    String passwd;

    public AuthData(boolean correct, String name, String passwd) {
        this.correct = correct;
        this.name = name;
        this.passwd = passwd;
    }
}
