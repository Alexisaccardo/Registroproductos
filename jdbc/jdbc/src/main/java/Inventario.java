import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class Inventario {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("*****BIENVENIDOS*****");

        System.out.println("¿Deseas registar o consultar un producto?:");
        String respuesta = scanner.nextLine();

        if (respuesta.equals("registrar")) {
            //

            System.out.println("Deseas agregar un pedido?");
            String agregar = scanner.nextLine();

            while (agregar.equals("si")){

            System.out.print("Ingrese el Id Producto: ");
            String IdProducto = scanner.nextLine();

            System.out.print("Ingrese el nombre del producto: ");
            String Nombre = scanner.nextLine();

            System.out.print("Ingrese el tipo producto: ");
            String Tipo = scanner.nextLine();

            System.out.print("Ingrese la cantidad del producto: ");
            String Cantidad = scanner.nextLine();

            System.out.print("Ingrese el valor del producto: ");
            String Valor = scanner.nextLine();

            if (IdProducto.equals("") || Nombre.equals("") || Tipo.equals("") || Cantidad.equals("") || Valor.equals("")){
                System.out.println("No se admiten datos vacios.");
                }else {


                Productos productos = new Productos(IdProducto, Nombre, Tipo, Cantidad, Valor);

                String driver = "com.mysql.cj.jdbc.Driver";
                String url = "jdbc:mysql://localhost:3306/productos";
                String username = "root";
                String password = "";

                try {
                    Class.forName(driver);
                    Connection connection = DriverManager.getConnection(url, username, password);
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM inventario");

                    Insert(productos, connection);
                    connection.close();
                    statement.close();
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
                System.out.println("Deseas agregar un pedido: ?");
                agregar = scanner.nextLine();
            }

        } else {
            if (respuesta.equals("consultar")) {
                System.out.println("Ingrese el id a consultar: ");
                String id = scanner.nextLine();

                String driver = "com.mysql.cj.jdbc.Driver";
                String url = "jdbc:mysql://localhost:3306/productos";
                String username = "root";
                String password = "";

                try {
                Class.forName(driver);
                Connection connection = DriverManager.getConnection(url, username, password);

                String consultaSQL = "SELECT * FROM inventario WHERE IdProducto = ?";

                    PreparedStatement statement = connection.prepareStatement(consultaSQL);
                    statement.setString(1, id); // Establecer el valor del parámetro

                    // Ejecutar la consulta
                    ResultSet resultSet = statement.executeQuery();

                    // Procesar el resultado si existe
                    if (resultSet.next()) {
                        String IdProducto = resultSet.getString("IdProducto");
                        String nombre = resultSet.getString("Nombre");
                        String tipo = resultSet.getString("Tipo");
                        String cantidad = resultSet.getString("Cantidad");
                        String valor = resultSet.getString("Valor");

                        System.out.println("Estes es el nombre del producto a consultar: " +nombre+"\n" + "de tipo: " +tipo+"\n" + "cantidad: " + cantidad+"\n" + "con un valor de: "+ valor);

                    } else {
                        System.out.println("No se encontró un registro con el ID especificado.");
                    }

                    // Cerrar recursos
                    resultSet.close();
                    statement.close();
                    connection.close();

                }catch (SQLException e){
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    public static void Insert(Productos productos, Connection connection){

        try {
            // Sentencia INSERT
            String sql = "INSERT INTO inventario (IdProducto, Nombre, Tipo, Cantidad, Valor) VALUES (?, ?, ?, ?, ?)";

            // Preparar la sentencia
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, productos.getId_Producto());
            preparedStatement.setString(2, productos.getNombre());
            preparedStatement.setString(3, productos.getTipo());
            preparedStatement.setString(4, productos.getCantidad());
            preparedStatement.setString(5, productos.getValor());


            // Ejecutar la sentencia
            int filasAfectadas = preparedStatement.executeUpdate();

            if (filasAfectadas > 0) {
                System.out.println("Registro insertado exitosamente.");
            } else {
                System.out.println("No se pudo insertar el registro.");
            }

            preparedStatement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}