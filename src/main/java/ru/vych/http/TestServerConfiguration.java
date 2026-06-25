package ru.vych.http;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class TestServerConfiguration {
    public static String TEST_SERVER_URI = "http://localhost:9090";


    @Bean(initMethod = "start", destroyMethod = "shutdown")
    public HttpServer testServer() {

        ResourceConfig config =
                new ResourceConfig()
                        .packages("ru.vych");


        return GrizzlyHttpServerFactory.createHttpServer(
                URI.create(TEST_SERVER_URI),
                config,
                false
        );
    }
}
