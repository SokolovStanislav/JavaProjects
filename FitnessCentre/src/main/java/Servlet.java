import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/Servlet")
public class Servlet extends HttpServlet {


    private Quaery quaery;

    private Databace databace;

    public Servlet(Quaery quaery, Databace databace) {
        this.quaery = quaery;
        this.databace = databace;
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {


        List<Object> objects;

        Object o = Converter.fromHttpRequest(quaery.getEntity(), request);
        if(o == null) {
            objects = Converter.fromResultSet(
                    quaery.getEntity(),
                    databace.getStatment().executeQuery(
                            quaery.select()
                    )
            );
        }else {
            objects = Converter.fromResultSet(
                    quaery.getEntity(),
                    databace.getStatment().executeQuery(
                            quaery.select()
                    )
            );
        }

        response.getWriter().println(
                "Found %d\n".formatted(objects.size()) +
                objects
        );
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object o = Converter.fromHttpRequest(quaery.getEntity(), request);

        response.getWriter().println(
                databace.getStatment().execute(
                        quaery.insert(o)
                )
        );
    }

    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object o = Converter.fromHttpRequest(quaery.getEntity(), request);

        response.getWriter().println(
                databace.getStatment().execute(
                        quaery.delete(o)
                )
        );
    }
    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object set = Converter.fromHttpRequest(quaery.getEntity(), request, "SET");
        Object where = Converter.fromHttpRequest(quaery.getEntity(), request, "WH");


        response.getWriter().println(
                databace.getStatment().execute(
                        quaery.update(where, set)
                )
        );
    }

}