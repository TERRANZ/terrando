package ru.terra.ndo.server.controller;

import com.sun.jersey.api.core.HttpContext;
import ru.terra.ndo.server.constants.FilePatchConstants;
import ru.terra.server.controller.AbstractResource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Date: 26.06.14
 * Time: 15:56
 */
@Path("/res")
@Produces("image/*")
public class ResourceController extends AbstractResource {
    @GET
    @Path("/picz/{uid}/{file}")
    public Response getPhoto(@Context HttpContext hc, @PathParam("uid") String uid, @PathParam("file") String file) {
        return getFile(FilePatchConstants.getPiczFolder() + uid + "/" + file);
    }
}
