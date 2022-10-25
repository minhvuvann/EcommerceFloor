package vn.mellow.ecom.ecommercefloor.enums;

/**
 * Represents the reason for the order's cancellation.
 */
public enum OrderCancelReason {

    /**
     * The customer wanted to cancel the order.
     */
    CUSTOMER("CUSTOMER"),
    /**
     * The order was fraudulent.
     */
    FRAUD("FRAUD"),
    /**
     * There was insufficient inventory.
     */
    ADMIN("ADMIN"),
    /**
     * Payment was declined.
     */
    DECLINED("DECLINED"),
    /**
     * The order was canceled for an unlisted reason.
     */
    OTHER("OTHER");

    private final String graphqlName;

    private OrderCancelReason(String graphqlName) {
        this.graphqlName = graphqlName;
    }

    @Override
    public String toString() {
        return this.graphqlName;
    }

}
