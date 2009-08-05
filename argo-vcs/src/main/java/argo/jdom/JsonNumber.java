package argo.jdom;

import java.util.List;
import java.util.Map;

public final class JsonNumber implements JsonNode {

    private final String value;

    public JsonNumber(final String value) {
        if (value == null) {
            throw new NullPointerException("Attempt to construct a JsonNumber with a null value.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean hasText() {
        return true;
    }

    public String getText() {
        return getValue();
    }

    public boolean hasFields() {
        return false;
    }

    public Map<JsonString, JsonNode> getFields() {
        throw new RuntimeException("Attempt to get fields on a JsonNode without fields.");
    }

    public boolean hasElements() {
        return false;
    }

    public List<JsonNode> getElements() {
        throw new RuntimeException("Attempt to get elements on a JsonNode without elements.");
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        final JsonNumber thatJsonNumber = (JsonNumber) that;
        return this.value.equals(thatJsonNumber.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("JsonNumber value:[")
                .append(value)
                .append("]")
                .toString();
    }
}
