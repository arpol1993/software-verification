package edu.univ.software.verification.serializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.univ.software.verification.model.ltl.Atom;
import java.lang.reflect.Type;

public class AtomSerializer implements JsonSerializer<Atom>, JsonDeserializer<Atom> {

    @Override
    public Atom deserialize(JsonElement jsonElement, Type type,
            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Atom(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Atom atom, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(atom.getName());
    }
}
