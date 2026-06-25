package ru.vych.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vych.http.config.HttpClientBuilder;
import ru.vych.http.config.HttpClientConfig;
import ru.vych.http.impl.HttpClient;
import ru.vych.http.impl.exceptions.HttpClientException;

import java.util.Map;

import static ru.vych.http.TestServerConfiguration.TEST_SERVER_URI;

@Configuration
public class TestServerHttpClientConfiguration {
    public static final String CLIENT_NAME = "TestServerHttpClient";

    @Bean(name = CLIENT_NAME)
    public HttpClient client(HttpClientBuilder builder) throws HttpClientException {
        HttpClientConfig config = new HttpClientConfig()
                .setRoot(TEST_SERVER_URI)
                .setTimeout(15000)
                .setStoreCookies(true);
        return builder.build(config);
    }
}
