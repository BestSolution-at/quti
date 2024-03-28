package at.bestsolution.quti.client.jdkhttp.impl;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.function.Function;

import at.bestsolution.quti.client.CalendarService;
import at.bestsolution.quti.client.dto.CalendarDTO;
import at.bestsolution.quti.client.dto.CalendarNewDTO;
import at.bestsolution.quti.client.dto.EventViewDTO;
import at.bestsolution.quti.client.dto.CalendarNewDTO.Builder;
import at.bestsolution.quti.client.jdkhttp.impl.dto.CalendarDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.CalendarNewDTOImpl;
import at.bestsolution.quti.client.jdkhttp.impl.dto.EventViewDTOImpl;
import jakarta.json.Json;

public class CalendarServiceImpl implements CalendarService {
	private final String baseURI;
	private final HttpClient client;

	public CalendarServiceImpl(HttpClient client, String baseURI) {
		this.client = client;
		this.baseURI = baseURI;
	}

	@Override
	public CalendarDTO get(String key) {
		var request = HttpRequest.newBuilder()
				.uri(URI.create(String.format("%s/%s", baseURI, key)))
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
	public List<EventViewDTO> eventView(String key, LocalDate start, LocalDate end, ZoneId timezone) {
		var uri = URI.create(String.format("%s/%s/view?from=%s&to=%s&timezone=%s", baseURI, key, start, end, URLEncoder.encode(timezone.toString(), StandardCharsets.UTF_8)));

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

	@Override
	public String create(Function<Builder, CalendarNewDTO> factory) {
		return create(factory.apply(new CalendarNewDTOImpl.BuilderImpl()));
	}

	@Override
	public String create(CalendarNewDTO calendar) {
		var request = HttpRequest.newBuilder()
			.uri(URI.create(baseURI))
            .header("Content-Type", "application/json")
			.POST(BodyPublishers.ofString(Utils.stringify(((CalendarNewDTOImpl)calendar).data, false)))
			.build();
		try {
			var response = this.client.send(request, BodyHandlers.ofString());
            System.err.println(response);
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
