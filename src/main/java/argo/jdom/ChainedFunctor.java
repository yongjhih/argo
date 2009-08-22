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

final class ChainedFunctor<T, U, V> implements Functor<T, V> {
    private final JsonNodeSelector<T, U> parentJsonNodeSelector;
    private final JsonNodeSelector<U, V> childJsonNodeSelector;

    ChainedFunctor(final JsonNodeSelector<T, U> parentJsonNodeSelector, final JsonNodeSelector<U, V> childJsonNodeSelector) {
        this.parentJsonNodeSelector = parentJsonNodeSelector;
        this.childJsonNodeSelector = childJsonNodeSelector;
    }

    public boolean matchesValue(final T jsonNode) {
        return parentJsonNodeSelector.matches(jsonNode) && childJsonNodeSelector.matches(parentJsonNodeSelector.getValue(jsonNode));
    }

    public V getValue(final T jsonNode) {
        return childJsonNodeSelector.getValue(parentJsonNodeSelector.getValue(jsonNode));
    }

    @Override
    public String toString() {
        return parentJsonNodeSelector.toString() + ", with " + childJsonNodeSelector.toString();
    }
}