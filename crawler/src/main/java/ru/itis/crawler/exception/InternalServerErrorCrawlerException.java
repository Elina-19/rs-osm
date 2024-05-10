package ru.itis.crawler.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.ws.rs.core.Response;

@RegisterForReflection
public class InternalServerErrorCrawlerException extends CrawlerException {

    public InternalServerErrorCrawlerException(String msg) {
        super(Response.Status.INTERNAL_SERVER_ERROR, msg);
    }
}
