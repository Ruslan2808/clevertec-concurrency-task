package ru.clevertec.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.clevertec.model.Request;
import ru.clevertec.model.Response;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class ServerTest {

    @Spy
    private Server server;

    @BeforeEach
    void setUp() {
        doReturn(0).when(server).getRandomDelay();
    }

    @Test
    void checkProcessRequestShouldReturnResponseEqualToValuesSize() {
        Request requestOne = new Request(1);
        Request requestTwo = new Request(10);

        server.processRequest(requestOne);
        Response actualResponse = server.processRequest(requestTwo);

        assertThat(actualResponse.getValue()).isEqualTo(server.getValues().size());
    }

    @Test
    void checkProcessRequestShouldValuesContainsRequestValue() {
        int expectedRequestValue = 1;
        Request request = new Request(expectedRequestValue);

        server.processRequest(request);

        assertThat(server.getValues()).contains(expectedRequestValue);
    }
}
