package pbouda.nativeimage.springboot;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends ReactiveMongoRepository<Note, Long> {
}