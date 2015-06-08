package edu.univ.software.verification.serializers;

import com.google.gson.*;
import edu.univ.software.verification.model.KripkeState;
import edu.univ.software.verification.model.kripke.BasicState;
import edu.univ.software.verification.model.kripke.BasicStructure;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BasicStructureSerializer implements JsonSerializer<BasicStructure>, JsonDeserializer<BasicStructure> {
    @Override
    public BasicStructure deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        BasicStructure structure = new BasicStructure();
        Map<String, KripkeState> states = new HashMap<>();
        JsonArray jsonStates = jsonElement.getAsJsonObject().get("states").getAsJsonArray();
        for (JsonElement jsonState : jsonStates) {
            BasicState deserializedState = jsonDeserializationContext.deserialize(jsonState, BasicState.class);
            states.put(deserializedState.getLabel(), deserializedState);
        }
        structure.setStates(states);
        Map<String, Set<String>> transitions = new HashMap<>();
        JsonArray jsonTransitions = jsonElement.getAsJsonObject().get("transitions").getAsJsonArray();
        for (JsonElement jsonTransition : jsonTransitions) {
            String source = jsonTransition.getAsJsonObject().get("source").getAsString();
            Set<String> destinations = new HashSet<>();
            JsonArray jsonDestinations = jsonTransition.getAsJsonObject().get("destinations").getAsJsonArray();
            for (JsonElement jsonDestination : jsonDestinations) {
                destinations.add(jsonDestination.getAsString());
            }
            transitions.put(source, destinations);
        }
        structure.setTransitions(transitions);
        return structure;
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
