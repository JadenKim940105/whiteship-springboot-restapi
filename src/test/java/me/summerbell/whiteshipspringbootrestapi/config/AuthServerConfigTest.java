package me.summerbell.whiteshipspringbootrestapi.config;

import me.summerbell.whiteshipspringbootrestapi.accounts.Account;
import me.summerbell.whiteshipspringbootrestapi.accounts.AccountRole;
import me.summerbell.whiteshipspringbootrestapi.accounts.AccountService;
import me.summerbell.whiteshipspringbootrestapi.common.BaseControllerTest;
import me.summerbell.whiteshipspringbootrestapi.commons.AppProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @DisplayName("인증 토큰을 발급 받는 테스트")
    void getAuthToken() throws Exception {
        mockMvc.perform(post("/oauth/token")
        .with(httpBasic(appProperties.getClientId(), appProperties.getClientPassword()))
        .param("username", appProperties.getUserUsername())
        .param("password", appProperties.getUserPassword())
        .param("grant_type", "password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());
    }

}