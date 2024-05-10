package ru.itis.crawler.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.ws.rs.core.Response;

@RegisterForReflection
public class BadRequestCrawlerException extends CrawlerException {

    public BadRequestCrawlerException(String msg) {
        super(Response.Status.BAD_REQUEST, msg);
    }
}
