package com.example.ecommerce.controllers;

import com.example.ecommerce.annotations.RequirePermission;
import com.example.ecommerce.dtos.APIResponse;
import com.example.ecommerce.dtos.PageableResponse;
import com.example.ecommerce.enums.PermissionEnum;
import com.example.ecommerce.services.BaseServiceInterface;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Base Controller representing a collection of controllers
 *
 * @param <D> DTO
 * @param <C> StoreRequest
 * @param <U> UpdateRequest
 * @param <ID> Data type of id
 */
@SecurityRequirement(name = "Bearer Authentication")
public abstract class BaseController <D, C, U, ID>{
    protected final BaseServiceInterface<D, C, U, ID> service;

    @Getter
    protected final PermissionEnum module;

    protected abstract String getEntityName();

    public BaseController(BaseServiceInterface<D, C, U, ID> service, PermissionEnum module){
        this.service = service;
        this.module = module;
    }

//    @GetMapping("/debug")
//    public ResponseEntity<?> debug() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Current User Authorities: " + authentication.getAuthorities());
//        return ResponseEntity.ok(authentication.getAuthorities());
//    }

    @Operation(
        summary = "List of records combined with search",
        description = "Return list of records and combine with research filter without paginating"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Success",
            content = @Content(schema = @Schema(implementation = APIResponse.class))
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Access Denied",
            content = @Content(schema = @Schema(implementation = APIResponse.class))
        ),
        @ApiResponse(
            responseCode = "500",
            description = "INTERNAL_SERVER_ERROR",
            content = @Content(schema = @Schema(implementation = APIResponse.class))
        )
    })
    @GetMapping("/all")
    @RequirePermission(action = "list")
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        List<D> response = service.getAll(params);
        return ResponseEntity.ok(APIResponse.ok(response,getEntityName()+"s fetched successfully"));
    }

    @GetMapping
    @RequirePermission(action = "paginate")
    public ResponseEntity<?> paginate(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        PageableResponse<D> response = service.paginate(params);
        return ResponseEntity.ok(APIResponse.ok(response,getEntityName()+"s fetched successfully"));
    }

    @GetMapping("/{id}")
    @RequirePermission(action = "show")
    public ResponseEntity<?> show(@PathVariable ID id){
        D dto = service.show(id);
        APIResponse<D> response = APIResponse.ok(dto,getEntityName()+" fetched successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @RequirePermission(action = "create")
    public ResponseEntity<?> store(@Valid @RequestBody C request) {
        D dto = service.create(request);

        APIResponse<D> response = APIResponse.ok(dto,getEntityName()+" created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @RequirePermission(action = "update")
    public ResponseEntity<?> update(@PathVariable ID id, @Valid @RequestBody U request){
        D dto = service.update(id,request);
        APIResponse<D> response = APIResponse.ok(dto,getEntityName()+" updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @RequirePermission(action = "delete")
    public ResponseEntity<?> delete(@PathVariable ID id){
        service.delete(id);
        return ResponseEntity.ok(APIResponse.message(getEntityName()+" deleted successfully",HttpStatus.NO_CONTENT));
    }

    @DeleteMapping
    @RequirePermission(action = "delete_many")
    public ResponseEntity<?> deleteMany(@RequestBody List<ID> ids){
        Boolean flag = service.deleteMany(ids);
        if (flag)
            return ResponseEntity.ok(APIResponse.message(getEntityName()+"s deleted successfully",HttpStatus.NO_CONTENT));
        return ResponseEntity.badRequest().body(APIResponse.error("DELETE_FAILED","Failed to delete "+getEntityName()+"s: No id found",HttpStatus.BAD_REQUEST));
    }
}
