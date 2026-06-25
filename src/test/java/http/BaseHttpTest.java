package http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.vych.App;
import ru.vych.http.TestServerHttpClientConfiguration;
import ru.vych.http.impl.HttpClient;
import ru.vych.http.impl.Response;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = App.class)
public abstract class BaseHttpTest {
    @Autowired
    @Qualifier(TestServerHttpClientConfiguration.CLIENT_NAME)
    protected HttpClient httpClient;

    protected void checkResponseStatus(Response response, Integer... expected) {
        assertThat(response.getStatus()).isIn((Object[]) expected).describedAs("Invalid status code");
    }
}
