package argo.saj;

import argo.jdom.JsonNode;
import argo.jdom.JsonNodeFactory;
import static argo.jdom.JsonNodeFactory.aJsonNumber;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class JsonArrayTest {

    @Test
    public void testImmutablility() {
        final JsonNode baseJsonNode = aJsonNumber("0");
        final List<JsonNode> baseElements = new LinkedList<JsonNode>(Arrays.<JsonNode>asList(baseJsonNode));
        final JsonNode jsonArray = JsonNodeFactory.aJsonArray(baseElements);
        assertEquals(1, jsonArray.getElements().size());
        assertEquals(baseJsonNode, jsonArray.getElements().get(0));
        baseElements.add(aJsonNumber("1"));
        assertEquals(1, jsonArray.getElements().size());
        assertEquals(baseJsonNode, jsonArray.getElements().get(0));
        jsonArray.getElements().add(aJsonNumber("1"));
        assertEquals(1, jsonArray.getElements().size());
        assertEquals(baseJsonNode, jsonArray.getElements().get(0));
    }

    @Test
    public void testEquals() {
        assertEquals(JsonNodeFactory.aJsonArray(new LinkedList<JsonNode>()), JsonNodeFactory.aJsonArray(new LinkedList<JsonNode>()));
        assertEquals(JsonNodeFactory.aJsonArray(Collections.<JsonNode>singletonList(aJsonNumber("0"))), JsonNodeFactory.aJsonArray(Collections.<JsonNode>singletonList(aJsonNumber("0"))));
        assertFalse(JsonNodeFactory.aJsonArray(Collections.<JsonNode>singletonList(aJsonNumber("0"))).equals(JsonNodeFactory.aJsonArray(Collections.<JsonNode>singletonList(aJsonNumber("1")))));
    }

    @Test
    public void testHashCode() {
        assertEquals(JsonNodeFactory.aJsonArray(new LinkedList<JsonNode>()).hashCode(), JsonNodeFactory.aJsonArray(new LinkedList<JsonNode>()).hashCode());
        assertEquals(JsonNodeFactory.aJsonArray(Collections.<JsonNode>singletonList(aJsonNumber("0"))).hashCode(), JsonNodeFactory.aJsonArray(Collections.<JsonNode>singletonList(aJsonNumber("0"))).hashCode());
    }

    @Test
    public void testToString() {
        JsonNodeFactory.aJsonArray(Collections.<JsonNode>singletonList(aJsonNumber("0"))).toString();
    }
}
