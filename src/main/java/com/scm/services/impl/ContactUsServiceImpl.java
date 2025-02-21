package com.scm.services.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scm.entities.ContactUs;
import com.scm.repositories.ContactUsRepo;
import com.scm.services.ContactUsService;

@Service
public class ContactUsServiceImpl implements ContactUsService {

    @Autowired
    private ContactUsRepo contactUsRepo;

    @Override
    public void saveContactUsData(ContactUs contactUs) {
        String id = UUID.randomUUID().toString();
        contactUs.setId(id);
        // save contact us data to db
        contactUsRepo.save(contactUs);

    }
}
