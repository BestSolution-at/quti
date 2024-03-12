package at.bestsolution.quti.client.spi;

import java.net.URI;

import at.bestsolution.quti.client.QutiClient;

public interface QutiClientFactory {
    public QutiClient create(URI uri);
}
