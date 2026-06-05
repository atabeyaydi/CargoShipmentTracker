import java.util.Locale;
import java.util.Scanner;

/**
 * Entry point and menu loop. Wires the nine menu options to {@link CargoCompany}
 * and {@link Shipment}. All risky parses are wrapped in try/catch so the
 * program never crashes on bad input; unknown menu choices emit
 * "Unknown option. Skipping..." verbatim.
 */
public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);          // belt-and-braces: ensure '.' decimal separator
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter company name: ");
        String companyName = sc.nextLine().trim();
        CargoCompany co = new CargoCompany(companyName);

        boolean running = true;
        while (running) {
            System.out.println();
            System.out.println("--- Cargo Menu ---");
            System.out.println("1. Register a standard shipment");
            System.out.println("2. Register an express shipment");
            System.out.println("3. Register a same-day shipment");
            System.out.println("4. Advance shipment status");
            System.out.println("5. List all shipments");
            System.out.println("6. Find shipment by ID");
            System.out.println("7. Sort shipments");
            System.out.println("8. Show revenue & insurance summary");
            System.out.println("9. Exit");
            System.out.print("Choose an option (1-9): ");
            String opt = sc.nextLine().trim();

            switch (opt) {
                case "1": case "2": case "3": {
                    System.out.print("Sender: ");        String sender    = sc.nextLine().trim();
                    System.out.print("Recipient: ");     String recipient = sc.nextLine().trim();
                    double dist, wt;
                    try {
                        System.out.print("Distance (km): ");
                        dist = Double.parseDouble(sc.nextLine().trim());
                        System.out.print("Weight (kg): ");
                        wt   = Double.parseDouble(sc.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid number. Skipping...");
                        break;
                    }
                    Shipment s;
                    switch (opt) {
                        case "1": s = new StandardShipment(sender, recipient, dist, wt); break;
                        case "2": s = new ExpressShipment (sender, recipient, dist, wt); break;
                        default : s = new SameDayShipment (sender, recipient, dist, wt); break;
                    }
                    co.registerShipment(s);
                    break;
                }
                case "4": {
                    int id;
                    try {
                        System.out.print("Enter shipment ID: ");
                        id = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid ID. Skipping...");
                        break;
                    }
                    System.out.print("Enter new status (IN_TRANSIT/DELIVERED/RETURNED): ");
                    String st = sc.nextLine().trim();
                    Shipment s = co.findById(id);
                    if (s == null) {
                        System.out.println("No shipment found with that ID.");
                        break;
                    }
                    ShipmentStatus prev = s.getStatus();
                    try {
                        ShipmentStatus next = ShipmentStatus.valueOf(st);
                        s.advanceStatus(next);
                        System.out.println("Status updated. ID " + id + ": " + prev + " -> " + next);
                    } catch (InvalidStatusTransitionException ex) {
                        System.out.println(ex.getMessage());
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Unknown status. Skipping...");
                    }
                    break;
                }
                case "5":
                    co.listAllShipments();
                    break;
                case "6": {
                    int fid;
                    try {
                        System.out.print("Enter shipment ID: ");
                        fid = Integer.parseInt(sc.nextLine().trim());
                    } catch (NumberFormatException ex) {
                        System.out.println("Invalid ID. Skipping...");
                        break;
                    }
                    Shipment found = co.findById(fid);
                    if (found == null) {
                        System.out.println("No shipment found with that ID.");
                    } else {
                        System.out.println(found);
                        System.out.printf(Locale.US, "Insurance: %.2f TL%n", found.getInsuranceCost());
                    }
                    break;
                }
                case "7":
                    System.out.print("Sort by (cost/distance/status): ");
                    co.listSortedBy(sc.nextLine().trim());
                    break;
                case "8":
                    co.summary();
                    break;
                case "9":
                    running = false;
                    System.out.println("Exiting. Goodbye!");
                    break;
                default:
                    System.out.println("Unknown option. Skipping...");
            }
        }
        sc.close();
    }
}
