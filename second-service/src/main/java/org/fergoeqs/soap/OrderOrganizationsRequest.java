package org.fergoeqs.soap;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {
    "sort",
    "filters",
    "page",
    "size"
})
@XmlRootElement(name = "OrderOrganizationsRequest", namespace = "http://fergoeqs.org/orgdirectory")
public class OrderOrganizationsRequest {

    protected SortOptions sort;
    protected FilterConditions filters;
    protected Integer page;
    protected Integer size;

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory", required = true)
    public SortOptions getSort() {
        return sort;
    }

    public void setSort(SortOptions value) {
        this.sort = value;
    }

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory")
    public FilterConditions getFilters() {
        return filters;
    }

    public void setFilters(FilterConditions value) {
        this.filters = value;
    }

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory", defaultValue = "0")
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer value) {
        this.page = value;
    }

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory", defaultValue = "20")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer value) {
        this.size = value;
    }
}
