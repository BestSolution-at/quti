package at.bestsolution.quti.client.jdkhttp.impl;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;

public class Utils {
    public static String stringify(JsonValue object, boolean pretty) {
        var writer = new StringWriter();
        var generator = Json.createGeneratorFactory(Map.of(JsonGenerator.PRETTY_PRINTING, Boolean.TRUE))
                .createGenerator(writer);
        generator.write(object);
        generator.close();
        return writer.toString();
    }

    public static <T> List<T> mapObjects(JsonObject object, String property, Function<JsonObject, T> mapper) {
        if (object.containsKey(property)) {
            return object.getJsonArray(property).stream()
                    .map(JsonValue::asJsonObject)
                    .map(mapper)
                    .collect(Collectors.toUnmodifiableList());
        }
        return List.of();
    }

    public static <T> List<T> mapObjects(JsonArray object, Function<JsonObject, T> mapper) {
        return object.stream()
                .map(JsonValue::asJsonObject)
                .map(mapper)
                .collect(Collectors.toUnmodifiableList());
    }

    public static <T> List<T> mapStrings(JsonObject object, String property, Function<String, T> mapper) {
        if (object.containsKey(property)) {
            return object.getJsonArray(property)
                    .getValuesAs(JsonString.class)
                    .stream()
                    .map(JsonString::getString)
                    .map(mapper)
                    .collect(Collectors.toUnmodifiableList());
        }
        return List.of();
    }

    public static List<String> mapStrings(JsonObject object, String property) {
        if (object.containsKey(property)) {
            return object.getJsonArray(property)
                    .getValuesAs(JsonString.class)
                    .stream()
                    .map(JsonString::getString)
                    .collect(Collectors.toUnmodifiableList());
        }
        return List.of();
    }

    public static Collector<String, ?, JsonArray> toStringArray() {
        return Collector.of(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add,
                JsonArrayBuilder::build);
    }

    public static Collector<JsonValue, ?, JsonArray> toArray() {
        return Collector.of(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::add,
                JsonArrayBuilder::build);
    }

    public static <T> Collector<T, ?, JsonArray> toArray(Function<T, JsonValue> jsonValueConverter) {
        return Collector.of(Json::createArrayBuilder, (b, v) -> jsonValueConverter.apply(v), JsonArrayBuilder::add,
                JsonArrayBuilder::build);
    }

}
