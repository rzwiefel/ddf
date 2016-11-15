package ddf.catalog.persistent.operation.impl;

import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import ddf.catalog.persistent.operation.POperation;

public class POperationImpl implements POperation {
    private PMap<String, Serializable> properties;

    public POperationImpl(Map<String, Serializable> properties) {
        this(HashTreePMap.from(ofNullable(properties).orElse(emptyMap())));
    }

    private POperationImpl(PMap<String, Serializable> properties) {
        this.properties = properties;
    }

    @Override
    public Set<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public Serializable getPropertyValue(String name) {
        return properties.get(name);
    }

    @Override
    public boolean containsPropertyName(String name) {
        return properties.containsKey(name);
    }

    @Override
    public boolean hasProperties() {
        return !properties.isEmpty();
    }

    @Override
    public PMap<String, Serializable> getProperties() {
        return properties;
    }

    @Override
    public POperation addPropertyValue(String name, Serializable value) {
        return new POperationImpl(properties.plus(name, value));
    }

    @Override
    public POperation setProperties(Map<String, Serializable> map) {
        return new POperationImpl(HashTreePMap.from(map));
    }

    @Override
    public POperation addProperties(Map<String, Serializable> values) {
        return new POperationImpl(properties.plusAll(values));
    }
}
