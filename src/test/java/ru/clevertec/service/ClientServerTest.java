package ru.clevertec.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class ClientServerTest {

    @Spy
    private Server server;

    @BeforeEach
    void setUp() {
        doReturn(0).when(server).getRandomDelay();
    }

    @ParameterizedTest
    @MethodSource("getClientArguments")
    void checkClientValuesSizeShouldReturn0(Client client) {
        int expectedSize = 0;

        client.sendRequests(server);

        assertThat(client.getValues()).hasSize(expectedSize);
    }

    @ParameterizedTest
    @MethodSource("getClientAndAccumulatorArguments")
    void checkClientAccumulatorShouldReturnAccumulator(Client client, int expectedAccumulator) {
        client.sendRequests(server);

        assertThat(client.getAccumulator()).isEqualTo(expectedAccumulator);
    }

    @ParameterizedTest
    @MethodSource("getClientArguments")
    void checkServerValuesSizeShouldReturnValuesSize(Client client) {
        int expectedSize = client.getValues().size();

        client.sendRequests(server);

        assertThat(server.getValues()).hasSize(expectedSize);
    }

    @ParameterizedTest
    @MethodSource("getClientArguments")
    void checkServerValuesShouldContainsRequestValues(Client client) {
        List<Integer> expectedValues = client.getValues();

        client.sendRequests(server);

        assertThat(server.getValues()).containsAll(expectedValues);
    }

    static Stream<Arguments> getClientArguments() {
        return Stream.of(
                Arguments.of(new Client(100, 10)),
                Arguments.of(new Client(250, 25)),
                Arguments.of(new Client(500, 50)),
                Arguments.of(new Client(1000, 100))
        );
    }

    static Stream<Arguments> getClientAndAccumulatorArguments() {
        return Stream.of(
                Arguments.of(new Client(100, 10), 5050),
                Arguments.of(new Client(250, 25), 31375),
                Arguments.of(new Client(500, 50), 125250),
                Arguments.of(new Client(1000, 100), 500500)
        );
    }
}
