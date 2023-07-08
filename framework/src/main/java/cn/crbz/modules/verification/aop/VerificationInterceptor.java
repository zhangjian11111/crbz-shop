package cn.crbz.modules.verification.aop;

import cn.crbz.common.enums.ResultCode;
import cn.crbz.common.exception.ServiceException;
import cn.crbz.modules.verification.aop.annotation.Verification;
import cn.crbz.modules.verification.entity.enums.VerificationEnums;
import cn.crbz.modules.verification.service.VerificationService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * 验证码验证拦截
 *
 * @author Chopper
 */
@Aspect
@Configuration
@Slf4j
public class VerificationInterceptor {

    @Autowired
    private VerificationService verificationService;

    @Before("@annotation(cn.crbz.modules.verification.aop.annotation.Verification)")
    public void interceptor(JoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Verification verificationAnnotation = method.getAnnotation(Verification.class);
        VerificationEnums verificationEnums = verificationAnnotation.type();
        String uuid = verificationAnnotation.uuid();
        boolean result = verificationService.check(uuid, verificationEnums);
        if (result) {
            return;
        }
        throw new ServiceException(ResultCode.VERIFICATION_ERROR);
    }
}
