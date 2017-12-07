package at.fhv.itm3.s2.roundabout.test.event.mocks;

import at.fhv.itm3.s2.roundabout.api.entity.IStreetSection;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class SectionMock {

    private static IStreetSection section = Mockito.mock(StreetSection.class);

    public static IStreetSection getSection() {
        return section;
    }

    static {
//        when(section.firstCarCouldEnterNextSection()).thenReturn(true);
//
//        when(section.getFirstCar().getNextSection()).then({
//
//        });
//
//        when(section.moveFirstCarToNextSection()).then({
//
//        });
//
//        when(section.isEmpty()).then({
//
//        });
//
//        when(section.updateAllCarsPositions()).then({
//
//        });
//
//        when(section.getPreviousStreetConnector().getPreviousSections()).then({
//
//        });
    }
}
