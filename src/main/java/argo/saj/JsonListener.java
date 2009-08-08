package argo.saj;

/**
 * A JsonListener is notified of events generated by a <code>SajParser</code> from a stream of JSON characters.
 *
 * The first call generated by a JSON stream will always be to <code>startDocument()</code> and the last will be
 * to <code>endDocument()</code>.
 *
 * @see SajParser
 */
public interface JsonListener {

    void startDocument();

    void endDocument();

    void startArray();

    void endArray();

    void startObject();

    void endObject();

    void startField(String name);

    void endField();

    void stringValue(String value);

    void numberValue(String value);

    void trueValue();

    void falseValue();

    void nullValue();
}