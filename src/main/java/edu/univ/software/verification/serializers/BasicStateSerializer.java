package edu.univ.software.verification.serializers;

import com.google.gson.*;
import edu.univ.software.verification.model.kripke.BasicState;
import edu.univ.software.verification.model.ltl.Atom;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class BasicStateSerializer implements JsonSerializer<BasicState>, JsonDeserializer<BasicState> {
    @Override
    public BasicState deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        BasicState state = new BasicState();
        Set<Atom> atoms = new HashSet<>();
        JsonArray jsonAtoms = jsonElement.getAsJsonObject().get("atoms").getAsJsonArray();
        for (JsonElement jsonAtom : jsonAtoms) {
            atoms.add(jsonDeserializationContext.deserialize(jsonAtom, Atom.class));
        }
        state.setAtoms(atoms);
        state.setInitial(jsonElement.getAsJsonObject().get("initial").getAsBoolean());
        state.setLabel(jsonElement.getAsJsonObject().get("name").getAsString());
        return state;
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
