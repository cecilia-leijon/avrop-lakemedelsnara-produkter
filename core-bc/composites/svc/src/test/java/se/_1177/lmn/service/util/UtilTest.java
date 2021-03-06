package se._1177.lmn.service.util;

import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Patrik Björk
 */
public class UtilTest {

    @Test
    public void isValidEmailAddress() throws Exception {

        List<String> validEmails = new ArrayList<>();
        validEmails.add("user@domain.com");
        validEmails.add("user@domain.co.in");
        validEmails.add("user.name@domain.com");
        validEmails.add("user_name@domain.com");
        validEmails.add("username@yahoo.corporate.in");

        List<String> invalidEmails = new ArrayList<>();

        invalidEmails.add(".username@yahoo.com");
        invalidEmails.add("username@yahoo.com.");
        invalidEmails.add("username@yahoo..com");
        invalidEmails.add("username@yahoo.c");
        invalidEmails.add("username@yahoo.corporate");

        for (String validEmail : validEmails) {
            assertTrue(Util.isValidEmailAddress(validEmail));
        }

        for (String invalidEmail : invalidEmails) {
            assertFalse(Util.isValidEmailAddress(invalidEmail));
        }
    }

    @Test
    public void isBeforeToday() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, -1);
        XMLGregorianCalendar yesterday = Util.toXmlGregorianCalendar((GregorianCalendar) calendar);
        assertTrue(Util.isBeforeToday(yesterday));

        calendar.add(Calendar.DATE, 1);
        XMLGregorianCalendar today = Util.toXmlGregorianCalendar((GregorianCalendar) calendar);
        assertFalse(Util.isBeforeToday(today));
    }
}