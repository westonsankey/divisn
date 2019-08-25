package co.divisn.destination.snowflake;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class BatchProcessor {

    public boolean processBatch(List<String> batch) {

        try {
            String url = "jdbc:snowflake://yb01486.us-east-1.snowflakecomputing.com/?user=westonsankey&" +
                    "password=Snowflake2020&" +
                    "db=DIVISN&" +
                    "warehouse=DIVISN&" +
                    "schema=public";

            Connection connection = DriverManager.getConnection(url);
            Statement stmt = connection.createStatement();
            stmt.execute("create or replace table emp_basic (\n" +
                    "  first_name string ,\n" +
                    "  last_name string ,\n" +
                    "  email string ,\n" +
                    "  streetaddress string ,\n" +
                    "  city string ,\n" +
                    "  start_date date\n" +
                    "  );");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return true;
    }
}
