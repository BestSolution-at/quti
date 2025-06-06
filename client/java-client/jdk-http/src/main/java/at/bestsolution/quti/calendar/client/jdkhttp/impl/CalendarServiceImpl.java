// Generated by RSD - Do not modify
package at.bestsolution.quti.calendar.client.jdkhttp.impl;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import at.bestsolution.quti.calendar.client.CalendarService;
import at.bestsolution.quti.calendar.client.InvalidArgumentException;
import at.bestsolution.quti.calendar.client.InvalidContentException;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model._JsonUtils;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.CalendarDataImpl;
import at.bestsolution.quti.calendar.client.jdkhttp.impl.model.EventViewDataImpl;
import at.bestsolution.quti.calendar.client.model.Calendar;
import at.bestsolution.quti.calendar.client.model.CalendarNew;
import at.bestsolution.quti.calendar.client.model.EventView;
import at.bestsolution.quti.calendar.client.NotFoundException;

public class CalendarServiceImpl implements CalendarService {
	private final String baseURI;
	private final HttpClient client;

	public CalendarServiceImpl(HttpClient client, String baseURI) {
		this.baseURI = baseURI;
		this.client = client;
	}

	public String create(CalendarNew.Data calendar)
			throws InvalidContentException {
		Objects.requireNonNull(calendar, "calendar must not be null");

		var $path = "%s/api/calendar/".formatted(
				this.baseURI);

		var $body = BodyPublishers.ofString(_JsonUtils.toJsonString(calendar, false));

		var $uri = URI.create($path);
		var $request = HttpRequest.newBuilder()
				.uri($uri)
				.header("Content-Type", "application/json")
				.POST($body)
				.build();

		try {
			var $response = this.client.send($request, BodyHandlers.ofString());
			if ($response.statusCode() == 201) {
				return ServiceUtils.mapString($response);
			} else if ($response.statusCode() == 422) {
				throw new InvalidContentException(ServiceUtils.mapString($response));
			}
			throw new IllegalStateException(String.format("Unsupported Http-Status '%s':\n%s", $response.statusCode(), $response.body()));
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public Calendar.Data get(String key)
			throws NotFoundException,
			InvalidArgumentException {
		Objects.requireNonNull(key, "key must not be null");

		var $path = "%s/api/calendar/%s".formatted(
				this.baseURI,
				key);

		var $uri = URI.create($path);
		var $request = HttpRequest.newBuilder()
				.uri($uri)
				.GET()
				.build();

		try {
			var $response = this.client.send($request, BodyHandlers.ofString());
			if ($response.statusCode() == 200) {
				return ServiceUtils.mapObject($response, CalendarDataImpl::of);
			} else if ($response.statusCode() == 404) {
				throw new NotFoundException(ServiceUtils.mapString($response));
			} else if ($response.statusCode() == 400) {
				throw new InvalidArgumentException(ServiceUtils.mapString($response));
			}
			throw new IllegalStateException(String.format("Unsupported Http-Status '%s':\n%s", $response.statusCode(), $response.body()));
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public void update(String key, Calendar.Patch changes)
			throws NotFoundException,
			InvalidArgumentException {
		Objects.requireNonNull(key, "key must not be null");
		Objects.requireNonNull(changes, "changes must not be null");

		var $path = "%s/api/calendar/%s".formatted(
				this.baseURI,
				key);

		var $body = BodyPublishers.ofString(_JsonUtils.toJsonString(changes, false));

		var $uri = URI.create($path);
		var $request = HttpRequest.newBuilder()
				.uri($uri)
				.method("PATCH", $body)
				.build();

		try {
			var $response = this.client.send($request, BodyHandlers.ofString());
			if ($response.statusCode() == 204) {
				return;
			} else if ($response.statusCode() == 404) {
				throw new NotFoundException(ServiceUtils.mapString($response));
			} else if ($response.statusCode() == 400) {
				throw new InvalidArgumentException(ServiceUtils.mapString($response));
			}
			throw new IllegalStateException(String.format("Unsupported Http-Status '%s':\n%s", $response.statusCode(), $response.body()));
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public List<EventView.Data> eventView(String key, LocalDate start, LocalDate end, ZoneId timezone)
			throws NotFoundException,
			InvalidArgumentException {
		Objects.requireNonNull(key, "key must not be null");
		Objects.requireNonNull(start, "start must not be null");
		Objects.requireNonNull(end, "end must not be null");
		Objects.requireNonNull(timezone, "timezone must not be null");

		var $path = "%s/api/calendar/%s/view".formatted(
				this.baseURI,
				key);

		var $queryParams = Map.of(
				"from", ServiceUtils.toQueryString(start),
				"to", ServiceUtils.toQueryString(end),
				"timezone", ServiceUtils.toQueryString(timezone));
		var $queryParamString = ServiceUtils.toURLQueryPart($queryParams);

		var $uri = URI.create($path + $queryParamString);
		var $request = HttpRequest.newBuilder()
				.uri($uri)
				.GET()
				.build();

		try {
			var $response = this.client.send($request, BodyHandlers.ofString());
			if ($response.statusCode() == 200) {
				return ServiceUtils.mapObjects($response, EventViewDataImpl::of);
			} else if ($response.statusCode() == 404) {
				throw new NotFoundException(ServiceUtils.mapString($response));
			} else if ($response.statusCode() == 400) {
				throw new InvalidArgumentException(ServiceUtils.mapString($response));
			}
			throw new IllegalStateException(String.format("Unsupported Http-Status '%s':\n%s", $response.statusCode(), $response.body()));
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}

	public List<EventView.Data> eventView(String key, LocalDate start, LocalDate end, ZoneId timezone, ZoneId resultTimeZone)
			throws NotFoundException,
			InvalidArgumentException {
		Objects.requireNonNull(key, "key must not be null");
		Objects.requireNonNull(start, "start must not be null");
		Objects.requireNonNull(end, "end must not be null");
		Objects.requireNonNull(timezone, "timezone must not be null");
		Objects.requireNonNull(resultTimeZone, "resultTimeZone must not be null");

		var $path = "%s/api/calendar/%s/view".formatted(
				this.baseURI,
				key);

		var $queryParams = Map.of(
				"from", ServiceUtils.toQueryString(start),
				"to", ServiceUtils.toQueryString(end),
				"timezone", ServiceUtils.toQueryString(timezone));
		var $queryParamString = ServiceUtils.toURLQueryPart($queryParams);

		var $headerParams = Map.of(
				"timezone", Objects.toString(resultTimeZone));
		var $headers = ServiceUtils.toHeaders($headerParams);

		var $uri = URI.create($path + $queryParamString);
		var $request = HttpRequest.newBuilder()
				.uri($uri)
				.headers($headers)
				.GET()
				.build();

		try {
			var $response = this.client.send($request, BodyHandlers.ofString());
			if ($response.statusCode() == 200) {
				return ServiceUtils.mapObjects($response, EventViewDataImpl::of);
			} else if ($response.statusCode() == 404) {
				throw new NotFoundException(ServiceUtils.mapString($response));
			} else if ($response.statusCode() == 400) {
				throw new InvalidArgumentException(ServiceUtils.mapString($response));
			}
			throw new IllegalStateException(String.format("Unsupported Http-Status '%s':\n%s", $response.statusCode(), $response.body()));
		} catch (IOException | InterruptedException e) {
			throw new IllegalStateException(e);
		}
	}
}
