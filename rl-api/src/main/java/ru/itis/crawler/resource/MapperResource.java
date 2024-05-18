package ru.itis.crawler.resource;

import lombok.RequiredArgsConstructor;
import ru.itis.crawler.service.PostprocessService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@RequiredArgsConstructor
@Path("/mapper")
public class MapperResource {

    private final PostprocessService mapperService;

    @GET
    public Response map() {
        mapperService.postprocess();
        return Response.ok().build();
    }
}
