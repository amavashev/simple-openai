package io.github.sashirestela.openai.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.exception.UncheckedException;

public class JsonUtilTest {

  @Test
  void shouldConvertObjectToJsonWhenClassHasNoIssues() {
    TestClass object = new TestClass("test", 10);
    String actualJson = JsonUtil.get().objectToJson(object);
    String expectedJson = "{\"first\":\"test\",\"second\":10}";
    assertEquals(expectedJson, actualJson);
  }

  @Test
  void shouldThrowExceptionWhenConvertingAnObjectOfClassWithIssues() {
    FailClass object = new FailClass("test", 10);
    assertThrows(UncheckedException.class, () -> JsonUtil.get().objectToJson(object));
  }

  @Test
  void shouldConvertJsonToObjectWhenJsonHasNoIssues() {
    String json = "{\"first\":\"test\",\"second\":10}";
    TestClass actualObject = JsonUtil.get().jsonToObject(json, TestClass.class);
    TestClass expectedObject = new TestClass("test", 10);
    assertEquals(expectedObject.getFirst(), actualObject.getFirst());
    assertEquals(expectedObject.getSecond(), actualObject.getSecond());
  }

  @Test
  void shouldThrowExceptionWhenConvertingJsonToObjectWithIssues() {
    String json = "{\"first\":\"test\",\"secondish\":10}";
    assertThrows(UncheckedException.class, () -> JsonUtil.get().jsonToObject(json, TestClass.class));
  }

  @Test
  void shouldConvertJsonToListWhenJsonHasNoIssues() {
    String json = "[{\"first\":\"test1\",\"second\":10},{\"first\":\"test2\",\"second\":20}]";
    List<TestClass> actualList = JsonUtil.get().jsonToList(json, TestClass.class);
    List<TestClass> expectedList = Arrays.asList(
        new TestClass("test1", 10),
        new TestClass("test2", 20));
    assertEquals(expectedList.size(), actualList.size());
    assertEquals(expectedList.get(0).getFirst(), actualList.get(0).getFirst());
    assertEquals(expectedList.get(0).getSecond(), actualList.get(0).getSecond());
    assertEquals(expectedList.get(1).getFirst(), actualList.get(1).getFirst());
    assertEquals(expectedList.get(1).getSecond(), actualList.get(1).getSecond());
  }

  @Test
  void shouldThrowExceptionWhenConvertingJsonToListWithIssues() {
    String json = "[{\"first\":\"test1\",\"second\":10},{\"firstish\":\"test2\",\"secondish\":20}]";
    assertThrows(UncheckedException.class, () -> JsonUtil.get().jsonToList(json, TestClass.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  void shouldConvertJsonToParametricObjectWhenJsonHasNoIssues() {
    String json = "{\"id\":\"abc\",\"data\":[{\"first\":\"test1\",\"second\":10},{\"first\":\"test2\",\"second\":20}]}";
    TestGeneric<TestClass> actualObject = JsonUtil.get().jsonToParametricObject(json, TestGeneric.class,
        TestClass.class);
    List<TestClass> actualList = actualObject.getData();
    List<TestClass> expectedList = Arrays.asList(
        new TestClass("test1", 10),
        new TestClass("test2", 20));
    TestGeneric<TestClass> expectedObject = new TestGeneric<>("abc", expectedList);

    assertEquals(expectedObject.getId(), actualObject.getId());
    assertEquals(expectedList.size(), actualList.size());
    assertEquals(expectedList.get(0).getFirst(), actualList.get(0).getFirst());
    assertEquals(expectedList.get(0).getSecond(), actualList.get(0).getSecond());
    assertEquals(expectedList.get(1).getFirst(), actualList.get(1).getFirst());
    assertEquals(expectedList.get(1).getSecond(), actualList.get(1).getSecond());
  }

  @Test
  void shouldThrowExceptionWhenConvertingJsonToParametricObjectWithIssues() {
    String json = "{\"id\":\"abc\",\"data\":[{\"first\":\"test1\",\"second\":10},{\"firstish\":\"test2\",\"secondish\":20}]}";
    assertThrows(UncheckedException.class,
        () -> JsonUtil.get().jsonToParametricObject(json, TestGeneric.class, TestClass.class));
  }

  @Test
  void shouldGenerateFullJsonSchemaWhenClassHasSomeFields() {
    String actualJsonSchema = JsonUtil.get().classToJsonSchema(TestClass.class).toString();
    String expectedJsonSchema = "{\"type\":\"object\",\"properties\":{\"first\":{\"type\":\"string\"},\"second\":{\"type\":\"integer\"}},\"required\":[\"first\"]}";
    assertEquals(expectedJsonSchema, actualJsonSchema);
  }

  @Test
  void shouldGenerateEmptyJsonSchemaWhenClassHasNoFields() {
    String actualJsonSchema = JsonUtil.get().classToJsonSchema(EmptyClass.class).toString();
    String expectedJsonSchema = Constant.JSON_EMPTY_CLASS;
    assertEquals(expectedJsonSchema, actualJsonSchema);
  }

  static class TestClass {
    @JsonProperty(required = true)
    public String first;
    public Integer second;

    public TestClass() {
    }

    public TestClass(String first, Integer second) {
      this.first = first;
      this.second = second;
    }

    public String getFirst() {
      return first;
    }

    public Integer getSecond() {
      return second;
    }
  }

  static class TestGeneric<T> {
    private String id;
    private List<T> data;

    public TestGeneric() {
    }

    public TestGeneric(String id, List<T> data) {
      this.id = id;
      this.data = data;
    }

    public String getId() {
      return id;
    }

    public List<T> getData() {
      return data;
    }

  }

  static class FailClass {
    private String first;
    private Integer second;

    public FailClass() {
    }

    public FailClass(String first, Integer second) {
      this.first = first;
      this.second = second;
    }
  }

  static class EmptyClass {
  }
}
