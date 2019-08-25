
CREATE SCHEMA router;

CREATE TABLE router.kafka_topic (
    kafka_topic_id SERIAL PRIMARY KEY ,
    name           VARCHAR(255)
);

CREATE TABLE router.destination_type (
    destination_type_id SERIAL PRIMARY KEY ,
    name                VARCHAR(255)
);

CREATE TABLE router.source_type (
    source_type_id SERIAL PRIMARY KEY ,
    name           VARCHAR(255)
);

CREATE TABLE router.source (
    source_id       SERIAL PRIMARY KEY ,
    source_type_id  INT NOT NULL REFERENCES router.source_type(source_type_id),
    created_date    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE router.destination (
    destination_id      SERIAL PRIMARY KEY ,
    destination_type_id INT NOT NULL REFERENCES router.destination_type(destination_type_id),
    source_id           INT NOT NULL REFERENCES router.source(source_id),
    kafka_topic_id      INT NOT NULL REFERENCES router.kafka_topic(kafka_topic_id),
    created_date        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO router.source_type (name)
VALUES ('http');

INSERT INTO router.destination_type (name)
VALUES ('bigquery'),
       ('elasticsearch'),
       ('snowflake');


INSERT INTO router.kafka_topic (name)
VALUES ('divisn.destination.bigquery'),
       ('divisn.destination.elasticsearch'),
       ('divisn.destination.snowflake');


INSERT INTO router.source (source_type_id)
VALUES (1);

INSERT INTO router.destination (destination_type_id, source_id, kafka_topic_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 1, 3);


CREATE OR REPLACE VIEW router.destination_config AS
SELECT ROW_NUMBER() over (order by D.source_id) AS id, D.source_id, KT.name AS kafka_topic
FROM router.destination AS D
    JOIN router.kafka_topic AS KT ON D.kafka_topic_id = KT.kafka_topic_id;


CREATE SCHEMA destination;

CREATE TABLE destination.config_snowflake
(
    destination_id INT NOT NULL PRIMARY KEY REFERENCES router.destination(destination_id),
    snowflake_user VARCHAR(255) NOT NULL,
    password       VARCHAR(255) NOT NULL,
    account        VARCHAR(255) NOT NULL,
    database       VARCHAR(255) NOT NULL,
    warehouse      VARCHAR(255) NOT NULL
);

INSERT INTO destination.config_snowflake (destination_id, snowflake_user, password, account, database, warehouse)
VALUES (3, 'westonsankey', 'KAJMl2BEj5w*a3A4Zi2grN!s', 'yb01486.us-east-1', 'DIVISN_EVENTS', 'DIVISN_WH');


CREATE SCHEMA schema;

CREATE TABLE schema.schema
(
    schema_id     VARCHAR(500) PRIMARY KEY,
    schema        TEXT NOT NULL,
    created_date  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
