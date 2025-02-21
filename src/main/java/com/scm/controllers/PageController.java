package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.ContactUs;
import com.scm.entities.User;
import com.scm.forms.ContactUsForm;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactUsService;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

    @Autowired
    UserService userService;

    @Autowired
    ContactUsService contactUsService;

    @Value("${user.profile-pic.default}")
    private String defaultProfilePic;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String homePage() {
        System.out.println("Home page loading");
        return "home";
    }

    @RequestMapping("/about")
    public String aboutPage() {
        System.out.println("About page loading");
        return "about";
    }

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("Services page loading");
        return "services";
    }

    @GetMapping("/contact-us")
    public String contact(Model model) {
        ContactUsForm contactUsForm = new ContactUsForm();
        model.addAttribute("contactUsForm", contactUsForm);
        return new String("contact");
    }

    @RequestMapping(value = "/contact-us", method = RequestMethod.POST)
    public String processContact(@Valid @ModelAttribute ContactUsForm contactUsForm, BindingResult rBindingResult,
            HttpSession session) {
        System.out.println("processing contact");
        // fetch form data
        System.out.println(contactUsForm);

        // validate data
        if (rBindingResult.hasErrors()) {
            return "contact";
        }

        // save to database
        ContactUs contactUs = new ContactUs();
        contactUs.setName(contactUsForm.getName());
        contactUs.setEmail(contactUsForm.getEmail());
        contactUs.setMessage(contactUsForm.getMessage());

        contactUsService.saveContactUsData(contactUs);

        Message message = Message.builder().content("Message sent").type(MessageType.green).build();
        session.setAttribute("message", message);

        return "redirect:/contact-us";
    }

    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    @GetMapping("/register")
    public String register(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    // processing register
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {
        System.out.println("processing register");
        // fetch form data
        System.out.println(userForm);

        // validate data
        if (rBindingResult.hasErrors()) {
            return "register";
        }

        // save to database
        // User user = User.builder()
        // .name(userForm.getName())
        // .email(userForm.getEmail())
        // .password(userForm.getPassword())
        // .about(userForm.getAbout())
        // .phoneNumber(userForm.getPhoneNumber())
        // .profilePic("")
        // .build();

        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setProfilePic(defaultProfilePic);

        User savedUser = userService.saveUser(user);

        System.out.println("user saved");

        // display message
        Message message = Message.builder().content("Registration Successfull").type(MessageType.green).build();
        session.setAttribute("message", message);

        return "redirect:/register";
    }

}
