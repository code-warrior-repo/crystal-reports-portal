package com.company.reports.crystal;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CrystalResourceCleaner {

    private static final Logger log = LoggerFactory.getLogger(CrystalResourceCleaner.class);

    public void disposeViewer(Object viewer) {
        invokeIfPresent(viewer, "dispose");
    }

    public void closeReport(Object reportClientDocument) {
        invokeIfPresent(reportClientDocument, "close");
    }

    private void invokeIfPresent(Object target, String methodName) {
        if (target == null) {
            return;
        }
        try {
            Method method = target.getClass().getMethod(methodName);
            method.invoke(target);
        } catch (NoSuchMethodException noMethod) {
            log.debug("Crystal object {} has no {} method", target.getClass().getName(), methodName);
        } catch (Exception exception) {
            log.warn("Unable to call Crystal cleanup method {}", methodName, exception);
        }
    }
}
