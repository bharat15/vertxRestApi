package com.vertx.restapi;

import java.util.ArrayList;
import java.util.List;

import com.vertx.entity.Product;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

public class HelloWorldVerticleAPI extends AbstractVerticle {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldVerticleAPI.class);
	
	public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();
        
        //passing config to deployment options
//        DeploymentOptions deploymentOptions = new DeploymentOptions();
//        deploymentOptions.setConfig(new JsonObject().put("http.port", 8080));
        
        ConfigRetriever configRetriever = ConfigRetriever.create(vertx);
        
        configRetriever.getConfig(completionHandler -> {
        	
        	if(completionHandler.succeeded()) {
        		JsonObject config = completionHandler.result();
        		
        		DeploymentOptions deploymentOptions = new DeploymentOptions();
        		deploymentOptions.setConfig(config);
        		// deploy HelloWorldVerticleAPI verticle
                vertx.deployVerticle(new HelloWorldVerticleAPI(), deploymentOptions);
        	}
        	
        	JsonObject responseObject = new JsonObject();
    		
    		Product firstItem = new Product(1,"prod1", "description 1");
    		Product secondItem = new Product(2,"prod2", "description 2");
    		
    		List<Product> products = new ArrayList<Product>();
    		
    		products.add(firstItem);
    		products.add(secondItem);
    		
    		responseObject.put("products", products);
        });
        
    }
	
	@Override
	public void start() {
		LOGGER.info("Verticle started");
		
//		vertx.createHttpServer()
//		.requestHandler(routingContext -> {
//			routingContext.response().end("Hi from Vert.x");
//		})
//		.listen(8080);
		
		Router router = Router.router(vertx);
		
//		router.get("/yo.html")
//		.handler(routingContext -> {
//			
//			File file = new File("C:\\Users\\bharat\\eclipse-workspace\\vertx-rest-api\\src\\main\\resources\\webroot\\yo.html");
//			String result = "";
//			
//			try {
//				
//				BufferedReader br = new BufferedReader(new FileReader(file));
//				String st = "";
//				while((st = br.readLine()) != null) {
//					
//					result = result + st;
//				}
//				
//				if(result.contains("{name}")) {
//					result = result.replace("{name}", "bharat");
//				}
//				
//				if(result.contains("{address}")) {
//					result = result.replace("{address}", "India");
//				}
//				
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			routingContext.response().putHeader("content/type", "text/html").end(result);
//		});
		
		//API routing
		router.get("/products").handler(this::getAllProducts);
		
		router.get("/products/:id").handler(this::getProductsById);
		
		router.post("/products").handler(this::createProducts);
		
		router.put("/products/:id").handler(this::updateProducts);
		
		router.delete("/products/:id").handler(this::deleteProducts);
		
		// default if no routes are matched
		router.route().handler(StaticHandler.create().setCachingEnabled(false));
		
		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port"));
	}
	
	private void getAllProducts(RoutingContext routingContext) {
//		JsonObject jsonObject = new JsonObject();
//		
//		JsonArray items = new JsonArray();
//		
//		JsonObject item1 = new JsonObject();
//		
//		item1.put("itemname", "item1");
//		item1.put("itemdescription", "item1 description");
//		items.add(item1);
//		
//		JsonObject item2 = new JsonObject();
//		
//		item2.put("itemname", "item2");
//		item2.put("itemdescription", "item2 description");
//		items.add(item2);
//		
//		jsonObject.put("products", items);
		
		JsonObject responseObject = new JsonObject();
		
		Product firstItem = new Product(1,"prod1", "description 1");
		Product secondItem = new Product(2,"prod2", "description 2");
		
		List<Product> products = new ArrayList<Product>();
		
		products.add(firstItem);
		products.add(secondItem);
		
		responseObject.put("products", products);
		
		routingContext.response().putHeader("content-type", "application/json").end(Json.encodePrettily(responseObject));
	}
	
	public void getProductsById(RoutingContext routingContext) {
		
		final String productId = routingContext.request().getParam("id");
		
		Product firstItem = new Product(Integer.parseInt(productId), "prod"+ productId, "description"+productId);
		
		routingContext.response()
		.setStatusCode(200)
		.putHeader("content-type", "application/json")
		.end(Json.encodePrettily(firstItem));
		
	}
	
	public void updateProducts(RoutingContext routingContext) {
		
		final String productId = routingContext.request().getParam("id");
		
		Product updatedItem = new Product(Integer.parseInt(productId), "prod"+ productId, "description"+productId);
		
		routingContext.response()
		.setStatusCode(200)
		.putHeader("content-type", "application/json")
		.end(Json.encodePrettily(updatedItem));
		
	}
	
	public void deleteProducts(RoutingContext routingContext) {
		
		routingContext.response()
		.setStatusCode(200)
		.putHeader("content-type", "application/json")
		.end("");
	}
	
	public void createProducts(RoutingContext routingContext) {
		
		JsonObject requestBody = routingContext.getBodyAsJson();
		
		int id = Integer.parseInt(requestBody.getString("id"));
		String number = requestBody.getString("number");
		String description = requestBody.getString("description");
		
		Product newItem = new Product(id, number, description);
		
		routingContext.response()
		.setStatusCode(200)
		.putHeader("content-type", "application/json")
		.end(Json.encodePrettily(newItem));
	}
	
	
	@Override
	public void stop() {
		LOGGER.info("Verticle stopped");
	}
}
