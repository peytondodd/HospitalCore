package de.gabik21.hospitalcore.types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Report {

    public static List<Report> list = new ArrayList<Report>();

    private String reported;
    private String reporter;
    private String reason;
    private UUID id;

    public Report(String reporter, String reported, String reason) {

	this.reporter = reporter;
	this.reported = reported;
	this.reason = reason;
	this.id = UUID.randomUUID();
	list.add(this);

    }

    public void remove() {

	list.remove(this);

    }

    public String getReporter() {

	return this.reporter;

    }

    public String getReported() {

	return this.reported;

    }

    public String getReason() {

	return this.reason;

    }

    public UUID getId() {

	return this.id;

    }

}
