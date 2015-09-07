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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static argo.RandomListFactory.aList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public final class ImmutableListFactoriesTest {
    @Test
    public void handlesEmptyList() throws Exception {
        assertThat(ImmutableListFactories.immutableListOf(emptyList()), is(emptyList()));
    }

    @Test
    public void returnedListIsEqualToSourceList() throws Exception {
        final List<Object> sourceList = aList();
        assertThat(ImmutableListFactories.immutableListOf(sourceList), equalTo(sourceList));
    }

    @Test
    public void returnedListIsImmutable() throws Exception {
        final List<Object> originalSourceList = aList();
        final List<Object> mutableSourceList = new ArrayList<Object>(originalSourceList);
        final List<Object> immutableList = ImmutableListFactories.immutableListOf(mutableSourceList);
        mutableSourceList.add(new Object());
        assertThat(immutableList, equalTo(originalSourceList));
        assertThat(immutableList, not(equalTo(mutableSourceList)));
    }

}
