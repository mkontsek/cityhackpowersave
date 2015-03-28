package cityhack.powersave;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import cityhack.powersave.json.Appliance;
import cityhack.powersave.json.PowerSaveResponse;
import cityhack.powersave.json.PowerSpike;

import com.cloudant.client.api.Database;
import com.google.gson.Gson;

@Path("/powersave")
public class PowerSaveService {

	private static final Logger log = Logger.getLogger(PowerSaveService.class);

	@POST
	@Consumes("application/json")
	@Path("/addAppliance")
	public Response createAppliance(Appliance appliance) throws Exception {
		Gson gson = new Gson();

		log.info("Create invoked...");
		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			return Response.ok(gson.toJson(new PowerSaveResponse(404, "DB not found")), MediaType.APPLICATION_JSON)
					.build();
		}

		try {
			db.save(appliance);
		} catch(Exception ex) {
			return Response.ok(gson.toJson(new PowerSaveResponse(4, "Appliance already in DB")), MediaType.APPLICATION_JSON)
					.build();
		}

		log.info("Create Successful...");

		return Response.ok(gson.toJson(new PowerSaveResponse(0, "Appliance added")), MediaType.APPLICATION_JSON)
				.build();
	}

	@DELETE
	@Path("/delete/{id}")
	public Response delete(@PathParam("id") String id) throws Exception {
		Database db = null;
		Gson gson = new Gson();

		try {
			db = getDB();
		} catch (Exception re) {
			return Response.ok(gson.toJson(new PowerSaveResponse(404, "DB not found")), MediaType.APPLICATION_JSON)
					.build();
		}
		
		Object doc;
		try {
			doc = db.find(id);
			if(doc != null)
				db.remove(doc);
		} catch (Exception ex) {
			return Response.ok(gson.toJson(new PowerSaveResponse(2, "Object with id " + id + " not found")),
					MediaType.APPLICATION_JSON).build();
		}

		return Response.ok(gson.toJson(new PowerSaveResponse(0, "Object with id " + id + " deleted")),
				MediaType.APPLICATION_JSON).build();
	}

	@GET
	@Path("/getAppliance/{id}")
	public Response getAppliance(@PathParam("id") String id) throws Exception {
		Database db = null;
		Gson gson = new Gson();

		try {
			db = getDB();
		} catch (Exception re) {
			return Response.ok(gson.toJson(new PowerSaveResponse(404, "DB not found")), MediaType.APPLICATION_JSON)
					.build();
		}

		// check if document exists
		Appliance appl;
		try {
			appl = db.find(Appliance.class, id);
		} catch (Exception ex) {
			return Response.ok(gson.toJson(new PowerSaveResponse(2, "Appliance with id " + id + " not found")),
					MediaType.APPLICATION_JSON).build();
		}

		return Response.ok(gson.toJson(appl), MediaType.APPLICATION_JSON).build();
	}

	@POST
	@Consumes("application/json")
	@Path("/addSpike")
	public Response createPowerSpike(PowerSpike powerspike) throws Exception {
		Gson gson = new Gson();

		log.info("Create invoked...");
		Database db = null;
		try {
			db = getDB();
		} catch (Exception re) {
			return Response.ok(gson.toJson(new PowerSaveResponse(404, "DB not found")), MediaType.APPLICATION_JSON)
					.build();
		}

		try {
			db.save(powerspike);
		} catch(Exception ex) {
			return Response.ok(gson.toJson(new PowerSaveResponse(4, "PowerSpike already in DB")), MediaType.APPLICATION_JSON)
					.build();
		}

		log.info("Create Successful...");
		
		return Response.ok(gson.toJson(new PowerSaveResponse(0, "Spike  added")), MediaType.APPLICATION_JSON)
				.build();
	}

	@GET
	@Path("/getSpike/{id}")
	public Response getSpike(@PathParam("id") String id) throws Exception {
		Database db = null;
		Gson gson = new Gson();

		try {
			db = getDB();
		} catch (Exception re) {
			return Response.ok(gson.toJson(new PowerSaveResponse(404, "DB not found")), MediaType.APPLICATION_JSON)
					.build();
		}

		PowerSpike powerspike;
		try {
			powerspike = db.find(PowerSpike.class, "");
		} catch (Exception ex) {
			return Response.ok(gson.toJson(new PowerSaveResponse(2, "Spike with id " + id + " not found")),
					MediaType.APPLICATION_JSON).build();
		}

		return Response.ok(gson.toJson(powerspike), MediaType.APPLICATION_JSON).build();
	}
	
	@GET
	@Path("/getAll")
	public Response getAll() throws Exception {
		Database db = null;
		Gson gson = new Gson();

		try {
			db = getDB();
		} catch (Exception re) {
			return Response.ok(gson.toJson(new PowerSaveResponse(404, "DB not found")), MediaType.APPLICATION_JSON)
					.build();
		}

		try {
			List<PowerSpike> powerspikes = db.view("_all_docs").includeDocs(true).query(PowerSpike.class);
			List<Appliance> appliances = db.view("_all_docs").includeDocs(true).query(Appliance.class);
		
			List<Object> everything = new ArrayList<Object>();
			everything.add(powerspikes);
			everything.add(appliances);
			return Response.ok(gson.toJson(everything), MediaType.APPLICATION_JSON).build();
		} catch (Exception ex) {
			return Response.ok(gson.toJson(new PowerSaveResponse(2, "An error: " + ex.getMessage())),
					MediaType.APPLICATION_JSON).build();
		}
	}
	
	@GET
	@Path("/calculate")
	public Response calcuate(){
		Database db = null;
		Gson gson = new Gson();
		
		try {
			db = getDB();
		} catch (Exception re) {
			return Response.ok(gson.toJson(new PowerSaveResponse(404, "DB not found")), MediaType.APPLICATION_JSON)
					.build();
		}
				
		try {
			List<PowerSpike> powerspikes = db.view("_all_docs").includeDocs(true).query(PowerSpike.class);
			List<Appliance> appliances = db.view("_all_docs").includeDocs(true).query(Appliance.class);
			
			return Response.ok(gson.toJson(CalculationMgr.calculate(powerspikes, appliances)), MediaType.APPLICATION_JSON).build();	
		} catch (Exception ex) {
			return Response.ok(gson.toJson(new PowerSaveResponse(10, "Calculation failed")),
					MediaType.APPLICATION_JSON).build();
		}		
	}
	
	@POST
	@Consumes("application/json")
	@Path("/calculateWithInfo")
	public Response calcuateWithInfo(Everything everything){
		Gson gson = new Gson();
		
		List<PowerSpike> powerspikes = everything.spikes;
		List<Appliance> appliances = everything.appliances;
		
		return Response.ok(gson.toJson(CalculationMgr.calculate(powerspikes, appliances)), MediaType.APPLICATION_JSON).build();			
	}

	private Database getDB() {
		return CloudantClientMgr.getDB();
	}

	static class Everything {
		public List<PowerSpike> spikes;
		public List<Appliance> appliances;
	}
}
