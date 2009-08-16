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

import static argo.jdom.JsonFieldBuilder.aJsonFieldBuilder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public final class JsonObjectBuilder implements JsonNodeBuilder<JsonRootNode> {

    private final List<JsonFieldBuilder> fieldBuilders = new LinkedList<JsonFieldBuilder>();

    JsonObjectBuilder() {}

    public JsonObjectBuilder withField(final String name, final JsonNodeBuilder value) {
        return withField(JsonNodeBuilders.aJsonString(name), value);
    }

    public JsonObjectBuilder withField(final JsonNodeBuilder<JsonStringNode> name, final JsonNodeBuilder value) {
        return withFieldBuilder(aJsonFieldBuilder().withKey(name).withValue(value));
    }

    public JsonObjectBuilder withFieldBuilder(final JsonFieldBuilder jsonFieldBuilder) {
        fieldBuilders.add(jsonFieldBuilder);
        return this;
    }

    public JsonRootNode build() {
        final HashMap<JsonStringNode, JsonNode> fields = new HashMap<JsonStringNode, JsonNode>();
        for (JsonFieldBuilder fieldBuilder : fieldBuilders) {
            fields.put(fieldBuilder.buildKey(), fieldBuilder.buildValue());
        }
        return JsonNodeFactories.aJsonObject(fields);
    }
}
