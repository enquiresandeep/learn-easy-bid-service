package com.learneasy.user.infrastructure.db;

import com.learneasy.user.domain.Bid;
import com.learneasy.user.service.BidService;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends MongoRepository<Bid,String> {

    Optional<Bid> findById(String bidId);

    Optional<List<Bid>> findBidsByTutorId(String tutorId);

}
