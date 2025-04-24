package com.example.ecommerce.modules.user.services.impl;

import com.example.ecommerce.modules.user.dtos.UserCatalogueDTO;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.StoreRequest;
import com.example.ecommerce.modules.user.dtos.requests.UserCatalogue.UpdateRequest;
import com.example.ecommerce.modules.user.entities.UserCatalogue;
import com.example.ecommerce.modules.user.mappers.UserCatalogueMapper;
import com.example.ecommerce.modules.user.repositories.UserCatalogueRepo;
import com.example.ecommerce.modules.user.services.UserCatalogueService;
import com.example.ecommerce.services.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCatalogueServiceImpl extends BaseService<UserCatalogue, UserCatalogueDTO, StoreRequest, UpdateRequest,Long, UserCatalogueRepo> implements UserCatalogueService {
    @Autowired
    private UserCatalogueRepo userCatalogueRepo;
    @Autowired
    private UserCatalogueMapper userCatalogueMapper;

//    @Override
//    @Transactional
//    public UserCatalogueDTO create(StoreRequest request) {
//        UserCatalogue userCatalogue = userCatalogueMapper.toEntity(request);
//
//        userCatalogueRepo.save(userCatalogue);
//
//        return userCatalogueMapper.toDTO(userCatalogue);
//    }
//
//    @Override
//    public UserCatalogueDTO show(Long id) {
//        UserCatalogue userCatalogue = userCatalogueRepo.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User Catalogue","id",id));
//
//        return userCatalogueMapper.toDTO(userCatalogue);
//    }
//
//    @Override
//    public PageableResponse<UserCatalogueDTO> paginate(Map<String, String[]> params) {
//        int pageNumber = params.containsKey("page") ? Integer.parseInt(params.get("page")[0]) : AppConstants.PAGE_NUMBER ;
//        int pageSize = params.containsKey("perpage") ? Integer.parseInt(params.get("perpage")[0]) : AppConstants.PAGE_SIZE ;
//        String sortParam = params.containsKey("sort") ? params.get("sort")[0] : null;
//        Sort sort = createSort(sortParam);
//
//        String keyword = FilterParameter.filterKeyword(params);
//        Map<String,String> simpleFilter = FilterParameter.filterSimple(params);
//        Map<String,Map<String,String>> complexFilter = FilterParameter.filterComplex(params);
//
//        // Build specification: Search by keyword in multiple fields, filter by multiple simple filters
//        Specification<UserCatalogue> specs = Specification.where(BaseSpecification.<UserCatalogue>keywordSpec(keyword, "name"))
//                .and(BaseSpecification.simpleFilterSpec(simpleFilter))
//                .and(BaseSpecification.complexFilterSpec(complexFilter));
//
//        Pageable pageDetails = PageRequest.of(pageNumber-1, pageSize, sort);
//        Page<UserCatalogue> pageUserCatalogues = userCatalogueRepo.findAll(specs,pageDetails);
//
//        List<UserCatalogue> userCatalogues = pageUserCatalogues.getContent();
//
//        List<UserCatalogueDTO> userCatalogueDTOs = userCatalogueMapper.toList(userCatalogues);
//
//        PageableResponse<UserCatalogueDTO> response = new PageableResponse<>();
//        response.setContent(userCatalogueDTOs);
//        response.setPageNumber(pageUserCatalogues.getNumber()+1);
//        response.setPageSize(pageUserCatalogues.getSize());
//        response.setTotalElements(pageUserCatalogues.getTotalElements());
//        response.setTotalPages(pageUserCatalogues.getTotalPages());
//        response.setLastPage(pageUserCatalogues.isLast());
//
//        return response;
//    }
//
//    @Override
//    public List<UserCatalogueDTO> getAll(Map<String, String[]> params) {
//        String sortParam = params.containsKey("sort") ? params.get("sort")[0] : null;
//        Sort sort = createSort(sortParam);
//
//        String keyword = FilterParameter.filterKeyword(params);
//        Map<String,String> simpleFilter = FilterParameter.filterSimple(params);
//        Map<String,Map<String,String>> complexFilter = FilterParameter.filterComplex(params);
//
//        // Build specification: Search by keyword in multiple fields, filter by multiple simple filters
//        Specification<UserCatalogue> specs = Specification.where(BaseSpecification.<UserCatalogue>keywordSpec(keyword, "name"))
//                .and(BaseSpecification.simpleFilterSpec(simpleFilter))
//                .and(BaseSpecification.complexFilterSpec(complexFilter));
//
//        List<UserCatalogue> userCatalogues = userCatalogueRepo.findAll(specs,sort);
//
//        return userCatalogueMapper.toList(userCatalogues);
//    }

//    @Override
//    @Transactional
//    public UserCatalogueDTO update(Long id, UpdateRequest request) {
//        UserCatalogue existingUserCatalogue = userCatalogueRepo.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User Catalogue","id",id));
//
//        userCatalogueMapper.updateEntityFromRequest(request, existingUserCatalogue);
//
//        UserCatalogue updatedUserCatalogue = userCatalogueRepo.save(existingUserCatalogue);
//
//        return userCatalogueMapper.toDTO(updatedUserCatalogue);
//    }
//
//
//    @Override
//    @Transactional
//    public void delete(Long id){
//        userCatalogueRepo.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User Catalogue","id",id));
//
//        userCatalogueRepo.deleteById(id);
//    }
//
//    @Override
//    @Transactional
//    public Boolean deleteMany(List<Long> ids){
//        if (ids == null || ids.isEmpty()) {
//            return false;
//        }
//
//        long count = userCatalogueRepo.countByIdIn(ids);
//        if (count != ids.size()) {
//            throw new ResourceNotFoundException("Some User Catalogue Ids","ids",ids);
//        }
//
//        userCatalogueRepo.deleteAllById(ids);
//        return true;
//    }

    @Override
    protected String[] getSearchFields() {
        return new String[]{"name"};
    }

    @Override
    protected String getEntityName() {
        return "UserCatalogue";
    }

    @Override
    protected String[] getRelations() {
        return new String[]{"permissions"};
    }

    @Override
    protected UserCatalogueRepo getRepository() {
        return this.userCatalogueRepo;
    }

    @Override
    protected UserCatalogueMapper getMapper() {
        return this.userCatalogueMapper;
    }
}
