package pbouda.nativeimage.imperative;

import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbProperty;

public class Note {

    private final String firstname;
    private final String lastname;
    private final String email;
    private final String subject;
    private final String content;

    @JsonbCreator
    public Note(
            @JsonbProperty("firstname") String firstname,
            @JsonbProperty("lastname") String lastname,
            @JsonbProperty("email") String email,
            @JsonbProperty("subject") String subject,
            @JsonbProperty("content") String content) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.subject = subject;
        this.content = content;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
