package ru.itis.crawler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import ru.itis.crawler.kafka.dto.ParsedDTO;
import ru.itis.crawler.parser.WebDriverParser;
import ru.itis.crawler.utils.CrawlerUtils;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.itis.crawler.utils.CrawlerUtils.strToDouble;
import static ru.itis.crawler.utils.CrawlerUtils.strToInteger;

@RequiredArgsConstructor
@ApplicationScoped
@Slf4j
public class YandexCrawlerService {

    private final WebDriverParser webDriverParser;

    public Set<String> getLinks(String url, Integer quantity) {
        Set<String> links = new HashSet<>();
        var page = 1;
        while (true) {
            var document = webDriverParser.getPageWithScrolling(url + "?page=" + page++);
            var pageLinks = getItemElements(document).stream()
                    .map(e -> e.attr("href"))
                    .map(e -> StringUtils.startsWith(e, "/") ? e : e + "/")
                    .map(e -> CrawlerUtils.BASE_URL + e)
                    .map(link -> StringUtils.contains(link, "?") ? StringUtils.substringBefore(link, "?") : link)
                    .map(link -> link + "/spec?track=char")
                    .collect(Collectors.toList());
            links.addAll(pageLinks);
            if (links.size() >= quantity || pageLinks.isEmpty()) return links;
        }
    }

    public ParsedDTO getItem(String url, LocalDateTime crawlingStart) {
        var document = webDriverParser.getPage(url);
        var ratingText = getElementTextByAttr(document, "data-zone-name", "Stars");
        var isServiced = getElementTextByIdAndTag(document, "obsluzhivanie");
        var typeSize = getElementTextById(document, "tiporazmer");

        var parsedDto = ParsedDTO.builder()
                .crawling_start(crawlingStart)
                .crawling_date(LocalDateTime.now())
                .source(CrawlerUtils.SOURCE)
                .name(getElementTextByAttr(document, "data-baobab-name", "title"))
                .description(getElementTextByAttr(document, "data-auto", "full-description-text"))
                .imgUrl(getImgUrl(document))
                .rating(strToDouble(StringUtils.substringBefore(ratingText, "(")))
                .ratingsNumber(strToInteger(StringUtils.substringBetween(ratingText, "(", ")")))
                .url(url)
                .polarity(getElementTextById(document, "poliarnost"))
                .voltage(strToInteger(getElementTextById(document, "napriazhenie")))
                .inrushCurrent(strToInteger(getElementTextById(document, "puskovoi tok")))
                .capacity(strToInteger(getElementTextById(document, "emkost")))
                .typeOfVehicle(getElementTextByIdAndTag(document, "tip avtotekhniki"))
                .batteryTechnology(getElementTextById(document, "tekhnologiia akkumuliatora"))
                .isServiced(StringUtils.equalsIgnoreCase(isServiced, "обслуживаемый"))
                .features(getElementTextById(document, "osobennosti"))
                .klemmy(getElementTextById(document, "klemmy"))
                .caseType(getElementTextByIdAndTag(document, "tip korpusa"))
                .batteryMarking(getElementTextByIdAndTag(document, "markirovka motoakkumuliatora"))
                .additional(getElementTextByIdAndTag(document, "dopolnitelnaia informatsiia"))
                .length(strToDouble(StringUtils.substringBefore(typeSize, "х")))
                .width(strToDouble(StringUtils.substringBetween(typeSize, "х", "х")))
                .height(strToDouble(StringUtils.substringAfterLast(typeSize, "х")))
                .weight(strToDouble(getElementTextById(document, "ves")))
                .serviceLife(strToInteger(getElementTextByIdAndTag(document, "srok sluzhby")))
                .warrantyPeriod(strToInteger(getElementTextById(document, "garantiinyi srok")))
                .build();
        setPrice(parsedDto, document);

        return parsedDto;
    }

    private void setPrice(ParsedDTO parsed, Optional<Document> document) {
        var price = strToDouble(getElementTextByAttr(document, "data-auto", "snippet-price-current"));
        var oldPrice = Optional.ofNullable(getElementTextByAttr(document, "data-auto", "snippet-price-old"))
                .map(p -> StringUtils.substringBefore(p, "₽"))
                .map(CrawlerUtils::strToDouble)
                .orElse(null);

        if (Objects.nonNull(oldPrice)) {
            parsed.setPrice(oldPrice);
            parsed.setSalePrice(price);
        } else {
            parsed.setPrice(price);
        }
    }

    private List<Element> getItemElements(Optional<Document> document) {
        var linkElements = document.map(doc -> doc.getElementsByAttributeValue("data-auto", "snippet-link").stream())
                .orElse(Stream.empty())
                .collect(Collectors.toList());
        if (linkElements.isEmpty()) {
            linkElements = document.map(d -> d.getElementsByAttributeValue("data-zone-name", "title").stream())
                    .orElse(Stream.empty())
                    .map(e -> e.getElementsByTag("a").first())
                    .collect(Collectors.toList());
        }
        return linkElements;
    }

    private String getElementTextByAttr(Optional<Document> document, String attr, String value) {
        return document.map(doc -> doc.getElementsByAttributeValue(attr, value).first())
                .map(Element::text)
                .orElse(null);
    }

    private String getElementTextById(Optional<Document> document, String id) {
        return document.map(doc -> doc.getElementById(id))
                .map(e -> e.getElementsByClass("cia-vs").first())
                .map(Element::text)
                .orElse(null);
    }

    private String getElementTextByIdAndTag(Optional<Document> document, String id) {
        return document.map(doc -> doc.getElementById(id))
                .map(e -> e.getElementsByTag("dd").first())
                .map(Element::text)
                .orElse(null);
    }

    private String getImgUrl(Optional<Document> document) {
        return document.map(doc -> doc.getElementsByAttributeValue("data-zone-name", "picture").first())
                .map(e -> e.getElementsByTag("img").first())
                .map(e -> e.attr("srcset"))
                .map(src -> StringUtils.substringBetween(src, ", ", " "))
                .orElse(null);
    }
}
