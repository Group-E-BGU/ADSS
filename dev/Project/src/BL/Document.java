package BL;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Document{

    private int document_id;
    private Map<String, Integer> deliveryGoods;

    public Document(){
    }

    public int getTruckWeight() {
        return TruckWeight;
    }

    public void setTruckWeight(int truckWeight) {
        TruckWeight = truckWeight;
    }

    public int getDocumentID()
    {
        return document_id;
    }

    public void setDocumentID(int document_id)
    {
        this.document_id = document_id;
    }

    public void setDeliveryGoods(Map<String, Integer> deliveryGoods) {
        this.deliveryGoods = deliveryGoods;
    }

    public Map<String, Integer> getDeliveryGoods() {
        return deliveryGoods;
    }
}