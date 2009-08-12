/*
 * Copyright 2009 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.jdom;

import static argo.jdom.JsonNodeFactory.*;
import argo.saj.JsonListener;

import java.util.*;

final class JsonListenerToJdomAdapter implements JsonListener {

    private final Stack<MutableJsonNode> stack = new Stack<MutableJsonNode>();
    private JsonNodeGenerator document;

    public JsonRootNode getDocument() {
        return (JsonRootNode) document.generateJsonValue();
    }

    public void startDocument() {
        stack.push(new MutableJsonDocument());
    }

    public void endDocument() {
        document = (JsonNodeGenerator) stack.pop();
    }

    public void startArray() {
        final MutableJsonArray mutableJsonArray = new MutableJsonArray();
        stack.peek().addValue(mutableJsonArray);
        stack.push(mutableJsonArray);
    }

    public void endArray() {
        stack.pop();
    }

    public void startObject() {
        final MutableJsonObject jsonObject = new MutableJsonObject();
        stack.peek().addValue(jsonObject);
        stack.push(jsonObject);
    }

    public void endObject() {
        stack.pop();
    }

    public void numberValue(final String value) {
        stack.peek().addValue(JsonNodeFactory.aJsonNumber(value));
    }

    public void trueValue() {
        stack.peek().addValue(aJsonTrue());
    }

    public void falseValue() {
        stack.peek().addValue(aJsonFalse());
    }

    public void nullValue() {
        stack.peek().addValue(aJsonNull());
    }

    public void stringValue(final String value) {
        stack.peek().addValue(JsonNodeFactory.aJsonString(value));
    }

    public void startField(final String name) {
        final MutableJsonField field = new MutableJsonField(name);
        stack.peek().addField(field);
        stack.push(field);
    }

    public void endField() {
        stack.pop();
    }

    private static final class MutableJsonArray implements MutableJsonNode, JsonNodeGenerator {
        private final List<Object> elements = new LinkedList<Object>();

        public void addValue(final Object element) {
            elements.add(element);
        }

        public void addField(final MutableJsonField field) {
            throw new RuntimeException("Coding failure in Argo:  Attempt to add a field [" + field + "] to a MutableJsonArray.");
        }

        List<Object> getElements() {
            return elements;
        }

        public JsonNode generateJsonValue() {
            final List<JsonNode> jsonNodeElements = new ArrayList<JsonNode>(elements.size());
            for (Object element : elements) {
                jsonNodeElements.add(JsonListenerToJdomAdapter.generateJsonValue(element));
            }
            return JsonNodeFactory.aJsonArray(jsonNodeElements);
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("MutableJsonArray elements:[")
                    .append(elements)
                    .append("]")
                    .toString();
        }
    }

    private static final class MutableJsonObject implements MutableJsonNode, JsonNodeGenerator {
        private final List<MutableJsonField> fields = new LinkedList<MutableJsonField>();

        public void addField(final MutableJsonField field) {
            fields.add(field);
        }

        public void addValue(final Object value) {
            throw new RuntimeException("Coding failure in Argo:  Attempt to add a value [" + value + "] to a MutableJsonObject.");
        }

        public List<MutableJsonField> getFields() {
            return fields;
        }

        public JsonNode generateJsonValue() {
            final Map<JsonStringNode, JsonNode> jsonStringJsonValueHashMap = new HashMap<JsonStringNode, JsonNode>();
            for (MutableJsonField field : fields) {
                jsonStringJsonValueHashMap.put(aJsonString(field.getName()), JsonListenerToJdomAdapter.generateJsonValue(field.getValue()));
            }
            return aJsonObject(jsonStringJsonValueHashMap);
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("MutableJsonObject fields:[")
                    .append(fields)
                    .append("]")
                    .toString();
        }
    }

    private static final class MutableJsonField implements MutableJsonNode, JsonNodeGenerator {
        final String name;
        Object value;

        MutableJsonField(final String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }

        Object getValue() {
            return value;
        }

        public void addValue(final Object value) {
            this.value = value;
        }

        public void addField(final MutableJsonField field) {
            throw new RuntimeException("Coding failure in Argo:  Attempt to add a field [" + field + "] to a MutableJsonField.");
        }

        public JsonNode generateJsonValue() {
            return JsonListenerToJdomAdapter.generateJsonValue(value);
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("MutableJsonField name:[")
                    .append(name)
                    .append("], value:[")
                    .append(value)
                    .append("]")
                    .toString();
        }
    }

    private static final class MutableJsonDocument implements MutableJsonNode, JsonNodeGenerator {
        Object value;

        public void addValue(final Object value) {
            this.value = value;
        }

        public void addField(final MutableJsonField field) {
            throw new RuntimeException("Coding failure in Argo:  Attempt to add a field [" + field + "] to a MutableJsonDocument.");
        }

        Object getValue() {
            return value;
        }

        public JsonNode generateJsonValue() {
            return JsonListenerToJdomAdapter.generateJsonValue(value);
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append("MutableJsonDocument value:[")
                    .append(value)
                    .append("]")
                    .toString();
        }
    }


    private static interface MutableJsonNode {
        void addValue(Object value);

        void addField(MutableJsonField field);
    }

    private static interface JsonNodeGenerator {
        JsonNode generateJsonValue();
    }

    private static JsonNode generateJsonValue(final Object value) {
        final JsonNode result;
        if (value instanceof JsonNode) {
            result = (JsonNode) value;
        } else {
            result = ((JsonNodeGenerator) value).generateJsonValue();
        }
        return result;
    }
}
