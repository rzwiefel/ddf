package ddf.catalog.persistent.operation;

import org.pcollections.HashTreePSet;
import org.pcollections.PSet;

import ddf.catalog.operation.Request;

public interface PRequest extends Request {
    default PSet<String> getStoreIds() {
        return HashTreePSet.empty();
    }
}
