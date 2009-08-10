/*
 * Copyright 2009 Mark Slater
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package argo.format;

import argo.jdom.JsonRootNode;

import java.io.IOException;
import java.io.Writer;

/**
 * A <code>JsonFormat</code> provides operations to turn <code>JsonRootNode</code>s into valid JSON text.
 */
public interface JsonFormat {

    /**
     * Returns the specified <code>JsonRootNode</code> formatted as a String.
     *
     * @param jsonRootNode the <code>JsonRootNode</code> to format.
     * @return the specified <code>JsonRootNode</code> formatted as a String.
     * @throws IOException if there was a problem outputting to the String.
     */
    String format(JsonRootNode jsonRootNode) throws IOException;

    /**
     * Streams the specified <code>JsonRootNode</code> formatted to the specified <code>Writer</code>.
     *
     * @param jsonRootNode the <code>JsonRootNode</code> to format.
     * @param writer       the <code>Writer</code> to stream the formatted <code>JsonRootNode</code> to.
     * @throws IOException if there was a problem writing to the <code>Writer</code>.
     */
    void format(JsonRootNode jsonRootNode, Writer writer) throws IOException;

}
