package io.nirahtech.ai.softgpt.system;

import java.io.IOException;

public interface SystemctlService extends Service {
    public static final String SYSTEM_COMMAND = "systemctl";

    public static final int SUCCESS_EXIT_CODE = 0;
    public static final int GENERAL_FAILURE_EXIT_CODE = 1;
    public static final int BAD_USAGE_EXIT_CODE = 2;
    public static final int UNIT_NOT_ACTIVATED_EXIT_CODE = 3;
    public static final int UNIT_DISABLED_OR_HiDDEN_EXIT_CODE = 4;
    public static final int UNIT_NOT_FOUND_EXIT_CODE = 5;
    public static final int PERMISSION_DENIED_EXIT_CODE = 6;
    public static final int INTERNAL_ERROR_EXIT_CODE = 7;

    default void reloadDaemon() throws IOException {

    }
}
