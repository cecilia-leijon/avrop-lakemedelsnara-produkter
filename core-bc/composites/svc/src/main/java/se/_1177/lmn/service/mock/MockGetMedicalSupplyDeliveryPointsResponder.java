package se._1177.lmn.service.mock;

import riv.crm.selfservice.medicalsupply._0.CountryCodeEnum;
import riv.crm.selfservice.medicalsupply._0.DeliveryPointType;
import riv.crm.selfservice.medicalsupply._0.ResultCodeEnum;
import riv.crm.selfservice.medicalsupply._0.ServicePointProviderEnum;
import riv.crm.selfservice.medicalsupply.getmedicalsupplydeliverypoints._0.rivtabp21.GetMedicalSupplyDeliveryPointsResponderInterface;
import riv.crm.selfservice.medicalsupply.getmedicalsupplydeliverypointsresponder._0.GetMedicalSupplyDeliveryPointsResponseType;
import riv.crm.selfservice.medicalsupply.getmedicalsupplydeliverypointsresponder._0.GetMedicalSupplyDeliveryPointsType;
import riv.crm.selfservice.medicalsupply.getmedicalsupplydeliverypointsresponder._0.ObjectFactory;

import javax.jws.WebService;
import java.util.Random;

/**
 * @author Patrik Björk
 */
@WebService(targetNamespace = "urn:riv:crm:selfservice:medicalsupply:GetMedicalSupplyDeliveryPoints:0:rivtabp21")
public class MockGetMedicalSupplyDeliveryPointsResponder
        implements GetMedicalSupplyDeliveryPointsResponderInterface {

    private final ObjectFactory objectFactory = new ObjectFactory();

    private Random random;

    public GetMedicalSupplyDeliveryPointsResponseType getMedicalSupplyDeliveryPoints(
            String logicalAddress,
            GetMedicalSupplyDeliveryPointsType parameters) {

        /*try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        String postalCode = parameters.getPostalCode();
        ServicePointProviderEnum provider = parameters.getServicePointProvider();

        if (postalCode == null) {
            postalCode = "0";
        }

        random = new Random(postalCode.hashCode() + provider.hashCode());

        GetMedicalSupplyDeliveryPointsResponseType response = objectFactory
                .createGetMedicalSupplyDeliveryPointsResponseType();

        response.setComment("Kommentar???");

        response.setResultCode(ResultCodeEnum.OK);

        for (int i = 0; i <= random.nextInt(5) + 10; i++) {
            DeliveryPointType deliveryPoint = new DeliveryPointType();

            deliveryPoint.setCountryCode(CountryCodeEnum.SE);

            deliveryPoint.setDeliveryPointAddress("Gatan " + random.nextInt(100));
            deliveryPoint.setDeliveryPointCity("Staden" + random.nextInt(100));
            deliveryPoint.setDeliveryPointId("Leverans-id" + random.nextInt(100));
            deliveryPoint.setDeliveryPointName("Leverans-namn" + random.nextInt(100));
            deliveryPoint.setDeliveryPointPostalCode(random.nextInt(100000) + "");

            response.getDeliveryPoint().add(deliveryPoint);

        }

        return response;
    }

}
