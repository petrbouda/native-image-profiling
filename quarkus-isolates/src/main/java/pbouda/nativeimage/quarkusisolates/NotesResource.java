package pbouda.nativeimage.quarkusisolates;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotesResource {

    private final NotesService service;

    public NotesResource(NotesService service) {
        this.service = service;
    }

    @GET
    public List<Note> list(@DefaultValue("10") @QueryParam("limit") int limit) {
        return service.list(limit);
    }

    @POST
    public String post(Note note) {
        return service.add(note);
    }

    @GET
    @Path("words")
    public int words(@DefaultValue("10") @QueryParam("limit") int limit) {
        return service.words(limit);
    }
}