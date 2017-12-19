package at.fhv.itm3.s2.roundabout.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

public interface ILogger {
    Logger LOGGER = LogManager.getLogger(MethodHandles.lookup().lookupClass());
}
