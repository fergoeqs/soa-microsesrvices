package org.fergoeqs.soap;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "", propOrder = {
    "minAnnualTurnover",
    "maxAnnualTurnover",
    "filters",
    "sort",
    "page",
    "size"
})
@XmlRootElement(name = "FilterByTurnoverRequest", namespace = "http://fergoeqs.org/orgdirectory")
public class FilterByTurnoverRequest {

    protected int minAnnualTurnover;
    protected int maxAnnualTurnover;
    protected FilterConditions filters;
    protected SortOptions sort;
    protected Integer page;
    protected Integer size;

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory", required = true)
    public int getMinAnnualTurnover() {
        return minAnnualTurnover;
    }

    public void setMinAnnualTurnover(int value) {
        this.minAnnualTurnover = value;
    }

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory", required = true)
    public int getMaxAnnualTurnover() {
        return maxAnnualTurnover;
    }

    public void setMaxAnnualTurnover(int value) {
        this.maxAnnualTurnover = value;
    }

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory")
    public FilterConditions getFilters() {
        return filters;
    }

    public void setFilters(FilterConditions value) {
        this.filters = value;
    }

    @XmlElement(namespace = "http://fergoeqs.org/orgdirectory")
    public SortOptions getSort() {
        return sort;
    }

    public void setSort(SortOptions value) {
        this.sort = value;
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
