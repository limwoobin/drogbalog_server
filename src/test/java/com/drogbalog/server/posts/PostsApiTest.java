package com.drogbalog.server.posts;

import com.drogbalog.server.domain.posts.api.PostsApi;
import com.drogbalog.server.domain.posts.service.PostsService;
import com.drogbalog.server.global.config.security.SecurityConfiguration;
import com.drogbalog.server.global.config.security.jwt.JwtAuthenticationInterceptor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.Is.is;

@Log4j2
@WebMvcTest(
        controllers = PostsApi.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfiguration.class
                )
        }
)
public class PostsApiTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostsService postsService;

    @MockBean
    private JwtAuthenticationInterceptor interceptor;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void basic_test() throws Exception {
        log.info("Basic Test");

        final ResultActions actions = mvc.perform(get("/posts/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.test" , is("testData")));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getSearchAll_test() throws Exception {
        log.info("PostsApi Controller Test");

        final String keyword = "test";
        final PageRequest pageRequest = PageRequest.of(5 , 5);

        // given
        given(postsService.searchAll(keyword , pageRequest))
            .willReturn(null);

        // when
        final ResultActions actions = mvc.perform(get("/posts/search/" + keyword))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        actions
                .andExpect(status().isOk())
                .andDo(print());

    }
}
