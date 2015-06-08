package edu.univ.software.verification.serializers;

import com.google.gson.*;
import edu.univ.software.verification.model.ltl.Atom;

import java.lang.reflect.Type;

public class AtomSerializer implements JsonSerializer<Atom>, JsonDeserializer<Atom>
{

    @Override
    public Atom deserialize( JsonElement jsonElement, Type type,
                             JsonDeserializationContext jsonDeserializationContext ) throws JsonParseException
    {
        return null;
    }

    @Override
    public JsonElement serialize( Atom atom, Type type, JsonSerializationContext jsonSerializationContext )
    {
        return new JsonPrimitive( atom.getName() );
    }
}