package ru.itis.crawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.itis.crawler.dto.CarBatteryDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@RequiredArgsConstructor
@ApplicationScoped
@Slf4j
public class PostprocessService {

    private static final String TABLE_NAME = "rs_osm.car_battery_processed";

    private final EntityService entityService;

    public void postprocess() {
        var entities = entityService.getCarBatteryEntities();
        entities.stream().forEach(this::postprocess);
        entityService.save(entities, TABLE_NAME);
    }

    private void postprocess(CarBatteryDTO dto) {
        dto.setName(processName(dto.getName()));
        dto.setRating(processDouble(dto.getRating()));
        dto.setPrice(getPrice(dto.getPrice(), dto.getSalePrice()));
        dto.setTypeOfVehicle(basePostProcessStr(dto.getTypeOfVehicle()));
        dto.setBatteryTechnology(basePostProcessStr(dto.getBatteryTechnology()));
        dto.setFeatures(basePostProcessStr(dto.getFeatures()));
        dto.setKlemmy(basePostProcessStr(dto.getKlemmy()));
        dto.setCaseType(basePostProcessStr(dto.getCaseType()));
        dto.setBatteryMarking(basePostProcessStr(dto.getBatteryMarking()));
        dto.setAdditional(basePostProcessStr(dto.getAdditional()));
        dto.setLength(processDouble(dto.getLength()));
        dto.setWidth(processDouble(dto.getWidth()));
        dto.setHeight(processDouble(dto.getHeight()));
        dto.setWeight(processDouble(dto.getWeight()));
    }

    private String processName(String name) {
        return Optional.ofNullable(name)
                .map(n -> n.toLowerCase())
                .map(n -> StringUtils.contains(n, ", ") ? StringUtils.substringBefore(n, ", ") : n)
                .map(n -> n.replaceAll("\\s*\\(.*?\\)", "")
                        .replaceAll("\\b\\d+х\\d+х\\d+\\b", "")
                        .replaceAll("\\b\\d+x\\d+x\\d+\\b", "") //another x
                        .replace("обратная полярность", "")
                        .replace("прямая полярность", "")
                        .replace("полярность прямая", "")
                        .replace("обратной полярности", "")
                        .replace("прямой полярности", "")
                        .replace("обр. пол.", "")
                        .replace("обр.", "")
                        .replace("прям.", "")
                        .replaceAll("[^a-zA-Z0-9а-яА-Я-\\s]", "")
                        .replaceAll("\\s+", " ")
                        .trim()
                )
                .orElse(null);
    }

    private Double getPrice(Double price, Double salePrice) {
        if (salePrice == 0.0 || salePrice == -1.0) {
            return price;
        }
        if (price == 0.0 || price == -1.0) {
            return salePrice;
        }
        return salePrice;
    }

    private String basePostProcessStr(String str) {
        return Optional.ofNullable(str)
                .map(s -> s.toLowerCase().trim()
                        .replaceAll("\\s+", " ")
                        .replace(",", "")
                        .replace("(", "")
                        .replace(")", ""))
                .orElse(null);
    }

    private Double processDouble(Double doub) {
        return Optional.ofNullable(doub)
                .map(d -> d == 0.0 ? -1.0 : d)
                .orElse(-1.0);
    }
}
