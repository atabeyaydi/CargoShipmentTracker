/**
 * Represents a standard priority shipment.
 * This class defines the specific business rules for standard deliveries,
 * including a rate of 1.5 TL/km, a maximum weight capacity of 30 kg,
 * and an insurance premium calculated at 5% of the total shipping cost.
 */
public class StandardShipment extends Shipment {

    // Core pricing and capacity constants defined by company policy
    private static final double RATE_PER_KM   = 1.5;
    private static final double MAX_WEIGHT_KG = 30.0;
    private static final double INSURANCE_PCT = 0.05;

    /**
     * Initializes a standard shipment by passing the details to the abstract base class.
     */
    public StandardShipment(String sender, String recipient, double distanceKm, double weightKg) {
        super(sender, recipient, distanceKm, weightKg);
    }
    
    // Overridden methods to provide class-specific values and calculate insurance based on the interface
    @Override public double getRatePerKm()     { return RATE_PER_KM; }
    @Override public double getMaxWeightKg()   { return MAX_WEIGHT_KG; }
    @Override public String typeLabel()        { return "Standard"; }
    @Override public double getInsuranceCost() { return getCost() * INSURANCE_PCT; }
}
