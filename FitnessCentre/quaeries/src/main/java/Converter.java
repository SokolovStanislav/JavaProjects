import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.http.HttpRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Converter {
    public static String putInQuotation(Object o){
        return o.getClass().equals(String.class) ? "\'%s\'".formatted(o) : o.toString();
    }
    public static Object fromConsole(Entity entity) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object o = entity.getEntityType().newInstance();
        int count = 0;
        for (Field f: entity.getFields()) {
            System.out.print(f.getName()+": ");
            String val = new Scanner(System.in).nextLine().trim();
            if(val.isEmpty())
                continue;
            count++;
            new Reflect().setVal(
                    o,
                    f,
                    SqlTypes.byValue(f.getType()).castType(val)
            );
        }

        return count == 0 ? null : o;
    }

    public static Object fromHttpRequest(Entity entity, HttpServletRequest request, String prefix) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        Object o = entity.getEntityType().newInstance();
        int count = 0;
        for (Field f: entity.getFields()) {

            String val = request.getParameter(prefix+f.getName());
            if(val == null || val.isEmpty())
                continue;
            count++;
            new Reflect().setVal(
                    o,
                    f,
                    SqlTypes.byValue(f.getType()).castType(val)
            );
        }

        return count == 0 ? null : o;
    }
    public static Object fromHttpRequest(Entity entity, HttpServletRequest request) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        return fromHttpRequest(entity, request, "");
    }

    public static List<Object> fromResultSet(Entity entity, ResultSet resultSet) throws SQLException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        List<Object> objects = new ArrayList<>();
        while (resultSet.next()){
            Object o = entity.getEntityType().newInstance();
            for (Field f: entity.getFields()) {
                Object val = resultSet.getObject(f.getName(), f.getType());
                if(val == null )
                    continue;

                new Reflect().setVal(
                        o,
                        f,
                        val
                );
            }
            objects.add(o);
        }

        return objects;
    }

    public static String resultSetToString(ResultSet resultSet, Entity entity) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Table %s:".formatted(entity.getTableName())).append("\n");
        int count = 0;
        while (resultSet.next()){
            stringBuilder.append("count: %d".formatted(count)).append("\n");
            for (Field f:entity.getFields()) {
                stringBuilder.append(
                        "%s = %s".formatted(
                                f.getName(),
                                resultSet.getObject(f.getName()).toString()
                        )
                ).append("\n");
            }
            count++;
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
