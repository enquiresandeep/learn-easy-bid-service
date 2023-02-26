package com.learneasy.user.infrastructure;

import com.learneasy.user.domain.Bid;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BidRepository extends MongoRepository<Bid,String> {

    Optional<Bid> findById(String bidId);

}
