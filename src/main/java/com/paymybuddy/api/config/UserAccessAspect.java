package com.paymybuddy.api.config;

import java.lang.reflect.Field;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.paymybuddy.api.exceptions.ForbiddenAccessException;
import com.paymybuddy.api.model.RoleName;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class UserAccessAspect {
	private static final Logger logger = LogManager.getLogger(UserAccessAspect.class);

	private final JwtDecoder jwtDecoder;

	public UserAccessAspect(JwtDecoder jwtDecoder) {
		this.jwtDecoder = jwtDecoder;
	}

	private String validateAndFormatToken(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			return token.substring(7);
		} else {
			throw new RuntimeException("Authorization header missing or malformed");
		}
	}

	// TODO clean code
	@Around(value = "@annotation(com.paymybuddy.api.annotations.AuthenticatedUser)")
	public Object doUserAccessCheck(ProceedingJoinPoint pjp) throws Throwable {

		// récupérer la requête pour accéder à ses infos (le RequestContextHolder accède
		// au x requêtes via le DispatcherServlet)
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		String token = request.getHeader("Authorization");

		token = validateAndFormatToken(token);

		Jwt decodedToken = jwtDecoder.decode(token);
		long tokenUserId = decodedToken.getClaim("id");

		// récup les param depuis la signature dela méthode
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		String[] paramNames = methodSignature.getParameterNames();

		// récupérer les argument depuis le joinpoint
		Object[] arguments = pjp.getArgs();

		Integer requestUserId = null;

		for (int i = 0; i < paramNames.length; i++) {
			// pathvariable id
			if (paramNames[i].equals("id")) {
				requestUserId = (Integer) arguments[i];
				break;
			}

			// dto contenant une propriété id
			Object argument = arguments[i];
			if (argument != null) {
				try {
					// récup les informations ud champ id du dto passé en argument
					Field idProp = argument.getClass().getDeclaredField("id");
					idProp.setAccessible(true); // éviter l'IllegalAccessException
					Object value = idProp.get(argument); // récup la valeur du champ
					requestUserId = (Integer) value;
					break;
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw e;
				}
			}
		}

		if (tokenUserId != requestUserId) {
			logger.warn("Access denied: user {} tried to access resource of user {} at {}", tokenUserId, requestUserId,
					request.getRequestURI());
			throw new ForbiddenAccessException("Forbidden access");
		}

		return pjp.proceed();
	}

	// TODO refactor
	@Around(value = "@annotation(com.paymybuddy.api.annotations.AuthenticatedUserOrAdmin)")
	public Object doUserOrAdminAccessCheck(ProceedingJoinPoint pjp) throws Throwable {
		// retrieve users role
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		String token = request.getHeader("Authorization");
		token = validateAndFormatToken(token);

		Jwt decodedToken = jwtDecoder.decode(token);
		// suppressio des caractères autour du rôle
		String tokenUserRole = decodedToken.getClaim("authorities").toString().replaceAll("[\\[\\]\"]", "");
		long tokenUserId = decodedToken.getClaim("id");

		if (tokenUserRole.equals(RoleName.ROLE_ADMIN.toString())) {
			logger.info("ADMIN (id: {}) initiated a soft-delete operation on a user via endpoint: {}", tokenUserId,
					request.getRequestURI());
			return pjp.proceed();
		}

		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		String[] paramNames = methodSignature.getParameterNames();

		Object[] arguments = pjp.getArgs();

		Integer requestUserId = null;

		for (int i = 0; i < paramNames.length; i++) {
			// pathvariable id
			if (paramNames[i].equals("id")) {
				requestUserId = (Integer) arguments[i];
				break;
			}

			// dto contenant une propriété id
			Object argument = arguments[i];
			if (argument != null) {
				try {
					// récup les informations ud champ id du dto passé en argument
					Field idProp = argument.getClass().getDeclaredField("id");
					idProp.setAccessible(true); // éviter l'IllegalAccessException
					Object value = idProp.get(argument); // récup la valeur du champ
					requestUserId = (Integer) value;
					break;
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw e;
				}
			}
		}

		if (tokenUserId != requestUserId && !tokenUserRole.equals(RoleName.ROLE_ADMIN.toString())) {
			logger.warn("Access denied: user {} tried to access resource of user {} at {}", tokenUserId, requestUserId,
					request.getRequestURI());
			throw new ForbiddenAccessException("Forbidden access");
		}

		return pjp.proceed();
	}

	@Around(value = "@annotation(com.paymybuddy.api.annotations.AdminOnly)")
	public Object doAdminAccessCheck(ProceedingJoinPoint pjp) throws Throwable {

		// retrieve users role
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();

		String token = request.getHeader("Authorization");
		token = validateAndFormatToken(token);

		Jwt decodedToken = jwtDecoder.decode(token);

		String tokenUserRole = decodedToken.getClaim("authorities").toString().replaceAll("[\\[\\]\"]", "");
		long tokenUserId = decodedToken.getClaim("id");

		if (!tokenUserRole.equals(RoleName.ROLE_ADMIN.toString())) {
			logger.warn("User (id: {}) initiated a hard delete operation on a user via endpoint: {}", tokenUserId,
					request.getRequestURI());
			throw new ForbiddenAccessException("Forbidden.");
		}
		
		logger.info("ADMIN (id: {}) initiated an operation on a user via endpoint: {}", tokenUserId,
				request.getRequestURI());

		return pjp.proceed();
	}
}
