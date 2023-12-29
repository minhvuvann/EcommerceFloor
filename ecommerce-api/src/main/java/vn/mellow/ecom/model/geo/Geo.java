package vn.mellow.ecom.model.geo;

import lombok.*;
import vn.mellow.ecom.base.model.BaseModel;
import vn.mellow.ecom.model.enums.CountryCode;
import vn.mellow.ecom.model.enums.GeoType;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Getter
@Setter
public class Geo extends BaseModel {
    private String name;
    private int ghn_id;
    private int parent_id;
    private String code;
    private GeoType type;
    private String description;
    private CountryCode countryCode;
}
