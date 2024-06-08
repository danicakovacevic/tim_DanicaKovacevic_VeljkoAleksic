package zus.model.utility;

import zus.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Properties;

public class JDBCUtils {

    public static Connection connection = null;

    public static void connect() {
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "root");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/zus", properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //login methods
    public static UserDto authenticateUser(String userName, String pass) {
        UserDto user = null;
        String query = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            statement.setString(2, pass);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt(1);
                    String uName = resultSet.getString(2);
                    String firstName = resultSet.getString(3);
                    String lastName = resultSet.getString(4);
                    String pwd = resultSet.getString(5);
                    LocalDate dtCreated = resultSet.getDate(6).toLocalDate();
                    user = new UserDto(userId, uName, firstName, lastName, pwd, dtCreated);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public static Boolean registerNewUser(String uName, String firstName, String lastName, String pwd){
        if(isUserNameTaken(uName)) {
            return false;
        }

        String formattedDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String query = "insert into user (username, firstname, lastname, password, createdAtDt)" +
                "values (?, ?, ?, ? , ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            statement.setString(1, uName);
            statement.setString(2, firstName);
            statement.setString(3, lastName);
            statement.setString(4, pwd);
            statement.setString(5, formattedDateTime);
            statement.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //planetView methods
    public static ArrayList<PlanetDto> getHabitablePlanets(){
        ArrayList<PlanetDto> habitablePlanets = new ArrayList<PlanetDto>();
        String query =
                "SELECT p.* " +
                        "FROM " +
                        "rezultat_misije rm " +
                        "JOIN misija m ON rm.misija_id = m.Id " +
                        "JOIN planeta p ON m.planeta_id = p.Id " +
                        "JOIN gas g ON g.Id = rm.gas_id " +
                        "WHERE " +
                        "(rm.SrednjaUdaljenostOdNajblizegObj > 100 AND rm.SrednjaUdaljenostOdNajblizegObj < 200) AND " +
                        "(rm.MinTemp > 150 AND rm.MinTemp < 250) AND (rm.MaxTemp > 250 AND rm.MaxTemp < 350) AND " +
                        "(rm.MaxTemp - rm.MinTemp < 120) AND " +
                        "(rm.ProcenatKiseonika BETWEEN 15 AND 25) AND " +
                        "(g.JeLetalan = 0 AND (rm.ProcenatKiseonika + rm.ProcenatGasa) > 90 AND (rm.ProcenatKiseonika + rm.ProcenatGasa) < 100) AND " +
                        "(rm.MaxVisinaGravitacionogPolja >= 1000) AND " +
                        "(rm.BrzinaOrbitiranjaOkoNajblizeZvezde > 25 AND rm.BrzinaOrbitiranjaOkoNajblizeZvezde < 35) AND " +
                        "p.Id NOT IN (" +
                        "  SELECT p.Id " +
                        "  FROM planeta p " +
                        "  JOIN stambeni_objekat so ON so.planeta_id = p.Id " +
                        "  JOIN stanovnik s ON s.stambeni_objekat_id = so.Id " +
                        "  WHERE s.PreminuoAtDt IS NOT NULL " +
                        "  AND s.PreminuoAtDt BETWEEN '2203-01-01' AND '2203-12-31' " +
                        "  AND TIMESTAMPDIFF(YEAR, s.DatumRodjenja, s.PreminuoAtDt) < 40 " +
                        "  AND TIMESTAMPDIFF(YEAR, s.DoselioDt, s.PreminuoAtDt) <= 1 " +
                        "  GROUP BY p.Id " +
                        "  HAVING COUNT(s.Id) <= 20" +
                        ");";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int planetId = resultSet.getInt(1);
                String naziv = resultSet.getString(2);
                String opis = resultSet.getString(3);
                Boolean jeNaseljiva = resultSet.getBoolean(4);
                Boolean jePlaneta = resultSet.getBoolean(5);
                LocalDate dtCreated = resultSet.getDate(6).toLocalDate();
                habitablePlanets.add(new PlanetDto(planetId, naziv, opis, jeNaseljiva, jePlaneta, dtCreated));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return habitablePlanets;
    }

    public static ArrayList<PropertyDto> getPropertiesOnPlanet(int planetId) {
        ArrayList<PropertyDto> propertiesOnPlanet = new ArrayList<>();
        String query = "SELECT * FROM stambeni_objekat WHERE planeta_id = ? AND JeRaspoloziv = 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, planetId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    String naziv = resultSet.getString(2);
                    String kvadratura = resultSet.getString(3);
                    String tip = resultSet.getString(4);
                    Boolean jeRaspoloziv = resultSet.getBoolean(5);

                    propertiesOnPlanet.add(new PropertyDto(id, naziv, kvadratura, tip, jeRaspoloziv));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return propertiesOnPlanet;
    }

    public static Boolean purchaseProperty(int propertyId, int userId) {
        String query = "UPDATE stambeni_objekat SET vlasnik_id = ?, JeRaspoloziv = 0 WHERE Id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);

            statement.setInt(1, userId);
            statement.setInt(2, propertyId);

            int rowsUpdated = statement.executeUpdate();
            connection.commit();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Greska", rollbackEx);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException finalEx) {
                throw new RuntimeException("Greska 2", finalEx);
            }
        }
    }

