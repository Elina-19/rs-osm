package ru.itis.crawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.itis.crawler.db.clickhouse.EntityRepository;
import ru.itis.crawler.dto.CarBatteryDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@ApplicationScoped
public class EntityService {

    private final EntityRepository repository;

    public List<CarBatteryDTO> getCarBatteryEntities() {
        var query = "SELECT * FROM rs_osm.car_battery";
        return repository.getEntities(query, CarBatteryDTO.class);
    }

    public <T> void save(List<T> entities, String tableName) {
        repository.save(tableName, entities);
    }
}
