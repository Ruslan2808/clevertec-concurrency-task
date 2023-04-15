package ru.clevertec.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.model.Response;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ClientTest {

    @Mock
    private Server server;
    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client(100, 10);
    }

    @ParameterizedTest
    @MethodSource("getClientArguments")
    void checkSendRequestsShouldCallProcessRequest(Client client) {
        int expectedNumberCall = client.getValues().size();
        doReturn(new Response(1)).when(server).processRequest(any());

        client.sendRequests(server);

        verify(server, times(expectedNumberCall)).processRequest(any());
    }

    @ParameterizedTest
    @MethodSource("getClientArguments")
    void checkSendRequestsShouldReturnValuesSize0(Client client) {
        int expectedValuesSize = 0;
        doReturn(new Response(1)).when(server).processRequest(any());

        client.sendRequests(server);

        assertThat(client.getValues()).hasSize(expectedValuesSize);
    }

    @Test
    void checkSendRequestsShouldReturnAccumulator100() {
        int expectedAccumulator = 100;
        doReturn(new Response(1)).when(server).processRequest(any());

        client.sendRequests(server);

        assertThat(client.getAccumulator()).isEqualTo(expectedAccumulator);
    }

    static Stream<Arguments> getClientArguments() {
        return Stream.of(
                Arguments.of(new Client(100, 10)),
                Arguments.of(new Client(250, 25)),
                Arguments.of(new Client(500, 50)),
                Arguments.of(new Client(1000, 100))
        );
    }
}
