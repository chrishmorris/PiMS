package org.pimslims.upgrader;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RevisionNumber {
    private final static String NAME = "pims";

    public final static int REVISION = 45; // change this only when release is ready

    private final static String RELEASE = "PiMSPro 0_7_3 xtalPIMS1_4";

    private final static String TAG = "DM45";

    private final static String AUTHOR = "Chris";

    private final static String DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

    public static String getName() {
        return NAME;
    }

    public static int getRevision() {
        int ret = REVISION;
        if (ret == 11) {
            ret = 12;
        }
        return ret;
    }

    public static String getRelease() {
        return RELEASE;
    }

    public static String getTag() {
        return TAG;
    }

    public static String getAuthor() {
        return AUTHOR;
    }

    public static String getDate() {
        return DATE;
    }

    public static void main(String[] args) {
        System.out.println(DATE);
    }
}
