package com.learneasy.user.service;

import com.avro.le.Schedule;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learneasy.user.domain.Bid;
import com.learneasy.user.infrastructure.db.BidRepository;
import com.learneasy.user.infrastructure.dto.BidDTO;
import com.learneasy.user.infrastructure.mapper.BidMapper;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BidService implements  IBidService{

    @Autowired
     private BidRepository bidRepository;

    @Autowired
    private BidMapper bidMapper;

    @Autowired
    private  KafkaTemplate<Long, com.avro.le.Bid> kafkaTemplate;

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

    public BidDTO createBidAsync(BidDTO bidDTO) throws Exception {
//        KafkaAvroSerializer serializer = new KafkaAvroSerializer(schemaRegistryClient);

 //       ParsedSchema schema = schemaRegistryClient.getSchemaBySubjectAndId("le-bid-value", 3);
//        Headers headers = new RecordHeaders();
//        headers.add(new RecordHeader("schema", schema.toString().getBytes()));

   //     byte[] serializedBid = serializer.serialize("le-bid",null, mapToBidAvro(bidDTO));

     //   System.out.println(new String(serializedBid));

        ProducerRecord<Long, com.avro.le.Bid> record = new ProducerRecord<Long, com.avro.le.Bid>("le-bid",123456L, mapToBidAvro(bidDTO));

        kafkaTemplate.send(record);
        kafkaTemplate.flush();
        return bidDTO;
    }

    public class BidSerializer implements Serializer<com.avro.le.Bid> {

        @Override
        public byte[] serialize(String topic, com.avro.le.Bid data) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try {
                BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);
                DatumWriter<com.avro.le.Bid> writer = new SpecificDatumWriter<>(com.avro.le.Bid.class);
                writer.write(data, encoder);
                encoder.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return out.toByteArray();
        }

        @Override
        public void close() {

        }
    }

    public static GenericRecord buildRecord() throws Exception {
        // avro schema avsc file path.
        String schemaPath = "/Users/sandeepchandan/Documents/SandeepChandan/SHIVA WS/learneasy/bid-mgmt-service/src/main/avro/bid.avsc";
        // avsc json string.
        String schemaString = null;

        FileInputStream inputStream = new FileInputStream(schemaPath);
        try {
            schemaString = new String(IOUtils.toByteArray(inputStream));
        } finally {
            inputStream.close();
        }
        // avro schema.
        Schema schema = new Schema.Parser().parse(schemaString);
        // generic record for page-view-event.
        GenericData.Record record = new GenericData.Record(schema);
        // put the elements according to the avro schema.
        record.put("itemId", "any-item-id");

        return record;
    }

    public static com.avro.le.Bid mapToBidAvro(BidDTO bidDTO) {
        List<Schedule> schedules = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        for (com.learneasy.user.domain.Schedule scheduleDTO : bidDTO.getSchedules()) {
            Schedule schedule = new Schedule();
            schedule.setStartDateTime(scheduleDTO.getStartDateTime().format(formatter));
            schedule.setEndDateTime(scheduleDTO.getEndDateTime().format(formatter));
            schedules.add(schedule);
        }


        com.avro.le.Bid bid = com.avro.le.Bid.newBuilder()
                .setBidId("")
                .setSchedules(schedules)
                .setSubjectId(bidDTO.getSubjectId())
                .setTutorId(bidDTO.getTutorId())
                .build();


        return bid;
    }

}
