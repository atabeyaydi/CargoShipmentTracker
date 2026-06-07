/**
 * Same-day shipment: 6.0 TL/km, max 10 kg, insurance = 12 % of cost.
 * This class implements the specific pricing and capacity rules for same-day deliveries.
 */
public class SameDayShipment extends Shipment {
    private static final double RATE_PER_KM   = 6.0;
    private static final double MAX_WEIGHT_KG = 10.0;
    private static final double INSURANCE_PCT = 0.12;

    public SameDayShipment(String sender, String recipient, double distanceKm, double weightKg) {
        super(sender, recipient, distanceKm, weightKg);
    }

    @Override public double getRatePerKm()     { return RATE_PER_KM; }
    @Override public double getMaxWeightKg()   { return MAX_WEIGHT_KG; }
    @Override public String typeLabel()        { return "SameDay"; }
    @Override public double getInsuranceCost() { return getCost() * INSURANCE_PCT; }
}
