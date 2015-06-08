
package edu.univ.software.verification.model.kripke;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import edu.univ.software.verification.model.KripkeState;
import edu.univ.software.verification.model.KripkeStructure;
import edu.univ.software.verification.model.ltl.Atom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Default Kripke structure implementation
 *
 * @author arthur
 */
public class BasicStructure implements KripkeStructure {
    protected Map<String, KripkeState> states = ImmutableMap.of();
    protected Map<String, Set<String>> transitions = ImmutableMap.of();
    
    public static Builder builder() {
        return new BasicBuilder();
    }
    
    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public BasicStructure() {}
    
    public BasicStructure(Map<String, KripkeState> states, Map<String, Set<String>> transitions) {
        this.states = ImmutableMap.copyOf(states);
        this.transitions = ImmutableMap.copyOf(transitions);
    }
    //</editor-fold>
    
    @Override
    public KripkeState getState(String label) {
        return states.get(label);
    }
    
    @Override
    public Set<KripkeState> getStates() {
        return ImmutableSet.copyOf(states.values());
    }

    public Map<String, Set<String>> getTransitions() {
        return ImmutableMap.copyOf(transitions);
    }

    @Override
    public boolean hasTransition(String from, String to) {
        Set<String> outgoing = transitions.get(from);
        
        return outgoing != null && outgoing.contains(to);
    }
    
    public static class BasicBuilder implements Builder {
        private static final Logger logger = LoggerFactory.getLogger(BasicBuilder.class);
        
        private final Map<String, KripkeState> states = new LinkedHashMap<>();
        private final Map<String, Set<String>> transitions = new LinkedHashMap<>();
        
        private BasicBuilder() {}
        
        @Override
        public BasicBuilder withState(String label) {
            return withState(label, KripkeState.DEFAULT_ATOMS);
        }
        
        @Override
        public BasicBuilder withState(String label, Collection<? extends Atom> atoms) {
            return withState(label, atoms, KripkeState.DEFAULT_INITIAL);
        }
        
        @Override
        public BasicBuilder withState(String label, Collection<? extends Atom> atoms, boolean initial) {
            this.states.put(label, new BasicState(label, atoms, initial));
            
            return this;
        }
        
        @Override
        public BasicBuilder withTransition(String from, String to) throws IllegalArgumentException {
            if (states.get(from) == null || states.get(to) == null) {
                throw new IllegalArgumentException(String.format(
                        "Both states '%s' and '%s' must be present in Kripke structure to add transition", from, to));
            }
            
            Set<String> outgoing = transitions.get(from);
            
            if (outgoing == null) {
                outgoing = new LinkedHashSet<>();
                transitions.put(from, outgoing);
            }
            
            if (!outgoing.add(to)) {
                logger.warn("Repetitive transition from '{}' to '{}' attempted", from, to);
            }
            
            return this;
        }
        
        @Override
        public BasicStructure build() {
            return new BasicStructure(states, transitions);
        }
    }

//    class BasicStructureSerializer implements JsonSerializer<BasicStructure>, JsonDeserializer<BasicStructure>
//    {
//
//        @Override
//        public BasicStructure deserialize( JsonElement jsonElement, Type type,
//                                           JsonDeserializationContext jsonDeserializationContext ) throws JsonParseException
//        {
//            return null;
//        }
//
//        @Override
//        public JsonElement serialize( BasicStructure basicStructure, Type type,
//                                      JsonSerializationContext jsonSerializationContext )
//        {
//            List<JsonObject> states = new ArrayList<>(  );
//            List<JsonObject> transitions = new ArrayList<>(  );
//
//            JsonObject jsonStructure = new JsonObject();
//            jsonStructure.addProperty( "states",  );
//            return null;
//        }
//    }
}
