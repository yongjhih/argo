package argo.jdom;

import argo.jax.JsonListenerException;
import argo.jax.JsonParser;

import java.io.IOException;
import java.io.Reader;

public final class JdomParser {
    public JsonValue parse(final Reader reader) throws JsonListenerException, IOException {
        final JsonListenerToJdomAdapter jsonListenerToJdomAdapter = new JsonListenerToJdomAdapter();
        new JsonParser().parse(reader, jsonListenerToJdomAdapter);
        return jsonListenerToJdomAdapter.getDocument();
    }
}
