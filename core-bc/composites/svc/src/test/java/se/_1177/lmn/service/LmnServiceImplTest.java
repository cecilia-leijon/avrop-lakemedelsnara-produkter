package se._1177.lmn.service;

import org.junit.Before;
import org.junit.Test;
import riv.crm.selfservice.medicalsupply._0.ArticleType;
import riv.crm.selfservice.medicalsupply._0.PrescriptionItemType;
import riv.crm.selfservice.medicalsupply._0.ResultCodeEnum;
import riv.crm.selfservice.medicalsupply._0.StatusEnum;
import riv.crm.selfservice.medicalsupply._0.SubjectOfCareType;
import riv.crm.selfservice.medicalsupply.getmedicalsupplydeliverypoints._0.rivtabp21.GetMedicalSupplyDeliveryPointsResponderInterface;
import riv.crm.selfservice.medicalsupply.getmedicalsupplyprescriptions._0.rivtabp21.GetMedicalSupplyPrescriptionsResponderInterface;
import riv.crm.selfservice.medicalsupply.getmedicalsupplyprescriptionsresponder._0.GetMedicalSupplyPrescriptionsResponseType;
import riv.crm.selfservice.medicalsupply.getmedicalsupplyprescriptionsresponder._0.GetMedicalSupplyPrescriptionsType;
import riv.crm.selfservice.medicalsupply.registermedicalsupplyorder._0.rivtabp21.RegisterMedicalSupplyOrderResponderInterface;
import se._1177.lmn.model.MedicalSupplyPrescriptionsHolder;
import se._1177.lmn.service.util.Util;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Patrik Björk
 */
public class LmnServiceImplTest {

    private GetMedicalSupplyDeliveryPointsResponderInterface medicalSupplyDeliveryPoints;
    private GetMedicalSupplyPrescriptionsResponderInterface medicalSupplyPrescriptions;
    private RegisterMedicalSupplyOrderResponderInterface registerMedicalSupplyOrder;

    @Before
    public void setup() {

        medicalSupplyPrescriptions = mock(GetMedicalSupplyPrescriptionsResponderInterface.class);

        GetMedicalSupplyPrescriptionsResponseType response = new GetMedicalSupplyPrescriptionsResponseType();

        SubjectOfCareType subjectOfCare = new SubjectOfCareType();

        ArticleType article = new ArticleType();
        article.setIsOrderable(true);

        // Orderable
        PrescriptionItemType item1 = new PrescriptionItemType();
        item1.setNextEarliestOrderDate(null);
        item1.setStatus(StatusEnum.AKTIV);
        item1.setNoOfRemainingOrders(1);
        item1.setLastValidDate(Util.toXmlGregorianCalendar(new GregorianCalendar()));

        // Not orderable, MAKULERAD
        PrescriptionItemType item2 = new PrescriptionItemType();
        item2.setNextEarliestOrderDate(null);
        item2.setStatus(StatusEnum.MAKULERAD);
        item2.setNoOfRemainingOrders(1);
        item2.setLastValidDate(Util.toXmlGregorianCalendar(new GregorianCalendar()));

        // Not orderable, UTGÅTT
        PrescriptionItemType item3 = new PrescriptionItemType();
        item3.setNextEarliestOrderDate(null);
        item3.setStatus(StatusEnum.UTGÅTT);
        item3.setNoOfRemainingOrders(1);
        item3.setLastValidDate(Util.toXmlGregorianCalendar(new GregorianCalendar()));

        // Not orderable, LEVERERAD
        PrescriptionItemType item4 = new PrescriptionItemType();
        item4.setNextEarliestOrderDate(null);
        item4.setStatus(StatusEnum.LEVERERAD);
        item4.setNoOfRemainingOrders(1);
        item4.setLastValidDate(Util.toXmlGregorianCalendar(new GregorianCalendar()));

        // Orderable (even though it's not really orderable (not yet) but still it's shown in the same table) - next
        // earliest order date in future
        PrescriptionItemType item5 = new PrescriptionItemType();

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, 1);

        item5.setNextEarliestOrderDate(Util.toXmlGregorianCalendar(calendar));
        item5.setStatus(StatusEnum.AKTIV);
        item5.setNoOfRemainingOrders(1);
        item5.setLastValidDate(Util.toXmlGregorianCalendar(new GregorianCalendar()));

