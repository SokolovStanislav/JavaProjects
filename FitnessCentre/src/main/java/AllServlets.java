import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.util.List;

public class AllServlets {


    private final static int PORT = 8080;



    public void formirate(Databace databace, List<Class> classes){
        Server server = new Server(PORT);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        for (Class c: classes) {
            context.addServlet(
                    new ServletHolder(
                            new Servlet(
                                    new Quaery(new Entity(c)),
                                    databace
                            )
                    ),
                    "/%s".formatted(c.getSimpleName()));
        }



        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context });
        server.setHandler(handlers);

        try {
            server.start();
            System.out.println("Listening port : " + PORT );

            server.join();
        } catch (Exception e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }


}