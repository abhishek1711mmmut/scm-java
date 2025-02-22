package com.scm.controllers;

import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    // user dashboard page
    @RequestMapping(value = "/dashboard")
    public String userDashboard(Model model, Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        String userId = user.getUserId();
        int length = contactService.getByUserID(userId).size();
        model.addAttribute("contactLength", length);
        return "user/dashboard";
    }
}
