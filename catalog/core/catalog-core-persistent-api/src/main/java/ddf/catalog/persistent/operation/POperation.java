package ddf.catalog.persistent.operation;

import java.io.Serializable;
import java.util.Map;

import org.pcollections.PMap;

import ddf.catalog.operation.Operation;

public interface POperation extends Operation {
    PMap<String, Serializable> getProperties();

    POperation addPropertyValue(String name, Serializable value);

    POperation setProperties(Map<String, Serializable> values);

    POperation addProperties(Map<String, Serializable> values);
}
