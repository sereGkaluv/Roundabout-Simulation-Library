package at.fhv.itm3.s2.roundabout.mocks;

import at.fhv.itm14.trafsim.model.entities.IConsumer;
import at.fhv.itm14.trafsim.model.entities.intersection.Intersection;
import at.fhv.itm3.s2.roundabout.controller.IntersectionController;
import at.fhv.itm3.s2.roundabout.entity.StreetSection;
import desmoj.core.simulator.Model;
import org.mockito.Mockito;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

public class StreetSectionMock extends StreetSection {

    private RouteGenerator routeGenerator;

    public StreetSectionMock(double length, Model model, String modelDescription, boolean showInTrace, RouteGenerator routeGenerator) {
        super(length, model, modelDescription, showInTrace);
        this.routeGenerator = routeGenerator;

        initMockingComponents();
    }

    private void initMockingComponents() {

        this.intersectionController = Mockito.mock(IntersectionController.class);

        when(this.intersectionController.getInDirectionOfIConsumer((Intersection)notNull(), (IConsumer)notNull())).then(invocationOnMock -> {
           Object[] args = invocationOnMock.getArguments();
           IConsumer consumer = (IConsumer)args[1];

            return routeGenerator.getInDirection((StreetSectionMock)consumer);
        });

        when(this.intersectionController.getOutDirectionOfIConsumer((Intersection)notNull(), (IConsumer)notNull())).then(invocationOnMock -> {
            Object[] args = invocationOnMock.getArguments();
            IConsumer consumer = (IConsumer)args[1];

            return routeGenerator.getOutDirection((StreetSectionMock)consumer);
        });
    }
}
