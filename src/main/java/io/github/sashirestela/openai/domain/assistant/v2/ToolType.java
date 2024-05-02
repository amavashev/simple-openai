package io.github.sashirestela.openai.domain.assistant.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ToolType {

    @JsonProperty("code_interpreter")
    CODE_INTERPRETER,

    @JsonProperty("file_search")
    FILE_SEARCH,

    @JsonProperty("function")
    FUNCTION;

}
