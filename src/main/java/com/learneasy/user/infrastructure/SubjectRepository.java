package com.learneasy.user.infrastructure;

import com.learneasy.user.domain.Address;
import com.learneasy.user.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    Optional<Subject> findById(String subjectId);

    List<Subject> findSubjectsByStudentId(String studentId);


}
