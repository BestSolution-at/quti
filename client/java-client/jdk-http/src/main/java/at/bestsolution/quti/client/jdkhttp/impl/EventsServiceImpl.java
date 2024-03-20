package at.bestsolution.quti.client.jdkhttp.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.function.Function;

import at.bestsolution.quti.client.EventService;
import at.bestsolution.quti.client.EventsService;
import at.bestsolution.quti.client.dto.EventNewDTO;
import at.bestsolution.quti.client.dto.EventNewDTO.Builder;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventNewDTOImpl;

public class EventsServiceImpl implements EventsService {
    private final String baseURI;
    private final HttpClient client;

    EventsServiceImpl(HttpClient client, String baseURI) {
        this.client = client;
        this.baseURI = baseURI;
    }

	@Override
	public EventService event(String key) {
		return new EventServiceImpl(client, String.format("%s/%s", baseURI, key));
	}

	@Override
	public String create(Function<Builder, EventNewDTO> factory) {
		return create( factory.apply(new EventNewDTOImpl.BuilderImpl()) );
	}

    @Override
    public String create(EventNewDTO newEvent) {
        var request = HttpRequest.newBuilder()
				.uri(URI.create(baseURI))
				.header("Content-Type", "application/json")
				.POST(BodyPublishers.ofString(Utils.stringify(((EventNewDTOImpl)newEvent).data, false)))
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 201 ) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
            var location = response.headers().firstValue("location").orElseThrow();
			return location.substring(location.lastIndexOf('/')+1);
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
    }
}
