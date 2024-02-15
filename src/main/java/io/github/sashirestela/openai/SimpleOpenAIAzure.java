package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.OpenAI.Assistants;
import io.github.sashirestela.openai.OpenAI.Audios;
import io.github.sashirestela.openai.OpenAI.ChatCompletions;
import io.github.sashirestela.openai.OpenAI.Completions;
import io.github.sashirestela.openai.OpenAI.Embeddings;
import io.github.sashirestela.openai.OpenAI.Files;
import io.github.sashirestela.openai.OpenAI.FineTunings;
import io.github.sashirestela.openai.OpenAI.Images;
import io.github.sashirestela.openai.OpenAI.Models;
import io.github.sashirestela.openai.OpenAI.Moderations;
import io.github.sashirestela.openai.OpenAI.Threads;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */
@Getter
public class SimpleOpenAIAzure implements BaseSimpleOpenAI {
    private static final String END_OF_STREAM = "[DONE]";

    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final CleverClient cleverClient;
    private ChatCompletions chatCompletionService;

    private static UnaryOperator<HttpRequestData> prepareRequestInterceptor(String apiVersion) {
        return request -> {
            var url = request.getUrl();
            var contentType = request.getContentType();
            var body = request.getBody();

            // add a query parameter to url
            url += (url.contains("?") ? "&" : "?") + "api-version=" + apiVersion;
            // remove '/vN' or '/vN.M' from url
            url = url.replaceFirst("(\\/v\\d+\\.*\\d*)", "");
            request.setUrl(url);

            if (contentType != null) {
                if (contentType.equals(ContentType.APPLICATION_JSON)) {
                    var bodyJson = (String) request.getBody();
                    // remove a field from body (as Json)
                    bodyJson = bodyJson.replaceFirst(",?\"model\":\"[^\"]*\",?", "");
                    bodyJson = bodyJson.replaceFirst("\"\"", "\",\"");
                    body = bodyJson;
                }
                if (contentType.equals(ContentType.MULTIPART_FORMDATA)) {
                    Map<String, Object> bodyMap = (Map<String, Object>) request.getBody();
                    // remove a field from body (as Map)
                    bodyMap.remove("model");
                    body = bodyMap;
                }
                request.setBody(body);
            }

            return request;
        };
    }


    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param baseUrl        The URL of the Azure OpenAI deployment.   Mandatory.
     * @param apiVersion     Azure OpeAI API version. See:
     *                       https://learn.microsoft.com/en-us/azure/ai-services/openai/reference#rest-api-versioning
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object.
     *                       One is created by default if not provided. Optional.
     */
    @Builder
    public SimpleOpenAIAzure(
        @NonNull String apiKey,
        @NonNull String baseUrl,
        @NonNull String apiVersion,
        HttpClient httpClient) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.httpClient = Optional.ofNullable(httpClient).orElse(HttpClient.newHttpClient());
        this.cleverClient = CleverClient.builder()
            .httpClient(this.httpClient)
            .baseUrl(this.baseUrl)
            .headers(Map.of("api-key", apiKey))
            .endOfStream(END_OF_STREAM)
            .requestInterceptor(prepareRequestInterceptor(apiVersion))
            .build();
    }

    @Override
    public Audios audios() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public ChatCompletions chatCompletions() {
        if (this.chatCompletionService == null) {
            this.chatCompletionService = cleverClient.create(OpenAI.ChatCompletions.class);
        }
        return this.chatCompletionService;
    }

    @Override
    public Completions completions() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Embeddings embeddings() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Files files() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public FineTunings fineTunings() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Images images() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Models models() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Moderations moderations() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Assistants assistants() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Threads threads() {
        throw new SimpleUncheckedException("Not implemented");
    }
}
