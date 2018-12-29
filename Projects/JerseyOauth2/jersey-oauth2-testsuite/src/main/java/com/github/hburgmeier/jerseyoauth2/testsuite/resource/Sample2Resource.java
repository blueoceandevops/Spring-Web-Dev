package com.github.hburgmeier.jerseyoauth2.testsuite.resource;

import javax.jws.WebParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.github.hburgmeier.jerseyoauth2.rs.api.IOAuthPrincipal;
import com.github.hburgmeier.jerseyoauth2.rs.api.annotations.AllowedScopes;
import com.github.hburgmeier.jerseyoauth2.rs.api.annotations.OAuth20;
import com.github.hburgmeier.jerseyoauth2.testsuite.base.SampleEntity;

@Path("/sample2")
@OAuth20
@AllowedScopes(scopes={"defaultScope"})
public class Sample2Resource {

	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public SampleEntity getEntity(@WebParam(name="id") String id, @Context SecurityContext securityContext)
	{
		IOAuthPrincipal principal = (IOAuthPrincipal)securityContext.getUserPrincipal();
		return new SampleEntity(id, principal.getUser().getName(), principal.getClientId());
	}
	
}
