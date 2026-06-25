package ru.vych.http;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.Map;
import java.util.stream.Collectors;

import static ru.vych.http.TestController.TEST_CONTROLLER_PATH;

@Path(TEST_CONTROLLER_PATH)
public class TestController {
    public static final String TEST_CONTROLLER_PATH = "/test";

    public static final String UUID_PARAM_KEY = "uuid";

    public static final String GET_HELLO_ENDPOINT = "/getHelloWorld";
    public static final String GET_QUERY_ENDPOINT = "/getQuery";
    public static final String GET_MANY_QUERY_ENDPOINT = "/getManyQuery";
    public static final String GET_PATH_ENDPOINT = "/getQuery";
    public static final String GET_PATH_AND_QUERY_ENDPOINT = "/getPathNQuery";

    public static final String HELLO_TEXT = "Hello, World!";

    @GET
    @Path(GET_HELLO_ENDPOINT)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getHello() {
        return Response.ok().entity(HELLO_TEXT).build();
    }

    @GET
    @Path(GET_QUERY_ENDPOINT)
    @Produces(MediaType.TEXT_PLAIN)
    public Response getQuery(@QueryParam(UUID_PARAM_KEY) String uuid) {
        return Response.ok().entity(uuid).build();
    }

    @GET
    @Path(GET_MANY_QUERY_ENDPOINT)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManyQuery(@Context UriInfo uriInfo) {
        Map<String, String> params =
                uriInfo.getQueryParameters()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> e.getValue().get(0)
                        ));
        return Response.ok().entity(params).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path(GET_PATH_ENDPOINT + "/{" + UUID_PARAM_KEY + "}")
    public Response getPath(@PathParam(UUID_PARAM_KEY) String uuid) {
        return Response.ok().entity(uuid).build();
    }

    @GET
    @Path(GET_PATH_AND_QUERY_ENDPOINT + "/{" + UUID_PARAM_KEY + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPathAndQuery(@PathParam(UUID_PARAM_KEY) String key, @QueryParam(UUID_PARAM_KEY) String value) {
        return Response.ok().entity(Map.of(key, value)).build();
    }
}
