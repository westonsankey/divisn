package co.divisn.destination;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to lookup data sources and the destinations to which events
 * from the source should be sent. Only a single instance of this
 * class will ever exist.
 */
public class DestinationLookup {

    // In-memory store of source destination mapping
    private Map<Integer, List<DestinationConfig>> cache;

    // Singleton instance of class
    private static DestinationLookup destinationLookup = null;

    /**
     * Given a source ID, retrieve the corresponding destinations.
     *
     * @param sourceId the data source
     * @return         the destinations corresponding to the source
     */
    public List<DestinationConfig> getDestinations(int sourceId) {
        if (cache.containsKey(sourceId)) {
            return cache.get(sourceId);
        } else {
            // TODO: log cache miss
            return new ArrayList<>();
        }
    }

    /**
     * Get the single instance of this class.
     *
     * @return an instance of this class
     */
    public static DestinationLookup getInstance() {
        if (destinationLookup == null) {
            destinationLookup = new DestinationLookup();
            destinationLookup.refreshCache();
        }

        return destinationLookup;
    }

    /**
     * Refresh the in-memory source to destination mapping from the database.
     */
    private void refreshCache() {
        cache = new HashMap<>();
        List<Source> sourceIDs = getSourcesFromDatabase();

        for (Source source : sourceIDs) {
            int sourceId = source.getId();
            List<DestinationConfig> destinations = getDestinationConfigs(sourceId);
            cache.put(sourceId, destinations);
        }
    }

    /**
     * Retrieve the list of sources from the database.
     *
     * @return the list of source IDs
     */
    private List<Source> getSourcesFromDatabase() {
        List<Source> sourceIDs = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");

            SessionFactory factory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Source.class)
                    .buildSessionFactory();

            Session session = factory.getCurrentSession();
            session.beginTransaction();

            sourceIDs = session
                    .createQuery("FROM Source")
                    .getResultList();

            factory.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("PostgreSQL driver class not found");
            ex.printStackTrace();
        }

        return sourceIDs;
    }

    /**
     * Given a source ID, retrieve all destinations to which the source events
     * should be sent.
     *
     * @param sourceId a data source identifier
     * @return the list of destinations for the source
     */
    private List<DestinationConfig> getDestinationConfigs(int sourceId) {
        List<DestinationConfig> configs = new ArrayList<>();

        try {
            Class.forName("org.postgresql.Driver");

            SessionFactory factory = new Configuration()
                    .configure()
                    .addAnnotatedClass(DestinationConfig.class)
                    .buildSessionFactory();

            Session session = factory.getCurrentSession();
            session.beginTransaction();

            configs = session
                    .createQuery("FROM DestinationConfig D WHERE D.sourceId = :sourceId")
                    .setParameter("sourceId", sourceId)
                    .getResultList();

            factory.close();

        } catch (ClassNotFoundException ex) {
            System.out.println("PostgreSQL driver class not found");
            ex.printStackTrace();
        }

        return configs;
    }
}
