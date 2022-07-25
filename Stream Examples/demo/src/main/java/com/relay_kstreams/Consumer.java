package com.relay_kstreams;

import org.apache.kafka.clients.consumer.ConsumerConfig;  
import org.apache.kafka.clients.consumer.ConsumerRecord;  
import org.apache.kafka.clients.consumer.ConsumerRecords;  
import org.apache.kafka.clients.consumer.KafkaConsumer;  
import org.apache.kafka.common.serialization.StringDeserializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;  
import java.util.Arrays;    
import java.util.Properties;  

public class Consumer {  
    public static void main(String[] args) throws JsonMappingException, JsonProcessingException {    
        String bootstrapServers="localhost:9092";  // the localhost is the broker itself
        String grp_id="client_1";    

        //Creating consumer properties  
        Properties properties=new Properties();  
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);  
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());  
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());  
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id);  
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");  

        String grp_id2 = "client_2";

        Properties properties2 = new Properties();
        properties2.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        properties2.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());  
        properties2.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());  
        properties2.setProperty(ConsumerConfig.GROUP_ID_CONFIG,grp_id2);  
        properties2.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"latest");  

        
        
        KafkaConsumer<String, String> msgConsumer = new KafkaConsumer<>(properties);
        msgConsumer.subscribe(Arrays.asList("message"));

        KafkaConsumer<String,String> notificationConsumer = new KafkaConsumer<>(properties2);
        notificationConsumer.subscribe(Arrays.asList("notifications"));

        while(true){
            ConsumerRecords<String, String> msg = msgConsumer.poll(Duration.ofSeconds(10));
            
            for(ConsumerRecord<String, String> record : msg){
                
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readValue(record.value(), JsonNode.class);
                System.out.println(node.get("event_type"));
                JsonNode payload = mapper.readValue(node.get("payload").toString(), JsonNode.class);

            }
            
            ConsumerRecords<String, String> notification = notificationConsumer.poll(Duration.ofSeconds(1));
            for(ConsumerRecord<String, String> record : notification){

                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readValue(record.value(), JsonNode.class);
                System.out.println(node.get("event_type"));
                
            }
        }

    }
    
}  