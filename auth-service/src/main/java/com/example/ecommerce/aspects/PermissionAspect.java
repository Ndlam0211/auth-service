package com.example.ecommerce.aspects;

import com.example.ecommerce.annotations.RequirePermission;
import com.example.ecommerce.config.CustomPermissionEvaluator;
import com.example.ecommerce.config.UserDetailsConfig;
import com.example.ecommerce.controllers.BaseController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect
@Component
public class PermissionAspect {
    @Autowired
    private CustomPermissionEvaluator customPermissionEvaluator;

    // Check if the user has the required permission to access the method
    // This method is called before the method that use annotation "RequirePermission" called
    @Before("@annotation(requirePermission)")
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission){
        Object target = joinPoint.getTarget(); // get the object of the method that use annotation "RequirePermission"

        if (target instanceof BaseController){
            BaseController<?,?,?,?> baseController = (BaseController<?,?,?,?>) target;

            String module = baseController.getModule().getPrefix();
            String permission = module + ":" + requirePermission.action();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(!customPermissionEvaluator.hasPermission(authentication,permission)){
                throw new AccessDeniedException("You don't have permission to perform this action");
            }

//            if("list".equals(requirePermission.action()) || "paginate".equals(requirePermission.action())){
//                handleListPermission(joinPoint, authentication, module, requirePermission.viewAll());
//            }
        }
    }

//    private void handleListPermission(JoinPoint joinPoint, Authentication authentication, String module, String viewAll){
//        Object[] args = joinPoint.getArgs();
//        String permission = module + viewAll;
//
//        boolean checkViewAll = customPermissionEvaluator.hasPermission(authentication, permission) && !viewAll.isEmpty();
//
//        if (!checkViewAll){
//            for (Object arg : args) {
//                if(arg instanceof HttpServletRequest){
//                    HttpServletRequest request = (HttpServletRequest) arg;
//                    Map<String, String[]> params = request.getParameterMap();
//
//                    UserDetailsConfig userDetailsConfig = (UserDetailsConfig) authentication.getPrincipal();
//                    Long userId = userDetailsConfig.getId();
//                }
//            }
//        }
//    }
}

