package com.learneasy.user.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learneasy.user.domain.Bid;
import com.learneasy.user.domain.ErrorResponse;
import com.learneasy.user.infrastructure.dto.BidDTO;
import com.learneasy.user.service.BidService;
import com.learneasy.user.service.IBidService;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.JsonValidator;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.SchemaValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.validation.Schema;
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
        log.info("BidService saveBid new logs "+bid.getTutorId());
        try{
            return ResponseEntity.ok( _bidService.createBid(bid));
        }catch(Exception e){
            log.error("BidService error {}", bid.getTutorId());
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

    @GetMapping("/findBidsByTutorId/{tutorId}")
    public List<BidDTO> findBidsByTutorId(@PathVariable("tutorId") String tutorId){
        log.info("BidService findBidsByTutorId "+tutorId);
        return _bidService.findBidsByTutorId(tutorId);
    }

    @DeleteMapping("")
    public ResponseEntity<String> deleteBidByBidId(@RequestBody BidDTO bid)  {

        log.info("BidService findBidBuId "+bid.getBidId());
        try {
            _bidService.deleteBid(bid);
        } catch (Exception e) {
            return new ResponseEntity<>("Bid not found with id: " + bid.getBidId(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/createBidAsync")
    public ResponseEntity<BidDTO> createBid(@RequestBody BidDTO bidDTO) {
        try {
            // Parse the request body as a Bid object
            //BidDTO bidDTO = _bidService.parseBid(requestBody);

            // Create the bid using the BidService
            _bidService.createBidAsync(bidDTO);

            // Return a 200 OK response with the Bid object
            return ResponseEntity.ok(bidDTO);
        } catch (SchemaValidationException e) {
            // Return a 400 Bad Request response with the validation error(s)
            BidDTO error = new BidDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            e.printStackTrace();
            // Return a 500 Internal Server Error response if an unexpected error occurs
            BidDTO error = new BidDTO();
            error.setErrorMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error);
        }
    }


}
