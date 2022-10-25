package vn.mellow.ecom.ecommercefloor.base.model;

import lombok.Data;

@Data
public class DimensionMeasurement {
    private DimensionUnit dimensionUnit;
    private double width;
    private double height;
    private double length;
}
