package logic;

import logic.platform.AbsolutePlatform;
import logic.platform.MixedPlatform;
import logic.platform.PercentPlatform;
import logic.platform.Platform;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class HelperClass {
    /**
     *
     */
    //TODO JavaDoc
    static final PercentPlatform COINBASE = new PercentPlatform("Coinbase", 1.49);

    /**
     *
     */
    //TODO JavaDoc
    static final AbsolutePlatform ONVISTA_BANK = new AbsolutePlatform("Onvista Bank", 7);

    /**
     *
     */
    //TODO JavaDoc
    //0,25% of Order Value (min. 8,90 EUR, max. 58,90 EUR)
    static final MixedPlatform MAX_BLUE = new MixedPlatform("Max Blue", 2.5, 8.90);

    /**
     *
     */
    //TODO JavaDoc
    static Set<Platform> createPlatforms() {
        return Set.of(
                COINBASE,
                ONVISTA_BANK,
                MAX_BLUE
        );
    }

    /**
     * @return
     */
    //TODO JavaDoc
    static List<Investment> createInvestments() {

        Investment investment1 = new Investment(LocalDate.of(2021, 12, 10),
                ONVISTA_BANK,
                "Amazon",
                1755,
                1755);

        Investment investment2 = new Investment(LocalDate.of(2020, 8, 22),
                ONVISTA_BANK,
                "Deutsche Bank",
                7.10,
                213.06);

        return Arrays.asList(
                investment1,
                investment2
        );
    }

    /**
     * @return
     */
    //TODO JavaDoc
    static InvestmentBook createInvestmentBook() {
        return new InvestmentBook(createPlatforms(), createInvestments(), new FakeGUI());
    }
}
