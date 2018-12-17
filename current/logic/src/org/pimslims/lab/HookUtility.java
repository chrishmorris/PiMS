package org.pimslims.lab;

public class HookUtility {
    static public Long getID(String hook) {
        if (hook == null || !hook.contains(":")) {
            throw new RuntimeException("invalid hook:" + hook);
        }
        String[] parts = hook.split(":");
        return new Long(parts[1]);
    }

}
