package dev.bolohonov.model.compilation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@JsonComponent
public class CompilationEventsListDeserializer extends JsonDeserializer<List<Long>> {
    @Override
    public List<Long> deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        String str = node.asText();
        System.out.println(str);
        return Collections.emptyList();
    }
}
