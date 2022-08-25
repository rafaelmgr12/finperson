package com.rafaelmgr12.finperson.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class ExpenseControllerTest {
    @Autowired
    MockMvc mockMvc;

    String jsonElement1 = "{\"descricao\":\"mercado\",\"valor\":\"500\",\"data\":\"20/08/2022\",\"categoria\":\"Alimentação\"}";
    String jsonExpectedElement1 = "{\"id\":1,\"descricao\":\"mercado\",\"valor\":500.00,\"data\":\"20/08/2022\",\"categoria\":\"Alimentação\"}";

    String jsonElement2 = "{\"descricao\":\"acougue\",\"valor\":\"200\",\"data\":\"20/09/2022\",\"categoria\":\"Alimentação\"}";
    String jsonExpectedElement2 = "{\"id\":2,\"descricao\":\"acougue\",\"valor\":200.00,\"data\":\"20/09/2022\",\"categoria\":\"Alimentação\"}";

    String jsonElement3 = "{\"descricao\":\"combustivel\",\"valor\":\"800\",\"data\":\"23/09/2022\",\"categoria\":\"Transporte\"}";
    String jsonExpectedElement3 = "{\"id\":3,\"descricao\":\"combustivel\",\"valor\":800.00,\"data\":\"23/09/2022\",\"categoria\":\"Transporte\"}";

    String jsonElementWithoutCategory = "{\"descricao\":\"outras\",\"valor\":\"80.00\",\"data\":\"08/10/2022\"}";
    String jsonExpectedElementWithoutCategory = "{\"id\":4,\"descricao\":\"outras\",\"valor\":80.00,\"data\":\"08/10/2022\",\"categoria\":\"Outras\"}";

    String jsonExpectedAllElements = "[" + jsonExpectedElement1 + "," + jsonExpectedElement2 + "," + jsonExpectedElement3 + "," + jsonExpectedElementWithoutCategory + "]";
    String jsonExpectedSeptemberElements = "[" + jsonExpectedElement2 + "," + jsonExpectedElement3 + "]";


    @Test
    @Order(1)
    void shouldReturnCreatedOnValidPost() throws Exception {

        URI uri = new URI("/despesas");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(jsonElement1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());


        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(jsonElement2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());


        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(jsonElement3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated());
    }


    @Test
    @Order(2)
    void shouldAssignCategoryToUncategorizedElementOnPosting() throws Exception {

        URI uri = new URI("/despesas");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(jsonElementWithoutCategory)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isCreated())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals(jsonExpectedElementWithoutCategory, jsonResponse);
    }


    @Test
    @Order(3)
    void shouldReturnBadRequestOnDuplicatePost() throws Exception {

        URI uri = new URI("/despesas");

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(jsonElement1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    @Order(4)
    void shouldReturnBadRequestWhenPostMissingOneAttribute() throws Exception {

        URI uri = new URI("/despesas");
        String json = "{\"descricao\":\"outro\",\"valor\":\"80\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    @Order(5)
    void shouldReturnBadRequestWhenPostWithInvalidDate() throws Exception {

        URI uri = new URI("/despesas");
        String json = "{\"descricao\":\"outro\",\"valor\":\"80\",\"data\":\"20/15/2022\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    @Order(6)
    void shouldReturnOkWhenSearchingAllElements() throws Exception {

        URI uri = new URI("/despesas");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals(jsonExpectedAllElements, jsonResponse);
    }


    @Test
    @Order(7)
    void shouldReturnOkWhenSearchingElementsByDescription() throws Exception {

        URI uri = new URI("/despesas?descricao=combustivel");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals("[" + jsonExpectedElement3 + "]", jsonResponse);
    }


    @Test
    @Order(8)
    void shouldReturnOkWhenSearchingInexistentElementsByDescription() throws Exception {

        URI uri = new URI("/despesas?descricao=notExistent");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals("[]", jsonResponse);
    }


    @Test
    @Order(9)
    void shouldReturnOkWhenSearchingOneElement() throws Exception {

        // Element 1

        URI uri1 = new URI("/despesas/1");

        MvcResult mvcResult1 = mockMvc.perform(MockMvcRequestBuilders
                        .get(uri1))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        String jsonResponse1 = mvcResult1.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals(jsonExpectedElement1, jsonResponse1);

        // Element 2

        URI uri2 = new URI("/despesas/2");

        MvcResult mvcResult2 = mockMvc.perform(MockMvcRequestBuilders
                        .get(uri2))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        String jsonResponse2 = mvcResult2.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals(jsonExpectedElement2, jsonResponse2);
    }



    @Test
    @Order(10)
    void shouldReturnNotFoundWhenSearchingInexistestElement() throws Exception {

        URI uri = new URI("/despesas/5");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }


    @Test
    @Order(11)
    void shouldReturnBadRequestOnSearchingByWrongMonth() throws Exception {

        URI uri = new URI("/despesas/2022/15");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    @Order(12)
    void shouldReturnOkWhenSearchingMonthWithResults() throws Exception {

        URI uri = new URI("/despesas/2022/09");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                .andReturn();

        String jsonResponse = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        assertEquals(jsonExpectedSeptemberElements, jsonResponse);
    }


    @Test
    @Order(13)
    void shouldReturnNotFoundWhenUpdatingWithInexistentId() throws Exception {

        URI uri = new URI("/despesas/5");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .content(jsonElement3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }


    @Test
    @Order(14)
    void shouldReturnBadRequestWhenDuplicatingElementOnUpdate() throws Exception {

        URI uri = new URI("/despesas/3");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .content(jsonElement2)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    @Order(15)
    void shouldReturnOkOnValidUpdate() throws Exception {

        URI uri = new URI("/despesas/3");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .content(jsonElement3)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }


    @Test
    @Order(16)
    void shouldReturnBadRequestWhenUpdatingWithInvalidDate() throws Exception {

        URI uri = new URI("/despesas/3");
        String json = "{\"descricao\":\"outro\",\"valor\":\"80\",\"data\":\"20/15/2022\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest());
    }


    @Test
    @Order(17)
    void shouldReturnNotFoundWhenTryingToDeleteInexistentId() throws Exception {

        URI uri = new URI("/despesas/5");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound());
    }


    @Test
    @Order(18)
    void shouldReturnOkOnDeleteValidId() throws Exception {

        URI uri = new URI("/despesas/3");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(uri))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk());
    }
}
