package ddf.catalog.persistent.data.impl;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ddf.catalog.data.Attribute;
import ddf.catalog.data.AttributeDescriptor;
import ddf.catalog.data.Metacard;
import ddf.catalog.persistent.data.PMetacard;
import ddf.catalog.persistent.data.PMetacardType;

public final class PMetacardImpl implements PMetacard {
    private static final Logger LOGGER = LoggerFactory.getLogger(PMetacardImpl.class);

    private final transient PMap<String, Attribute> map;

    private final transient PMetacardType type;

    private final String sourceId;

    private PMetacardImpl(String sourceId, PMetacardType type, PMap<String, Attribute> map) {
        this.sourceId = sourceId;
        this.type = type;
        this.map = map;
    }

    public static PMetacardImpl empty() {
        return new PMetacardImpl("", PMetacardTypeImpl.empty(), HashTreePMap.empty());
    }

    public static PMetacardImpl from(Metacard metacard) {
        if (metacard instanceof PMetacardImpl) {
            return (PMetacardImpl) metacard;
        }

        PMap<String, Attribute> descriptors = metacard.getMetacardType()
                .getAttributeDescriptors()
                .stream()
                .map(AttributeDescriptor::getName)
                .map(metacard::getAttribute)
                .reduce(HashTreePMap.empty(),
                        (PMap<String, Attribute> res, Attribute val) -> res.plus(val.getName(),
                                val),
                        PMap::plusAll);

        return new PMetacardImpl(metacard.getSourceId(),
                new PMetacardTypeImpl(metacard.getMetacardType()),
                descriptors);
    }

    @Override
    public PMetacardImpl plusAttribute(Attribute attribute) {
        return new PMetacardImpl(sourceId, type, map.plus(attribute.getName(), attribute));
    }

    @Override
    public PMetacard plusSourceId(String sourceId) {
        return new PMetacardImpl(sourceId, type, map);
    }

    @Override
    public Attribute getAttribute(String name) {
        return map.get(name);
    }

    @Override
    public PMetacard setMetacardType(PMetacardType type) {
        return new PMetacardImpl(sourceId, type, map);
    }

    @Override
    @Deprecated
    public void setAttribute(Attribute attribute) {
        throw new RuntimeException("Cannot modify immutable data!");
    }

    @Override
    public PMetacardType getMetacardType() {
        return type;
    }

    @Override
    public String getId() {
        return requestData(ID, String.class);
    }

    public PMetacardImpl setId(String id) {
        return plusAttribute(new PAttributeImpl(ID, id));
    }

    @Override
    public String getMetadata() {
        return requestData(METADATA, String.class);
    }

    public PMetacardImpl setMetadata(String metadata) {
        return plusAttribute(new PAttributeImpl(METADATA, metadata));
    }

    @Override
    public Date getCreatedDate() {
        return requestData(CREATED, Date.class);
    }

    public PMetacardImpl setCreatedDate(Date date) {
        return plusAttribute(new PAttributeImpl(CREATED, date));
    }

    @Override
    public Date getModifiedDate() {
        return requestData(MODIFIED, Date.class);
    }

    public PMetacardImpl setModifiedDate(Date modified) {
        return plusAttribute(new PAttributeImpl(MODIFIED, modified));
    }

    @Override
    public Date getExpirationDate() {
        return requestData(EXPIRATION, Date.class);
    }

    public PMetacardImpl setExpirationDate(Date expiration) {
        return plusAttribute(new PAttributeImpl(EXPIRATION, expiration));
    }

    @Override
    public Date getEffectiveDate() {
        return requestData(EFFECTIVE, Date.class);
    }

    public PMetacardImpl setEffectiveDate(Date effective) {
        return plusAttribute(new PAttributeImpl(EFFECTIVE, effective));
    }

    @Override
    public String getLocation() {
        return requestData(GEOGRAPHY, String.class);
    }

    public PMetacardImpl setLocation(String location) {
        return plusAttribute(new PAttributeImpl(GEOGRAPHY, location));
    }

    @Override
    public String getSourceId() {
        return sourceId;
    }

    @Override
    @Deprecated
    public void setSourceId(String sourceId) {
        throw new RuntimeException("Cannot modify immutable data!");
    }

