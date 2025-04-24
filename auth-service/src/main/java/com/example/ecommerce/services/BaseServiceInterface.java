package com.example.ecommerce.services;

import com.example.ecommerce.dtos.PageableResponse;

import java.util.List;
import java.util.Map;

public interface BaseServiceInterface <D , C, U, ID> {
    D create(C request);
    D update(ID id, U request);
    PageableResponse<D> paginate(Map<String, String[]> params);
    List<D> getAll(Map<String, String[]> params);
    D show(ID id);
    void delete(ID id);
    Boolean deleteMany(List<ID> ids);
}
