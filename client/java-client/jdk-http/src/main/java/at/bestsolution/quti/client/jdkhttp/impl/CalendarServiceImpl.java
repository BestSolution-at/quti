package at.bestsolution.quti.client.jdkhttp.impl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import at.bestsolution.quti.client.CalendarService;
import at.bestsolution.quti.client.EventService;
import at.bestsolution.quti.client.EventsService;
import at.bestsolution.quti.client.dto.CalendarDTO;
import at.bestsolution.quti.client.dto.EventViewDTO;
import at.bestsolution.quti.client.jdkhttp.impl.dto.CalendarDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventViewDTOImpl;
import jakarta.json.Json;

public class CalendarServiceImpl implements CalendarService {
	private final String baseURI;
	private final HttpClient client;

	CalendarServiceImpl(HttpClient client, String baseURI) {
		this.client = client;
		this.baseURI = baseURI;
	}

	@Override
	public EventsService events() {
		return new EventsServiceImpl(client, String.format("%s/events",baseURI));
	}

	@Override
	public EventService event(String key) {
		return events().event(key);
	}

	@Override
	public CalendarDTO get() {
		var request = HttpRequest.newBuilder()
				.uri(URI.create(baseURI))
				.GET()
				.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
			var data = Json.createReader(new StringReader(response.body())).readObject();
			return CalendarDTOImpl.of(data);
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	public List<EventViewDTO> eventView(LocalDate start, LocalDate end, ZoneId timezone) {
		var uri = URI.create(String.format("%s/view?from=%s&to=%s&timezone=%s", baseURI, start, end, URLEncoder.encode(timezone.toString(), StandardCharsets.UTF_8)));

		var request = HttpRequest.newBuilder()
				.uri(uri)
				.header("timezone", timezone.toString())
				.GET()
				.build();
		try {
			var response = client.send(request, BodyHandlers.ofString());
			if (response.statusCode() != 200) {
				throw new IllegalStateException(String.format("Remote operation failed:\n%s", response.body()));
			}
			var data = Json.createReader(new StringReader(response.body())).readArray();
			return EventViewDTOImpl.of(data);
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}

	}
}
