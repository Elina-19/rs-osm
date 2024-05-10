package ru.itis.crawler.exception.handler;

import ru.itis.crawler.api.response.ErrorDTO;
import ru.itis.crawler.exception.CrawlerException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CrawlerExceptionHandler implements ExceptionMapper<CrawlerException> {

    @Override
    public Response toResponse(CrawlerException e) {
        return Response.status(e.statusCode)
                .entity(ErrorDTO.builder()
                        .message(e.getMessage())
                        .build())
                .build();
    }
}
