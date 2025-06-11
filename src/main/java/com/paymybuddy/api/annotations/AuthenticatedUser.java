package com.paymybuddy.api.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

// TODO clean code
//https://docs.oracle.com/javase/8/docs/api/java/lang/annotation/package-summary.html
@Retention(RUNTIME) // accessible pendant l'exécution de l'appli donc détectable par l'aspct
@Target(METHOD) // définit le type d'element qui peut être annoté
public @interface AuthenticatedUser {

}
