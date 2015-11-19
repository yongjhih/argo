/*
 * Copyright 2015 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.jdom;

import argo.saj.JsonListener;

import java.util.Stack;

import static argo.jdom.JsonNodeBuilders.*;
import static argo.jdom.JsonNodeFactories.field;

final class JsonListenerToJdomAdapter implements JsonListener {

    private final Stack<NodeContainer> stack = new Stack<NodeContainer>();
    private JsonNodeBuilder<JsonRootNode> root;

    JsonRootNode getDocument() {
        return root.build();
    }

    public void startDocument() {
    }

    public void endDocument() {
    }

    public void startArray() {
        final ArrayNodeContainer arrayNodeContainer = new ArrayNodeContainer();
        addRootNode(arrayNodeContainer);
        stack.push(arrayNodeContainer);
    }

    public void endArray() {
        stack.pop();
    }

    public void startObject() {
        final ObjectNodeContainer objectNodeContainer = new ObjectNodeContainer();
        addRootNode(objectNodeContainer);
        stack.push(objectNodeContainer);
    }

    public void endObject() {
        stack.pop();
    }

    public void startField(final String name) {
        final FieldNodeContainer fieldNodeContainer = new FieldNodeContainer(JsonNodeFactories.string(name));
        stack.peek().addField(fieldNodeContainer);
        stack.push(fieldNodeContainer);
    }

    public void endField() {
        stack.pop();
    }

    public void numberValue(final String value) {
        addValue(aNumberBuilder(value));
    }

    public void trueValue() {
        addValue(aTrueBuilder());
    }

    public void stringValue(final String value) {
        addValue(aStringBuilder(value));
    }

    public void falseValue() {
        addValue(aFalseBuilder());
    }

    public void nullValue() {
        addValue(aNullBuilder());
    }

    private void addRootNode(final JsonNodeBuilder<JsonRootNode> rootNodeBuilder) {
        if (root == null) {
            root = rootNodeBuilder;
        } else {
            addValue(rootNodeBuilder);
        }
    }

    private void addValue(final JsonNodeBuilder nodeBuilder) {
        stack.peek().addNode(nodeBuilder);
    }

    private static interface NodeContainer {

        void addNode(JsonNodeBuilder jsonNodeBuilder);

        void addField(JsonFieldBuilder jsonFieldBuilder);

    }

    private static final class ArrayNodeContainer implements NodeContainer, JsonNodeBuilder<JsonRootNode> {
        private final JsonArrayNodeBuilder arrayBuilder = anArrayBuilder();

        public void addNode(final JsonNodeBuilder jsonNodeBuilder) {
            arrayBuilder.withElement(jsonNodeBuilder);
        }

        public void addField(final JsonFieldBuilder jsonFieldBuilder) {
            throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
        }

        public JsonRootNode build() {
            return arrayBuilder.build();
        }
    }

    private static final class ObjectNodeContainer implements NodeContainer, JsonNodeBuilder<JsonRootNode> {
        private final JsonObjectNodeBuilder objectNodeBuilder = anObjectBuilder();

        public void addNode(final JsonNodeBuilder jsonNodeBuilder) {
            throw new RuntimeException("Coding failure in Argo:  Attempt to add a node to an object.");
        }

        public void addField(final JsonFieldBuilder jsonFieldBuilder) {
            objectNodeBuilder.withFieldBuilder(jsonFieldBuilder);
        }

        public JsonRootNode build() {
            return objectNodeBuilder.build();
        }
    }

    private static final class FieldNodeContainer implements NodeContainer, JsonFieldBuilder {
        private final JsonStringNode key;
        private JsonNodeBuilder valueBuilder;

        FieldNodeContainer(JsonStringNode key) {
            this.key = key;
        }

        public void addNode(final JsonNodeBuilder jsonNodeBuilder) {
            valueBuilder = jsonNodeBuilder;
        }

        public void addField(final JsonFieldBuilder jsonFieldBuilder) {
            throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to a field.");
        }

        public JsonStringNode buildKey() {
            return key;
        }

        public JsonField build() {
            if (valueBuilder == null) {
                throw new RuntimeException("Coding failure in Argo:  Attempt to create a field without a value.");
            } else {
                return field(buildKey(), valueBuilder.build());
            }
        }
    }
}
