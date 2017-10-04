package models;

/**
 * Created by manjeet on 12/09/15.
 */


import java.util.Date;
import java.util.UUID;

/**
 * Created by manjeet on 10/08/15.
 */


public class DeliveryTracking {
        public TrackingStatus trackingStatus;
        public String trackingNumber;
        //public Date estimatedDate;

        public DeliveryTracking() {
                trackingStatus = TrackingStatus.INITIATED;
                trackingNumber = UUID.randomUUID().toString();
        }

        public enum TrackingStatus {
                INITIATED,
                PACKED,
                ONTRUCK,
                CLOSED
        }
}
