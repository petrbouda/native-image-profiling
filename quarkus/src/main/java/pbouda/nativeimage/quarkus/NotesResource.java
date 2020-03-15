package pbouda.nativeimage.quarkus;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.List;

@Path("/notes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NotesResource {

    private static final String HEAP_DUMP_COMMAND = "HeapDump.dumpHeap(FileOutputStream, Boolean)Boolean";

    private final NotesService service;

    public NotesResource(NotesService service) {
        this.service = service;
    }

    @GET
    public List<Note> list(@DefaultValue("10") @QueryParam("limit") int limit) {
//        int parallelism = ForkJoinPool.commonPool().getParallelism();
//        System.out.println("FORK_JOIN_POOL:  parallelism: " + parallelism);
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        System.out.println("HEAP MEMORY: " + memoryMXBean.getHeapMemoryUsage());
        System.out.println("NON-HEAP MEMORY: " + memoryMXBean.getNonHeapMemoryUsage());
        return service.list(limit);
    }

    @GET
    @Path("dump")
    public void generateHeapDump() {
        createHeapDump();
    }

    @POST
    public String post(Note note) {
        return service.add(note);
    }

    private static void createHeapDump() {
        boolean heapDumpCreated = false;
        try {
            File file = File.createTempFile("SVMHeapDump-", ".hprof");
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                // Create heap dump
                final Object[] args = new Object[]{HEAP_DUMP_COMMAND, fileOutputStream, Boolean.TRUE};
                final Object result = Compiler.command(args);
                // Following code checks if heap dump was created using return value
                if (result instanceof Boolean) {
                    heapDumpCreated = ((Boolean) result).booleanValue();
                }
            }

            if (heapDumpCreated) {
                System.out.println("  Heap dump created " + file.getAbsolutePath() + ", size: " + file.length());
            } else {
                // Delete the file to not pollute disk with empty files.
                System.out.println("  Heap dump creation failed.");
                file.delete();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}