package com.scaleset.geo.geojson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.shape.Rectangle;
import com.vividsolutions.jts.geom.Coordinate;


/**
 * The Rectangle deserializer deserializes a Rectangle from ElasticSearch Envelope representation.
 * The coordinates consists of the upper left and lower right points of the shape to 
 * represent a bounding rectangle.
 * 
 * <p>
 * <pre>
 * { "type": "Envelope", 
 *    "coordinates": [ [-45.0, 46.0], [45.0, -46.0] ]
 * };
 * </pre>
 * @author sfellah
 *
 */
public class RectangleDeserializer extends JsonDeserializer<Rectangle> {

	@Override
	public Rectangle deserialize(JsonParser jsonParser,
			DeserializationContext deserializationContext) throws IOException,
			JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		Rectangle result = rectangle(node);
		return result;
	}

	Rectangle rectangle(JsonNode node) {
		Rectangle result = null;
		String type = node.get("type").textValue();
		if (type.equalsIgnoreCase("Envelope")) {
			ArrayNode coordinatesArrayNode = (ArrayNode) node
					.get("coordinates");
			Coordinate[] coordinates = toCoordinateArray(coordinatesArrayNode);
			// first coordinate is upper left and second coordinate is lower
			// right
			Coordinate ul = coordinates[0];
			Coordinate lr = coordinates[1];
			SpatialContext spatialContext = SpatialContext.GEO;
			result = spatialContext.makeRectangle(ul.x, lr.x, lr.y, ul.y);
		}
		return result;
	}

	Coordinate[] toCoordinateArray(ArrayNode nodes) {
		Coordinate[] result = new Coordinate[nodes.size()];
		for (int i = 0; i < result.length; ++i) {
			result[i] = toCoordinate((ArrayNode) nodes.get(i));
		}
		return result;
	}

	Coordinate toCoordinate(ArrayNode node) {
		double x = 0, y = 0;
		if (node.size() > 1) {
			x = node.get(0).asDouble();
			y = node.get(1).asDouble();
		}
		Coordinate result = new Coordinate(x, y);
		return result;
	}

}
