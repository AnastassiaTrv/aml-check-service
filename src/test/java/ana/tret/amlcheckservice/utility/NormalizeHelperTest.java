package ana.tret.amlcheckservice.utility;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NormalizeHelperTest {

    @Test
    void toNormalized_shouldReplaceDiacritics() {
        var normalized = NormalizeHelper.toNormalized("lälölü");
        assertThat(normalized).isEqualTo("lalolu");
    }

    @Test
    void toNormalized_shouldLower() {
        var normalized = NormalizeHelper.toNormalized("HELLO");
        assertThat(normalized).isEqualTo("hello");
    }

    @Test
    void toNormalized_shouldSplitByWhiteSpaceAndReorderAlphabetically() {
        var normalized = NormalizeHelper.toNormalized("world hello again");
        assertThat(normalized).isEqualTo("again hello world");
    }

    @Test
    void toNormalized_shouldCutNoise() {
        var normalized = NormalizeHelper.toNormalized("mr laden and osama the evil");
        assertThat(normalized).isEqualTo("evil laden osama");
    }
}
