package eu.seatter.homemeasurement.collector.controllers;

import eu.seatter.homemeasurement.collector.cache.AlertSystemCache;
import eu.seatter.homemeasurement.collector.model.Measurement;
import eu.seatter.homemeasurement.collector.model.SystemAlert;
import eu.seatter.homemeasurement.collector.services.cache.AlertMeasurementCacheService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: jas
 * Date: 09/05/2019
 * Time: 09:17
 */
@Controller
public class AlertsController {

    private final AlertMeasurementCacheService alertCacheService;
    private final AlertSystemCache alertSystemCache;

    public AlertsController(AlertMeasurementCacheService cache, AlertSystemCache alertSystemCache) {
        this.alertCacheService = cache;
        this.alertSystemCache = alertSystemCache;
    }

    @SuppressWarnings("SameReturnValue")
    @RequestMapping(value="/alert")
    public String index(final Model model) {
//        ZoneId zoneId= ZoneId.systemDefault();

        Map<String, List<Measurement>> allSortedMeasurementAlerts = alertCacheService.getAllSorted();
        for(String id : allSortedMeasurementAlerts.keySet()) {
//            allSortedMeasurementAlerts.get(id).forEach((srec) -> srec.setMeasureTimeUTC(srec.getMeasureTimeUTC().withZoneSameInstant(zoneId)));
            allSortedMeasurementAlerts.get(id).forEach((srec) -> srec.setMeasureTimeUTC(srec.getMeasureTimeUTC()));
        }

        @SuppressWarnings("unchecked")
        List<SystemAlert> systemAlerts = alertSystemCache.getAllSorted();
//        systemAlerts.forEach((srec) -> srec.setAlertTimeUTC(srec.getAlertTimeUTC().withZoneSameInstant(ZoneId.systemDefault())));
        systemAlerts.forEach((srec) -> srec.setAlertTimeUTC(srec.getAlertTimeUTC()));

        model.addAttribute("systemalerts",systemAlerts);
        model.addAttribute("measurementalerts", allSortedMeasurementAlerts);
        return "alert";
    }
}
