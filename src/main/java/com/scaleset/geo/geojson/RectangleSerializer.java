package com.scaleset.geo.geojson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.spatial4j.core.shape.Rectangle;


/**
 * The Rectangle Serializer serializes a Rectangle to ElasticSearch Envelope representation.
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
public class RectangleSerializer extends JsonSerializer<Rectangle> {

   
    @Override
    public void serialize(Rectangle value, JsonGenerator generator, SerializerProvider provider) throws IOException {
    	write(value, generator);
    }

   
    void write(Rectangle rectangle, JsonGenerator gen) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("type", "Envelope");
        gen.writeFieldName("coordinates");
        gen.writeStartArray();
        //upper left first
        writeCoordinate(rectangle.getMinX(), rectangle.getMaxY(), gen);
       // lower right next
        writeCoordinate(rectangle.getMaxX(), rectangle.getMinY(), gen);
        gen.writeEndArray();
        gen.writeEndObject();
    }

    void writeCoordinate(double x, double y, JsonGenerator gen) throws IOException {
        gen.writeStartArray();
        gen.writeNumber(x);
        gen.writeNumber(y);
        gen.writeEndArray();
    }

}
