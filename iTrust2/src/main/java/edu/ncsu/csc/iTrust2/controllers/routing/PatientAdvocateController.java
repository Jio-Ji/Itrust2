package edu.ncsu.csc.iTrust2.controllers.routing;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class responsible for managing the behavior for the patient
 * advocate Landing Screen
 *
 * @author Qichen Wang
 * @author Shaolong zhou
 * @author Austin Heyward
 * @author Nick Bleuzen
 *
 */
public class PatientAdvocateController {

    /**
     * Returns the PA for the given model
     *
     * @param model
     *            model to check
     * @return role
     */
    @RequestMapping ( value = "pa/index" )
    @PreAuthorize ( "hasRole('ROLE_PA')" )
    public String index ( final Model model ) {
        return "pa/index";
    }

}
