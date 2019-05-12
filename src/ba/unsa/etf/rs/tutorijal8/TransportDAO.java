package ba.unsa.etf.rs.tutorijal8;

import javax.swing.plaf.nimbus.State;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TransportDAO {

    private static TransportDAO instance=null;
    private static Connection connection;
    private static PreparedStatement addDriverStatement;
    private static PreparedStatement addBusStatement;
    private static PreparedStatement deleteDriverStatement;
    private static PreparedStatement deleteBusStatement;
    private static PreparedStatement getDriversStatement;
    private static PreparedStatement getBussesStatement;
    private static PreparedStatement getNextDriverId;
    private static PreparedStatement getNextBusId;
    private static PreparedStatement resetDriversStatement;
    private static PreparedStatement resetBussesStatement;
    private static PreparedStatement findDriverById;

    private TransportDAO(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:baza.db");
            initializePreparedStatements();
        } catch (Exception e) {
            createDatabaseTables();
            try {
                initializePreparedStatements();
            } catch (SQLException ignored) {
            }
        }
    }

    public static TransportDAO getInstance(){
        if(instance==null){
            instance=new TransportDAO();
        }
        return instance;
    }

    public void addDriver(Driver driver) {
        ArrayList<Driver> drivers = getDrivers();
        if(drivers.contains(driver))throw new IllegalArgumentException("Taj vozač već postoji!");
        int nextId = 0;
        try {
            ResultSet resultSet = getNextDriverId.executeQuery();
            if(resultSet.next()){
                nextId = resultSet.getInt(1);
            }
        } catch (SQLException ignored) {
        }
        try {
            addDriverStatement.setInt(1,nextId);
            addDriverStatement.setString(2, driver.getName());
            addDriverStatement.setString(3, driver.getSurname());
            addDriverStatement.setString(4, driver.getUmcn());
            addDriverStatement.setDate(5, Date.valueOf(driver.getBirthday()));
            addDriverStatement.setDate(6, Date.valueOf(driver.getHireDate()));
            addDriverStatement.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    public ArrayList<Driver> getDrivers() {
        ArrayList<Driver> drivers = new ArrayList<>();
        Driver driver = null;
        try {
            ResultSet resultSet = getDriversStatement.executeQuery();
            while((driver=getDriverFromResultSet(resultSet))!=null){
                drivers.add(driver);
            }
            resultSet.close();
        } catch (SQLException e) {
        }
        return drivers;
    }

    public void addBus(Bus bus) {
        int nextId = 0;
        try {
            ResultSet resultSet = getNextBusId.executeQuery();
            if(resultSet.next()){
                nextId = resultSet.getInt(1);
            }
        } catch (SQLException e) {

        }
        bus.setId(nextId);
        try {
            addBusStatement.setInt(1, bus.getId());
            addBusStatement.setString(2, bus.getMaker());
            addBusStatement.setString(3, bus.getSeries());
            addBusStatement.setInt(4, bus.getSeatNumber());

            if(bus.getDriverOne()!=null){
                addBusStatement.setInt(5,bus.getDriverOne().getId());
            }else{
                addBusStatement.setInt(5,-1);
            }

            if(bus.getDriverTwo()!=null){
                addBusStatement.setInt(6,bus.getDriverTwo().getId());
            }else{
                addBusStatement.setInt(6,-1);
            }
            addBusStatement.executeUpdate();
        } catch (SQLException ignored) {

        }
    }

    public ArrayList<Bus> getBusses() {
        ArrayList<Bus> busses = new ArrayList<>();
        Bus bus=null;
        try {
            ResultSet resultSet = getBussesStatement.executeQuery();
            while((bus=getBusFromResultSet(resultSet))!=null){
                busses.add(bus);
            }
            resultSet.close();
        } catch (SQLException e) {

        }
        return busses;
    }

    public void deleteBus(Bus bus) {
        try{
            deleteBusStatement.setInt(1,bus.getId());
            deleteBusStatement.executeUpdate();
        }catch (SQLException ignored){

        }
    }


    public void deleteDriver(Driver driver) {
        try {
            deleteDriverStatement.setString(1,driver.getUmcn());
            deleteDriverStatement.executeUpdate();
            Statement statement = connection.createStatement();
            statement.execute("update busses set driverOne=-1 where driverOne="+driver.getId() );
            statement.execute("update busses set driverTwo=-1 where driverTwo="+driver.getId() );
        } catch (SQLException e) {

        }
    }

    public void resetDatabase() {
        try {
            resetDriversStatement.executeUpdate();
            resetBussesStatement.executeUpdate();
        } catch (SQLException ignored) {
        }

    }

    private Driver getDriverFromResultSet(ResultSet resultSet) {
        Driver driver = new Driver();
        try {
            if (resultSet.next()){
                driver.setId(resultSet.getInt("id"));
                driver.setName(resultSet.getString("name"));
                driver.setSurname(resultSet.getString("surname"));
                driver.setUmcn(resultSet.getString("umcn"));
                driver.setBirthday(resultSet.getDate("birthday").toLocalDate());
                driver.setHireDate(resultSet.getDate("hireDate").toLocalDate());
            }else{
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
        return driver;
    }

    private Bus getBusFromResultSet(ResultSet resultSet) {
        Bus bus = new Bus();
        try {
            if (resultSet.next()){
                bus.setId(resultSet.getInt("id"));
                bus.setMaker(resultSet.getString("maker"));
                bus.setSeries(resultSet.getString("series"));
                bus.setSeatNumber(resultSet.getInt("seatNumber"));
                findDriverById.setInt(1,resultSet.getInt("driverOne"));
                ResultSet resultSet1 = findDriverById.executeQuery();
                Driver driverOne = null;
                driverOne=getDriverFromResultSet(resultSet1);
                bus.setDriverOne(driverOne);
                Driver driverTwo = null;
                findDriverById.setInt(1,resultSet.getInt("driverTwo"));
                resultSet1.close();
                resultSet1 = findDriverById.executeQuery();
                driverTwo = getDriverFromResultSet(resultSet1);
                resultSet1.close();
                bus.setDriverTwo(driverTwo);
            }else{
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
        return bus;
    }

    private void createDatabaseTables() {
        String[] sqlStatements = parseSQLStatementsFromFile();
        try {
            Statement statement = connection.createStatement();
            for(String statementText:sqlStatements){
                statement.execute(statementText);
            }
        } catch (SQLException ex) {
        }
    }

    private String[] parseSQLStatementsFromFile() {
        String sqlBundle = "";
        try {
            URL file = getClass().getResource("/databaseSetup.sql");
            Scanner tok = new Scanner(new FileReader(file.getFile()));
            while(tok.hasNextLine()){
                sqlBundle+= tok.nextLine();
            }
            tok.close();
        } catch (FileNotFoundException ex) {
        }
        sqlBundle=sqlBundle.replace("\n"," ");
        sqlBundle=sqlBundle.replace(";","\n");
        String[] sqlStatements = sqlBundle.split("\n");
        sqlStatements = Arrays.copyOfRange(sqlStatements,0,sqlStatements.length);
        return sqlStatements;
    }

    private void initializePreparedStatements() throws SQLException {
        addDriverStatement = connection.prepareStatement("insert into drivers values (?,?,?,?,?,?)");
        addBusStatement = connection.prepareStatement("insert into busses values (?,?,?,?,?,?)");
        deleteDriverStatement = connection.prepareStatement("delete from drivers where umcn=?");
        deleteBusStatement = connection.prepareStatement("delete from busses where id=?");
        getDriversStatement = connection.prepareStatement("select * from drivers");
        getBussesStatement = connection.prepareStatement("select * from busses");
        getNextDriverId = connection.prepareStatement("select max(id)+1 from drivers");
        getNextBusId = connection.prepareStatement("select max(id)+1 from busses");
        resetDriversStatement = connection.prepareStatement("delete from drivers");
        resetBussesStatement = connection.prepareStatement("delete from busses");
        findDriverById = connection.prepareStatement("select * from drivers where id=?");
    }

    public void dodijeliVozacuAutobus(Driver driver, Bus bus, int which) {
        PreparedStatement preparedStatement = null;
        try {
            if (which == 1) {
                preparedStatement = connection.prepareStatement("update busses set driverOne=? where id=?");
            } else {
                preparedStatement = connection.prepareStatement("update  busses set driverTwo=? where id = ?");
            }
            preparedStatement.setInt(1, driver.getId());
            preparedStatement.setInt(2,bus.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {

        }
    }
}

