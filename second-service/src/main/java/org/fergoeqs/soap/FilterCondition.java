package org.fergoeqs.soap;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "FilterCondition", propOrder = {
    "field",
    "operator",
    "value"
})
public class FilterCondition {

    protected String field;
    protected String operator;
    protected Object value;

    @XmlElement(required = true)
    public String getField() {
        return field;
    }

    public void setField(String value) {
        this.field = value;
    }

    @XmlElement(required = true)
    public String getOperator() {
        return operator;
    }

    public void setOperator(String value) {
        this.operator = value;
    }

    @XmlAnyElement(lax = true)
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
