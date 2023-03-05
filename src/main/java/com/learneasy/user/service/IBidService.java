package com.learneasy.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.learneasy.user.infrastructure.dto.BidDTO;

import java.util.List;

public interface IBidService {
    public BidDTO createBid(BidDTO bid) throws Exception;

    public BidDTO findBidByBidId(String bidId);
    public List<BidDTO> findBidsByTutorId(String tutorId);

    public BidDTO updateBid(BidDTO updatedBid) throws JsonMappingException ;

    public void deleteBid(BidDTO bid) throws Exception;

   // BidDTO parseBid(String requestBody) throws JsonProcessingException, Exception;

    public BidDTO createBidAsync(BidDTO bid) throws Exception;


}
