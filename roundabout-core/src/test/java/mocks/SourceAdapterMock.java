package mocks;

import at.fhv.itm14.trafsim.model.entities.Car;
import at.fhv.itm14.trafsim.model.events.CarDepartureEvent;
import at.fhv.itm14.trafsim.persistence.model.DTO;
import at.fhv.itm3.s2.roundabout.RoundaboutSimulationModel;
import at.fhv.itm3.s2.roundabout.adapter.SourceAdapter;
import at.fhv.itm3.s2.roundabout.api.entity.Street;
import at.fhv.itm3.s2.roundabout.event.RoundaboutEventFactory;
import desmoj.core.simulator.Model;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Matchers.notNull;
import static org.mockito.Mockito.when;

public class SourceAdapterMock extends SourceAdapter {

    private RoundaboutSimulationModel model;
    private int carsToGenerate;
    private int counter;
    private RouteGenerator routeGenerator;
    private RouteType type;

    public SourceAdapterMock(Model model, String description, boolean showInTrace, Street connectedStreet, int carsToGenerate, RouteGenerator routeGenerator, RouteType type) {
        super(model, description, showInTrace, connectedStreet);
        this.model = (RoundaboutSimulationModel)model;
        this.carsToGenerate = carsToGenerate;
        this.routeGenerator = routeGenerator;
        this.type = type;

        initMockingComponents();
    }

    private void initMockingComponents() {

    }

    @Override
    public void startGeneratingCars() {
        if (carsToGenerate > counter) {
            counter++;
            super.startGeneratingCars();
        }
    }
}
