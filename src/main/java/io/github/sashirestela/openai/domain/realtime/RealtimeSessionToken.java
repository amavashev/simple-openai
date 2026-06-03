package io.github.sashirestela.openai.domain.realtime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RealtimeSessionToken extends RealtimeSession {

    private Secret clientSecret;

    public Secret getClientSecretAtResponse() {
        return clientSecret;
    }

    @NoArgsConstructor
    @Getter
    @ToString
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Secret {

        private String value;
        private Long expiresAt;

    }

}
