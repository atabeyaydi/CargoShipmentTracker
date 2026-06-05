import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Aggregator/manager. Owns every registered {@link Shipment} in two parallel
 * structures: an {@link ArrayList} that preserves insertion order (for listing
 * and sorting) and a {@link HashMap} keyed by tracking ID (for O(1) lookup as
 * mandated by the specification).
 *
 * Exposes three explicit {@link Comparator} strategies on the sort path:
 *   - by cost     (high -> low)
 *   - by distance (high -> low)
 *   - by status   (PENDING first -> terminal last)
 */
public class CargoCompany {
    private final String companyName;
    private final ArrayList<Shipment> shipments          = new ArrayList<>();
    private final HashMap<Integer, Shipment> shipmentById = new HashMap<>();

    // ----- Comparator strategies (Strategy pattern via named constants) -----
    public static final Comparator<Shipment> BY_COST_DESC =
            (a, b) -> Double.compare(b.getCost(), a.getCost());

    public static final Comparator<Shipment> BY_DISTANCE_DESC =
            (a, b) -> Double.compare(b.getDistanceKm(), a.getDistanceKm());

    /** PENDING (0) -> IN_TRANSIT (1) -> DELIVERED (2) -> RETURNED (3). */
    public static final Comparator<Shipment> BY_STATUS_LIFECYCLE =
            Comparator.comparingInt(s -> s.getStatus().ordinal());

    public CargoCompany(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName()   { return companyName; }
    public int    getShipmentCount() { return shipments.size(); }

    // ----- register ---------------------------------------------------------
    public void registerShipment(Shipment s) {
        if (s.getWeightKg() > s.getMaxWeightKg()) {
            System.out.println("Weight exceeds capacity for this shipment class. Shipment not registered.");
            return;
        }
        shipments.add(s);
        shipmentById.put(s.getId(), s);
        System.out.println("Shipment registered. ID: " + s.getId());
    }

    // ----- queries ----------------------------------------------------------
    public Shipment findById(int id) {
        return shipmentById.get(id);           // O(1), no linear search
    }

    public void listAllShipments() {
        for (Shipment s : shipments) {
            System.out.println(s);             // polymorphic toString()
        }
    }

    public double getTotalRevenue() {
        double sum = 0.0;
        for (Shipment s : shipments) sum += s.getCost();
        return sum;
    }

    public double getTotalInsurance() {
        double sum = 0.0;
        for (Shipment s : shipments) sum += s.getInsuranceCost();
        return sum;
    }

    // ----- sort & list ------------------------------------------------------
    public void listSortedBy(String criterion) {
        Comparator<Shipment> cmp;
        String header;
        switch (criterion == null ? "" : criterion.trim().toLowerCase(Locale.US)) {
            case "cost":
                cmp    = BY_COST_DESC;
                header = "===== Sorted by Cost (high -> low) =====";
                break;
            case "distance":
                cmp    = BY_DISTANCE_DESC;
                header = "===== Sorted by Distance (high -> low) =====";
                break;
            case "status":
                cmp    = BY_STATUS_LIFECYCLE;
                header = "===== Sorted by Status (PENDING -> RETURNED) =====";
                break;
            default:
                System.out.println("Unknown option. Skipping...");
                return;
        }
        List<Shipment> copy = new ArrayList<>(shipments);
        copy.sort(cmp);
        System.out.println(header);
        for (Shipment s : copy) System.out.println(s);
    }

    // ----- summary ----------------------------------------------------------
    public void summary() {
        double stdIns = 0.0, expIns = 0.0, sdIns = 0.0;
        for (Shipment s : shipments) {
            if      (s instanceof StandardShipment) stdIns += s.getInsuranceCost();
            else if (s instanceof ExpressShipment)  expIns += s.getInsuranceCost();
            else if (s instanceof SameDayShipment)  sdIns  += s.getInsuranceCost();
        }
        System.out.println("===== Revenue & Insurance Summary =====");
        System.out.println("Total Shipments: " + shipments.size());
        System.out.printf(Locale.US, "Total Revenue: %.2f TL%n",   getTotalRevenue());
        System.out.printf(Locale.US, "Total Insurance: %.2f TL%n", getTotalInsurance());
        System.out.printf(Locale.US, "(Standard: %.2f + Express: %.2f + SameDay: %.2f)%n",
                stdIns, expIns, sdIns);
    }
}
