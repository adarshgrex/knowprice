package dataobjects;

public class LocationState {
    String id;
    String stateName;
    String stateCode;
    String petrolPrice;
    String dieselPrice;
    String gasPrice;

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPetrolPrice() {
        return petrolPrice;
    }

    public void setPetrolPrice(String petrolPrice) {
        this.petrolPrice = petrolPrice;
    }

    public String getDieselPrice() {
        return dieselPrice;
    }

    public void setDieselPrice(String dieselPrice) {
        this.dieselPrice = dieselPrice;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

}
