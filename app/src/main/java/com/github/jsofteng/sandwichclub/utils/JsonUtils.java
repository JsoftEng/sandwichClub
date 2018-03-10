package com.github.jsofteng.sandwichclub.utils;

import com.github.jsofteng.sandwichclub.model.Sandwich;

import android.util.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static Sandwich parseSandwichJson(String json) throws IOException {
        InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        JsonReader reader = new JsonReader(new InputStreamReader(is,"UTF-8"));

        return readSandwich(reader);
    }

    private static Sandwich readSandwich(JsonReader reader)throws IOException{
        Sandwich sandwich = new Sandwich();
        String data;

        reader.beginObject();
        while (reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("mainName")){
                data = reader.nextString();
                if(data.length() > 0){
                    sandwich.setMainName(data);
                }else{
                    sandwich.setMainName("Not Available");
                }
            }else if(name.equals("alsoKnownAs")) {
                sandwich.setAlsoKnownAs(readStringArray(reader));
            }else if(name.equals("placeOfOrigin")){
                data = reader.nextString();
                if (data.length() > 0) {
                    sandwich.setPlaceOfOrigin(data);
                }else{
                    sandwich.setPlaceOfOrigin("Not Available");
                }
            }else if(name.equals("description")) {
                data = reader.nextString();
                if(data.length() > 0) {
                    sandwich.setDescription(data);
                }else{
                    sandwich.setDescription("Not Available");
                }
            }else if(name.equals("image")){
                sandwich.setImage(reader.nextString());
            }else if(name.equals("ingredients")){
                sandwich.setIngredients(readStringArray(reader));
            }else{
                reader.skipValue();
            }
        }
        reader.endObject();

        return sandwich;
    }

    private static List<String> readStringArray(JsonReader reader) throws IOException{
        List<String> strings = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()){
            strings.add(reader.nextString());
        }
        reader.endArray();

        return strings;
    }
}
