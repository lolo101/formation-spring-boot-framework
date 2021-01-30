import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomnessInverterTest {

    @InjectMocks
    private RandomnessInverter sut;

    @Mock
    private RandomnessSource source;

    @ParameterizedTest
    @CsvSource({"1.0, 1.0", "10.0, 0.1", "100.0, 0.01", "0, Infinity"})
    void should_return_inverse(double input, double expected) {
        when(source.nextUniformRandom()).thenReturn(input);

        double actual = sut.nextInvert();

        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_return_NaN_on_NaN_input() {
        when(source.nextUniformRandom()).thenReturn(Double.NaN);

        double actual = sut.nextInvert();

        Assertions.assertThat(actual).isNaN();
    }
}