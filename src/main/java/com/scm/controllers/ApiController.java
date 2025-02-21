package com.scm.controllers;

import java.io.StringWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;
import com.scm.entities.Contact;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @GetMapping("/contact/{contactId}")
    public Contact getContact(@PathVariable String contactId) {
        return contactService.getById(contactId);
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportContactsToCSV(Model model, Authentication authentication) {
        String email = Helper.getEmailOfLoggedInUser(authentication);
        List<Contact> contacts = contactService.getByEmail(email);

        StringWriter writer = new StringWriter();
        try (CSVWriter csvWriter = new CSVWriter(writer)) {
            String[] header = { "Name", "Email", "Phone Number", "Address", "Description" };
            csvWriter.writeNext(header);

            for (Contact contact : contacts) {
                String[] data = { contact.getName(), contact.getEmail(), contact.getPhoneNumber(), contact.getAddress(),
                        contact.getDescription() };
                csvWriter.writeNext(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=contacts.csv");

        return new ResponseEntity<>(writer.toString(), headers, HttpStatus.OK);
    }

}
