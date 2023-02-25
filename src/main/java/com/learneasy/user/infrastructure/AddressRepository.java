package com.learneasy.user.infrastructure;

import com.learneasy.user.domain.Address;
import com.learneasy.user.domain.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends MongoRepository<Address,String> {

    Optional<Address> findById(String addressId);

    List<Address> findByStudentId(String studentId);

}
