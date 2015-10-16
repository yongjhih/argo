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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static argo.jdom.ImmutableListFactories.immutableListOf;
import static java.util.Collections.unmodifiableMap;

final class JsonObject extends AbstractJsonObject {

    private final List<JsonField> fields;
    private transient Map<JsonStringNode, JsonNode> fieldMap;

    JsonObject(final Iterable<JsonField> fields) {
        this.fields = immutableListOf(fields);
    }

    @Override
    public Map<JsonStringNode, JsonNode> getFields() {
        if (fieldMap == null) {
            fieldMap = unmodifiableMap(new LinkedHashMap<JsonStringNode, JsonNode>(fields.size() * 4 / 3 + 1) {{
                for (final JsonField field : fields) {
                    put(field.getName(), field.getValue());
                }
            }});
        }
        return fieldMap;
    }

    @Override
    public List<JsonField> getFieldList() {
        return fields;
    }
}
