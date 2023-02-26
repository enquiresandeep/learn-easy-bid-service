package com.learneasy.user.controller;

import com.learneasy.user.infrastructure.dto.AddressDTO;
import com.learneasy.user.infrastructure.dto.BidDTO;
import com.learneasy.user.service.IBidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequestMapping("/bid")
public class BidController {

    @Autowired
    private final IBidService _bidService;

    public BidController(IBidService bidService){
        this._bidService = bidService;
    }


    @PostMapping("/")
    public ResponseEntity<BidDTO> saveBid(@RequestBody BidDTO bid) {
        log.info("BidService saveBid new logs "+bid.getFirstName());
        try{
            return ResponseEntity.ok( _bidService.createBid(bid));
        }catch(Exception e){
            log.error("BidService error {}", bid.getFirstName());
            BidDTO errorBid = new BidDTO();
            errorBid.setErrorMessage("Server Error");
            return new ResponseEntity<>(errorBid, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/")
    public ResponseEntity<BidDTO> updateBid(@RequestBody BidDTO bid) {
        try {
            BidDTO updatedBid = _bidService.updateBid(bid);
            return ResponseEntity.ok(updatedBid);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            BidDTO errorBid = new BidDTO();
            errorBid.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorBid, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            BidDTO errorBid = new BidDTO();
            errorBid.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorBid, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public BidDTO findBidByBidId(@PathVariable("id") String bidId){
        log.info("BidService findBidBuId "+bidId);
        return _bidService.findBidByBidId(bidId);
    }

    @GetMapping("/")
    public List<BidDTO>  findAll(){
        log.info("findAll ");
        return _bidService.findAll();
    }

    @PostMapping("/createAddress")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO address) {
        log.info("BidService saveBid "+address.getStreet());
        try {
            AddressDTO updatedAddress = _bidService.createAddress(address);
            return ResponseEntity.ok(updatedAddress);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            AddressDTO errorAddress = new AddressDTO();
            errorAddress.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorAddress, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @PutMapping("/updateAddress")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO address) {
        try {
            AddressDTO updatedAddress = _bidService.updateAddress(address);
            return ResponseEntity.ok(updatedAddress);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            AddressDTO errorAddress = new AddressDTO();
            errorAddress.setErrorMessage(e.getMessage());
            return new ResponseEntity<>(errorAddress, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/address/{id}")
    public ResponseEntity<List<AddressDTO>> findAddresesByBidId(@PathVariable("id") String bidId){
        log.info("BidService findBidBuId "+bidId);
        return ResponseEntity.ok(_bidService.findAddressesByBidId(bidId));
    }



}
