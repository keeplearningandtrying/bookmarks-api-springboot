package com.sivalabs.bookmarks.common;

import static com.sivalabs.bookmarks.common.TestConstants.PROFILE_TEST;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles(PROFILE_TEST)
public abstract class AbstractWebMvcTest {
    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper objectMapper;
}
