package ba.unsa.etf.rs.tutorijal8;

public class Bus {

    private String maker,series;
    private int seatNumber;
    private int id;
    private Driver driverOne,driverTwo;

    public Bus(String maker, String series, int seatNumber) {
        this.maker=maker;
        this.series=series;
        this.seatNumber=seatNumber;
    }

    public Bus() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Driver getDriverOne() {
        return driverOne;
    }

    public void setDriverOne(Driver driverOne) {
        this.driverOne = driverOne;
    }

    public Driver getDriverTwo() {
        return driverTwo;
    }

    public void setDriverTwo(Driver driverTwo) {
        this.driverTwo = driverTwo;
    }

    @Override
    public String toString() {
        return maker+" "+ series+" ( seats: "+seatNumber+" )"+((driverOne!=null)?" - ("+driverOne+")":"")+" "+((driverTwo!=null)?" - ("+driverTwo+")":"");
    }
}
