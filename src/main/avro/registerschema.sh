curl -X POST -H "Content-Type: application/vnd.schemaregistry.v1+json" \
     --data '{"schema": "{\"namespace\":\"com.avro.le\",\"type\":\"record\",\"name\":\"Bid\",\"fields\":[{\"name\":\"bidId\",\"type\":\"string\"},{\"name\":\"schedules\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Schedule\",\"fields\":[{\"name\":\"startDateTime\",\"type\":\"string\"},{\"name\":\"endDateTime\",\"type\":\"string\"}]}}},{\"name\":\"subjectId\",\"type\":\"string\"},{\"name\":\"tutorId\",\"type\":\"string\"}]}"}' \
     http://localhost:8081/subjects/le-bid-value/versions
