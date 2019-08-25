package co.divisn.api.schema;

import java.util.List;

public interface SchemaDAO {

    List<Schema> findAll();

    Schema findById(String id);

    boolean insertSchema(Schema schema);

    boolean updateSchema(Schema schema);

    boolean deleteSchema(Schema schema);
}
