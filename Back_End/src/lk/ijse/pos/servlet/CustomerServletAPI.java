package lk.ijse.pos.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


@WebServlet(urlPatterns = {"/pages/customer"})
public class CustomerServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from customer");
            ResultSet rst = pstm.executeQuery();

            resp.addHeader("Content-Type", "application/json");
            resp.addHeader("Access-Control-Allow-Origin", "*");


            JsonArrayBuilder allCustomer = Json.createArrayBuilder();
            JsonObjectBuilder customerObject = Json.createObjectBuilder();

            while (rst.next()) {
                String id = rst.getString(1);
                String name = rst.getString(2);
                String address = rst.getString(3);

                customerObject.add("id", id);
                customerObject.add("name", name);
                customerObject.add("address", address);

                allCustomer.add(customerObject.build());
            }

            resp.getWriter().print(allCustomer.build());


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");


        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("insert into customer values(?,?,?)");
            pstm.setObject(1, cusID);
            pstm.setObject(2, cusName);
            pstm.setObject(3, cusAddress);
            if (pstm.executeUpdate() > 0) {

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state","Ok");
                objectBuilder.add("message","Successfully Added....!");
                objectBuilder.add("Data"," ");
                resp.getWriter().print(objectBuilder.build());

            }


        } catch (ClassNotFoundException e) {

            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("Data"," ");
            resp.setStatus(500);
            resp.getWriter().print(error.build());

        } catch (SQLException e) {
            JsonObjectBuilder error = Json.createObjectBuilder();
            error.add("state","Error");
            error.add("message",e.getLocalizedMessage());
            error.add("Data"," ");
            resp.getWriter().print(error.build());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "1234");


            PreparedStatement pstm2 = connection.prepareStatement("delete from Customer where id=?");
            pstm2.setObject(1, id);
            if (pstm2.executeUpdate() > 0) {

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state", "OK");
                objectBuilder.add("message", "Successfully Deleted.....");
                objectBuilder.add("Data", " ");
                resp.getWriter().print(objectBuilder.build());
            }else {
                throw new RuntimeException("Can't Delete...!");
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getLocalizedMessage());
            response.add("data", "");
            resp.setStatus(500);
            resp.getWriter().print(response.build());

        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getLocalizedMessage());
            response.add("data", "");
            resp.getWriter().print(response.build());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject customerObject = reader.readObject();

        String id = customerObject.getString("id");
        String name = customerObject.getString("name");
        String address = customerObject.getString("address");

        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("update Customer set name=?,address=? where id=?");
            pstm.setObject(3,id);
            pstm.setObject(1,name);
            pstm.setObject(2,address);
            boolean b = pstm.executeUpdate() > 0;

            if(b){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("state", "OK");
                objectBuilder.add("message", "Successfully Updated.....");
                objectBuilder.add("Data", " ");
                resp.getWriter().print(objectBuilder.build());
            }else {
                throw new RuntimeException("Can't Update...!");
            }


        } catch (RuntimeException e) {
            e.printStackTrace();
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getLocalizedMessage());
            response.add("data", "");
            resp.setStatus(500);
            resp.getWriter().print(response.build());

        }catch (ClassNotFoundException | SQLException e){
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("state", "Error");
            response.add("message", e.getLocalizedMessage());
            response.add("data", "");
            resp.getWriter().print(response.build());
        }
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods","PUT");
        resp.addHeader("Access-Control-Allow-Methods","DELETE");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
    }
}
