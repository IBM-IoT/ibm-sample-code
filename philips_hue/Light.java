import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.informix.json.JSON;
import com.informix.json.JSONSerializers;

public class Light {
	
	protected final String url;
	protected final String username;
	protected final int id;
	protected final Client restClient;
	protected final WebTarget baseTarget;
	protected final String basePath;

	public Light(final String url, final String username, final int id) {
		this.url = url;
		this.username = username;
		this.id = id;
		
		restClient = ClientBuilder.newClient();
		basePath = String.format("api/%s/lights/%d", username, id);
		baseTarget = restClient.target(url).path(basePath);
	}
	
	public Object get(final String path) {
		WebTarget target = baseTarget.path(path);
		Response response = target.request().get();
		String s = response.readEntity(String.class);
		return JSON.parse(s);
	}
	
	public Object put(final String path, final Object data) {
		WebTarget target = baseTarget.path(path);
		final Response response;
		if (data instanceof String) {
			response = target.path(path).request().put(javax.ws.rs.client.Entity.text((String) data));
		} else if (data instanceof BSONObject) {
			response = target.path(path).request().put(createEntity((BSONObject) data));
		} else {
			throw new IllegalArgumentException("data must be an instance of String of BSONObject");
		}
		String s = response.readEntity(String.class);
		return JSON.parse(s);
	}
	
	public Object post(final String path, final Object data) {
		WebTarget target = baseTarget.path(path);
		final Response response;
		if (data instanceof String) {
			response = target.path(path).request().post(javax.ws.rs.client.Entity.text((String) data));
		} else if (data instanceof BSONObject) {
			response = target.path(path).request().post(createEntity((BSONObject) data));
		} else {
			throw new IllegalArgumentException("data must be an instance of String of BSONObject");
		}
		String s = response.readEntity(String.class);
		return JSON.parse(s);
	}

	protected static Entity<String> createEntity(BSONObject doc) {
		String jsonString = JSONSerializers.getStrict().serialize(doc);
		Entity<String> entity = javax.ws.rs.client.Entity.text(jsonString);
		return entity;
	}
	
	public static class StateBuilder {
		final BasicBSONObject doc = new BasicBSONObject();
		
		public BSONObject getStateAsDocument() {
			return doc;
		}
		
		public String getStateAsString() {
			return JSON.serialize(doc);
		}
		
		public StateBuilder on(boolean value) {
			doc.put("on", value);
			return this;
		}
		
		public StateBuilder brightness(int value) {
			if (value < 0 || value > 255) {
				throw new IllegalArgumentException("the brightness must be between 0 and 255, inclusive");
			}
			doc.put("bri", value);
			return this;
		}

		public StateBuilder hue(int value) {
			if (value < 0 || value > 65535) {
				throw new IllegalArgumentException("the hue must be between 0 and 65535, inclusive");
			}
			doc.put("hue", value);
			return this;
		}
		
		public StateBuilder saturation(int value) {
			if (value < 0 || value > 255) {
				throw new IllegalArgumentException("the saturation must be between 0 and 255, inclusive");
			}
			doc.put("sat", value);
			return this;
		}
		
		public StateBuilder xy(float x, float y) {
			doc.put("xy", Arrays.asList(x, y));
			return this;
		}
		
		public StateBuilder colorTemperature(int value) {
			if (value < 150 || value > 500) {
				throw new IllegalArgumentException("the color temperature must be between 150 and 500, inclusive");
			}
			doc.put("ct", value);
			return this;
		}

		public StateBuilder transitionTime(int value) {
			if (value < 0) {
				throw new IllegalArgumentException("the transition time must be greater than or equal to zero");
			}
			doc.put("transitiontime", value);
			return this;
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Light light = new Light("http://localhost/", "newdeveloper", 2);
		Object o = light.get("");
		System.out.println(JSON.serialize(o));
		
		Random random = new Random();
		StateBuilder stateBuilder = new StateBuilder();
		stateBuilder.transitionTime(1);
		for (int i=0; i < 1000; ++i) {
			if (i%3 == 0) {
				int value = random.nextInt() % 255;
				if (value < 0) {
					value *= -1;
				}
				System.out.println(MessageFormat.format("setting brightness to {0}", value));
				stateBuilder.brightness(value).on(true);
			} else if (i%3 == 1) {
				int value = random.nextInt() % 65535;
				if (value < 0) {
					value *= -1;
				}
				System.out.println(MessageFormat.format("setting hue to {0}", value));
				stateBuilder.hue(value).on(true);
			} else {
				System.out.println("setting on to false");
//				stateBuilder.on(false);
			}
			
			light.put("state", stateBuilder.getStateAsDocument());
			try {
				Thread.sleep(200);
			} catch (Exception e) {
				// do nothing
			}
		}  // end for loop
		
	} // end main

}
