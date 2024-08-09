package java_projects.demo.validators;

import java_projects.demo.exceptions.SecurityFaultException;

public class SqlInjectionValidator {
    public static void validate(String... inputs) throws SecurityFaultException {
        for (var input : inputs) {
            if (input == null)
                continue;
            if (input.toLowerCase().contains("add"))
                throw new SecurityFaultException();
            if (input.toLowerCase().contains("delete"))
                throw new SecurityFaultException();
            if (input.toLowerCase().contains("update"))
                throw new SecurityFaultException();
            if (input.toLowerCase().contains("select"))
                throw new SecurityFaultException();
        }
    }
}
