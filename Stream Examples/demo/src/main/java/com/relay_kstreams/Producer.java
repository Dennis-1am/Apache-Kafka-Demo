package com.relay_kstreams;


import java.io.IOException;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Producer
 */

public class Producer {
    public static void main(String args[]) throws StreamReadException, DatabindException, IOException{

        Properties props = new Properties();
        
        // setting producer properties
        props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName() );
        props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class.getName());
        
        // creating Kakfa producer with the properties that have been set
        KafkaProducer<String, JsonNode> producer = new KafkaProducer<>(props);
        
        Reader read = new Reader();
        JsonNode node = read.readJson("readExample.json"); //ArrayList of JsonNode

        // create a record of the event: topic value pair
        for(JsonNode n : node){
            if(String.valueOf(n.get("event_type")).equals("\"message\"")){
                ProducerRecord<String, JsonNode> record = new ProducerRecord<String,JsonNode>("message", n);
                producer.send(record);
                producer.flush();
            } else if(String.valueOf(n.get("event_type")).equals("\"notification\"")){
                ProducerRecord<String, JsonNode> record = new ProducerRecord<String,JsonNode>("notifications", n);
                producer.send(record);
                producer.flush();
            } else {
                System.out.println("unknown event type");
            }
        }
    
        // send the record to the kafka cluster for processing
        producer.close();

    }
}