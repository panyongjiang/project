package com.uway.mobile.domain;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class Smooth {
	@XStreamImplicit(itemFieldName="event")
	private List<Event> event;

	public List<Event> getEvent() {
		return event;
	}

	public void setEvent(List<Event> event) {
		this.event = event;
	}
	
}
