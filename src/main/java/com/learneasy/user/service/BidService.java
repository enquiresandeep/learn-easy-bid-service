package com.learneasy.user.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.learneasy.user.domain.Address;
import com.learneasy.user.domain.Bid;
import com.learneasy.user.infrastructure.AddressRepository;
import com.learneasy.user.infrastructure.BidRepository;
import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.BidDTO;
import com.learneasy.user.infrastructure.mapper.AddressMapper;
import com.learneasy.user.infrastructure.mapper.PhoneMapper;
import com.learneasy.user.infrastructure.mapper.BidMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BidService implements  IBidService{
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
     private BidRepository bidRepository;

    @Autowired
    private BidMapper bidMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private PhoneMapper phoneMapper;

    public BidDTO createBid(BidDTO bidDTO) throws Exception{
        log.info("BidService saveBid "+bidDTO.getFirstName());
        Bid bid =  bidRepository.save(bidMapper.bidDTOToBid(bidDTO) );
        return bidMapper.bidToBidDTO(bid);
    }

    public BidDTO findBidByBidId(String bidId){
        log.info("BidService findBidBuId "+bidId);
        return  bidMapper.bidToBidDTO(bidRepository.findById(bidId).get());
    }

    public BidDTO updateBid(BidDTO updatedBid) throws JsonMappingException {
         bidRepository.findById(updatedBid.getBidId())
                .orElseThrow(() -> new RuntimeException("Bid not found with id " + updatedBid.getBidId()));
        Bid bid = bidRepository.save(bidMapper.bidDTOToBid(updatedBid));
        return  bidMapper.bidToBidDTO(bid);
    }

    public List<BidDTO> findAll(){
        return bidMapper.bidToBidDTOs(bidRepository.findAll());
    }

    public AddressDTO createAddress(AddressDTO addressDTO) throws JsonMappingException{
        log.info("BidService createAddress "+addressDTO.getStreet());
        String bidId = addressDTO.getBidId();
        bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found with id " + bidId));
        Address address = addressRepository.save(addressMapper.addressDTOToAddress(addressDTO));
        return  addressMapper.addressToAddressDTO(address);
    }

    public List<AddressDTO> findAddressesByBidId(String bidId) {
        return  addressMapper.addressToAddressDTOs(addressRepository.findByBidId(bidId));
    }

    public AddressDTO updateAddress( AddressDTO updatedAddress) throws JsonMappingException {
         addressRepository.findById(updatedAddress.getId())
                .orElseThrow(() -> new RuntimeException("Bid not found with id " + updatedAddress.getId()));

        Address address = addressRepository.save( addressMapper.addressDTOToAddress(updatedAddress));

        return addressMapper.addressToAddressDTO(address);
    }


}
