package ddf.catalog.persistent.data;

import ddf.catalog.data.Attribute;
import ddf.catalog.data.Metacard;

public interface PMetacard extends Metacard {
    PMetacard plusAttribute(Attribute attribute);

    PMetacard plusSourceId(String sourceId);

    PMetacardType getMetacardType();

    PMetacard setMetacardType(PMetacardType metacardType);



}
