package com.example.ecommerce.services;

import com.example.ecommerce.repositories.BaseRepo;
import com.example.ecommerce.config.AppConstants;
import com.example.ecommerce.dtos.PageableResponse;
import com.example.ecommerce.exceptions.ResourceNotFoundException;
import com.example.ecommerce.helpers.FilterParameter;
import com.example.ecommerce.mappers.BaseMapper;
import com.example.ecommerce.specifications.BaseSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

@Service
public abstract class BaseService <E, D, C, U, ID, R extends BaseRepo<E, ID>> {
    @Autowired
    private ApplicationContext applicationContext;

    protected abstract String[] getSearchFields();
    protected abstract R getRepository();
    protected abstract BaseMapper<E,D,C,U> getMapper();
    protected abstract String getEntityName();
    protected String[] getRelations(){return new String[0];} // Override this method in child class to get relations, if no relation return empty array
    protected void preProcessCreate(C request){}
    protected void preProcessUpdate(U request, ID id){}

    public D show(ID id) {
        E entity = getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(),"id",id));

        return getMapper().toDTO(entity);
    }

    public List<D> getAll(Map<String, String[]> params){
        Sort sort = getSort(params);
        Specification<E> specs = buildSpecification(params, getSearchFields());

        List<E> entittList = getRepository().findAll(specs, sort);
        return getMapper().toList(entittList);
    }
    
    public PageableResponse<D> paginate(Map<String, String[]> params){
        int pageNumber = params.containsKey("page") ? Integer.parseInt(params.get("page")[0]) : AppConstants.PAGE_NUMBER ;
        int pageSize = params.containsKey("perpage") ? Integer.parseInt(params.get("perpage")[0]) : AppConstants.PAGE_SIZE ;

        Sort sort = getSort(params);
        Specification<E> specs = buildSpecification(params, getSearchFields());

        Pageable pageDetails = PageRequest.of(pageNumber-1, pageSize, sort);
        Page<E> pageEntities = getRepository().findAll(specs,pageDetails);

        List<E> entities = pageEntities.getContent();

        List<D> DTOs = getMapper().toList(entities);

        PageableResponse<D> response = new PageableResponse<>();
        response.setContent(DTOs);
        response.setPageNumber(pageEntities.getNumber()+1);
        response.setPageSize(pageEntities.getSize());
        response.setTotalElements(pageEntities.getTotalElements());
        response.setTotalPages(pageEntities.getTotalPages());
        response.setLastPage(pageEntities.isLast());

        return response;
    }

    @Transactional
    private void handleManyToManyRelations(E entity, Object request){
        String[] relations = getRelations();
        System.out.println("Relations: " + Arrays.toString(relations));
        for (String relation : relations) {
            try {
                // Get the corresponding field from request using reflection
                Field requestField = request.getClass().getDeclaredField(relation);
                requestField.setAccessible(true);

                // Extract the List of IDs from the request field (e.g., List of Permission IDs)
                @SuppressWarnings("unchecked")
                List<ID> ids = (List<ID>) requestField.get(request);
                System.out.println(relation + " ids: " + ids);
                if (ids != null && !ids.isEmpty()) {
                    // Get the corresponding field from entity using reflection
                    Field entityField = entity.getClass().getDeclaredField(relation);
                    entityField.setAccessible(true);

                    // Get the type of entity in the Set (e.g., Permission.class)
                    ParameterizedType setType = (ParameterizedType) entityField.getGenericType();
                    Class<?> entityClass = (Class<?>) setType.getActualTypeArguments()[0];

                    // Build repository bean name (e.g., "permissionRepo")
                    String repoName = entityClass.getSimpleName() + "Repo";
                    repoName = repoName.toLowerCase().charAt(0) + repoName.substring(1);

                    // Get repository instance from Spring context (e.g., PermissionRepo)
                    @SuppressWarnings("unchecked")
                    BaseRepo<E, ID> repo = (BaseRepo<E, ID>) applicationContext.getBean(repoName);
                    // Fetch all related entities by their IDs
                    List<E> entityList = repo.findAllById(ids);
                    // Check if all IDs are valid
                    long count = repo.countByIdIn(ids);
                    System.out.println(relation + " count: " + count);
                    if (count != ids.size()) {
                        throw new ResourceNotFoundException("Some "+entityClass.getSimpleName()+" Ids","ids",ids);
                    }
                    // Convert the List to a Set
                    Set<E> entitySet = new HashSet<>(entityList);
                    // Set the fetched entities in the main entity's relation field
                    entityField.set(entity, entitySet);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error occurred while processing relationship " + relation + ": " + e.getMessage());
            }
        }
    }

    @Transactional
    public D create(C request) {
        preProcessCreate(request);
        E entity = getMapper().toEntity(request);

        E createdEntity =  getRepository().save(entity); // add new record into original table
        handleManyToManyRelations(createdEntity, request); // add new record into relationship table

        return getMapper().toDTO(createdEntity);
    }

    @Transactional
    public D update(ID id, U request) {
        E existingEntity= getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(),"id",id));

        preProcessUpdate(request, id);

        getMapper().updateEntityFromRequest(request, existingEntity);

        E updatedEntity = getRepository().save(existingEntity);
        handleManyToManyRelations(updatedEntity, request); // add new record into relationship table

        return getMapper().toDTO(updatedEntity);
    }

    @Transactional
    public void delete(ID id){
        getRepository().findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(getEntityName(),"id",id));

        getRepository().deleteById(id);
    }

    @Transactional
    public Boolean deleteMany(List<ID> ids){
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        long count = getRepository().countByIdIn(ids);
        if (count != ids.size()) {
            throw new ResourceNotFoundException("Some "+getEntityName()+" Ids","ids",ids);
        }

        getRepository().deleteAllById(ids);
        return true;
    }

    protected Specification<E> buildSpecification(Map<String, String[]> params, String[] searchFields){
        String keyword = FilterParameter.filterKeyword(params);
        Map<String,String> simpleFilter = FilterParameter.filterSimple(params);
        Map<String,Map<String,String>> complexFilter = FilterParameter.filterComplex(params);

        // Build specification: Search by keyword in multiple fields, filter by multiple simple filters
        return Specification.where(BaseSpecification.<E>keywordSpec(keyword, searchFields))
                .and(BaseSpecification.simpleFilterSpec(simpleFilter))
                .and(BaseSpecification.complexFilterSpec(complexFilter));
    }

    protected Sort getSort(Map<String, String[]> params){
        String sortParam = params.containsKey("sort") ? params.get("sort")[0] : null;
        return createSort(sortParam);
    }

    protected Sort createSort(String sortParam){
        if (sortParam == null || sortParam.isEmpty()){
            return Sort.by(Sort.Order.desc(AppConstants.SORT_BY));
        }

        String[] sortParams = sortParam.split(",");
        String sortBy = sortParams[0];
        String sortDirection = (sortParams.length > 1) ? sortParams[1] : AppConstants.SORT_DIR;

        if (sortDirection.equalsIgnoreCase("asc"))
            return Sort.by(Sort.Order.asc(sortBy));
        else
            return Sort.by(Sort.Order.desc(sortBy));
    }
}
