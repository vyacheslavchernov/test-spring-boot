package http;

import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vych.App;
import ru.vych.http.config.TestServerHttpClientConfiguration;
import ru.vych.http.impl.HttpClient;
import ru.vych.http.impl.Request;
import ru.vych.http.impl.Response;
import ru.vych.http.impl.exceptions.HttpClientException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
public abstract class BaseHttpTest {
    @Autowired
    @Qualifier(TestServerHttpClientConfiguration.CLIENT_NAME)
    protected HttpClient httpClient;

    @Step("Проверка статус-кода ответа")
    protected void checkResponseStatus(Response response, Integer... expected) {
        assertThat(response.getStatus()).isIn((Object[]) expected).describedAs("Invalid status code");
    }

    @Step("Отправка запроса")
    protected Response sendRequest(Request request) throws HttpClientException {
        return httpClient.execute(request);
    }

    @Step("Проверяем, что тело ответа содержит только ожидаемое значение")
    protected <T> void bodyEqualsTo(T body, T expected) {
        assertThat(body)
                .describedAs("Тело запроса не соответствует ожидаемому значению")
                .isEqualTo(expected);
    }

    @Step("Проверяем, что тело ответа содержит только ожидаемые байты")
    protected void bodyContainsExactlyBytes(byte[] body, byte[] expected) {
        assertThat(body)
                .describedAs("Байты теле ответа не соответствуют ожидаемым")
                .containsExactly(expected);
    }

    @Step("Проверяем, что тело ответа содержит только ожидаемые элементы")
    protected <T> void bodyContainsExactlyEntriesOf(Map<T, T> body, Map<T, T> expected) {
        assertThat(body)
                .describedAs("Map теле ответа не соответствует ожидаемой")
                .containsExactlyEntriesOf(expected);
    }

    @Step("Проверяем, что тело ответа содержит только ожидаемые элементы")
    protected <T> void bodyContainsExactlyElementsOf(List<T> body, List<T> expected) {
        assertThat(body)
                .describedAs("Map теле ответа не соответствует ожидаемой")
                .containsExactlyElementsOf(expected);
    }

    @Step("Проверяем, что тело ответа содержит ожидаемый элемент")
    protected <K, V> void bodyContainsEntry(Map<K, V> body, K key, V value) {
        assertThat(body)
                .describedAs("Map теле ответа не соответствует ожидаемой")
                .containsEntry(key, value);
    }

    @Step("Проверяем, что тело ответа содержит только ожидаемый элемент")
    protected <K, V> void bodyContainsExactlyEntry(Map<K, V> body, K key, V value) {
        assertThat(body)
                .describedAs("Map теле ответа не соответствует ожидаемой")
                .containsExactly(Map.entry(key, value));
    }

}
