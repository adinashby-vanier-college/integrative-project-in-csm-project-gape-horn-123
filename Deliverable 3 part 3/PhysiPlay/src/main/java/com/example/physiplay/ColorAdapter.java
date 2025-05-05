package com.example.physiplay;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ColorAdapter extends TypeAdapter<Color> {
    @Override
    public void write(JsonWriter jsonWriter, Color color) throws IOException {
        if (color == null) {
            jsonWriter.nullValue();
            return;
        }
        jsonWriter.beginObject();
        jsonWriter.name("r").value(color.getRed());
        jsonWriter.name("g").value(color.getGreen());
        jsonWriter.name("b").value(color.getBlue());
        jsonWriter.name("a").value(color.getOpacity());
        jsonWriter.endObject();
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        double r = 0, g = 0, b = 0, a = 1;
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "r" -> r = in.nextDouble();
                case "g" -> g = in.nextDouble();
                case "b" -> b = in.nextDouble();
                case "a" -> a = in.nextDouble();
            }
        }
        in.endObject();
        return new Color(r, g, b, a);
    }
}
