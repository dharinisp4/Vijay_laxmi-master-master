package Model;

/**
 * Developed by Binplus Technologies pvt. ltd.  on 03,November,2020
 */
public class GstModel {
    String id,gst,pricewithoutgst;
    public GstModel() {
    }

    public GstModel(String id, String gst, String pricewithoutgst) {
        this.id = id;
        this.gst = gst;
        this.pricewithoutgst = pricewithoutgst;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPricewithoutgst() {
        return pricewithoutgst;
    }

    public void setPricewithoutgst(String pricewithoutgst) {
        this.pricewithoutgst = pricewithoutgst;
    }
}
