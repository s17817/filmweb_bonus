package rest;

import domain.Comment;
import domain.Movie;
import domain.Rating;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/movies")
@Stateless
public class MoviesResources {

    @PersistenceContext
    EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> getAll()
    {
        return em.createNamedQuery("movie.all", Movie.class).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Add(Movie movie){
        em.persist(movie);
        return Response.ok(movie.getId()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();
        if (result == null) {
            return Response.status(404).build();
        }
        return Response.ok(result).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Movie m){
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();
        if(result==null)
            return Response.status(404).build();
        result.setTitle(m.getTitle());
        result.setProductionYear(m.getProductionYear());
        result.setProductionCountry(m.getProductionCountry());
        em.persist(result);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id){
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", id)
                .getSingleResult();
        if(result==null)
            return Response.status(404).build();
        em.remove(result);
        return Response.ok().build();
    }

    @GET
    @Path("/{movieId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getComments(@PathParam("movieId") int movieId){
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", movieId)
                .getSingleResult();
        if(result==null)
            return null;
        return result.getComments();
    }

    @POST
    @Path("/{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") int movieId, Comment comment){
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", movieId)
                .getSingleResult();
        if(result==null)
            return Response.status(404).build();
        result.getComments().add(comment);
        comment.setMovie(result);
        em.persist(comment);
        return Response.ok().build();
    }

    @GET
    @Path("/{movieId}/ratings")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Rating> getRatings(@PathParam("movieId") int movieId){
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", movieId)
                .getSingleResult();
        if(result==null)
            return null;
        return result.getRatings();
    }

    @POST
    @Path("/{id}/ratings")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addRating(@PathParam("id") int movieId, Rating rating){
        Movie result = em.createNamedQuery("movie.id", Movie.class)
                .setParameter("movieId", movieId)
                .getSingleResult();
        if(result==null)
            return Response.status(404).build();
        result.getRatings().add(rating);
        rating.setMovie(result);
        em.persist(rating);
        return Response.ok().build();
    }
}
