package se._1177.lmn.service.mock;

import riv.crm.selfservice.medicalsupply._0.ArticleType;
import riv.crm.selfservice.medicalsupply._0.DeliveryChoiceType;
import riv.crm.selfservice.medicalsupply._0.DeliveryMethodEnum;
import riv.crm.selfservice.medicalsupply._0.OrderItemType;
import riv.crm.selfservice.medicalsupply._0.PrescriptionItemType;
import riv.crm.selfservice.medicalsupply._0.ProductAreaEnum;
import riv.crm.selfservice.medicalsupply._0.ResultCodeEnum;
import riv.crm.selfservice.medicalsupply._0.SubjectOfCareType;
import riv.crm.selfservice.medicalsupply.getmedicalsupplyprescriptions._0.rivtabp21.GetMedicalSupplyPrescriptionsResponderInterface;
import riv.crm.selfservice.medicalsupply.getmedicalsupplyprescriptionsresponder._0.GetMedicalSupplyPrescriptionsResponseType;
import riv.crm.selfservice.medicalsupply.getmedicalsupplyprescriptionsresponder._0.GetMedicalSupplyPrescriptionsType;
import riv.crm.selfservice.medicalsupply.getmedicalsupplyprescriptionsresponder._0.ObjectFactory;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * @author Patrik Björk
 */
@WebService(targetNamespace = "urn:riv:crm:selfservice:medicalsupply:GetMedicalSupplyPrescriptions:0:rivtabp21")
public class MockGetMedicalSupplyPrescriptionsResponder
        implements GetMedicalSupplyPrescriptionsResponderInterface {

    private final ObjectFactory objectFactory = new ObjectFactory();

    public GetMedicalSupplyPrescriptionsResponseType getMedicalSupplyPrescriptions(
            String logicalAddress,
            GetMedicalSupplyPrescriptionsType parameters) {

        Random random = new Random(4);

        GetMedicalSupplyPrescriptionsResponseType response = objectFactory
                .createGetMedicalSupplyPrescriptionsResponseType();

        response.setResultCode(ResultCodeEnum.OK);

        SubjectOfCareType subjectOfCare = new SubjectOfCareType();
        subjectOfCare.setSubjectOfCareId(random.nextInt(1000) + "");

        for (int i = 0; i <= random.nextInt(5); i++) {
            addPrescriptionItem(random, subjectOfCare, false);
        }

        addPrescriptionItem(random, subjectOfCare, true);

        response.setSubjectOfCareType(subjectOfCare);

        response.setComment("Kommentar" + random.nextInt(100));

        return response;
    }

    private void addPrescriptionItem(Random random, SubjectOfCareType subjectOfCare, boolean nextPossibleOrderDateInFuture) {
        OrderItemType orderItem = new OrderItemType();

        ArticleType article = new ArticleType();
        article.setArticleName("Artikelnamn" + random.nextInt(100));
        article.setArticleNo(random.nextInt(100000) + "");
        article.setIsOrderable(random.nextBoolean());
        article.setPackageSize(random.nextInt(100));
        article.setPackageSizeUnit("Enhet" + random.nextInt(100));
        article.setProductArea(ProductAreaEnum.values()[random.nextInt(ProductAreaEnum.values().length)]);

        orderItem.setArticle(article);

        orderItem.setDeliveredDate(getRandomCalendar(random));
        DeliveryChoiceType deliveryChoice = new DeliveryChoiceType();
        deliveryChoice.setDeliveryMethod(DeliveryMethodEnum.HEMLEVERANS);
//            deliveryChoice.setDeliveryPoint(); kanske todo utveckla mer på deliveryChoice...
        orderItem.setDeliveryChoice(deliveryChoice);

        subjectOfCare.getOrderItem().add(orderItem);

        PrescriptionItemType prescriptionItem = new PrescriptionItemType();

        ArticleType article2 = new ArticleType();
        article2.setArticleName("Artikelnamn" + random.nextInt(100));
        article2.setArticleNo(random.nextInt(100000) + "");
        article2.setIsOrderable(random.nextBoolean());
        article2.setPackageSize(random.nextInt(100));
        article2.setPackageSizeUnit("Enhet" + random.nextInt(100));
        article2.setProductArea(ProductAreaEnum.values()[random.nextInt(ProductAreaEnum.values().length)]);

        prescriptionItem.setArticle(article2);

        prescriptionItem.setNoOfOrders(random.nextInt(10));
        prescriptionItem.setNoOfRemainingOrders(random.nextInt(10));

        if (!nextPossibleOrderDateInFuture) {
            prescriptionItem.setNextEarliestOrderDate(getRandomCalendar(random));
        } else {
            Calendar c = Calendar.getInstance();

            c.add(Calendar.MONTH, 1);

            XMLGregorianCalendar randomCalendar = getRandomCalendar(random);
            randomCalendar.setYear(c.get(Calendar.YEAR) + 1);

            prescriptionItem.setNextEarliestOrderDate(randomCalendar);
        }
        prescriptionItem.setPrescriptionId(random.nextInt(100000) + "");
        prescriptionItem.setPrescriber("Kalle Karlsson");
        prescriptionItem.setLastValidDate(getRandomCalendar(random));
        prescriptionItem.setNoOfArticlesPerOrder(random.nextInt(5) * 1000 + 1000);
        prescriptionItem.setNoOfPackagesPerOrder(random.nextInt(5) * 50 + 50);

        subjectOfCare.getPrescriptionItem().add(prescriptionItem);
    }

    private XMLGregorianCalendar getRandomCalendar(Random random) {
        XMLGregorianCalendar xmlGregorianCalendar = null;
        DatatypeFactory datatypeFactory;
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new RuntimeException(e);
        }

        xmlGregorianCalendar = datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar());
        Duration duration = datatypeFactory.newDuration(random.nextInt());

        xmlGregorianCalendar.add(duration);
        return xmlGregorianCalendar;
    }

}
