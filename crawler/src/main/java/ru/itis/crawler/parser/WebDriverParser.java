package ru.itis.crawler.parser;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import ru.itis.crawler.utils.CrawlerUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class WebDriverParser {

    private static final int WAIT_LOAD_MILLIS = 2000;

    private WebDriver webDriver;

    @PostConstruct
    void init() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\HP\\Downloads\\chromedriver-124\\chromedriver-win64\\chromedriver.exe");
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        webDriver = new ChromeDriver();
    }

    @PreDestroy
    void destroy() {
        webDriver.quit();
    }

    @Retry
    @Fallback(fallbackMethod = "fallback")
    public Optional<Document> getPage(String url) {
        log.info("Get page: {}", url);
        webDriver.get(url);
        CrawlerUtils.sleep(WAIT_LOAD_MILLIS);

        return Optional.of(Jsoup.parse(webDriver.getPageSource()));
    }

    @Retry
    @Fallback(fallbackMethod = "fallback")
    public Optional<Document> getPageWithScrolling(String url) {
        log.info("Get page: {}", url);
        webDriver.get(url);
        CrawlerUtils.sleep(WAIT_LOAD_MILLIS);

        var footer = webDriver.findElement(By.cssSelector("[data-auto='pager-more']"));
        new Actions(webDriver)
                .scrollByAmount(0, footer.getRect().y)
                .perform();

        return Optional.of(Jsoup.parse(webDriver.getPageSource()));
    }

    public Map<String, String> getCookies(String url) {
        Map<String, String> cookies = new HashMap<>();
        webDriver.get(url);
        webDriver.manage().getCookies().stream().forEach(c -> cookies.put(c.getName(), c.getValue()));
        return cookies;
    }

    public Optional<Document> fallback(String url) {
        log.info("Failed load document from url: {}", url);
        return Optional.empty();
    }
}
