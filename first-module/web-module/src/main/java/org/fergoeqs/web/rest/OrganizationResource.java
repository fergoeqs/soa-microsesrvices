package org.fergoeqs.web.rest;

import org.fergoeqs.dto.*;
import org.fergoeqs.service.OrganizationServiceRemote;

import jakarta.ejb.EJB;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationResource {

    @EJB
    private OrganizationServiceRemote organizationService;

    public OrganizationResource() {
    }

    @GET
    @Path("/test")
    public String test() {
        return "rabotaet tvar: " + (organizationService != null ? "INJECTED" : "NULL");
    }

    @GET
    @Path("/health")
    public Response health() {
        return Response.ok(Map.of("status", "UP")).build();
    }

    @POST
    @Path("/search")
    public Response searchOrganizations(@Valid FilterRequestDTO filterRequest) {
        try {
            PaginatedResponseDTO response = organizationService.searchOrganizationsWithSorting(filterRequest);
            return Response.ok(response).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response createOrganization(@Valid OrganizationRequestDTO organizationRequest) {
        try {
            OrganizationResponseDTO created = organizationService.createOrganization(organizationRequest);
            return Response
                    .created(URI.create("/organizations/" + created.id()))
                    .entity(created)
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrganizationById(@PathParam("id") Long id) {
        try {
            OrganizationResponseDTO organization = organizationService.getOrganizationById(id);
            return Response.ok(organization).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response updateOrganization(
            @PathParam("id") Long id,
            @Valid OrganizationRequestDTO organizationRequest) {
        try {
            OrganizationResponseDTO updated = organizationService.updateOrganization(id, organizationRequest);
            return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteOrganization(@PathParam("id") Long id) {
        try {
            organizationService.deleteOrganization(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/by-address")
    public Response deleteOrganizationByAddress(@Valid AddressRequestDTO addressRequest) {
        try {
            organizationService.deleteOrganizationByAddress(addressRequest.street());
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/group-by-fullname")
    public Response groupOrganizationsByFullName() {
        try {
            Map<String, Long> result = organizationService.groupOrganizationsByFullName();
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/count-by-address")
    public Response countOrganizationsByAddressLessThan(@Valid AddressRequestDTO addressRequest) {
        try {
            Long count = organizationService.countOrganizationsByAddressLessThan(addressRequest.street());
            return Response.ok(Map.of("count", count)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
//
//    @POST
//    @Path("/count-by-address-exact")
//    public Response countOrganizationsByAddressExact(@Valid AddressRequestDTO addressRequest) {
//        try {
//            Long count = organizationService.countOrganizationsByAddress(addressRequest.street());
//            return Response.ok(Map.of("count", count)).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity(Map.of("error", e.getMessage()))
//                    .build();
//        }
//    }
}