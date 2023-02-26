package com.learneasy.user.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.BidDTO;

import java.util.List;

public interface IBidService {
    public BidDTO createBid(BidDTO bid) throws Exception;

    public BidDTO findBidByBidId(String bidId);

    public BidDTO updateBid(BidDTO updatedBid) throws JsonMappingException ;
    public List<BidDTO> findAll();

    public AddressDTO createAddress(AddressDTO address) throws JsonMappingException;

    public List<AddressDTO> findAddressesByBidId(String bidId) ;

    public AddressDTO updateAddress( AddressDTO updatedAddress) throws JsonMappingException ;
}
