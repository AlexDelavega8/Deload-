package es.pocketrainer.util.bbdd;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		String auditor = "SISTEMA";

		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//			if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
//				auditor = "ANONIMO";
//			} else {
			auditor = SecurityContextHolder.getContext().getAuthentication().getName();
//			}
		}

		return Optional.of(auditor);
	}
}
