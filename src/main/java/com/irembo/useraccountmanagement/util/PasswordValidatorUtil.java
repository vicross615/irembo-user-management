package com.irembo.useraccountmanagement.util;

import org.passay.*;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.Rule;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by USER on 5/5/2023.
 */
public class PasswordValidatorUtil {

    public static String validatePassword(String password) {
        // Create a password validator with a list of rules
        List<Rule> rules = new ArrayList<>();

        PasswordValidator validator = new PasswordValidator(rules);
        //Rule 1: Password length should be in between
        //8 and 16 characters
        rules.add(new LengthRule(8, 16));
        //Rule 2: No whitespace allowed
        rules.add(new WhitespaceRule());
        //Rule 3.a: At least one Upper-case character
        rules.add(new CharacterRule(EnglishCharacterData.UpperCase, 1));
        //Rule 3.b: At least one Lower-case character
        rules.add(new CharacterRule(EnglishCharacterData.LowerCase, 1));
        //Rule 3.c: At least one digit
        rules.add(new CharacterRule(EnglishCharacterData.Digit, 1));
        //Rule 3.d: At least one special character
        rules.add(new CharacterRule(EnglishCharacterData.Special, 1));

        // Validate the password using the validator
        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return null;
        } else {
            // Return the error messages if the password doesn't meet the requirements
            List<String> messages = validator.getMessages(result);
            return messages.stream().collect(Collectors.joining(", "));
        }
    }
}