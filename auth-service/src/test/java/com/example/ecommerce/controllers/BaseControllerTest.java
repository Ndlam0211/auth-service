package com.example.ecommerce.controllers;

import com.example.ecommerce.dtos.PageableResponse;
import com.example.ecommerce.services.BaseServiceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public abstract class BaseControllerTest <D, C, U, ID, S extends BaseServiceInterface<D,C,U,ID>> {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected S service;

    protected abstract String getApiPath();
    protected abstract String getTestKeyword();
    protected abstract String getEntityName();
    protected abstract PageableResponse<D> getTestPageable();
    protected abstract ResultActions getExpectedResponseKeywordFilterData(ResultActions result, List<D> filteredDTOs) throws Exception;
    protected abstract ResultActions getExpectedResponseSimpleFilterData(ResultActions result, List<D> filteredDTOs) throws Exception;
    protected abstract List<D> getTestDTOs();
    protected abstract List<D> getTestKeywordFilteredDTOs(List<D> testDTOs, String keyword);
    protected abstract List<D> getTestSimpleFilteredDTOs(List<D> testDTOs,Map<String, String[]> filters);
    protected abstract Map<String, String[]> getTestSimpleFilters();

    // Test trường hợp get không có filter: get all
    @Test
    void list_NoFilter_ShouldReturnAllRecords() throws Exception {
        // Arrange
        List<D> testDTOs = getTestDTOs();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        when(service.getAll(captor.capture())).thenReturn(testDTOs);

        // Act
        mockMvc.perform(get(getApiPath() + "/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(getEntityName()+"s fetched successfully"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.error").doesNotExist());

        // Assert
        verify(service).getAll(captor.getValue());
    }

    // Test trường hợp get có filter: keyword=admin
    @Test
    void list_WithKeywordFilter_ShouldReturnFilteredRecords() throws Exception {
        // Arrange
       // Create test data by calling abstract method that provides test DTOs
        List<D> testDTOs = getTestDTOs();

        // List DTOs to List DTOs which have name containing "admin"
        List<D> filteredDTOs = getTestKeywordFilteredDTOs(testDTOs, getTestKeyword());

        // Create an ArgumentCaptor to capture Map parameters passed to service.getAll()
        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        // Mock service.getAll() to return test DTOs and capture the input parameters
        when(service.getAll(captor.capture())).thenReturn(filteredDTOs);

        // Perform GET request to API endpoint with keyword parameter
        ResultActions actions = getExpectedResponseKeywordFilterData(mockMvc.perform(get(getApiPath() + "/all")
                .param("keyword", getTestKeyword())
                .contentType(MediaType.APPLICATION_JSON))
                , filteredDTOs);

        // Perform GET request to API endpoint with keyword parameter
        actions.andExpect(status().isOk())  // Verify HTTP 200 status
        // Verify response JSON structure and values
                .andExpect(jsonPath("$.success").value(true))  // Check success flag
                .andExpect(jsonPath("$.message").value(getEntityName()+"s fetched successfully"))  // Verify success message
                .andExpect(jsonPath("$.status").value("OK"))  // Verify status text
                .andExpect(jsonPath("$.data").isArray()) // Verify data is JSON array
                .andExpect(jsonPath("$.timestamp").exists())  // Verify timestamp exists
                .andExpect(jsonPath("$.errors").doesNotExist())  // Verify no errors array
                .andExpect(jsonPath("$.error").doesNotExist());  // Verify no error field

        // Get captured parameters and verify keyword value
        Map<String, String[]> params = captor.getValue();
        assertThat(params.get("keyword")).containsExactly(getTestKeyword());

        // Verify that service.getAll() was called with captured parameters
        verify(service).getAll(captor.getValue());
    }

    // Test trường hợp get có simple filter: id=1, name=abc,...
    @Test
    void list_WithSimpleFilter_ShouldReturnFilteredRecords() throws Exception {
        // Arrange
        List<D> testDTOs = getTestDTOs();

        Map<String, String[]> filters = getTestSimpleFilters();

        List<D> filteredDTOs = getTestSimpleFilteredDTOs(testDTOs,filters);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, String[]>> captor = ArgumentCaptor.forClass(Map.class);

        when(service.getAll(captor.capture())).thenReturn(filteredDTOs);

        MockHttpServletRequestBuilder request = get(getApiPath() + "/all")
                .contentType(MediaType.APPLICATION_JSON);

        filters.forEach((key, values) -> {
            for (String value : values) {
                request.param(key, value);
            }
        });

        ResultActions actions = getExpectedResponseSimpleFilterData(mockMvc.perform(request), filteredDTOs);

        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value(getEntityName()+"s fetched successfully"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.errors").doesNotExist())
                .andExpect(jsonPath("$.error").doesNotExist());

        Map<String, String[]> params = captor.getValue();

        for (Map.Entry<String, String[]> entry : filters.entrySet()){
            String key = entry.getKey();
            String[] values = entry.getValue();
            assertThat(params.get(key)).containsExactly(values);
        }

        verify(service).getAll(captor.getValue());
    }

    // Test trường hợp get có complex filter: age[lt]=30, price[gte]=1000,...


    // Test trường hợp get có tất cả các loại filter: keyword, simple, complex


    // Test trường hợp lỗi
}
