package pbouda.nativeimage.quarkus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

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

        int parallelism = ForkJoinPool.commonPool().getParallelism();
        System.out.println("FORK_JOIN_POOL:  parallelism: " + parallelism);

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.println("HEAP MEMORY: " + memoryMXBean.getHeapMemoryUsage());
        System.out.println("NON-HEAP MEMORY: " + memoryMXBean.getNonHeapMemoryUsage());

        return service.list(limit);
    }

    @POST
    public String post(Note note) {
        return service.add(note);
    }
}