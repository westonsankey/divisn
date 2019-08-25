package co.divisn.api.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Component
@RestController
public class SchemaController {

    private ObjectMapper mapper = new ObjectMapper();

    public SchemaController() {
    }

    @PostMapping("/schema")
    public ResponseEntity registerSchema(@RequestBody SchemaRequest schemaRequest) {
        try {
            String schemaId = schemaRequest.getId();
            String name = schemaRequest.getName();
            String json = mapper.writeValueAsString(schemaRequest);
            Schema schema = new Schema(schemaId, name, json);
            SchemaDAO dao = new SchemaDAOPostgres();
            dao.insertSchema(schema);
        } catch (JsonProcessingException ex) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
