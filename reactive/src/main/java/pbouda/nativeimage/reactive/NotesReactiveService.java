package pbouda.nativeimage.reactive;

import io.quarkus.mongodb.ReactiveMongoClient;
import io.quarkus.mongodb.ReactiveMongoCollection;
import org.bson.Document;
import org.bson.RawBsonDocument;
import org.bson.conversions.Bson;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ApplicationScoped
public class NotesReactiveService {

    private static final Bson FROM_NEWEST_SORTING = RawBsonDocument.parse("{ _id: -1 }");

    private final ReactiveMongoCollection<Document> collection;

    @Inject
    public NotesReactiveService(ReactiveMongoClient mongoClient) {
        this.collection = mongoClient.getDatabase("notes").getCollection("notes");
    }

    public CompletionStage<List<Note>> list(int limit) {
        return collection.find(FROM_NEWEST_SORTING)
                .limit(limit)
                .map(NotesReactiveService::toNote)
                .toList()
                .run();
    }

    public CompletionStage<String> add(Note note) {
        Document document = toDocument(note);
        return collection.insertOne(document)
                .thenApply(v -> document.getObjectId("_id").toString());
    }

    public static Note toNote(Document document) {
        return new Note(
                document.getString("firstname"),
                document.getString("lastname"),
                document.getString("email"),
                document.getString("subject"),
                document.getString("content"));
    }

    public static Document toDocument(Note note) {
        return new Document()
                .append("firstname", note.getFirstname())
                .append("lastname", note.getLastname())
                .append("email", note.getEmail())
                .append("subject", note.getSubject())
                .append("content", note.getContent());
    }
}