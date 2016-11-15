package ddf.catalog.persistent.operation.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.pcollections.HashTreePSet;
import org.pcollections.PMap;
import org.pcollections.PSet;
import org.pcollections.PVector;
import org.pcollections.TreePVector;

import ddf.catalog.data.Metacard;
import ddf.catalog.persistent.data.PMetacard;
import ddf.catalog.persistent.operation.PCreateRequest;
import ddf.catalog.persistent.operation.POperation;

public class PCreateRequestImpl extends POperationImpl implements PCreateRequest {
    private final PVector<PMetacard> metacards;

    private final PSet<String> destinations;

    private PCreateRequestImpl(PVector<PMetacard> metacards, PCreateRequest request,
            POperation operation) {
        this(metacards, request.getDestinations(), operation.getProperties());
    }

    private PCreateRequestImpl(PCreateRequest metacards, PSet<String> destinations,
            POperation operation) {
        this(metacards.getPersistentMetacards(), destinations, operation.getProperties());
    }

    private PCreateRequestImpl(PCreateRequest metacards, PCreateRequest destinations,
            PMap<String, Serializable> properties) {
        this(metacards.getPersistentMetacards(), destinations.getDestinations(), properties);
    }

    private PCreateRequestImpl(PVector<PMetacard> metacards, PSet<String> destinations,
            PMap<String, Serializable> properties) {
        super(properties);
        this.metacards = metacards;
        this.destinations = destinations;
    }

    private PCreateRequestImpl pMetacards(PVector<PMetacard> metacards) {
        return new PCreateRequestImpl(metacards, this, this);
    }

    private PCreateRequestImpl pDestinations(PSet<String> destinations) {
        return new PCreateRequestImpl(this, destinations, this);
    }

    private PCreateRequestImpl pProperties(PMap<String, Serializable> properties) {
        return new PCreateRequestImpl(this, this, properties);
    }

    private PCreateRequestImpl pProperties(POperation operation) {
        return new PCreateRequestImpl(this, this, operation.getProperties());
    }

    // TODO (RCZ) - How do we deal with this/do we want to
    @Override
    public PVector<Metacard> getMetacards() {
        throw new RuntimeException("Unavailable");
    }

    @Override
    public PVector<PMetacard> getPersistentMetacards() {
        return metacards;
    }

    @Override
    public PCreateRequest addDestination(String destination) {
        return pDestinations(destinations.plus(destination));
    }

    // TODO (RCZ) - Do we even need remove methods in the api?
    @Override
    public PCreateRequest removeDestination(String destination) {
        return pDestinations(destinations.minus(destination));
    }

    @Override
    public PCreateRequest setDestinations(Set<String> destinations) {
        return pDestinations(HashTreePSet.from(destinations));
    }

    @Override
    public PSet<String> getDestinations() {
        return destinations;
    }

    @Override
    public PCreateRequest addProperties(Map<String, Serializable> properties) {
        return pProperties(super.addProperties(properties));
    }

    @Override
    public PCreateRequest setProperties(Map<String, Serializable> properties) {
        return pProperties(super.setProperties(properties));
    }

    @Override
    public PCreateRequest addProperty(String key, Serializable value) {
        return pProperties(super.addPropertyValue(key, value));
    }

    @Override
    public PCreateRequest removeProperty(String key) {
        return pProperties(super.getProperties()
                .minus(key));
    }

    @Override
    public PCreateRequest addMetacard(PMetacard metacard) {
        return pMetacards(metacards.plus(metacard));
    }

    @Override
    public PCreateRequest removeMetacard(PMetacard metacard) {
        return pMetacards(metacards.minus(metacard));
    }

    @Override
    public PCreateRequest setMetacards(List<PMetacard> metacards) {
        return pMetacards(TreePVector.from(metacards));
    }
}
