package com.learneasy.user;

import com.google.gson.Gson;
import io.confluent.kafka.schemaregistry.client.rest.entities.requests.RegisterSchemaRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.avro.Schema;

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
//https://dzone.com/articles/kafka-avro-serialization-and-the-schema-registry
public class RunSchemaTest {
    private final static MediaType SCHEMA_CONTENT =
            MediaType.parse("application/vnd.schemaregistry.v1+json");

    private final static String BID_SCHEMA =
            "{\"type\":\"record\",\"name\":\"Bid\",\"namespace\":\"com.avro.le\",\"fields\":[{\"name\":\"bidId\",\"type\":\"string\"},{\"name\":\"schedules\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Schedule\",\"fields\":[{\"name\":\"startDateTime\",\"type\":\"string\"},{\"name\":\"endDateTime\",\"type\":\"string\"}]}}},{\"name\":\"subjectId\",\"type\":\"string\"},{\"name\":\"tutorId\",\"type\":\"string\"}]}";

    public static void main(String[] args) throws IOException {
        System.out.println(BID_SCHEMA);

        final OkHttpClient client = new OkHttpClient();

//        String jsonSchema = new Gson().toJson(BID_SCHEMA);

        // Parse the Avro schema string using the Avro Schema class
        Schema avroBidSchema = new Schema.Parser().parse(BID_SCHEMA);

        // Create a new RegisterSchemaRequest object with the parsed schema
        RegisterSchemaRequest requestBid = new RegisterSchemaRequest();
//"le-bid", avroBidSchema.toString(false)
        requestBid.setSchema(avroBidSchema.toString(false));
//        requestBid.setSchemaType("le-bid");
        // POST the new schema to the Schema Registry
        RequestBody schemaBodyRequest = RequestBody.create(SCHEMA_CONTENT, requestBid.toJson());

        //POST A NEW SCHEMA
        Request request = new Request.Builder()
                .post(schemaBodyRequest)
                .url("http://localhost:8081/subjects/le-bid/versions")
                .build();
        String output = "";
       // output = client.newCall(request).execute().schemaBodyRequest().string();
        System.out.println(output);

        if(true)return;
        //LIST ALL SCHEMAS
        request = new Request.Builder()
                .url("http://localhost:8081/subjects")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println(output);


        //SHOW ALL VERSIONS OF EMPLOYEE
        request = new Request.Builder()
                .url("http://localhost:8081/subjects/le-bid/versions/")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println(output);

        //SHOW VERSION 6 OF EMPLOYEE
        request = new Request.Builder()
                .url("http://localhost:8081/subjects/le-bid/versions/6")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println(output);

        //SHOW THE SCHEMA WITH ID 3
        request = new Request.Builder()
                .url("http://localhost:8081/schemas/ids/3")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println(output);


        //SHOW THE LATEST VERSION OF EMPLOYEE 2
        request = new Request.Builder()
                .url("http://localhost:8081/subjects/le-bid/versions/latest")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println(output);



        //CHECK IF SCHEMA IS REGISTERED
        request = new Request.Builder()
                .post(schemaBodyRequest)
                .url("http://localhost:8081/subjects/le-bid")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println("REGISTERED ----"+output);


        //TEST COMPATIBILITY
        request = new Request.Builder()
                .post(schemaBodyRequest)
                .url("http://localhost:8081/compatibility/subjects/le-bid/versions/latest")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println("COMPATIBILITY" +output);
        if(true) return;

        // TOP LEVEL CONFIG
        request = new Request.Builder()
                .url("http://localhost:8081/config")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println(output);


        // SET TOP LEVEL CONFIG
        // VALUES are none, backward, forward and full
        request = new Request.Builder()
                .put(RequestBody.create(SCHEMA_CONTENT, "{\"compatibility\": \"none\"}"))
                .url("http://localhost:8081/config")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println(output);

        // SET CONFIG FOR EMPLOYEE
        // VALUES are none, backward, forward and full
        request = new Request.Builder()
                .put(RequestBody.create(SCHEMA_CONTENT, "{\"compatibility\": \"backward\"}"))
                .url("http://localhost:8081/config/Employee")
                .build();

        output = client.newCall(request).execute().body().string();
        System.out.println(output);
    }
}
