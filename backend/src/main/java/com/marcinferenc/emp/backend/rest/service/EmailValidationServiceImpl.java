package com.marcinferenc.emp.backend.rest.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailValidationServiceImpl implements EmailValidationService {
    private static final int MAX_EMAIL_LENGTH = 254;
    private static final int MAX_LOCAL_PART_LENGTH = 64;
    private static final int MAX_DOMAIN_LENGTH = 253;
    private static final int MAX_DOMAIN_LABEL_LENGTH = 63;

    @Override
    public boolean isValidEmailAddress(String emailAddress) {
        if (emailAddress == null || emailAddress.length() > MAX_EMAIL_LENGTH) {
            return false;
        }

        int atIndex = emailAddress.indexOf('@');
        if (atIndex <= 0 || atIndex != emailAddress.lastIndexOf('@')) {
            return false;
        }

        String localPart = emailAddress.substring(0, atIndex);
        String domain = emailAddress.substring(atIndex + 1);

        return isValidLocalPart(localPart) && isValidDomain(domain);
    }

    private boolean isValidLocalPart(String localPart) {
        if (localPart.isEmpty() || localPart.length() > MAX_LOCAL_PART_LENGTH) {
            return false;
        }

        boolean previousWasDot = false;
        for (int i = 0; i < localPart.length(); i++) {
            char currentChar = localPart.charAt(i);
            if (currentChar == '.') {
                if (i == 0 || i == localPart.length() - 1 || previousWasDot) {
                    return false;
                }
                previousWasDot = true;
                continue;
            }

            if (!isAllowedLocalPartChar(currentChar)) {
                return false;
            }
            previousWasDot = false;
        }

        return true;
    }

    private boolean isAllowedLocalPartChar(char currentChar) {
        return isAsciiLetterOrDigit(currentChar)
            || currentChar == '!'
            || currentChar == '#'
            || currentChar == '$'
            || currentChar == '%'
            || currentChar == '&'
            || currentChar == '\''
            || currentChar == '*'
            || currentChar == '+'
            || currentChar == '-'
            || currentChar == '/'
            || currentChar == '='
            || currentChar == '?'
            || currentChar == '^'
            || currentChar == '_'
            || currentChar == '`'
            || currentChar == '{'
            || currentChar == '|'
            || currentChar == '}'
            || currentChar == '~';
    }

    private boolean isValidDomain(String domain) {
        if (domain.isEmpty() || domain.length() > MAX_DOMAIN_LENGTH || domain.charAt(0) == '.'
            || domain.charAt(domain.length() - 1) == '.') {
            return false;
        }

        int labelStart = 0;
        int labelCount = 0;
        boolean hasDot = false;
        for (int i = 0; i <= domain.length(); i++) {
            if (i == domain.length() || domain.charAt(i) == '.') {
                if (!isValidDomainLabel(domain, labelStart, i)) {
                    return false;
                }
                labelCount++;
                hasDot = hasDot || i < domain.length();
                labelStart = i + 1;
            }
        }

        return hasDot && labelCount >= 2 && hasValidTopLevelDomain(domain);
    }

    private boolean isValidDomainLabel(String domain, int startInclusive, int endExclusive) {
        int length = endExclusive - startInclusive;
        if (length == 0 || length > MAX_DOMAIN_LABEL_LENGTH || domain.charAt(startInclusive) == '-'
            || domain.charAt(endExclusive - 1) == '-') {
            return false;
        }

        for (int i = startInclusive; i < endExclusive; i++) {
            char currentChar = domain.charAt(i);
            if (!isAsciiLetterOrDigit(currentChar) && currentChar != '-') {
                return false;
            }
        }

        return true;
    }

    private boolean hasValidTopLevelDomain(String domain) {
        int lastDotIndex = domain.lastIndexOf('.');
        int tldLength = domain.length() - lastDotIndex - 1;
        if (tldLength < 2) {
            return false;
        }

        for (int i = lastDotIndex + 1; i < domain.length(); i++) {
            if (!isAsciiLetter(domain.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean isAsciiLetterOrDigit(char currentChar) {
        return isAsciiLetter(currentChar) || currentChar >= '0' && currentChar <= '9';
    }

    private boolean isAsciiLetter(char currentChar) {
        return currentChar >= 'a' && currentChar <= 'z' || currentChar >= 'A' && currentChar <= 'Z';
    }
}
