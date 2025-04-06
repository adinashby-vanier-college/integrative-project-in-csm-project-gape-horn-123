package com.example.physiplay.components;

import com.example.physiplay.singletons.SimulationManager;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;

public class RegularPolygonRenderer extends Renderer {
    public int sides = 6;
    public float size = 80;
    @Override
    public void initializeShapeCollider() {
        PolygonShape polygon = new PolygonShape();
        polygon.set(getVerticesOfRegularPolygon(), sides);
        parent.fixtureDef.shape = polygon;
    }

    private Vec2[] getVerticesOfRegularPolygon() {
        Vec2[] points = new Vec2[sides];
        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides - Math.PI / 2;
            double x = size * Math.cos(angle) / SimulationManager.getInstance().getScale();
            double y = size * Math.sin(angle) / SimulationManager.getInstance().getScale();
            points[i] = new Vec2((float) x, (float) y);
        }
        return points;
    }

    private double[] getVerticesIndividualCoordinates(int index) {
        if (index != 0 && index != 1) index = 0;
        double[] points = new double[sides];
        for (int i = 0; i < points.length; i++) {
            points[i] = index == 0 ? getVerticesOfRegularPolygon()[i].x * SimulationManager.getInstance().getScale():
                    getVerticesOfRegularPolygon()[i].y * SimulationManager.getInstance().getScale();
        }
        return points;
    }
    @Override
    public void drawShape() {
        gc.fillPolygon(getVerticesIndividualCoordinates(0), getVerticesIndividualCoordinates(1), sides);
        gc.restore();
    }

    @Override
    public void drawHologram() {
        applyTransformationsForHologram();
        gc.fillPolygon(getVerticesIndividualCoordinates(0), getVerticesIndividualCoordinates(1), sides);
        gc.restore();
    }
}
