package org.fergoeqs.soap;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "FilterConditions", propOrder = {
    "filter"
})
public class FilterConditions {

    protected List<FilterCondition> filter;

    @XmlElement(name = "filter")
    public List<FilterCondition> getFilter() {
        if (filter == null) {
            filter = new ArrayList<>();
        }
        return this.filter;
    }

    public void setFilter(List<FilterCondition> filter) {
        this.filter = filter;
    }
}
