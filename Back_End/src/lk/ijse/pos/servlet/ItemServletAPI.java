package lk.ijse.pos.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet(urlPatterns = {"/pages/item"})
public class ItemServletAPI extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String itemCode = req.getParameter("itemCode");
        String itemName = req.getParameter("itemName");
        int itemQty = Integer.parseInt(req.getParameter("itemQty"));
        double itemPrice = Double.parseDouble(req.getParameter("itemPrice"));


        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("insert into item values(?,?,?,?)");
            pstm.setObject(1, itemCode);
            pstm.setObject(2, itemName);
            pstm.setObject(3, itemQty);
            pstm.setObject(4, itemPrice);
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from item");
            ResultSet rst = pstm.executeQuery();

            resp.addHeader("Content-Type", "application/json");
            resp.addHeader("Access-Control-Allow-Origin", "*");


            JsonArrayBuilder itemArray = Json.createArrayBuilder();
            JsonObjectBuilder itemObject = Json.createObjectBuilder();

            while (rst.next()) {
                String code = rst.getString(1);
                String name = rst.getString(2);
                int qty = rst.getInt(3);
                double price = rst.getDouble(4);

                itemObject.add("itemCode", code);
                itemObject.add("itemName", name);
                itemObject.add("itemQty", qty);
                itemObject.add("itemPrice", price);

                itemArray.add(itemObject.build());
            }

            resp.getWriter().print(itemArray.build());


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject itemObject = reader.readObject();

        String itemCode = itemObject.getString("itemCode");
        String itemName = itemObject.getString("itemName");
        int itemQty = Integer.parseInt(itemObject.getString("itemQty"));
        double itemPrice = Double.parseDouble(itemObject.getString("itemPrice"));


        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "1234");

            PreparedStatement pstm = connection.prepareStatement("update item set Name=?,qty=?, Price=? where Code=?");
            pstm.setObject(4,itemCode);
            pstm.setObject(1,itemName);
            pstm.setObject(2,itemQty);
            pstm.setObject(3,itemPrice);
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String code = req.getParameter("itemCode");

        resp.addHeader("Content-Type", "application/json");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "1234");


            PreparedStatement pstm2 = connection.prepareStatement("delete from item where Code=?");
            pstm2.setObject(1, code);
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
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods","PUT");
        resp.addHeader("Access-Control-Allow-Methods","DELETE");
        resp.addHeader("Access-Control-Allow-Headers","Content-Type");
    }
}
