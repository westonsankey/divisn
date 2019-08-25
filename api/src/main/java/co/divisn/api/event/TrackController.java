package co.divisn.api.event;

import co.divisn.kafka.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
@ComponentScan(basePackages = "co.divisn.kafka")
public class TrackController {

    private Producer kafkaProducer;

    public TrackController(Producer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    private ObjectMapper mapper = new ObjectMapper();

    @PostMapping("/track")
    public ResponseEntity newTrack(@RequestBody Track track) {

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));

        try {
            String json = mapper.writeValueAsString(track);
            boolean success = kafkaProducer.SendMessageSynchronous(null, json);
            if (!success) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (JsonProcessingException ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
