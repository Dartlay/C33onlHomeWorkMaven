package org.time.controller;

import org.time.service.TimeService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
public class TimeController {
    @Inject
    private TimeService timeService;

    @GET
    @Path("/{city}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTime(@PathParam("city") String city) {
        return timeService.getCurrentTime(city);
    }
}
