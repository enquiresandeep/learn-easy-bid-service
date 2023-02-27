package com.learneasy.user.infrastructure;

import com.learneasy.user.domain.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    Optional<Subject> findById(String subjectId);

    List<Subject> findByStudentId(String studentId);

    @Query("{ 'subjectTags.tagName' : ?0, 'studentId' : ?1 }")
    List<Subject> findSubjectsByTagNameAndStudentId(String tagName, String studentId);

}
