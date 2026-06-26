package http;

import jakarta.ws.rs.core.MediaType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vych.common.RandomUtils;
import ru.vych.http.entities.DummyDto;
import ru.vych.http.impl.HttpMethod;
import ru.vych.http.impl.Request;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.qameta.allure.Allure.step;
import static ru.vych.http.controllers.PostTestController.*;

@DisplayName("Тесты отправки POST запросов")
public class HttpClientPostTests extends BaseHttpTest {
    @Test
    @SneakyThrows
    @DisplayName("Тест отправки POST запроса с пустым телом")
    public void emptyPostTest() {
        var rq = Request.builder()
                .setUrl(POST_CONTROLLER_PATH + EMPTY_POST_ENDPOINT)
                .setMethod(HttpMethod.POST)
                .build();
        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyEqualsTo(rs.getBody(), null);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки POST запроса с телом в виде строки")
    public void stringPostTest() {
        var uuid = UUID.randomUUID().toString();
        var rq = Request.builder()
                .setUrl(POST_CONTROLLER_PATH + STRING_POST_ENDPOINT)
                .setMethod(HttpMethod.POST)
                .setPayload(uuid)
                .setContentType(MediaType.TEXT_PLAIN)
                .setResponseClass(String.class)
                .build();
        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyEqualsTo(rs.getBody(), uuid);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки POST запроса с телом в виде массива байт")
    public void bytesPostTest() {
        var bytes = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        var rq = Request.builder()
                .setUrl(POST_CONTROLLER_PATH + BYTES_POST_ENDPOINT)
                .setMethod(HttpMethod.POST)
                .setPayload(bytes)
                .setContentType(MediaType.APPLICATION_OCTET_STREAM)
                .setResponseClass(byte.class)
                .build();
        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyContainsExactlyBytes(rs.getRawBytes(), bytes);
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки POST запроса с телом в виде коллекций")
    public void collectionPostTest() {
        var listPayload = RandomUtils.randomList(15);
        var mapPayload = RandomUtils.randomMap(15);
        var rqBuilder = Request.builder()
                .setUrl(POST_CONTROLLER_PATH + JSON_POST_ENDPOINT)
                .setMethod(HttpMethod.POST)
                .setContentType(MediaType.APPLICATION_JSON);

        step("Проверка List", () -> {
           rqBuilder.setPayload(listPayload).setResponseClass(List.class);
            var rs = sendRequest(rqBuilder.build());
            checkResponseStatus(rs, 200);
            bodyContainsExactlyElementsOf(rs.getCastedBody(), listPayload);
        });

        step("Проверка Map", () -> {
            rqBuilder.setPayload(mapPayload).setResponseClass(Map.class);
            var rs = sendRequest(rqBuilder.build());
            checkResponseStatus(rs, 200);
            bodyContainsExactlyEntriesOf(rs.getCastedBody(), mapPayload);
        });
    }

    @Test
    @SneakyThrows
    @DisplayName("Тест отправки POST запроса с телом в DTO")
    public void dtoPostTest() {
        var dummy = DummyDto.getDummy();
        var rq = Request.builder()
                .setUrl(POST_CONTROLLER_PATH + JSON_POST_ENDPOINT)
                .setMethod(HttpMethod.POST)
                .setPayload(dummy)
                .setContentType(MediaType.APPLICATION_JSON)
                .setResponseClass(DummyDto.class)
                .build();
        var rs = sendRequest(rq);
        checkResponseStatus(rs, 200);
        bodyEqualsTo(rs.getCastedBody(), dummy);
    }
}
