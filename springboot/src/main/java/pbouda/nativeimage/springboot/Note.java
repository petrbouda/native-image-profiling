package pbouda.nativeimage.springboot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {
    private String firstname;
    private String lastname;
    private String email;
    private String subject;
    private String content;
}
