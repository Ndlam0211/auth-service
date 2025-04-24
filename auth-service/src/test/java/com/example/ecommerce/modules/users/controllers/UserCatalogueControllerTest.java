package com.example.ecommerce.modules.users.controllers;

import com.example.ecommerce.config.SecurityConfig;
import com.example.ecommerce.controllers.BaseControllerTest;
import com.example.ecommerce.dtos.PageableResponse;
import com.example.ecommerce.modules.user.controllers.UserCatalogueController;
import com.example.ecommerce.modules.user.dtos.UserCatalogueDTO;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.UpdateRequest;
import com.example.ecommerce.modules.user.services.UserCatalogueService;
import com.example.ecommerce.security.JwtAuthFilter;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.ResultActions;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(value = UserCatalogueController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JwtAuthFilter.class, SecurityConfig.class}))
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureDataJpa
public class UserCatalogueControllerTest extends BaseControllerTest<UserCatalogueDTO, StoreRequest, UpdateRequest, Long, UserCatalogueService> {
    @Override
    protected String getApiPath() {
        return "/api/v1/user_catalogues";
    }

    @Override
    protected String getEntityName() {
        return "UserCatalogue";
    }

    @Override
    protected String getTestKeyword() {
        return "Admin";
    }

    @Override
    protected List<UserCatalogueDTO> getTestDTOs() {
        return Arrays.asList(
            UserCatalogueDTO.builder()
                    .id(1L)
                    .name("Admin")
                    .publish(1)
                    .build(),
            UserCatalogueDTO.builder()
                    .id(2L)
                    .name("SuperAdmin")
                    .publish(1)
                    .build(),
            UserCatalogueDTO.builder()
                    .id(3L)
                    .name("User")
                    .publish(2)
                    .build()
        );
    }

    @Override
    protected ResultActions getExpectedResponseKeywordFilterData(ResultActions result, List<UserCatalogueDTO> filteredDTOs) throws Exception {
         result.andExpect(jsonPath("$.data", hasSize(filteredDTOs.size())));

        for (int i = 0; i < filteredDTOs.size(); i++) {
            result.andExpect(jsonPath("$.data[" +i +"].id").value(filteredDTOs.get(i).getId())) // Verify data[0].id is 1L
                .andExpect(jsonPath("$.data[" +i +"].publish").value(filteredDTOs.get(i).getPublish())) // Verify data[0].publish is 1
                .andExpect(jsonPath("$.data[" +i +"].name", containsString(getTestKeyword())));// Verify data[0].name contains "admin"
        }

        return result;
    }

    @Override
    protected ResultActions getExpectedResponseSimpleFilterData(ResultActions result, List<UserCatalogueDTO> filteredDTOs) throws Exception {
        result.andExpect(jsonPath("$.data", hasSize(filteredDTOs.size())));

        for (int i = 0; i < filteredDTOs.size(); i++) {
            result.andExpect(jsonPath("$.data[" +i +"].id").value(filteredDTOs.get(i).getId())) // Verify data[0].id is 1L
                    .andExpect(jsonPath("$.data[" +i +"].publish").value(filteredDTOs.get(i).getPublish())) // Verify data[0].publish is 1
                    .andExpect(jsonPath("$.data[" +i +"].name").value(filteredDTOs.get(i).getName()));// Verify data[0].name contains "admin"
        }

        return result;
    }

    @Override
    protected PageableResponse<UserCatalogueDTO> getTestPageable() {
        return PageableResponse.<UserCatalogueDTO>builder()
                .content(getTestDTOs())
                .pageNumber(1)
                .pageSize(10)
                .totalElements(20L)
                .totalPages(5)
                .lastPage(true)
                .build();
    }

    @Override
    protected List<UserCatalogueDTO> getTestKeywordFilteredDTOs(List<UserCatalogueDTO> testDTOs, String keyword) {
        return testDTOs.stream()
                .filter(entry -> entry.getName().toLowerCase().contains(getTestKeyword().toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    protected List<UserCatalogueDTO> getTestSimpleFilteredDTOs(List<UserCatalogueDTO> testDTOs, Map<String, String[]> filters) {
        return testDTOs.stream()
                .filter(entry -> filters.entrySet().stream()
                    .allMatch(filter -> {
                        try {
                            String key = filter.getKey(); // publish
                            String[] values = filter.getValue(); // {"2",...}

                            String getterMethod = "get" + key.substring(0,1).toUpperCase() + key.substring(1);
                            Method getter = entry.getClass().getMethod(getterMethod); // getPublish()

                            Object fieldValue = getter.invoke(entry); // entry.getPublish()

                            if (fieldValue == null) return true;

                            // Convert all values to the same type as fieldValue and check if they are equal
                            return Arrays.stream(values)
                                    .map( value -> fieldValue instanceof Integer ? Integer.valueOf(value) : value)
                                    .allMatch(value -> value.equals(fieldValue));

                        } catch (Exception e){
                            return true;
                        }
                    }))
                .collect(Collectors.toList());
    }

    @Override
    protected Map<String, String[]> getTestSimpleFilters() {
        Map<String, String[]> filters = new HashMap<>();
        filters.put("publish", new String[]{"2"});
        return filters;
    }
}
