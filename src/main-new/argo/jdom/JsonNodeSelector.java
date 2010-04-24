/*
 * Copyright 2010 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.jdom;

/**
 * Instances of <code>JsonNodeSelector</code> extract values from <code>Object</code>s of a specified type.
 *
 * <p>For example, given a <code>JsonNode</code> representing <code>{"Fee":{"fi":"fo"}}</code>,
 * <pre>
    anObjectNodeWithField("Fee")
        .withChild(anObjectNodeWithField("fi"))
        .withChild(aStringNode())
        .getValue(jsonNode)
 * </pre> will return the <code>String</code> "fo".</p>
 *
 * @param <T> The type of Object worked on.
 * @param <U> The type of Object returned.
 */
public final class JsonNodeSelector<T, U> {

    private final Functor<T, U> valueGetter;

    JsonNodeSelector(final Functor<T, U> valueGetter) {
        this.valueGetter = valueGetter;
    }

    /**
     * Determines whether this <code>JsonNodeSelector</code> can exctract a value from the given <code>JsonNode</code>.
     * @param jsonNode the <code>JsonNode</code> to test.
     * @return true if a value can be extracted from the given <code>JsonNode</code>, false otherwise. 
     */
    public boolean matches(final T jsonNode) {
        return valueGetter.matchesNode(jsonNode);
    }

    /**
     * Extracts a value from the give <code>JsonNode</code>.
     *
     * @param argument the <code>JsonNode</code> to extract a value from.
     * @return the extracted value.
     * @throws IllegalArgumentException if calling <code>matches<code> with the given <code>JsonNode</code> would return false, indicating no value can be extracted from it. 
     */
    public U getValue(final T argument) {
        if (!matches(argument)) {
            throw new IllegalArgumentException("Argument [" + argument + "] does not match the requirments of this selector.");
        } else {
            return valueGetter.applyTo(argument);
        }
    }

    /**
     * <p>Constructs a JsonNodeSelector consisting of this chained with the given <code>JsonNodeSelector</code>.</p>
     *
     * <p>For example, if we have <code>JsonNodeSelectors</code> for the first element of an array, and another that
     * selects the second element of an array, and we chain them together in that order, we will get a selector that
     * works on nested arrays, selecting the second element from an array stored in the first element of a parent
     * array.</p> 
     *
     * @param childJsonNodeSelector the <code>JsonNodeSelector</code> to chain onto this.
     * @param <V> the type the chained <code>JsonNodeSelector</code> will return.
     * @return a new <code>JsonNodeSelector</code> representing this, and the given selector applied in sequence.
     */
    public <V> JsonNodeSelector<T, V> with(final JsonNodeSelector<U, V> childJsonNodeSelector) {
        return new JsonNodeSelector<T,V>(new ChainedFunctor<T, U, V>(this, childJsonNodeSelector));
    }

    @Override
    public String toString() {
        return valueGetter.toString();
    }

}
