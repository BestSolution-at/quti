package at.bestsolution.quti.client.jdkhttp.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.ZonedDateTime;

import at.bestsolution.quti.client.Event;
import jakarta.json.Json;

public class EventImpl implements Event {
	private final String baseURI;
	private final HttpClient client;

	EventImpl(HttpClient client, String baseURI) {
		this.client = client;
		this.baseURI = baseURI;
		System.err.println("URI: " + this.baseURI);
	}

    @Override
    public void delete() {
        var request = HttpRequest.newBuilder()
				.uri(URI.create(baseURI))
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
    public void move(ZonedDateTime start, ZonedDateTime end) {
		var builder = Json.createObjectBuilder();
		builder.add("start", start.toString());
		builder.add("end", end.toString());

        var request = HttpRequest.newBuilder()
				.uri(URI.create(baseURI))
				.header("Content-Type", "application/json")
				.PUT(BodyPublishers.ofString(Utils.stringify(builder.build(), false)))
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 204 & response.statusCode() != 201 ) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
    }

    @Override
    public void cancel() {
        var request = HttpRequest.newBuilder()
				.uri(URI.create(baseURI))
				.PUT(BodyPublishers.ofString(""))
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if( response.statusCode() != 204 & response.statusCode() != 201 ) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
    }

}
