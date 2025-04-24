package com.example.ecommerce.modules.user.controllers;

import com.example.ecommerce.controllers.BaseController;
import com.example.ecommerce.enums.PermissionEnum;
import com.example.ecommerce.modules.user.dtos.UserCatalogueDTO;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.UpdateRequest;
import com.example.ecommerce.modules.user.services.UserCatalogueService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@Tag(name="UserCatalogue API")
@Validated
@RestController
@RequestMapping("/api/v1/user_catalogues")
public class UserCatalogueController extends BaseController<UserCatalogueDTO, StoreRequest, UpdateRequest, Long> {

    @Override
    protected String getEntityName() {
        return "UserCatalogue";
    }

    public UserCatalogueController(UserCatalogueService userCatalogueService) {
        super(userCatalogueService, PermissionEnum.USER_CATALOGUE);
    }
//    @GetMapping("/all")
//    public ResponseEntity<?> getAll(HttpServletRequest request) {
//        Map<String, String[]> params = request.getParameterMap();
//        List<UserCatalogueDTO> response = userCatalogueService.getAll(params);
//        return ResponseEntity.ok(APIResponse.ok(response,"User Catalogues fetched successfully"));
//    }

//    @GetMapping
//    public ResponseEntity<?> paginate(HttpServletRequest request) {
//        Map<String, String[]> params = request.getParameterMap();
//        PageableResponse<UserCatalogueDTO> response = userCatalogueService.paginate(params);
//        return ResponseEntity.ok(APIResponse.ok(response,"User Catalogues fetched successfully"));
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> show(@PathVariable Long id){
//        UserCatalogueDTO userCatalogueDTO = userCatalogueService.show(id);
//        APIResponse<UserCatalogueDTO> response = APIResponse.ok(userCatalogueDTO,"User Catalogue fetched successfully");
//        return ResponseEntity.ok(response);
//    }

//    @PostMapping
//    public ResponseEntity<?> store(@Valid @RequestBody StoreRequest request) {
//        UserCatalogueDTO userCatalogueDTO = userCatalogueService.create(request);
//
//        APIResponse<UserCatalogueDTO> response = APIResponse.ok(userCatalogueDTO,"User Catalogue created successfully");
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateRequest request){
//        UserCatalogueDTO userCatalogueDTO = userCatalogueService.update(id,request);
//        APIResponse<UserCatalogueDTO> response = APIResponse.ok(userCatalogueDTO,"User Catalogue updated successfully");
//        return ResponseEntity.ok(response);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> delete(@PathVariable Long id){
//        userCatalogueService.delete(id);
//        return ResponseEntity.ok(APIResponse.message("User Catalogue deleted successfully",HttpStatus.NO_CONTENT));
//    }
//
//    @DeleteMapping
//    public ResponseEntity<?> deleteMany(@RequestBody List<Long> ids){
//        Boolean flag = userCatalogueService.deleteMany(ids);
//        if (flag)
//            return ResponseEntity.ok(APIResponse.message("User Catalogues deleted successfully",HttpStatus.NO_CONTENT));
//        return ResponseEntity.badRequest().body(APIResponse.error("DELETE_FAILED","Failed to delete User Catalogues: No id found",HttpStatus.BAD_REQUEST));
//    }
}
