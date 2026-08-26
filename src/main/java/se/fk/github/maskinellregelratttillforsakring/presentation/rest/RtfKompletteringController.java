package se.fk.github.maskinellregelratttillforsakring.presentation.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import se.fk.rimfrost.framework.regel.presentation.rest.KompletteringController;

@Produces("application/json")
@Consumes("application/json")
@ApplicationScoped
@Path("/regel/rtf-maskinell")
public class RtfKompletteringController extends KompletteringController<String, RtfKompletteringSvar>
{
}
