package dataobjects;

/**
 * Created by adarsh on 7/20/16.
 */
public class Travel {

    String startAddress;
    String distance;
    String endAddress;
    MyLatLogn sourceLocation;
    MyLatLogn destinatonLocation;

    public MyLatLogn getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(MyLatLogn sourceLocation) {
        this.sourceLocation = sourceLocation;
    }


    public MyLatLogn getDestinatonLocation() {
        return destinatonLocation;
    }

    public void setDestinatonLocation(MyLatLogn destinatonLocation) {
        this.destinatonLocation = destinatonLocation;
    }


    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
