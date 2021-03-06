package se._1177.lmn.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import mvk.crm.casemanagement.inbox._2.CaseTypeType;
import mvk.crm.casemanagement.inbox._2.HealthCareFacilityType;
import mvk.crm.casemanagement.inbox._2.MessageCaseType;
import mvk.crm.casemanagement.inbox.addmessage._2.rivtabp21.AddMessageResponderInterface;
import mvk.crm.casemanagement.inbox.addmessageresponder._2.AddMessageResponseType;
import mvk.crm.casemanagement.inbox.addmessageresponder._2.AddMessageType;
import org.springframework.stereotype.Service;
import riv.crm.selfservice.medicalsupply._0.DeliveryChoiceType;
import riv.crm.selfservice.medicalsupply._0.DeliveryMethodEnum;
import riv.crm.selfservice.medicalsupply._0.DeliveryPointType;
import riv.crm.selfservice.medicalsupply._0.PrescriptionItemType;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class for sending messages to a user's inbox in 1177 Vårdguiden e-tjänster.
 *
 * @author Patrik Björk
 */
@Service
public class MvkInboxService {

    private AddMessageResponderInterface addMessageResponderInterface;

    public MvkInboxService(AddMessageResponderInterface addMessageResponderInterface) {
        this.addMessageResponderInterface = addMessageResponderInterface;
    }

    /**
     * Send a message to the user's inbox. The message is composed by a freemarker template.
     *
     * @param pid
     * @param prescriptionItems
     * @param deliveryChoices
     * @return
     * @throws MvkInboxServiceException
     */
    public AddMessageResponseType sendInboxMessage(String pid,
                                                   List<PrescriptionItemType> prescriptionItems,
                                                   Collection<DeliveryChoiceType> deliveryChoices)
            throws MvkInboxServiceException {

        AddMessageType request = new AddMessageType();

        CaseTypeType caseType = new CaseTypeType();
        caseType.setCaseTypeDescription("Beställning läkemedelsnära produkter");

        HealthCareFacilityType healthCareFacility = new HealthCareFacilityType();
        healthCareFacility.setHealthCareFacilityName("Centrum Läkemedelsnära Produkter");
        healthCareFacility.setHsaId("SE2321000131-S000000016964"); // SE2321000131-S000000016964 = Mottagningen "Läkemedelsnära produkter"

        MessageCaseType messageCase = new MessageCaseType();
        messageCase.setCaseType(caseType);
        messageCase.setHeaderText("Beställning läkemedelsnära produkter");
        messageCase.setHealthCareFacility(healthCareFacility);

        try {
            messageCase.setMsg(composeMsg(prescriptionItems, deliveryChoices));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (TemplateException e) {
            throw new MvkInboxServiceException("Kvitto kunde inte skapas.", e);
        }

        request.setMessage(messageCase);
        request.setSubjectOfCareId(pid);
        request.setNotify(false);
        request.setSourceSystem("SE2321000131-S000000016964"); // SE2321000131-S000000016964 = Mottagningen "Läkemedelsnära produkter"

        AddMessageResponseType response = null;
        try {
            response = addMessageResponderInterface.addMessage(request);
        } catch (Exception e) {
            throw new MvkInboxServiceException("Kvitto kunde inte skapas.", e);
        }

        return response;
    }

    String composeMsg(List<PrescriptionItemType> prescriptionItems,
                      Collection<DeliveryChoiceType> deliveryChoices) throws IOException, TemplateException {

        if (allAreSame(deliveryChoices)) {
            // Make just one
            deliveryChoices = Arrays.asList(deliveryChoices.iterator().next());
        }

        Configuration cfg = new Configuration();

        cfg.setClassForTemplateLoading(this.getClass(), "/");

        Template template;
        try {
            // Needs prescriptionItems and deliveryChoices.
            template = cfg.getTemplate("inboxMessageTemplate.ftl");
        } catch (IOException e) {
            // Should happen always or never.
            throw new RuntimeException("Unable to process freemarker template.");
        }

        Map<String, Object> root = new HashMap<>();
        root.put("prescriptionItems", prescriptionItems);
        root.put("deliveryChoices", deliveryChoices);

        StringWriter out = new StringWriter();

        template.process(root, out);

        return out.toString();
    }

    static boolean allAreSame(Collection<DeliveryChoiceType> deliveryChoices) {
        if (deliveryChoices.size() == 0) {
            throw new IllegalArgumentException("There must be more than zero delivery choices");
        }

        DeliveryChoiceType first = deliveryChoices.iterator().next();

        DeliveryMethodEnum firstDeliveryMethod = first.getDeliveryMethod();
        DeliveryPointType firstDeliveryPoint = first.getDeliveryPoint();

        Iterator<DeliveryChoiceType> iterator = deliveryChoices.iterator();

        while (iterator.hasNext()) {
            DeliveryChoiceType next = iterator.next();

            if (!firstDeliveryMethod.equals(next.getDeliveryMethod())) {
                return false;
            } else {
                DeliveryPointType nextDeliveryPoint = next.getDeliveryPoint();
                if (firstDeliveryPoint == null && nextDeliveryPoint != null) {
                    return false;
                } else if (firstDeliveryPoint != null && nextDeliveryPoint == null) {
                    return false;
                } else if (firstDeliveryPoint == null && nextDeliveryPoint == null) {
                    continue;
                } else if (!(firstDeliveryPoint.getDeliveryPointId() + "")
                        .equals((nextDeliveryPoint.getDeliveryPointId() + ""))) {
                    return false;
                }
            }
        }

        return true;
    }
}
