package at.fhv.itm3.s2.roundabout.ui.util.config.dto;

import at.fhv.itm3.s2.roundabout.api.util.dto.IDTO;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ui")
public class UIConfig implements IDTO {
    private Map map;
    private Elements elements;

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Elements getElements() {
        return elements;
    }

    public void setElements(Elements elements) {
        this.elements = elements;
    }
}
