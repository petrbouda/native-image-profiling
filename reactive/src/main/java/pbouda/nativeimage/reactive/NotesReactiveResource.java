package pbouda.nativeimage.reactive;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotesReactiveResource {

    private final NotesReactiveService service;

    public NotesReactiveResource(NotesReactiveService service) {
        this.service = service;
    }

    @GET
    public CompletionStage<List<Note>> list(@DefaultValue("10") @QueryParam("limit") int limit) {
        return service.list(limit);
    }

    @POST
    public CompletionStage<String> post(Note note) {
        return service.add(note);
    }
}