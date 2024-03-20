package at.bestsolution.quti.client.jdkhttp.impl;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.function.Function;

import at.bestsolution.quti.client.CalendarService;
import at.bestsolution.quti.client.CalendarsService;
import at.bestsolution.quti.client.dto.CalendarNewDTO;
import at.bestsolution.quti.client.dto.CalendarNewDTO.Builder;
import at.bestsolution.quti.client.jdkhttp.impl.dto.CalendarNewDTOImpl;

public class CalendarsServiceImpl implements CalendarsService {
    private final String baseURI;
    private final HttpClient client;

    public CalendarsServiceImpl(HttpClient client, String baseURI) {
        this.client = client;
        this.baseURI = baseURI;
    }

    @Override
    public CalendarService calendar(String key) {
        return new CalendarServiceImpl(client, String.format("%s/%s",baseURI,key));
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
