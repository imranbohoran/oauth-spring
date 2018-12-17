package com.tib.oauth.api;

import com.tib.oauth.uuid.UUIDGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UUIDResource.class)
class UUIDResourceTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UUIDGenerator uuidGenerator;

    @Test
    void shouldGenerateUUID() throws Exception {

        String expected = "generated-uuid-value";
        when(uuidGenerator.generate()).thenReturn(expected);

        mockMvc.perform(get("/v1/api/uuid"))
            .andExpect(status().isOk())
            .andExpect(content().string(expected));
    }
}