        // Not orderable - no remaining orders
        PrescriptionItemType item6 = new PrescriptionItemType();
        item6.setNextEarliestOrderDate(null);
        item6.setStatus(StatusEnum.AKTIV);
        item6.setNoOfRemainingOrders(0);
        item6.setLastValidDate(Util.toXmlGregorianCalendar(new GregorianCalendar()));

        // Not orderable - last valid date older than today
        PrescriptionItemType item7 = new PrescriptionItemType();
        item7.setNextEarliestOrderDate(null);
        item7.setStatus(StatusEnum.AKTIV);
        item7.setNoOfRemainingOrders(1);
        item7.setLastValidDate(Util.toXmlGregorianCalendar(new GregorianCalendar()));

        calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1);

        item7.setLastValidDate(Util.toXmlGregorianCalendar(calendar));

        subjectOfCare.getPrescriptionItem().add(item1);
        subjectOfCare.getPrescriptionItem().add(item2);
        subjectOfCare.getPrescriptionItem().add(item3);
        subjectOfCare.getPrescriptionItem().add(item4);
        subjectOfCare.getPrescriptionItem().add(item5);
        subjectOfCare.getPrescriptionItem().add(item6);
        subjectOfCare.getPrescriptionItem().add(item7);

        for (PrescriptionItemType itemType : subjectOfCare.getPrescriptionItem()) {
            itemType.setArticle(article);
        }

        response.setSubjectOfCareType(subjectOfCare);
        response.setResultCode(ResultCodeEnum.OK);

        when(medicalSupplyPrescriptions.getMedicalSupplyPrescriptions(
                anyString(),
                any(GetMedicalSupplyPrescriptionsType.class)))
                .thenReturn(response);

    }

    @Test
    public void getMedicalSupplyPrescriptionsHolder() throws Exception {

        LmnServiceImpl service = new LmnServiceImpl(
                medicalSupplyDeliveryPoints,
                medicalSupplyPrescriptions,
                registerMedicalSupplyOrder,
                "");

        MedicalSupplyPrescriptionsHolder holder = service.getMedicalSupplyPrescriptionsHolder("aslkjsdfljk");

        assertEquals(2, holder.getOrderable().size());
        assertEquals(5, holder.getNoLongerOrderable().size());
    }

    @Test
    public void sortByOrderableToday() throws Exception {

        ArticleType article1 = new ArticleType();
        article1.setIsOrderable(true);

        ArticleType article2 = new ArticleType();
        article2.setIsOrderable(false);

        PrescriptionItemType p1, p2, p3, p4, p5;

        p1 = new PrescriptionItemType();
        p2 = new PrescriptionItemType();
        p3 = new PrescriptionItemType();
        p4 = new PrescriptionItemType();
        p5 = new PrescriptionItemType();

        p1.setNextEarliestOrderDate(getTodayPlusDays(-1));  // Orderable today
        p2.setNextEarliestOrderDate(null);                  // Orderable today
        p3.setNextEarliestOrderDate(getTodayPlusDays(1));   // Not orderable today
        p4.setNextEarliestOrderDate(null);                  // Orderable today
        p5.setNextEarliestOrderDate(getTodayPlusDays(0));   // Orderable today

        List<PrescriptionItemType> list = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5));

        for (PrescriptionItemType itemType : list) {
            itemType.setArticle(article1);
        }

        // We make an exception for p2. Set the non-orderable article
        p2.setArticle(article2);

        LmnServiceImpl.sortByOrderableToday(list);

        // p1, p4 and p5 should be among the three first and p3 the fourth in the list
        List<PrescriptionItemType> firstThree = list.subList(0, 3);

        assertTrue(firstThree.contains(p1));
        assertTrue(firstThree.contains(p4));
        assertTrue(firstThree.contains(p5));

        assertEquals(p3, list.get(3));
        assertEquals(p2, list.get(4)); // Should come last since the article isn't orderable.
    }

    private XMLGregorianCalendar getTodayPlusDays(int daysToAdd) {
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.add(Calendar.DATE, daysToAdd);

        return toXmlGregorianCalendar(calendar);
    }

    public XMLGregorianCalendar toXmlGregorianCalendar(GregorianCalendar calendar) {

        XMLGregorianCalendar xmlGregorianCalendar = null;
        DatatypeFactory datatypeFactory;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        /*// Test today
        GregorianCalendar calendar = new GregorianCalendar();*/

        xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(calendar);

        return xmlGregorianCalendar;
    }
}