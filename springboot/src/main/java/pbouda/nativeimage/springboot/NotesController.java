package pbouda.nativeimage.springboot;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path = "notes")
public class NotesController {

    private final NotesRepository service;

    public NotesController(NotesRepository service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Note> list(@RequestParam(name = "limit", defaultValue = "10") int limit) {
        return service.findAll()
                .limitRequest(limit);
    }

    @PostMapping
    public Mono<String> post(@RequestBody Note note) {
        return service.save(note)
                .map(Note::getSubject);
    }
}
