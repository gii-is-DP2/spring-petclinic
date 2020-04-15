package org.springframework.samples.petclinic.web.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RUNTIME)
@PreAuthorize("hasAuthority('admin')")
public @interface IsAdmin {

}
