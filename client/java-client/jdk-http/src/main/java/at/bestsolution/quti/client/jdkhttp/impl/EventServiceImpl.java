package at.bestsolution.quti.client.jdkhttp.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import at.bestsolution.quti.client.EventService;
import at.bestsolution.quti.client.dto.EventNewDTO;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventNewDTOImpl;
import jakarta.json.Json;

public class EventServiceImpl implements EventService {
	private final String baseURI;
	private final HttpClient client;

	public EventServiceImpl(HttpClient client, String baseURI) {
		this.client = client;
		this.baseURI = baseURI;
		System.err.println("URI: " + this.baseURI);
	}

    @Override
    public void delete(String calendarKey, String key) {
        var request = HttpRequest.newBuilder()
				.uri(URI.create(String.format("%s/%s/events/%s",baseURI, calendarKey, key)))
				.DELETE()
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 204 ) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
    }

    @Override
    public void move(String calendarKey, String key, ZonedDateTime start, ZonedDateTime end) {
		var builder = Json.createObjectBuilder();
		builder.add("start", start.toString());
		builder.add("end", end.toString());

        var request = HttpRequest.newBuilder()
				.uri(URI.create(String.format("%s/%s/events/%s/action/move",baseURI, calendarKey, key)))
				.header("Content-Type", "application/json")
				.PUT(BodyPublishers.ofString(Utils.stringify(builder.build(), false)))
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 204 ) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
    }

    @Override
    public void cancel(String calendarKey, String key) {
        var request = HttpRequest.newBuilder()
				.uri(URI.create(String.format("%s/%s/events/%s/action/cancel",baseURI, calendarKey, key)))
				.PUT(BodyPublishers.ofString(""))
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 204 ) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
    }

	@Override
    public void uncancel(String calendarKey, String key) {
        var request = HttpRequest.newBuilder()
				.uri(URI.create(String.format("%s/%s/events/%s/action/uncancel",baseURI, calendarKey, key)))
				.PUT(BodyPublishers.ofString(""))
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 204 ) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
    }

	@Override
	public void endRepeat(String calendarKey, String key, LocalDate end) {
		var request = HttpRequest.newBuilder()
				.uri(URI.create(String.format("%s/%s/events/%s/action/end-repeat",baseURI, calendarKey, key)))
				.header("Content-Type", "application/json")
				.PUT(BodyPublishers.ofString(String.format("\"%s\"",end)))
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 204 ) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public String create(String calendarKey, EventNewDTO newEvent) {
		var url = String.format("%s/%s/events",baseURI,calendarKey);
		System.err.println(url);
		var request = HttpRequest.newBuilder()
				.uri(URI.create(url))
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
