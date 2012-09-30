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

import argo.format.PrettyJsonBuilder;
import argo.jdom.JsonRootNode;
import argo.jdom.StajBasedJdomParser;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import static org.hamcrest.CoreMatchers.equalTo;

public final class RoundTrippingStajParserMatcher extends TypeSafeDiagnosingMatcher<StajParser> {

    private final Matcher<String> stringMatcher;
    private final JsonRootNode jsonRootNode;
    private JsonRootNode parseResult;

    private RoundTrippingStajParserMatcher(final JsonRootNode jsonRootNode) {
        this.jsonRootNode = jsonRootNode;
        stringMatcher = equalTo(PrettyJsonBuilder.json(jsonRootNode));
        parseResult = null;
    }

    public static Matcher<StajParser> parsesTo(final JsonRootNode jsonRootNode) {
        return new RoundTrippingStajParserMatcher(jsonRootNode);
    }

    @Override
    protected boolean matchesSafely(final StajParser item, final Description mismatchDescription) {
        if (parseResult == null) {
            parseResult = new StajBasedJdomParser().parse(item);
        }
        final boolean result = stringMatcher.matches(PrettyJsonBuilder.json(parseResult));
        if (!result) {
            stringMatcher.describeMismatch(PrettyJsonBuilder.json(parseResult), mismatchDescription);
        }
        return result;
    }

    public void describeTo(final Description description) {
        description.appendText("StajParser that generates JSON equal to ").appendValue(PrettyJsonBuilder.json(jsonRootNode));
    }
}