    public static ArrayList<TransportationLineDto> getAvailableTransportationLines(int planetId) {
        ArrayList<TransportationLineDto> transportationLines = new ArrayList<>();
        String query =
                "SELECT * FROM putovanje p WHERE p.destPlaneta_id = ? " +
                        "AND (SELECT COALESCE(SUM(broj_karata), 0) FROM user s WHERE s.putovanje_id = p.Id) < p.Kapacitet";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, planetId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String linija = resultSet.getString(2);
                int kapacitet = resultSet.getInt(3);
                String voziloMarkaModel = resultSet.getString(4);
                LocalDateTime dtPolazak = resultSet.getTimestamp(5).toLocalDateTime();
                int destPlanetaId = resultSet.getInt(6);

                transportationLines.add(new TransportationLineDto(id, linija, kapacitet, voziloMarkaModel, dtPolazak, destPlanetaId));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transportationLines;
    }

    public static Boolean purchaseFare(int putovanjeId, int userId, int brojKarata) {
        String query = "UPDATE user SET putovanje_id = ?, broj_karata = ? WHERE Id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);

            statement.setInt(1, putovanjeId);
            statement.setInt(2, brojKarata);
            statement.setInt(3, userId);

            int rowsUpdated = statement.executeUpdate();
            connection.commit();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Greska", rollbackEx);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException finalEx) {
                throw new RuntimeException("Greska 2", finalEx);
            }
        }
    }

    public static void insertPassengers(int stambeniObjekatId, String ime, String prezime, LocalDateTime doselioDt, LocalDate datumRodjenja, Integer userId, int putovanjeId) {
        String query = "insert into stanovnik (stambeni_objekat_id, ime, prezime, DoselioDt, DatumRodjenja, user_id, putovanje_id)" +
                "values (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            statement.setInt(1, stambeniObjekatId);
            statement.setString(2, ime);
            statement.setString(3, prezime);
            statement.setObject(4, doselioDt);
            statement.setObject(5, datumRodjenja);
            if (userId != null) {
                statement.setInt(6, userId);
            } else {
                statement.setNull(6, Types.INTEGER);
            }
            statement.setInt(7, putovanjeId);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<PropertyDto> getAllPropertiesByUserId(int userId) {
        ArrayList<PropertyDto> propertiesOwnedHByUser = new ArrayList<>();
        String query =
                "SELECT * FROM stambeni_objekat WHERE vlasnik_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String naziv = resultSet.getString(2);
                String kvadratura = resultSet.getString(3);
                String tip = resultSet.getString(4);
                Boolean jeRaspoloziv = resultSet.getBoolean(5);

                propertiesOwnedHByUser.add(new PropertyDto(id, naziv, kvadratura, tip, jeRaspoloziv));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return propertiesOwnedHByUser;
    }

    public static ArrayList<TransportationLineDtoExtension> getAllFaresByUserId(int userId) {
        ArrayList<TransportationLineDtoExtension> faresOwnedHByUser = new ArrayList<>();
        String query =
                "SELECT p.*, u.broj_karata FROM user u JOIN putovanje p ON u.putovanje_id = p.Id where u.Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String linija = resultSet.getString(2);
                int kapacitet = resultSet.getInt(3);
                String voziloMarkaModel = resultSet.getString(4);
                LocalDateTime dtPolazak = resultSet.getTimestamp(5).toLocalDateTime();
                int destPlanetaId = resultSet.getInt(6);
                int brojKarata = resultSet.getInt(7);

                faresOwnedHByUser.add(new TransportationLineDtoExtension(id, linija, kapacitet, voziloMarkaModel, dtPolazak, destPlanetaId, brojKarata));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return faresOwnedHByUser;
    }

    private static Boolean isUserNameTaken(String userName) {
        String query = "SELECT 1 FROM user WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, userName);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private JDBCUtils() {

    }

}
