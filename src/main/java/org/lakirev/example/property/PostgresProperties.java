package org.lakirev.example.property;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "postgres")
public class PostgresProperties {

    String url = "jdbc:postgresql://localhost:5432/postgres";
    String username = "postgres";
    String password = "example";

}
