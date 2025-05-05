package com.example.test;
import com.example.physiplay.Vector2;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2Test {
    @ParameterizedTest
    @CsvFileSource(resources = "/vector_test_data.csv", numLinesToSkip = 1)
    void testVectorSum(double x1, double y1, double x2, double y2, double expectedX, double expectedY) {
        Vector2 v1 = new Vector2(x1, y1);
        Vector2 v2 = new Vector2(x2, y2);
        v1.add(v2);
        assertEquals(expectedX, v1.x, 0.0001, "X component should match expected");
        assertEquals(expectedY, v1.y, 0.0001, "Y component should match expected");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/vector_dot_test_data.csv", numLinesToSkip = 1)
    void testVectorDot(double x1, double y1, double x2, double y2, double expectedDot) {
        Vector2 v1 = new Vector2(x1, y1);
        Vector2 v2 = new Vector2(x2, y2);
        double ans = v1.dot(v2);
        assertEquals(ans, expectedDot, "Dot product should match expected");
    }
}
