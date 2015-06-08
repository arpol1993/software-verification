package edu.univ.software.verification.serializers;

import com.google.gson.*;
import edu.univ.software.verification.model.kripke.BasicStructure;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

public class BasicStructureSerializer implements JsonSerializer<BasicStructure>, JsonDeserializer<BasicStructure> {
    @Override
    public BasicStructure deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(BasicStructure basicStructure, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonTransitions = new JsonArray();
        for (Map.Entry<String, Set<String>> transition : basicStructure.getTransitions().entrySet()) {
            JsonObject jsonTransition = new JsonObject();
            jsonTransition.addProperty("source", transition.getKey());
            jsonTransition.add("destinations", jsonSerializationContext.serialize(transition.getValue()));

            jsonTransitions.add(jsonTransition);
        }

        JsonObject jsonStructure = new JsonObject();
        jsonStructure.add("states", jsonSerializationContext.serialize(basicStructure.getStates()));
        jsonStructure.add("transitions", jsonTransitions);
        return jsonStructure;
    }
}
