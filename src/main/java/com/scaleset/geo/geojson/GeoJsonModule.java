package com.scaleset.geo.geojson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.spatial4j.core.shape.Rectangle;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GeoJsonModule extends SimpleModule {

    static GeometryDeserializer GEO_DE = new GeometryDeserializer();

    public GeoJsonModule() {
        this(null);
    }

    public GeoJsonModule(Integer precision) {
        // deserializers - Jackson requires a deserializer for each subclass of geometry
        GeometryDeserializer de = new GeometryDeserializer();
        addDeserializer(Geometry.class, new GeometryDeserializer<Geometry>());
        addDeserializer(GeometryCollection.class, new GeometryDeserializer<GeometryCollection>());
        addDeserializer(Point.class, new GeometryDeserializer<Point>());
        addDeserializer(LinearRing.class, new GeometryDeserializer<LinearRing>());
        addDeserializer(LineString.class, new GeometryDeserializer<LineString>());
        addDeserializer(MultiLineString.class, new GeometryDeserializer<MultiLineString>());
        addDeserializer(MultiPoint.class, new GeometryDeserializer<MultiPoint>());
        addDeserializer(MultiPolygon.class, new GeometryDeserializer<MultiPolygon>());
        addDeserializer(Polygon.class, new GeometryDeserializer<Polygon>());
        addDeserializer(Envelope.class, new EnvelopeDeserializer());
        addDeserializer(Rectangle.class, new RectangleDeserializer());

        // serializers
        addSerializer(Geometry.class, new GeometrySerializer(precision));
        addSerializer(Envelope.class, new EnvelopeSerializer(precision));
        addSerializer(Rectangle.class, new RectangleSerializer());
    }

}
