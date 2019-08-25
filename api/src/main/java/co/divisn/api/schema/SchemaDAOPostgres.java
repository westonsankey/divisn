package co.divisn.api.schema;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class SchemaDAOPostgres implements SchemaDAO {

    @Override
    public List<Schema> findAll() {
        return null;
    }

    @Override
    public Schema findById(String name) {
        Schema schema = null;

        try {
            Class.forName("org.postgresql.Driver");

            SessionFactory factory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Schema.class)
                    .buildSessionFactory();

            Session session = factory.getCurrentSession();
            session.beginTransaction();

            schema = (Schema) session.createQuery("FROM Schema S WHERE S.name = :name")
                    .setParameter("name", name)
                    .getSingleResult();

            factory.close();

        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return schema;
    }

    @Override
    public boolean insertSchema(Schema schema) {
        try {
            Class.forName("org.postgresql.Driver");

            SessionFactory factory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Schema.class)
                    .buildSessionFactory();

            Session session = factory.getCurrentSession();
            session.beginTransaction();
            session.save(schema);
            session.getTransaction().commit();
            factory.close();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateSchema(Schema schema) {
        return false;
    }

    @Override
    public boolean deleteSchema(Schema schema) {
        return false;
    }
}
