package com.github.dwiechert.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.dwiechert.RaspberryPiTemperatureRecordedConfiguration;
import com.github.dwiechert.alert.Alert;

/**
 * {@link Controller} that handles the REST endpoints for the {@link Alert}s.
 * 
 * @author Dan Wiechert
 */
@Controller
@RequestMapping("/alerts")
public class AlertsController {
	private static final String LIST_FORMAT = "Name=%-20s InOn=%-5b";

	@Autowired
	private RaspberryPiTemperatureRecordedConfiguration configuration;

	/**
	 * Lists all of the {@link Alert}s that are known.
	 * 
	 * @return The list of alerts.
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public @ResponseBody String listAlerts() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		for (final Alert alert : configuration.getAlerts()) {
			sb.append("\n\t").append(String.format(LIST_FORMAT, alert.getName(), alert.isOn()));
		}
		sb.append("\n").append("}");
		return sb.toString();
	}

	/**
	 * Sets the provided alert to the on status.
	 * 
	 * @param name
	 *            The name of the alert.
	 */
	@RequestMapping(value = "/setOn/{name}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void setOn(@PathVariable(value = "name") final String name) {
		for (final Alert alert : configuration.getAlerts()) {
			if (alert.getName().equals(name)) {
				alert.setOn(true);
			}
		}
	}

	/**
	 * Sets the provided alert to the off status.
	 * 
	 * @param name
	 *            The name of the alert.
	 */
	@RequestMapping(value = "/setOff/{name}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void setOff(@PathVariable(value = "name") final String name) {
		for (final Alert alert : configuration.getAlerts()) {
			if (alert.getName().equals(name)) {
				alert.setOn(false);
			}
		}
	}

	/**
	 * Updates the provided alert.
	 * 
	 * @param name
	 *            The name of the alert.
	 * @param message
	 *            The alert's update message.
	 */
	@RequestMapping(value = "/update/{name}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void update(@PathVariable(value = "name") final String name, @RequestBody final String message) {
		for (final Alert alert : configuration.getAlerts()) {
			if (alert.getName().equals(name)) {
				alert.update(message);
			}
		}
	}
}
