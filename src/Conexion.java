import java.sql.Connection;//Es el canal de comunicacion entre la aplicacion y la base de datos."Puente" por donde van y vienen los datos.
import java.sql.DriverManager;//Es la clase que se encarga de gestionar las conexiones a la base de datos. Proporciona el método getConnection() para establecer una conexión.
import java.sql.SQLException;//Es la red de seguridad para el manejo de errores
import java.sql.Statement;//Una vez q tengo la conexion abierta. El Statement es ese vehículo o mensajero que envuelve tus comandos de texto en SQL (como el CREATE DATABASE o CREATE TABLE)y los transporta a traves de la conecion para la base de datos los ejecute.


public class Conexion {
    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DATOS = "insumos_db";
    private static final String USUARIO = "root";
    private static final String CLAVE = "";

    private static final String URL_SERVIDOR = "jdbc:mysql://" + HOST + ":" + PUERTO
            + "/?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String URL_BASE_DATOS = "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
            + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    public Connection establecerConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontro el driver de MySQL. Agrega mysql-connector-j al proyecto.", e);
        }

        crearBaseSiNoExiste();
        Connection conexion = DriverManager.getConnection(URL_BASE_DATOS, USUARIO, CLAVE);
        crearTablas(conexion);
        return conexion;
    }

    private void crearBaseSiNoExiste() throws SQLException {
        try (Connection conexion = DriverManager.getConnection(URL_SERVIDOR, USUARIO, CLAVE);
                Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + BASE_DATOS
                    + " CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
        }
    }

    private void crearTablas(Connection conexion) throws SQLException {
        String sqlInsumos = "CREATE TABLE IF NOT EXISTS insumos ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "codigo VARCHAR(30) NOT NULL UNIQUE, "
                + "nombre VARCHAR(120) NOT NULL, "
                + "descripcion VARCHAR(255), "
                + "precio DECIMAL(10,2) NOT NULL, "
                + "stock INT NOT NULL"
                + ")";

        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(sqlInsumos);
        }
    }
}
