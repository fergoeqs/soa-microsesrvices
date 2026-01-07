package org.fergoeqs.soap;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "SortOptions", propOrder = {
    "sortOption"
})
public class SortOptions {

    protected List<SortOption> sortOption;

    @XmlElement(name = "sortOption")
    public List<SortOption> getSortOption() {
        if (sortOption == null) {
            sortOption = new ArrayList<>();
        }
        return this.sortOption;
    }

    public void setSortOption(List<SortOption> sortOption) {
        this.sortOption = sortOption;
    }
}
