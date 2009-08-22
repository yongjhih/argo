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

public final class JsonField {
    private final JsonStringNode name;
    private final JsonNode value;

    public JsonField(final JsonStringNode name, final JsonNode value) {
        this.name = name;
        this.value = value;
    }

    JsonStringNode getName() {
        return name;
    }

    JsonNode getValue() {
        return value;
    }
}
