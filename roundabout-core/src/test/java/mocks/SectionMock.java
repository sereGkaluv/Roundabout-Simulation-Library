package mocks;

import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.entity.RoundaboutCar;
import at.fhv.itm3.s2.roundabout.entity.StreetConnector;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class SectionMock {

    private StreetSection section;
    private StreetSection nextSection;
    private StreetSection previousSection;
    private RoundaboutCar firstCar;
    private StreetConnector previousStreetConnector;

    private boolean firstCarCouldEnterNextSection;
    private boolean returnValidNextSection;
    private boolean isSectionEmpty;
    private int nrOfPreviousSections;

    public SectionMock(
        boolean firstCarCouldEnterNextSection,
        boolean returnValidNextSection,
        boolean isSectionEmpty,
        int nrOfPreviousSections
    ) {
        this.firstCarCouldEnterNextSection = firstCarCouldEnterNextSection;
        this.returnValidNextSection = returnValidNextSection;
        this.isSectionEmpty = isSectionEmpty;
        this.nrOfPreviousSections = nrOfPreviousSections;
        initMockingComponents();
    }

    public StreetSection getSection() {
        return section;
    }

    private void initMockingComponents() {
        this.section = Mockito.mock(StreetSection.class);
        this.nextSection = Mockito.mock(StreetSection.class);
        this.previousSection = Mockito.mock(StreetSection.class);
        this.firstCar = Mockito.mock(RoundaboutCar.class);
        this.previousStreetConnector = Mockito.mock(StreetConnector.class);

        when(this.section.firstCarCouldEnterNextSection()).thenReturn(this.firstCarCouldEnterNextSection);

        when(this.section.getFirstCar()).thenReturn(this.firstCar);

        when(this.firstCar.getNextSection()).thenReturn(returnValidNextSection ? this.nextSection : null);

        doNothing().when(this.section).moveFirstCarToNextSection();

        when(this.section.isEmpty()).thenReturn(this.isSectionEmpty);

        doNothing().when(this.section).updateAllCarsPositions();

        when(this.section.getPreviousStreetConnector()).thenReturn(this.previousStreetConnector);

        when(this.previousStreetConnector.getPreviousSections()).then(invocation -> {
            Set<Street> previousSections = new HashSet<>();
            for (int i = 0; i < nrOfPreviousSections; i++) {
                previousSections.add(Mockito.mock(StreetSection.class));
            }
            return previousSections;
        });
    }
}



