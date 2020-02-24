package pbouda.nativeimage.quarkusisolates;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.bson.RawBsonDocument;
import org.bson.conversions.Bson;
import org.graalvm.nativeimage.CurrentIsolate;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.Isolates;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ApplicationScoped
public class NotesService {

    private static final Bson FROM_NEWEST_SORTING = RawBsonDocument.parse("{ $natural: -1 }");

    private final MongoCollection<Document> collection;

    @Inject
    public NotesService(MongoClient mongoClient) {
        this.collection = mongoClient.getDatabase("notes").getCollection("notes");
    }

    public int words(int limit) {
        IsolateThread currentContext = CurrentIsolate.getCurrentThread();
//        System.out.println("CURRENT " + currentContext);
        IsolateThread renderingContext = Isolates.createIsolate(Isolates.CreateIsolateParameters.getDefault());
//        System.out.println("RENDERING " + renderingContext);

        int resultWords = list(limit).stream()
                .map(entry -> entry.getContent().split(" "))
                .mapToInt(words -> words.length)
                .sum();

//        System.out.println("AFTER_COMPUTE " + CurrentIsolate.getCurrentThread());

        /* Tear down the isolate, freeing all the temporary objects. */
        Isolates.tearDownIsolate(renderingContext);

        return resultWords;
    }

    public List<Note> list(int limit) {
        FindIterable<Document> documents = collection.find()
                .sort(FROM_NEWEST_SORTING)
                .limit(limit);

        try (MongoCursor<Document> cursor = documents.iterator()) {
            Spliterator<Document> spliterator = Spliterators.spliteratorUnknownSize(cursor, Spliterator.ORDERED);
            return StreamSupport.stream(spliterator, false)
                    .map(NotesService::toNote)
                    .collect(Collectors.toUnmodifiableList());
        }
    }

    public String add(Note note) {
        Document document = toDocument(note);
        collection.insertOne(document);
        return document.getObjectId("_id").toString();
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