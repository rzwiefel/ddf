package ddf.catalog.persistent.data;

import ddf.catalog.data.AttributeDescriptor;
import ddf.catalog.data.MetacardType;

public interface PMetacardType extends MetacardType {

    PMetacardType setName(String name);

    PMetacardType setAttributeDescriptor(AttributeDescriptor attributeDescriptor);
}
