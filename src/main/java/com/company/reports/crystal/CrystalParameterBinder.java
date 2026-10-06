package com.company.reports.crystal;

import java.lang.reflect.Method;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CrystalParameterBinder {

    public void apply(Object reportClientDocument, Map<String, String[]> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            return;
        }

        Object dataDefController = invoke(reportClientDocument, "getDataDefController");
        Object parameterFieldController = invoke(dataDefController, "getParameterFieldController");

        parameters.forEach((name, values) -> {
            if (values != null && values.length > 0 && values[0] != null && !values[0].isBlank()) {
                invoke(parameterFieldController, "setCurrentValue", "", name, values[0]);
            }
        });
    }

    private Object invoke(Object target, String methodName, Object... args) {
        try {
            Method method = findMethod(target.getClass(), methodName, args.length);
            return method.invoke(target, args);
        } catch (Exception exception) {
            throw new ReportRenderException("Unable to bind Crystal parameter using " + methodName, exception);
        }
    }

    private Method findMethod(Class<?> type, String name, int argCount) {
        for (Method method : type.getMethods()) {
            if (method.getName().equals(name) && method.getParameterCount() == argCount) {
                return method;
            }
        }
        throw new IllegalArgumentException("Method not found: " + name);
    }
}
