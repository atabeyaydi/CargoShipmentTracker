/**
 * Express shipment: 3.0 TL/km, max 20 kg, insurance = 8 % of cost.
 * This class implements the specific pricing and capacity rules for express deliveries.
 */
public class ExpressShipment extends Shipment {

    // Constants defining the specific rules and limits for an express shipment
    private static final double RATE_PER_KM   = 3.0;
    private static final double MAX_WEIGHT_KG = 20.0;
    private static final double INSURANCE_PCT = 0.08;

    /**
     * Initializes an express shipment by passing the details to the abstract base class.
     */
    public ExpressShipment(String sender, String recipient, double distanceKm, double weightKg) {
        super(sender, recipient, distanceKm, weightKg);
    }

    @Override public double getRatePerKm()     { return RATE_PER_KM; }
    @Override public double getMaxWeightKg()   { return MAX_WEIGHT_KG; }
    @Override public String typeLabel()        { return "Express"; }
    @Override public double getInsuranceCost() { return getCost() * INSURANCE_PCT; }
}
