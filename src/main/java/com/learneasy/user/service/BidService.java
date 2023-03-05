package com.learneasy.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learneasy.user.domain.Bid;
import com.learneasy.user.infrastructure.db.BidRepository;
import com.learneasy.user.infrastructure.dto.BidDTO;
import com.learneasy.user.infrastructure.mapper.BidMapper;
import com.networknt.schema.JsonValidator;
import io.confluent.kafka.schemaregistry.ParsedSchema;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.SchemaValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BidService implements  IBidService{

    @Autowired
     private BidRepository bidRepository;

    @Autowired
    private BidMapper bidMapper;

    @Autowired
    private  KafkaTemplate<String,Object> kafkaTemplate;

    @Autowired
    private  SchemaRegistryClient schemaRegistryClient;

    @Autowired
    private  ObjectMapper objectMapper;


    public BidDTO createBid(BidDTO bidDTO) throws Exception{
        log.info("BidService saveBid "+bidDTO.getTutorId());
        bidDTO.setCreatedDate(ZonedDateTime.now());
        Bid bid =  bidRepository.save(bidMapper.bidDTOToBid(bidDTO) );
        return bidMapper.bidToBidDTO(bid);
    }

    public BidDTO findBidByBidId(String bidId){
        log.info("BidService findBidBuId "+bidId);
        return  bidMapper.bidToBidDTO(bidRepository.findById(bidId).get());
    }

    @Override
    public List<BidDTO> findBidsByTutorId(String tutorId) {
        log.info("BidService findBidsByTutorId "+tutorId);
        return  bidMapper.bidToBidDTOs(bidRepository.findBidsByTutorId(tutorId).get());
    }

    public BidDTO updateBid(BidDTO updatedBid) throws JsonMappingException {
         bidRepository.findById(updatedBid.getBidId())
                .orElseThrow(() -> new RuntimeException("Bid not found with id " + updatedBid.getBidId()));
        Bid bid = bidRepository.save(bidMapper.bidDTOToBid(updatedBid));
        return  bidMapper.bidToBidDTO(bid);
    }

    @Override
    public void deleteBid(BidDTO bidDTO) throws Exception {
        log.info("BidService saveBid "+bidDTO.getBidId());
        bidRepository.findById(bidDTO.getBidId())
                .orElseThrow(() -> new RuntimeException("Bid not found with id " + bidDTO.getBidId()));

         bidRepository.delete(bidMapper.bidDTOToBid(bidDTO) );
    }

//    @Override
//    public BidDTO parseBid(String requestBody) throws Exception {
//
//        BidDTO bid = objectMapper.readValue(requestBody, BidDTO.class);
//
//        // Validate the Bid object against the Avro schema
//        String schemaName = "Bid";
//        ParsedSchema schema = schemaRegistryClient.getSchemaBySubjectAndId(schemaName + "-value", 0);
//        JsonNode bidJson = objectMapper.readTree(requestBody);
//        JsonNode schemaJson = objectMapper.readTree(schema.toString());
//        JsonValidator jsonValidator = new JsonValidator(schemaJson);
//        JsonNode errors = jsonValidator.validate(bidJson);
//        if (errors != null && errors.size() > 0) {
//            // Throw a SchemaValidationException with the validation error(s)
//            List<String> errorMessages = new ArrayList<>();
//            errors.elements().forEachRemaining(error -> errorMessages.add(error.get("message").asText()));
//            throw new Exception("Validation failed" + errorMessages);
//        }
//
//        return bid;
//
//    }

    public BidDTO createBidAsync(BidDTO bidDTO) throws IOException {
        // Send the Bid message to Kafka
        String topicName = "le-bid";
        kafkaTemplate.send(topicName, bidDTO);
        return bidDTO;
    }

}
