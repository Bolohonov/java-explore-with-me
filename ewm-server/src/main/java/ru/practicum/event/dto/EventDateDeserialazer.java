//package ru.practicum.event.dto;
//
//import com.fasterxml.jackson.core.JacksonException;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonNode;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Locale;
//
//@Slf4j
//public class EventDateDeserialazer extends JsonDeserializer<LocalDateTime> {
//    @Override
//    public LocalDateTime deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException, JacksonException {
//        log.info("Пришел запрос DateDeserialize");
//        JsonNode node = jp.getCodec().readTree(jp);
//        String str = node.get("eventDate").toString();
//        System.out.println(str);
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",
//                Locale.getDefault());
//        LocalDateTime result = LocalDateTime.parse(str, formatter);
//        return result;
//    }
//}
