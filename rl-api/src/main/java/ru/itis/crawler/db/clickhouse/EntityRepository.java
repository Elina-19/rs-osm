package ru.itis.crawler.db.clickhouse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.agroal.api.AgroalDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class EntityRepository {

    private static final String BASE_STATEMENT = "SELECT formatRow('JSONEachRow', *) AS value FROM (%s)";

    private final AgroalDataSource dataSource;
    private final ObjectMapper mapper;

    public <T> List<T> getEntities(String query, Class<T> clazz) {
        try (var connection = dataSource.getConnection();
             var statement = connection.prepareStatement(String.format(BASE_STATEMENT, query)))
        {
            var result = statement.executeQuery();
            return mapToEntities(result, clazz);
        } catch (SQLException e) {
            log.error("Failed execute statement: {}", query, e);
            return Collections.emptyList();
        }
    }

    public <T> void save(String tableName, List<T> entities) {
        try {
            var json = mapper.writeValueAsString(entities);
            var query = "INSERT INTO " + tableName + " FORMAT JSONEachRow " + json;
            try (var connection = dataSource.getConnection()) {
                try (var statement = connection.createStatement()) {
                    statement.execute(query);
                }
                connection.commit();
            }
        } catch (JsonProcessingException | SQLException e) {
            log.error("Error when save entities");
        }
    }

    private <T> List<T> mapToEntities(ResultSet resultSet, Class<T> clazz) throws SQLException {
        var entities = new ArrayList<T>();
        try {
            while (resultSet.next()) {
                var entity = mapper.readValue(resultSet.getString(1), clazz);
                entities.add(entity);
            }
            return entities;
        } catch (JsonProcessingException e) {
            log.error("Failed map result set to entities", e);
            return Collections.emptyList();
        }
    }
}
