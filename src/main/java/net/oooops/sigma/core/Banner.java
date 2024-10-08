package net.oooops.sigma.core;

import net.oooops.sigma.SigmaBanner;

public interface Banner {

    int STRAP_LINE_SIZE = 39;
    String SIGMA = ":: Sigma ::";

    static Banner create() {
        return new SigmaBanner();
    }

    void printBanner();

    /**
     * An enumeration of possible values for configuring the Banner.
     */
    enum Mode {

        /**
         * Disable printing of the banner.
         */
        OFF,

        /**
         * Print the banner to System.out.
         */
        CONSOLE,

        /**
         * Print the banner to the log file.
         */
        LOG

    }
}
