package http;

import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vych.common.RandomUtils;
import ru.vych.http.impl.HttpMethod;
import ru.vych.http.impl.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ru.vych.http.controllers.GetTestController.*;

@DisplayName("Тесты отправки GET запросов")
public class HttpClientGetTests extends BaseHttpTest {
    @Test
    @SneakyThrows
    @DisplayName("Тест отправки GET запроса без параметров")
    public void getWithoutParamsTest() {
        var rq = Request.builder()
                .setUrl(GET_CONTROLLER_PATH + GET_HELLO_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .setResponseClass(String.class)
                .build();

        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyEqualsTo(rs.getBody(), HELLO_TEXT);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки GET запроса с query параметром")
    public void getWithQueryParamsTest() {
        var uuid = UUID.randomUUID().toString();
        var rq = Request.builder()
                .setUrl(GET_CONTROLLER_PATH + GET_QUERY_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .addQueryParam(UUID_PARAM_KEY, uuid)
                .setResponseClass(String.class)
                .build();

        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyEqualsTo(rs.getBody(), uuid);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки GET запроса с несколькими query параметрами")
    public void getWithManyQueryParamsTest() {
        var params = RandomUtils.randomMap(15);
        var rq = Request.builder()
                .setUrl(GET_CONTROLLER_PATH + GET_MANY_QUERY_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .setQueryParams(params)
                .setResponseClass(Map.class)
                .build();

        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyContainsExactlyEntriesOf(rs.getCastedBody(), params);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки GET запроса с некорректными query параметрами")
    public void getWithBrokenQueryParamsTest() {
        var uuid = UUID.randomUUID().toString();
        var rq = Request.builder()
                .setUrl(GET_CONTROLLER_PATH + GET_MANY_QUERY_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .addQueryParam("", "")
                .addQueryParam("a", "")
                .addQueryParam(null, null)
                .addQueryParam("b", null)
                .addQueryParam(UUID_PARAM_KEY, uuid)
                .setResponseClass(Map.class)
                .build();

        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyContainsEntry(rs.getCastedBody(), UUID_PARAM_KEY, uuid);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки GET запроса с path параметром")
    public void getWithPathParamsTest() {
        var uuid = UUID.randomUUID().toString();
        var rq = Request.builder()
                .setUrl(GET_CONTROLLER_PATH + GET_PATH_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .addPathParam(uuid)
                .setResponseClass(String.class)
                .build();

        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyEqualsTo(rs.getBody(), uuid);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки GET запроса с path и query параметрами одновременно")
    public void getWithPathNQueryParamsTest() {
        var key = UUID.randomUUID().toString();
        var uuid = UUID.randomUUID().toString();
        var rq = Request.builder()
                .setUrl(GET_CONTROLLER_PATH + GET_PATH_AND_QUERY_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .addPathParam(key)
                .addQueryParam(UUID_PARAM_KEY, uuid)
                .setResponseClass(Map.class)
                .build();

        var rs = sendRequest(rq);

        checkResponseStatus(rs, 200);
        bodyContainsExactlyEntry(rs.getCastedBody(), key, uuid);
    }
}
