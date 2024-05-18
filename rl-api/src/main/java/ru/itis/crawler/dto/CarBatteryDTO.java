package ru.itis.crawler.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.itis.crawler.utils.CrawlerUtils;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarBatteryDTO {

    @JsonFormat(pattern = CrawlerUtils.LOCAL_DATE_TIME_FORMAT)
    private LocalDateTime crawling_start;
    @JsonFormat(pattern = CrawlerUtils.LOCAL_DATE_TIME_FORMAT)
    private LocalDateTime crawling_date;
    private String source;
    private String name;
    private String description;
    @JsonProperty("img_url")
    private String imgUrl;
    private Double rating;
    private Integer ratingsNumber;
    private String url;
    private Double price;
    private Double salePrice;
    private String polarity;
    private Integer voltage;
    private Integer inrushCurrent;
    private Integer capacity;
    private String typeOfVehicle;
    private String batteryTechnology;
    private Boolean isServiced;
    private String features;
    private String klemmy;
    private String caseType;
    private String batteryMarking;
    private String additional;
    private Double length;
    private Double width;
    private Double height;
    private Double weight;
    private Integer serviceLife;
    private Integer warrantyPeriod;
}
