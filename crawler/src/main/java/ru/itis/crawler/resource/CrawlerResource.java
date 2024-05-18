package ru.itis.crawler.resource;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.openapi.annotations.tags.Tags;
import ru.itis.crawler.service.CrawlerService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Tag(name = "Linked records API")
@RequiredArgsConstructor
//@Path("/crawler")
@Path("/linked-records")
public class CrawlerResource {

    private final CrawlerService crawlerService;

    @GET
    public Response parse(@QueryParam("url") String url, @QueryParam("quantity") Integer quantity) {
        crawlerService.parse(url, quantity);
        return Response.ok().build();
    }

    @Operation(summary = "Получение связанных записей")
    @GET
    public Response getLinkedRecords(String date) {
        return null;
    }

    @Operation(summary = "Получение плоского отчёта из связанных записей")
    @GET
    @Path("/report")
    public Response getLinkedRecordsReport() {
        return null;
    }
}
