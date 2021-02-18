package eu.seatter.homemeasurement.collector.services.database;

import eu.seatter.homemeasurement.collector.TestData;
import eu.seatter.homemeasurement.collector.model.Measurement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 13/02/2021
 * Time: 17:41
 */
class MondoDBTestIT {
    MongoDB mondoDB = new MongoDB();

    @Test
    void givenMeasurement_thenAddToDB() {

        TestData testData = new TestData();

        List<Measurement> measurement = testData.getTestSensorList();
        measurement = testData.getTestMeasurements(measurement);
        for (Measurement m: measurement) {
            assertTrue(mondoDB.addEntry(m));
        }
    }

    @Test
    void whenReturnAll_then2Returned() {
        List<Measurement> measurements = mondoDB.getAll();
        assertTrue(measurements.size() > 0);
    }
}