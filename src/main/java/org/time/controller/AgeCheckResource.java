package org.time.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/age")
public class AgeCheckResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAge(@QueryParam("age") int age) {
        if (age < 1 || age > 120) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Age must be between 1 and 120"))
                    .build();
        }

        boolean isAdult = age >= 18;
        return Response.ok()
                .entity(new AgeResponse(age, isAdult))
                .build();
    }

    private static class AgeResponse {
        public int age;
        public boolean isAdult;
        public String message;

        public AgeResponse(int age, boolean isAdult) {
            this.age = age;
            this.isAdult = isAdult;
            this.message = isAdult ? "Access granted" : "Access denied - 18+ required";
        }
    }

    private static class ErrorResponse {
        public String error;
        public ErrorResponse(String error) { this.error = error; }
    }
}