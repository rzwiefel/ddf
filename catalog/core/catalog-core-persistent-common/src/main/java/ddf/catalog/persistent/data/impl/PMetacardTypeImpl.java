package ddf.catalog.persistent.data.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.PMap;

import ddf.catalog.data.AttributeDescriptor;
import ddf.catalog.data.MetacardType;
import ddf.catalog.persistent.data.PMetacardType;

public class PMetacardTypeImpl implements PMetacardType {
    private final transient PMap<String, AttributeDescriptor> descriptors;

    private final String name;

    public static PMetacardTypeImpl empty() {
        return new PMetacardTypeImpl("", Stream.empty());
    }

    public PMetacardTypeImpl(String name, Set<AttributeDescriptor> descriptors) {
        this(name, descriptors.stream());
    }

    public PMetacardTypeImpl(MetacardType type) {
        this(type.getName(), type.getAttributeDescriptors());
    }

    // TODO (RCZ) - PMetacardType or do we not even need that?
    public PMetacardTypeImpl(String name, MetacardType type,
            Set<AttributeDescriptor> additionalDescriptors) {
        this(name,
                Stream.concat(type.getAttributeDescriptors()
                        .stream(), additionalDescriptors.stream()));
    }

    public PMetacardTypeImpl(String name, List<MetacardType> types) {
        this(name,
                types.stream()
                        .map(MetacardType::getAttributeDescriptors)
                        .flatMap(Set::stream));
    }

    private PMetacardTypeImpl(String name, Stream<AttributeDescriptor> descriptorStream) {
        this(name,
                descriptorStream.reduce(HashTreePMap.empty(),
                        (PMap<String, AttributeDescriptor> res, AttributeDescriptor val) -> res.containsKey(
                                val.getName()) ? res : res.plus(val.getName(), val),
                        PMap::plusAll));
    }

    private PMetacardTypeImpl(String name, PMap<String, AttributeDescriptor> descriptors) {
        this.name = name;
        this.descriptors = descriptors;
    }

    @Override
    public PMetacardType setName(String name) {
        return new PMetacardTypeImpl(name, descriptors);
    }

    @Override
    public PMetacardType setAttributeDescriptor(AttributeDescriptor attributeDescriptor) {
        return new PMetacardTypeImpl(name,
                descriptors.plus(attributeDescriptor.getName(), attributeDescriptor));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Set<AttributeDescriptor> getAttributeDescriptors() {
        return HashTreePSet.from(descriptors.values());
    }

    @Override
    public AttributeDescriptor getAttributeDescriptor(String attributeName) {
        return descriptors.get(attributeName);
    }

    // TODO (RCZ) - Do serialization stuff
}
