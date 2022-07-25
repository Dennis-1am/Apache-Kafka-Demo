package com.relay_kstreams;

import java.io.IOException;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Reader {
    
    public JsonNode readJson(String fileName) throws StreamReadException, DatabindException, IOException{
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode map = mapper.readValue(Paths.get(fileName).toFile(), JsonNode.class);
        //System.out.println(map.getNodeType());
        
        return map;

    }
}
