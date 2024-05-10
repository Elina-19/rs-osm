package ru.itis.crawler.exception;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.ws.rs.core.Response;

@RegisterForReflection
public class CrawlerException extends RuntimeException {

    public final Response.Status statusCode;

    public CrawlerException(Response.Status statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }
}
