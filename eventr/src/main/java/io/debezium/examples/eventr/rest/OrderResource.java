package io.debezium.examples.eventr.rest;

import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import io.debezium.examples.eventr.model.Order;
import io.debezium.examples.eventr.repository.OrderRepository;

@RequestScoped
@Path("/orders")
public class OrderResource {

    @Inject
    private OrderRepository repository;

    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Response create(final Order order) {
        order.setDate(new Date());
        repository.create(order);
        return Response
                .created(UriBuilder.fromResource(OrderResource.class).path(String.valueOf(order.getId())).build())
                .build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response findById(@PathParam("id") final int id) {
        final Order order = repository.getById(id);
        if (order == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(order).build();
    }

    @GET
    @Produces("application/json")
    public List<Order> findOrders(@QueryParam("q") String searchTerm) {
        if (searchTerm != null) {
            return repository.getByCustomerOrEventName(searchTerm);
        }
        else {
            return repository.getAll();
        }
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    @Consumes("application/json")
    public Response update(@PathParam("id") int id, final Order order) {
        order.setDate(new Date());
        repository.save(order);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public Response deleteById(@PathParam("id") final int id) {
        repository.delete(id);
        return Response.noContent().build();
    }

    @GET
    @Path("/event/{id:[0-9][0-9]*}")
    @Produces("application/json")
    public List<Order> listByEvent(@PathParam("id") int eventId) {
        return repository.getByEventId(eventId);
    }
}
