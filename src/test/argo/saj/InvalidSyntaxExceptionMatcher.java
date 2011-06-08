/*
 * Copyright 2011 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.saj;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

final class InvalidSyntaxExceptionMatcher extends TypeSafeMatcher<InvalidSyntaxException> {
    private final int column;
    private final int line;

    static InvalidSyntaxExceptionMatcher anInvalidSyntaxExceptionAtPosition(final int column, final int line) {
        return new InvalidSyntaxExceptionMatcher(column, line);
    }

    private InvalidSyntaxExceptionMatcher(final int column, final int line) {
        this.column = column;
        this.line = line;
    }

    public boolean matchesSafely(final InvalidSyntaxException e) {
        return e.getColumn() == column && e.getLine() == line;
    }

    public void describeTo(final Description description) {
        description.appendText("An InvalidSyntaxException at line ").appendValue(line).appendText(", column ").appendValue(column);
    }
}