    @Override
    public String getTitle() {
        return requestData(TITLE, String.class);
    }

    public PMetacardImpl setTitle(String title) {
        return plusAttribute(new PAttributeImpl(TITLE, title));
    }

    @Override
    public URI getResourceURI() {
        String data = requestData(RESOURCE_URI, String.class);
        if (data == null) {
            return null;
        }

        try {
            return new URI(data);
        } catch (URISyntaxException e) {
            LOGGER.debug("Failed parsing resource URI string {}", data);
            return null;
        }
    }

    public PMetacardImpl setResourceURI(URI uri) {
        return plusAttribute(new PAttributeImpl(RESOURCE_URI, uri));
    }

    @Override
    public String getResourceSize() {
        return requestData(RESOURCE_SIZE, String.class);
    }

    public PMetacardImpl setResourceSize(String size) {
        return plusAttribute(new PAttributeImpl(RESOURCE_SIZE, size));
    }

    @Override
    public byte[] getThumbnail() {
        return requestData(THUMBNAIL, byte[].class);
    }

    public PMetacardImpl setThumbnail(byte[] thumbnail) {
        return plusAttribute(new PAttributeImpl(THUMBNAIL, thumbnail));
    }

    @Override
    public String getContentTypeName() {
        return requestData(CONTENT_TYPE, String.class);
    }

    public PMetacardImpl setContentTypeName(String name) {
        return plusAttribute(new PAttributeImpl(CONTENT_TYPE, name));
    }

    @Override
    public String getContentTypeVersion() {
        return requestData(CONTENT_TYPE_VERSION, String.class);
    }

    public PMetacardImpl setContentTypeVersion(String version) {
        return plusAttribute(new PAttributeImpl(CONTENT_TYPE_VERSION, version));
    }

    @Override
    public URI getContentTypeNamespace() {
        String data = requestData(TARGET_NAMESPACE, String.class);
        if (data == null) {
            return null;
        }
        try {
            return new URI(data);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    public PMetacardImpl setContentTypeNamespace(URI namespace) {
        return plusAttribute(new PAttributeImpl(TARGET_NAMESPACE, namespace.toASCIIString()));
    }

    public PMetacardImpl setTags(Set<String> tags) {
        return plusAttribute(new PAttributeImpl(TAGS, new ArrayList<>(tags)));
    }

    public String getPointOfContact() {
        return requestData(POINT_OF_CONTACT, String.class);
    }

    public PMetacardImpl setPointOfContact(String contact) {
        return plusAttribute(new PAttributeImpl(POINT_OF_CONTACT, contact));
    }

    public String getDescription() {
        return requestData(DESCRIPTION, String.class);
    }

    public PMetacardImpl setDescription(String description) {
        return plusAttribute(new PAttributeImpl(DESCRIPTION, description));
    }

    protected <T> T requestData(String attributeName, Class<T> returnType) {

        Attribute attribute = getAttribute(attributeName);

        if (attribute == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Attribute {} was not found, returning null", attributeName);
            }
            return null;
        }

        Serializable data = attribute.getValue();

        if (data == null) {
            return null;
        }

        if (returnType.isAssignableFrom(data.getClass())) {
            return returnType.cast(data);
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(data.getClass()
                        .toString() + " can not be assigned to " + returnType.toString());
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PMetacardImpl pMetacard = (PMetacardImpl) o;

        return new EqualsBuilder().append(map, pMetacard.map)
                .append(type, pMetacard.type)
                .append(sourceId, pMetacard.sourceId)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(map)
                .append(type)
                .append(sourceId)
                .toHashCode();
    }

    private class PAttributeImpl implements Attribute {
        private final String name;

        private final List<Serializable> values;

        private PAttributeImpl(String name, Serializable... values) {
            this(name, Arrays.asList(values));
        }

        private PAttributeImpl(String name, List<Serializable> values) {
            this.name = name;
            this.values = Collections.unmodifiableList(values);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Serializable getValue() {
            return values.isEmpty() ? null : values.get(0);
        }

        @Override
        public List<Serializable> getValues() {
            return values;
        }
    }

}
