package ru.itis.crawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import ru.itis.crawler.exception.BadRequestCrawlerException;
import ru.itis.crawler.kafka.KafkaService;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@ApplicationScoped
@Slf4j
public class CrawlerService {

    private final String DEFAULT_URL = "https://market.yandex.ru/catalog/avtomobilnye-akkumuliatory-54856";

    @ConfigProperty(name = "crawler.items.limit")
    Integer itemsLimit;

    private final KafkaService kafkaService;
    private final YandexCrawlerService yandexCrawlerService;

    // TODO: написать документацию в сваггере, сделать валидатор (не забыть зависимость с hibernate-validator)
    public void parse(String url, Integer quantity) {
        var crawlingStart = LocalDateTime.now();
        validate(quantity);
        url = StringUtils.isBlank(url) ? DEFAULT_URL : url;
        var links = yandexCrawlerService.getLinks(url, quantity);
        log.info("Start process {} links", links.size());
        links.stream()
                .map(link -> yandexCrawlerService.getItem(link, crawlingStart))
                .forEach(kafkaService::send);
    }

    private void validate(Integer quantity) {
        if (quantity < 1 || quantity > itemsLimit) {
            throw new BadRequestCrawlerException(String.format("Quantity should be in range: [1, %d]", itemsLimit));
        }
    }
}
