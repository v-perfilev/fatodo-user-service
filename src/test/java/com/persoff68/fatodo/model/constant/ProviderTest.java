package com.persoff68.fatodo.model.constant;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderTest {

    @Test
    void testEnum() {
        assertThat(Provider.LOCAL.getValue()).isEqualTo("LOCAL");
        assertThat(Provider.GOOGLE.getValue()).isEqualTo("GOOGLE");
        assertThat(Provider.APPLE.getValue()).isEqualTo("APPLE");
    }

    @Test
    void testContains() {
        boolean isTrue = Provider.contains("LOCAL");
        assertThat(isTrue).isTrue();
        boolean isFalse = Provider.contains("PROVIDER_NOT_EXISTS");
        assertThat(isFalse).isFalse();
    }

}
