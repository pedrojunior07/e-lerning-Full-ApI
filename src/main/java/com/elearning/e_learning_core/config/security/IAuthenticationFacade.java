package com.elearning.e_learning_core.config.security;

import com.elearning.e_learning_core.model.Usr;

public interface IAuthenticationFacade {

    Usr getPrincipal();

}
