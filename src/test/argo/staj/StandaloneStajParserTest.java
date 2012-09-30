/*
 * Copyright 2012 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.staj;

import argo.jdom.*;
import org.junit.Test;

import java.util.NoSuchElementException;

import static argo.jdom.JsonNodeFactories.*;
import static argo.jdom.JsonNodeTestBuilder.aJsonRootNode;
import static argo.jdom.JsonStringNodeTestBuilder.aStringNode;
import static argo.staj.ElementTrackingStandaloneStajParserMatcher.generatesElements;
import static argo.staj.JsonStreamElement.*;
import static argo.staj.JsonStreamElement.number;
import static argo.staj.JsonStreamElement.string;
import static argo.staj.RoundTrippingStandaloneStajParserMatcher.parsesTo;
import static argo.staj.StandaloneStajParserBuilder.standaloneStajParser;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public final class StandaloneStajParserTest {

    @Test
    public void arrayOnlyDocumentHasCorrectElements() throws Exception {
        assertThat(standaloneStajParser(array()), generatesElements(startDocument(), startArray(), endArray(), endDocument()));
    }

    @Test
    public void objectOnlyDocumentHasCorrectElements() throws Exception {
        assertThat(standaloneStajParser(object()), generatesElements(startDocument(), startObject(), endObject(), endDocument()));
    }

    @Test
    public void arrayWithChildHasCorrectElements() throws Exception {
        assertThat(standaloneStajParser(array(array())), generatesElements(startDocument(), startArray(), startArray(), endArray(), endArray(), endDocument()));
    }

    @Test
    public void arrayWithChildrenHasCorrectElements() throws Exception {
        assertThat(standaloneStajParser(array(array(), array())), generatesElements(
                startDocument(),
                startArray(),
                startArray(),
                endArray(),
                startArray(),
                endArray(),
                endArray(),
                endDocument()
        ));
    }

    @Test
    public void objectWithFieldHasCorrectElements() throws Exception {
        final JsonStringNode aFieldName = JsonStringNodeTestBuilder.aStringNode();
        assertThat(standaloneStajParser(object(field(aFieldName, array()))), generatesElements(
                startDocument(),
                startObject(),
                startField(aFieldName.getText()),
                startArray(),
                endArray(),
                endField(),
                endObject(),
                endDocument()
        ));
    }

    @Test
    public void objectWithFieldsHasCorrectElements() throws Exception {
        final JsonStringNode aFieldName = JsonStringNodeTestBuilder.aStringNode();
        final JsonStringNode anotherFieldName = JsonStringNodeTestBuilder.aStringNode();
        assertThat(standaloneStajParser(object(field(aFieldName, array()), field(anotherFieldName, object()))), generatesElements(
                startDocument(),
                startObject(),
                startField(aFieldName.getText()),
                startArray(),
                endArray(),
                endField(),
                startField(anotherFieldName.getText()),
                startObject(),
                endObject(),
                endField(),
                endObject(),
                endDocument()
        ));
    }

    @Test
    public void arrayWithNullHasCorrectElements() throws Exception {
        assertThat(standaloneStajParser(array(nullNode())), generatesElements(startDocument(), startArray(), nullValue(), endArray(), endDocument()));
    }

    @Test
    public void arrayWithNullsHasCorrectElements() throws Exception {
        assertThat(standaloneStajParser(array(nullNode(), nullNode())), generatesElements(startDocument(), startArray(), nullValue(), nullValue(), endArray(), endDocument()));
    }

    @Test
    public void arrayWithTrueHasCorrectElements() throws Exception {
        assertThat(standaloneStajParser(array(trueNode())), generatesElements(startDocument(), startArray(), trueValue(), endArray(), endDocument()));
    }

    @Test
    public void arrayWithFalseHasCorrectElements() throws Exception {
        assertThat(standaloneStajParser(array(falseNode())), generatesElements(startDocument(), startArray(), falseValue(), endArray(), endDocument()));
    }

    @Test
    public void arrayWithATextNodeHasCorrectElements() throws Exception {
        final JsonNode aStringNode = aStringNode();
        assertThat(standaloneStajParser(array(aStringNode)), generatesElements(startDocument(), startArray(), string(aStringNode.getText()), endArray(), endDocument()));
    }

    @Test
    public void arrayWithANumberNodeHasCorrectElements() throws Exception {
        final JsonNode aNumberNode = JsonNumberNodeTestBuilder.aNumberNode();
        assertThat(standaloneStajParser(array(aNumberNode)), generatesElements(startDocument(), startArray(), number(aNumberNode.getText()), endArray(), endDocument()));
    }

    @Test
    public void aRandomRootNodeHasCorrectElements() throws Exception {
        final JsonRootNode jsonRootNode = aJsonRootNode();
        assertThat(standaloneStajParser(jsonRootNode), parsesTo(jsonRootNode));
    }

    @Test
    public void nextWorksWithoutCallingHasNext() throws Exception {
        assertThat(standaloneStajParser(array()).next(), equalTo(startDocument()));
    }

    @Test(expected = NoSuchElementException.class)
    public void callingNextWhenHasNextReturnsFalseThrowsAnException() throws Exception {
        final StandaloneStajParser standaloneStajParser = standaloneStajParser(array());
        while (standaloneStajParser.hasNext()) {
            standaloneStajParser.next();
        }
        standaloneStajParser.next();
    }

}
