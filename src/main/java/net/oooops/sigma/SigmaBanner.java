package net.oooops.sigma;

import net.oooops.sigma.core.Banner;

public class SigmaBanner implements Banner {

    public static final String banner = """                        
            ███████╗██╗ ██████╗ ███╗   ███╗ █████╗
            ██╔════╝██║██╔════╝ ████╗ ████║██╔══██╗
            ███████╗██║██║  ███╗██╔████╔██║███████║
            ╚════██║██║██║   ██║██║╚██╔╝██║██╔══██║
            ███████║██║╚██████╔╝██║ ╚═╝ ██║██║  ██║
            ╚══════╝╚═╝ ╚═════╝ ╚═╝     ╚═╝╚═╝  ╚═╝
            """;

    @Override
    public void printBanner() {
        String version = SigmaVersion.getVersion();
        version = STR." (v\{version})";
        StringBuilder padding = new StringBuilder();
        while (padding.length() < STRAP_LINE_SIZE - (version.length() + SIGMA.length())) {
            padding.append(" ");
        }
        System.out.print(banner);
        System.out.print(STR."""
                \{SIGMA}\{padding.toString()}\{version}
                """);
    }
}
