package ana.tret.amlcheckservice.utility;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JwHelperTest {

    // ------- high similarity -------

    @Test
    void calculateJaroWinklerScore_identicalStrings() {
        double s = JwHelper.calculateJaroWinklerScore("bin laden osama", "bin laden osama");
        assertThat(s).isEqualTo(1.0);
    }

    @Test
    void calculateJaroWinklerScore_oneTokenAbsent() {
        double s = JwHelper.calculateJaroWinklerScore("laden osama", "bin laden osama");
        assertThat(s).isEqualTo(0.84);
    }

    @Test
    void calculateJaroWinklerScore_oneLetterAbsent() {
        double s = JwHelper.calculateJaroWinklerScore("bin ladn osama", "bin laden osama");
        assertThat(s).isEqualTo(0.99);
    }

    @Test
    void calculateJaroWinklerScore_oneLetterAbsent_oneTypo() {
        double s = JwHelper.calculateJaroWinklerScore("ben ladn osama", "bin laden osama");
        assertThat(s).isEqualTo(0.93);
    }

    @Test
    void calculateJaroWinklerScore_oneLetterAbsent_oneTokenAbsent() {
        double s = JwHelper.calculateJaroWinklerScore("osama ladn", "bin laden osama");
        assertThat(s).isEqualTo(0.83);
    }

    @Test
    void calculateJaroWinklerScore_initials() {
        double s = JwHelper.calculateJaroWinklerScore("joe l webb", "joe luis webb");
        assertThat(s).isEqualTo(0.94);
    }

    @Test
    void calculateJaroWinklerScore_initials_both_names() {
        double s = JwHelper.calculateJaroWinklerScore("j l webb", "joe luis webb");
        assertThat(s).isEqualTo(0.89);
    }

    // ------- suspicious similarity -------

    @Test
    void calculateJaroWinklerScore_initials_oneTokenAbsent() {
        double s = JwHelper.calculateJaroWinklerScore("l webb", "joe luis webb");
        assertThat(s).isEqualTo(0.77);
    }

    @Test
    void calculateJaroWinklerScore_oneLetterAbsent_oneTokenAbsent_twoLettersSwapped() {
        double s = JwHelper.calculateJaroWinklerScore("asoma ladn", "bin laden osama");
        assertThat(s).isEqualTo(0.73);
    }

    // ------- small or zero similarity -------

    @Test
    void calculateJaroWinklerScore_noOverlap_sameTokensCount_() {
        double s = JwHelper.calculateJaroWinklerScore("oksama labam", "bin laden osama");
        assertThat(s).isEqualTo(0.69);
    }

    @Test
    void calculateJaroWinklerScore_noOverlap_sameTokensCount() {
        double s = JwHelper.calculateJaroWinklerScore("alice in wonderland", "maria at school");
        assertThat(s).isEqualTo(0.42);
    }
}
