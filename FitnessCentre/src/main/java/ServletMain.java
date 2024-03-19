import org.example.*;

import java.util.List;

public class ServletMain {
    public static void main(String[] args) {
        Databace databace = new Databace();
        AllServlets allServlets = new AllServlets();
        allServlets.formirate(
                databace,
                List.of(
                   Coaches.class,
                        Customers.class,
                        FitnessCentre.class,
                        Halls.class,
                        Helper.class,
                        Subscription.class
                )
        );
    }
}
