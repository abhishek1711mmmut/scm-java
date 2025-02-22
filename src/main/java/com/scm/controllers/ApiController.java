package com.scm.controllers;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVWriter;
import com.scm.entities.Contact;
import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.ContactService;
import com.scm.services.UserService;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ContactService contactService;

    @Autowired
    private UserService userService;

    @GetMapping("/contact/{contactId}")
    public Contact getContact(@PathVariable String contactId) {
        return contactService.getById(contactId);
    }

    // export contact in CSV controller
    @GetMapping("/contacts/export")
    public ResponseEntity<String> exportContactsToCSV(Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        String userId = user.getUserId();
        List<Contact> contacts = contactService.getByUserID(userId);

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

    // export contact in excel controller
    @GetMapping("/contacts/export-excel")
    public ResponseEntity<byte[]> exportContactsToExcel(Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        String userId = user.getUserId();
        List<Contact> contacts = contactService.getByUserID(userId);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Contacts");
        Row headerRow = sheet.createRow(0);
        String[] columns = { "Name", "Email", "Phone Number", "Address", "Description" };

        for (int i = 0; i < columns.length; i++) {
            headerRow.createCell(i).setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (Contact contact : contacts) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(contact.getName());
            row.createCell(1).setCellValue(contact.getEmail());
            row.createCell(2).setCellValue(contact.getPhoneNumber());
            row.createCell(3).setCellValue(contact.getAddress());
            row.createCell(4).setCellValue(contact.getDescription());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=contacts.xlsx");

        return new ResponseEntity<>(bos.toByteArray(), headers, HttpStatus.OK);
    }

    @GetMapping("/contacts/recent")
    public List<Contact> getRecentContacts(Authentication authentication) {
        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);
        String userId = user.getUserId();
        return contactService.getByUserID(userId);
    }

}
