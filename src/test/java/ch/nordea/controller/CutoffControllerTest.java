package ch.nordea.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@SpringBootTest
class CutoffControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("{method-name}",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint())))
                .build();
    }


    @Test
    void cutoff() throws Exception {
        LocalDate today = LocalDate.now();
        this.mockMvc.perform(get("/api/cutoff/time")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("iso1", "USD")
                        .param("iso2", "EUR")
                        .param("date", today.toString())
                ).andExpect(status().isOk())
                .andDo(document("cutoff",
                        queryParameters(
                                parameterWithName("iso1").description("The ISO code of the first currency"),
                                parameterWithName("iso2").description("The ISO code of the second currency"),
                                parameterWithName("date").attributes(key("type").value("LocalDate"))
                                        .description("The date for which the cutoff time is requested")
                        ),
                        responseFields(
                                fieldWithPath("cutoffType").description("""
                                        The cutoff type for the current day. One of: NEVER, UNTIL, ALWAYS.
                                        NEVER means that trade is not possible for the given date.
                                        UNTIL means that trade is possible until the given cutoff time.
                                        ALWAYS means that trade is possible for the given date."""),
                                fieldWithPath("cutoffTime").attributes(key("type").value("LocalDate")).optional()
                                        .description("The cutoff time for the current day")
                        )
                ));
    }

}