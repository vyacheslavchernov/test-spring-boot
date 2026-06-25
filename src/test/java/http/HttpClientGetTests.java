package http;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import ru.vych.common.RandomUtils;
import ru.vych.http.impl.HttpMethod;
import ru.vych.http.impl.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.vych.http.TestController.*;

public class HttpClientGetTests extends BaseHttpTest {
    @Test
    @SneakyThrows
    public void getWithoutParamsTest() {
        var rq = Request.builder()
                .setUrl(TEST_CONTROLLER_PATH + GET_HELLO_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .setResponseClass(String.class)
                .build();

        var rs = httpClient.execute(rq);
        checkResponseStatus(rs, 200);
        assertThat(rs.getBody())
                .describedAs("Текст полученный в ответе не соответствует ожидаемому")
                .isEqualTo(HELLO_TEXT);

    }

    @Test
    @SneakyThrows
    public void getWithQueryParamsTest() {
        var uuid = UUID.randomUUID();
        var rq = Request.builder()
                .setUrl(TEST_CONTROLLER_PATH + GET_QUERY_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .addQueryParam(UUID_PARAM_KEY, uuid.toString())
                .setResponseClass(String.class)
                .build();

        var rs = httpClient.execute(rq);
        checkResponseStatus(rs, 200);
        assertThat(rs.getBody())
                .isEqualTo(uuid.toString())
                .describedAs("UUID переданный в запросе и полученный в ответе не совпадают");

    }

    @Test
    @SneakyThrows
    public void getWithManyQueryParamsTest() {
        var params = new HashMap<String, String>();
        for (var i = 0; i < RandomUtils.inRange(3, 12); i++) {
            params.put(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        }

        var rq = Request.builder()
                .setUrl(TEST_CONTROLLER_PATH + GET_MANY_QUERY_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .setQueryParams(params)
                .setResponseClass(Map.class)
                .build();

        var rs = httpClient.execute(rq);
        checkResponseStatus(rs, 200);
        assertThat(rs.<Map<String, String>>getCastedBody())
                .describedAs("Данные в ответе не соответствуют ожидаемым")
                .containsExactlyEntriesOf(params);
    }

    @Test
    @SneakyThrows
    public void getWithBrokenQueryParamsTest() {
        var uuid = UUID.randomUUID();
        var rq = Request.builder()
                .setUrl(TEST_CONTROLLER_PATH + GET_MANY_QUERY_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .addQueryParam("","")
                .addQueryParam("a", "")
                .addQueryParam(null, null)
                .addQueryParam("b", null)
                .addQueryParam(UUID_PARAM_KEY, uuid.toString())
                .setResponseClass(Map.class)
                .build();

        var rs = httpClient.execute(rq);
        checkResponseStatus(rs, 200);
        assertThat(rs.<Map<String, String>>getCastedBody())
                .describedAs("UUID переданный в запросе и полученный в ответе не совпадают")
                .containsEntry(UUID_PARAM_KEY, uuid.toString());
    }

    @Test
    @SneakyThrows
    public void getWithPathParamsTest() {
        var uuid = UUID.randomUUID();
        var rq = Request.builder()
                .setUrl(TEST_CONTROLLER_PATH + GET_PATH_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .addPathParam(uuid.toString())
                .setResponseClass(String.class)
                .build();

        var rs = httpClient.execute(rq);
        checkResponseStatus(rs, 200);
        assertThat(rs.getBody())
                .describedAs("UUID переданный в запросе и полученный в ответе не совпадают")
                .isEqualTo(uuid.toString());

    }

    @Test
    @SneakyThrows
    public void getWithPathNQueryParamsTest() {
        var key = UUID.randomUUID();
        var uuid = UUID.randomUUID();
        var rq = Request.builder()
                .setUrl(TEST_CONTROLLER_PATH + GET_PATH_AND_QUERY_ENDPOINT)
                .setMethod(HttpMethod.GET)
                .addPathParam(key.toString())
                .addQueryParam(UUID_PARAM_KEY, uuid.toString())
                .setResponseClass(Map.class)
                .build();

        var rs = httpClient.execute(rq);

        checkResponseStatus(rs, 200);
        assertThat(rs.<Map<String, String>>getCastedBody())
                .describedAs("Ответ должен содержать только пару %s=%s", key, uuid)
                .containsExactly(
                        Map.entry(key.toString(), uuid.toString())
                );
    }
}
