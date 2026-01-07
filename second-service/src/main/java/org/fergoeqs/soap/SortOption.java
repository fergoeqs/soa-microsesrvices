package org.fergoeqs.soap;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "SortOption", propOrder = {
    "field",
    "direction",
    "priority"
})
public class SortOption {

    protected String field;
    protected String direction;
    protected Integer priority;

    @XmlElement(required = true)
    public String getField() {
        return field;
    }

    public void setField(String value) {
        this.field = value;
    }

    @XmlElement
    public String getDirection() {
        return direction;
    }

    public void setDirection(String value) {
        this.direction = value;
    }

    @XmlElement
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer value) {
        this.priority = value;
    }
}
