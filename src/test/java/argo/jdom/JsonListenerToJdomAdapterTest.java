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

import static argo.jdom.JsonNodeFactories.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

public final class JsonListenerToJdomAdapterTest {

    @Test
    public void simpleStringObjectHappyCase() throws Exception {
        final JsonListenerToJdomAdapter jsonListenerToJdomAdapter = new JsonListenerToJdomAdapter();
        jsonListenerToJdomAdapter.startDocument();
        jsonListenerToJdomAdapter.startObject();
        jsonListenerToJdomAdapter.startField("Hello");
        jsonListenerToJdomAdapter.stringValue("World");
        jsonListenerToJdomAdapter.endField();
        jsonListenerToJdomAdapter.endObject();
        jsonListenerToJdomAdapter.endDocument();
        final JsonNode expected = aJsonObject(new HashMap<JsonStringNode, JsonNode>() {{
            put(aJsonString("Hello"), aJsonString("World"));
        }});
        assertThat(jsonListenerToJdomAdapter.getDocument(), equalTo(expected));
    }

    @Test
    public void simpleNumberObjectHappyCase() throws Exception {
        final JsonListenerToJdomAdapter jsonListenerToJdomAdapter = new JsonListenerToJdomAdapter();
        jsonListenerToJdomAdapter.startDocument();
        jsonListenerToJdomAdapter.startObject();
        jsonListenerToJdomAdapter.startField("Gigawatts");
        jsonListenerToJdomAdapter.numberValue("1.21");
        jsonListenerToJdomAdapter.endField();
        jsonListenerToJdomAdapter.endObject();
        jsonListenerToJdomAdapter.endDocument();
        final JsonNode expected = aJsonObject(new HashMap<JsonStringNode, JsonNode>() {{
            put(aJsonString("Gigawatts"), JsonNodeFactories.aJsonNumber("1.21"));
        }});
        assertThat(jsonListenerToJdomAdapter.getDocument(), equalTo(expected));
    }

    @Test
    public void simpleStringArrayHappyCase() throws Exception {
        final JsonListenerToJdomAdapter jsonListenerToJdomAdapter = new JsonListenerToJdomAdapter();
        jsonListenerToJdomAdapter.startDocument();
        jsonListenerToJdomAdapter.startArray();
        jsonListenerToJdomAdapter.stringValue("Hello");
        jsonListenerToJdomAdapter.stringValue("World");
        jsonListenerToJdomAdapter.endArray();
        jsonListenerToJdomAdapter.endDocument();
        final JsonNode expected = JsonNodeFactories.aJsonArray(Arrays.asList((JsonNode) aJsonString("Hello"), aJsonString("World")));
        assertThat(jsonListenerToJdomAdapter.getDocument(), equalTo(expected));
    }

    @Test
    public void simpleNumberArrayHappyCase() throws Exception {
        final JsonListenerToJdomAdapter jsonListenerToJdomAdapter = new JsonListenerToJdomAdapter();
        jsonListenerToJdomAdapter.startDocument();
        jsonListenerToJdomAdapter.startArray();
        jsonListenerToJdomAdapter.numberValue("1.21");
        jsonListenerToJdomAdapter.numberValue("42");
        jsonListenerToJdomAdapter.endArray();
        jsonListenerToJdomAdapter.endDocument();
        final JsonNode expected = JsonNodeFactories.aJsonArray(Arrays.asList(JsonNodeFactories.aJsonNumber("1.21"), JsonNodeFactories.aJsonNumber("42")));
        assertThat(jsonListenerToJdomAdapter.getDocument(), equalTo(expected));
    }

    @Test
    public void objectArrayHappyCase() throws Exception {
        final JsonListenerToJdomAdapter jsonListenerToJdomAdapter = new JsonListenerToJdomAdapter();
        jsonListenerToJdomAdapter.startDocument();
        jsonListenerToJdomAdapter.startArray();
        jsonListenerToJdomAdapter.startObject();
        jsonListenerToJdomAdapter.startField("anObject");
        jsonListenerToJdomAdapter.stringValue("objectValue");
        jsonListenerToJdomAdapter.endField();
        jsonListenerToJdomAdapter.endObject();
        jsonListenerToJdomAdapter.nullValue();
        jsonListenerToJdomAdapter.trueValue();
        jsonListenerToJdomAdapter.falseValue();
        jsonListenerToJdomAdapter.endArray();
        jsonListenerToJdomAdapter.endDocument();
        final JsonNode expected = JsonNodeFactories.aJsonArray(Arrays.asList(
                aJsonObject(new HashMap<JsonStringNode, JsonNode>() {{
                    put(aJsonString("anObject"), aJsonString("objectValue"));
                }})
                , aJsonNull()
                , aJsonTrue()
                , aJsonFalse()
        ));
        assertThat(jsonListenerToJdomAdapter.getDocument(), equalTo(expected));
    }
}
