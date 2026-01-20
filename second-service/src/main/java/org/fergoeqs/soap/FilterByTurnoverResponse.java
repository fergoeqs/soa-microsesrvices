package org.fergoeqs.soap;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {
    "result"
})
@XmlRootElement(name = "FilterByTurnoverResponse", namespace = "http://fergoeqs.org/orgdirectory")
public class FilterByTurnoverResponse {

    protected Object result;

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory", name = "result")
    public Object getResult() {
        return result;
    }

    public void setResult(Object value) {
        this.result = value;
    }
}
