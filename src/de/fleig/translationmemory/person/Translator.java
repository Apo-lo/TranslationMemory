package de.fleig.translationmemory.person;

public class Translator extends AuthorizedUser {

    public Translator (String email, String password) {
        super(email, password);
    }

    @Override
    public String toString() {
        return (this.getClass().toString() + " " + getPassword() + " " + getEmail());
    }
}
