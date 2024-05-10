package ru.itis.crawler.kafka;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import ru.itis.crawler.kafka.dto.ParsedDTO;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Slf4j
public class KafkaService {

    @Channel("parsed")
    Emitter<ParsedDTO> parsedEmitter;

    public void send(ParsedDTO parsedDTO) {
        try {
            parsedEmitter.send(parsedDTO);
        } catch (Exception e) {
            log.error("Failed send to kafka: {}", parsedDTO, e);
        }
    }
}
