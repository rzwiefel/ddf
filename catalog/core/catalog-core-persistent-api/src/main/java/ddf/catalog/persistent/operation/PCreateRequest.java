package ddf.catalog.persistent.operation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pcollections.PMap;
import org.pcollections.PSet;
import org.pcollections.PVector;

import ddf.catalog.data.Metacard;
import ddf.catalog.operation.CreateRequest;
import ddf.catalog.persistent.data.PMetacard;

public interface PCreateRequest extends CreateRequest, PRequest, POperation{
    @Deprecated
    PVector<Metacard> getMetacards();

    PVector<PMetacard> getPersistentMetacards();


    PCreateRequest addDestination(String destination);

    PCreateRequest removeDestination(String destination);

    PCreateRequest setDestinations(Set<String> destinations);

    PSet<String> getDestinations();


    PCreateRequest addProperties(Map<String, Serializable> properties);

    PCreateRequest setProperties(Map<String, Serializable> properties);

    PCreateRequest addProperty(String key, Serializable value);

    PCreateRequest removeProperty(String key);


    PCreateRequest addMetacard(PMetacard metacard);

    PCreateRequest removeMetacard(PMetacard metacard);

    PCreateRequest setMetacards(List<PMetacard> metacards);


}
