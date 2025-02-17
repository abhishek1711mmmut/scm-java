package com.scm.validators;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 2;

    // type

    // height

    // width

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if (file == null || file.isEmpty()) {
            // context.disableDefaultConstraintViolation();
            // context.buildConstraintViolationWithTemplate("File cannot be empty").addConstraintViolation();
            return true;
        }

        // file size
        if (file.getSize() > MAX_FILE_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File size should not be more than 2MB")
                    .addConstraintViolation();
            return false;
        }

        // resolution
        // try {
        // BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        // bufferedImage.getWidth() // you can get height and width and add more
        // validations

        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        return true;
    }

}
