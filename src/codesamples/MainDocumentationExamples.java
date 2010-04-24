/*
 * Copyright 2010 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.jdom.*;
import argo.saj.JsonListener;
import argo.saj.SajParser;
import argo.staj.JsonStreamElementType;
import argo.staj.StajParser;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

import static argo.format.JsonNumberUtils.asBigDecimal;
import static argo.jdom.JsonNodeBuilders.*;
import static argo.jdom.JsonNodeFactories.*;
import static argo.jdom.JsonNodeSelectors.aStringNode;
import static argo.jdom.JsonNodeSelectors.anArrayNode;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public final class MainDocumentationExamples {

    private static final JsonFormatter JSON_FORMATTER
            = new PrettyJsonFormatter();

    private static final JsonRootNode SAMPLE_JSON = aJsonObject(
            aJsonField("name", aJsonString("Black Lace"))
            , aJsonField("sales", aJsonNumber("110921"))
            , aJsonField("totalRoyalties", aJsonNumber("10223.82"))
            , aJsonField("singles", aJsonArray(
                    aJsonString("Superman")
                    , aJsonString("Agadoo")
            ))
    );

    private static final JdomParser JDOM_PARSER = new JdomParser();
    private static final JsonNodeSelector<JsonNode, String> SECOND_SINGLE
            = JsonNodeSelectors.aStringNode("singles", 1);

    private static final JsonNodeSelector<JsonNode, List<JsonNode>> SINGLES
            = anArrayNode("singles");
    private static final JsonNodeSelector<JsonNode, String> SINGLE_NAME
            = aStringNode();
    private static final SajParser SAJ_PARSER
            = new SajParser();

    @Test
    public void producesJsonFromFactory() throws Exception {
        JsonRootNode json = aJsonObject(
                aJsonField("name", aJsonString("Black Lace"))
                , aJsonField("sales", aJsonNumber("110921"))
                , aJsonField("totalRoyalties", aJsonNumber("10223.82"))
                , aJsonField("singles", aJsonArray(
                        aJsonString("Superman")
                        , aJsonString("Agadoo")
                ))
        );
        assertThat(json, equalTo(SAMPLE_JSON));
    }

    @Test
    public void producesJsonFromBuilder() throws Exception {
        JsonObjectNodeBuilder builder = anObjectBuilder()
                .withField("name", aStringBuilder("Black Lace"))
                .withField("sales", aNumberBuilder("110921"))
                .withField("totalRoyalties", aNumberBuilder("10223.82"))
                .withField("singles", anArrayBuilder()
                        .withElement(aStringBuilder("Superman"))
                        .withElement(aStringBuilder("Agadoo"))
                );
        JsonRootNode json = builder.build();
        assertThat(json, equalTo(SAMPLE_JSON));
    }

    @Test
    public void formatsJson() throws Exception {
        final JsonRootNode json = SAMPLE_JSON;
        String jsonText = JSON_FORMATTER.format(json);
        System.out.println("jsonText = " + jsonText);
        assertThat(JDOM_PARSER.parse(jsonText), equalTo(SAMPLE_JSON));
    }

    @Test
    public void parsesJsonAndGetsElementsWithCallToJsonNode() throws Exception {
        final String jsonText = FileUtils.readFileToString(new File(this.getClass().getResource("SimpleExample.json").getFile()));
        final JsonRootNode json = JDOM_PARSER.parse(jsonText);
        String secondSingle = json.aStringValue("singles", 1);
        assertThat(secondSingle, equalTo("Agadoo"));
    }

    @Test
    public void parsesJsonAndGetsElementsWithJsonNodeSelector() throws Exception {
        final String jsonText = FileUtils.readFileToString(new File(this.getClass().getResource("SimpleExample.json").getFile()));
        final JsonRootNode json = JDOM_PARSER.parse(jsonText);
        String secondSingle = SECOND_SINGLE.getValue(json);
        List<String> singles = new AbstractList<String>() {
            public String get(int index) {
                return SINGLE_NAME.getValue(SINGLES.getValue(json).get(index));
            }

            public int size() {
                return SINGLES.getValue(json).size();
            }
        };
        BigDecimal totalRoyalties = asBigDecimal(json.aNumberValue("totalRoyalties"));
        assertThat(secondSingle, equalTo("Agadoo"));
        assertThat(singles, equalTo(Arrays.asList("Superman", "Agadoo")));
        assertThat(totalRoyalties, equalTo(new BigDecimal("10223.82")));
    }

    @Test
    public void producesJsonBackedObject() throws Exception {
        final String jsonText = FileUtils.readFileToString(new File(this.getClass().getResource("SimpleExample.json").getFile()));
        final JsonRootNode json = JDOM_PARSER.parse(jsonText);
        final SimpleExample simpleExample = new JsonBackedSimpleExample(json);
        System.out.println("simpleExample.getName() = " + simpleExample.getName());
        System.out.println("simpleExample.getSales() = " + simpleExample.getSales());
        System.out.println("simpleExample.getSingles() = " + simpleExample.getSingles());
    }

    @Test
    public void parsesUsingSaj() throws Exception {
        final FileReader jsonReader = new FileReader(new File(this.getClass().getResource("SimpleExample.json").getFile()));
        final Set<String> fieldNames = new HashSet<String>();
        SAJ_PARSER.parse(jsonReader, new JsonListener() {
            public void startField(String name) {
                fieldNames.add(name);
            }
            public void startDocument() { }
            public void endDocument() { }
            public void startArray() { }
            public void endArray() { }
            public void startObject() { }
            public void endObject() { }
            public void endField() { }
            public void stringValue(final String value) { }
            public void numberValue(final String value) { }
            public void trueValue() { }
            public void falseValue() { }
            public void nullValue() { }
        });
        assertThat(fieldNames, equalTo((Set<String>)new HashSet<String>(Arrays.asList("name", "sales", "totalRoyalties", "singles"))));
    }

    @Test
    public void parsesUsingStaj() throws Exception {
        final FileReader jsonReader = new FileReader(new File(this.getClass().getResource("SimpleExample.json").getFile()));
Set<String> fieldNames = new HashSet<String>();
StajParser stajParser = null;
try {
    stajParser = new StajParser(jsonReader);
    while (stajParser.hasNext()) {
        if (stajParser.next() == JsonStreamElementType.START_FIELD) {
            fieldNames.add(stajParser.getText());
        }
    }
} finally {
    if (stajParser != null) {
        stajParser.close();
    }
}
        assertThat(fieldNames, equalTo((Set<String>)new HashSet<String>(Arrays.asList("name", "sales", "totalRoyalties", "singles"))));
    }
}
