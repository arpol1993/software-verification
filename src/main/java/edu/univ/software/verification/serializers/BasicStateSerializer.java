package edu.univ.software.verification.serializers;

import com.google.gson.*;
import edu.univ.software.verification.model.kripke.BasicState;

import java.lang.reflect.Type;

public class BasicStateSerializer implements JsonSerializer<BasicState>, JsonDeserializer<BasicState> {
    @Override
    public BasicState deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(BasicState basicState, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonState = new JsonObject();
        jsonState.addProperty("name", basicState.getLabel());
        jsonState.addProperty("initial", basicState.isInitial());
        jsonState.add("atoms", jsonSerializationContext.serialize(basicState.getAtoms()));
        return jsonState;
    }
}
