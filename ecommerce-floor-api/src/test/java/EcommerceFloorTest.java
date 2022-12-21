import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vn.mellow.ecom.ecommercefloor.EcommerceFloorApplication;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.controller.ProductController;
import vn.mellow.ecom.ecommercefloor.controller.RegisterController;
import vn.mellow.ecom.ecommercefloor.controller.ShopController;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.manager.GeoManager;
import vn.mellow.ecom.ecommercefloor.manager.ProductManager;
import vn.mellow.ecom.ecommercefloor.model.geo.Address;
import vn.mellow.ecom.ecommercefloor.controller.UserController;
import vn.mellow.ecom.ecommercefloor.model.geo.Geo;
import vn.mellow.ecom.ecommercefloor.model.industrial.IndustrialProduct;
import vn.mellow.ecom.ecommercefloor.model.input.*;
import vn.mellow.ecom.ecommercefloor.model.product.Product;
import vn.mellow.ecom.ecommercefloor.model.product.ProductVariant;
import vn.mellow.ecom.ecommercefloor.model.product.Trademark;
import vn.mellow.ecom.ecommercefloor.model.size.DimensionMeasurement;
import vn.mellow.ecom.ecommercefloor.model.user.User;
import vn.mellow.ecom.ecommercefloor.utils.GeoUtils;
import vn.mellow.ecom.ecommercefloor.utils.MoneyCalculateUtils;
import vn.mellow.ecom.ecommercefloor.utils.NumberUtils;
import vn.mellow.ecom.ecommercefloor.utils.RemoveAccentUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

import java.util.ArrayList;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = EcommerceFloorApplication.class)
@AutoConfigureMockMvc
class EcommerceFloorTest {
    @Autowired
    private UserController userController;

    @Autowired
    private RegisterController registerController;

    @Autowired
    private ShopController shopController;

    @Autowired
    private ProductController productController;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private GeoManager geoManager;

    private ActivityUser byUser;

    private List<String> fullNames = new ArrayList<String>();


    public EcommerceFloorTest() {
        this.byUser = new ActivityUser();
        byUser.setUserId("adminVVM");
        byUser.setUserName("adminTest001");
        byUser.setEmail("adminTest001@gmail.com");
        byUser.setPhone("0927382733");
        fullNames.add("Bùi Anh Tú");
        fullNames.add("Nguyễn Duy Sơn");
        fullNames.add("Nguyễn Thị Hoa");
        fullNames.add("Đậu Thị Nhi");
        fullNames.add("Hồ Xuân Thịnh");
        fullNames.add("Huỳnh Thị Diễm Ngân");
        fullNames.add("Trần Thanh Tú");
        fullNames.add("Trấn Thành");
        fullNames.add("Trường Giang");
        fullNames.add("Diệu Nhi");
        fullNames.add("Hà Anh Tuấn");
        fullNames.add("Ngô Kiến Huy");
        fullNames.add("Minh Hằng");
        fullNames.add("Lâm Chấn Khang");
        fullNames.add("Lâm Chấn Huy");
        fullNames.add("Châu Khải Phong");
        fullNames.add("Chi Dân");
        fullNames.add("Ngô Tấn Tài");
        fullNames.add("Huấn Hoa Hồng");
        fullNames.add("Linh Ka");
        fullNames.add("Sơn Tùng MTP");
        fullNames.add("Hoài Linh");
        fullNames.add("Đan Trường");
        fullNames.add("Noo Phước Thịnh");
        fullNames.add("ChiPu");
        fullNames.add("Thuỷ Tiên");
        fullNames.add("Ngọc Trinh");
        fullNames.add("Thuỷ Chi");
    }

    @Test
    public void testUser() throws ServiceException {
        List<Trademark> trademarks = productController.getListTrademark();
        for (Trademark t : trademarks) {
            String fullName = fullNames.get(trademarks.indexOf(t));
            CreateUserInput createUserInput = new CreateUserInput();
            UserInput user = new UserInput();
            user.setBirthday(NumberUtils.generateDate());
            user.setByUser(byUser);
            user.setEmail(RemoveAccentUtils.generateUserName(fullName) + "@gmail.com");
            user.setDescription("Tài khoản khách hàng");
            user.setFullName(fullName);
            user.setGender(GenderType.WOMEN);
            user.setServiceType(ServiceType.NORMALLY);
            user.setTelephone(NumberUtils.generateTelePhone());
            HashMap<String, String> mapGeo = GeoUtils.generateGeo();
            Address address = new Address();
            address.setAddress1(mapGeo.get("address"));
            address.setWardCode(Integer.valueOf(mapGeo.get("ward")));
            Geo geoWard = geoManager.getGeoGHN_ID(GeoType.WARD, Integer.valueOf(mapGeo.get("ward")));
            address.setWard(geoWard.getName());
            Geo geoDistrict = geoManager.getGeoGHN_ID(GeoType.DISTRICT, Integer.valueOf(mapGeo.get("district")));
            address.setDistrict(geoDistrict.getName());
            address.setDistrictCode(Integer.valueOf(mapGeo.get("district")));
            Geo geoProvince = geoManager.getGeoGHN_ID(GeoType.PROVINCE, Integer.valueOf(mapGeo.get("province")));
            address.setProvince(geoProvince.getName());
            address.setProvinceCode(Integer.valueOf(mapGeo.get("province")));
            user.setAddress(address);
            createUserInput.setUser(user);
            KeyPasswordInput password = new KeyPasswordInput();
            password.setPasswordStatus(PasswordStatus.NEW);
            password.setPassword("MTIzNDU2");
            createUserInput.setPassword(password);
            RoleInput roleInput = new RoleInput();
            roleInput.setRoleType(RoleType.PERSONAL);
            createUserInput.setRole(roleInput);
            User result = null;
            try {
                //Đăng kí tài khoản khách hàng (mua)
                result = (User) registerController.registerUser(createUserInput).getData();
                HashMap<String, String> mapGeoShop = GeoUtils.generateGeo();
                Address addressShop = new Address();
                addressShop.setAddress1(mapGeoShop.get("address"));
                addressShop.setWardCode(Integer.valueOf(mapGeoShop.get("ward")));
                Geo geoWardShop = geoManager.getGeoGHN_ID(GeoType.WARD, Integer.valueOf(mapGeoShop.get("ward")));
                addressShop.setWard(geoWardShop.getName());
                Geo geoDistrictShop = geoManager.getGeoGHN_ID(GeoType.DISTRICT, Integer.valueOf(mapGeoShop.get("district")));
                addressShop.setDistrict(geoDistrictShop.getName());
                addressShop.setDistrictCode(Integer.valueOf(mapGeoShop.get("district")));
                Geo geoProvinceShop = geoManager.getGeoGHN_ID(GeoType.PROVINCE, Integer.valueOf(mapGeoShop.get("province")));
                addressShop.setProvince(geoProvinceShop.getName());
                addressShop.setProvinceCode(Integer.valueOf(mapGeoShop.get("province")));
                //Đăng kí tài khoản khách hàng (mua và bán)
                CreateShopInput shopInput = new CreateShopInput();
                shopInput.setWardCode(geoWardShop.getCode());
                shopInput.setDistrict_id(geoDistrictShop.getGhn_id());
                shopInput.setAddress(address.getAddress1());
                shopInput.setAddressShop(addressShop);
                shopInput.setPhone(NumberUtils.generateTelePhone());
                shopInput.setName(t.getDescription());
                shopInput.setDescription("Successful people do what unsuccessful people are not willing to do. Don’t wish it were easier; wish you were better.");
                shopInput.setImageUrl(t.getIconUrl());

                result = shopController.createShop(result.getId(), shopInput);

            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

    }
    public void t(){

    }

    @Test
    public void testIndustrialProduct() {
        for (IndustrialType type : IndustrialType.values()) {
            IndustrialProduct deck = new IndustrialProduct();
            deck.setName(type.getDescription());
            deck.setDescription(type.getDescription());
            deck.setIconUrl(type.getUrl());
            try {
                deck = productController.createIndustrialProduct(deck);
                if (type.equals(IndustrialType.ELECTRON)) {
                    for (TrademarkTypeElectron electron : TrademarkTypeElectron.values()) {
                        Trademark trademark = new Trademark();
                        trademark.setIndustrialId(deck.getId());
                        trademark.setName(electron.getDescription());
                        trademark.setDescription(electron.getDescription());
                        trademark.setIconUrl(electron.getUrl());
                        try {
                            trademark = productController.createTrademarkProduct(trademark);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (type.equals(IndustrialType.HOUSE_LIFE)) {
                    for (TrademarkTypeLife life : TrademarkTypeLife.values()) {
                        Trademark trademark = new Trademark();
                        trademark.setIndustrialId(deck.getId());
                        trademark.setName(life.getDescription());
                        trademark.setDescription(life.getDescription());
                        trademark.setIconUrl(life.getUrl());
                        try {
                            trademark = productController.createTrademarkProduct(trademark);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (type.equals(IndustrialType.CAR)) {
                    for (TrademarkTypeCar car : TrademarkTypeCar.values()) {
                        Trademark trademark = new Trademark();
                        trademark.setIndustrialId(deck.getId());
                        trademark.setName(car.getDescription());
                        trademark.setDescription(car.getDescription());
                        trademark.setIconUrl(car.getUrl());
                        try {
                            trademark = productController.createTrademarkProduct(trademark);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (type.equals(IndustrialType.BEAUTY)) {
                    for (TrademarkTypeBeauty beauty : TrademarkTypeBeauty.values()) {
                        Trademark trademark = new Trademark();
                        trademark.setIndustrialId(deck.getId());
                        trademark.setName(beauty.getDescription());
                        trademark.setDescription(beauty.getDescription());
                        trademark.setIconUrl(beauty.getUrl());
                        try {
                            trademark = productController.createTrademarkProduct(trademark);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (type.equals(IndustrialType.BOOK_ONLINE)) {
                    for (TrademarkTypeBook book : TrademarkTypeBook.values()) {
                        Trademark trademark = new Trademark();
                        trademark.setIndustrialId(deck.getId());
                        trademark.setName(book.getDescription());
                        trademark.setDescription(book.getDescription());
                        trademark.setIconUrl(book.getUrl());
                        try {
                            trademark = productController.createTrademarkProduct(trademark);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (type.equals(IndustrialType.FASHION)) {
                    for (TrademarkTypeFashion fashion : TrademarkTypeFashion.values()) {
                        Trademark trademark = new Trademark();
                        trademark.setIndustrialId(deck.getId());
                        trademark.setName(fashion.getDescription());
                        trademark.setDescription(fashion.getDescription());
                        trademark.setIconUrl(fashion.getUrl());
                        try {
                            trademark = productController.createTrademarkProduct(trademark);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (type.equals(IndustrialType.HEALTH)) {
                    for (TrademarkTypeHealth health : TrademarkTypeHealth.values()) {
                        Trademark trademark = new Trademark();
                        trademark.setIndustrialId(deck.getId());
                        trademark.setName(health.getDescription());
                        trademark.setDescription(health.getDescription());
                        trademark.setIconUrl(health.getUrl());
                        try {
                            trademark = productController.createTrademarkProduct(trademark);
                        } catch (ServiceException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testProduct() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Apple Watch Series 7 45mm GPS Sport Band");
        productInput.setShopId(602329);
        productInput.setTradeMarkId("1671551420749648");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/decaf47f7cb7a2df555dbff0deeed698");
        images.add("https://cf.shopee.vn/file/a5dc7787667f07f10c26456c04a51d34");
        images.add("https://cf.shopee.vn/file/5fa46c2de37a681744f74bc59ed5005e");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/c42123a453771f56de2c938a6ee8af6d");
        productInput.setDescription("Trải nghiệm hoàn hảo trên màn hình lớn tràn viền\n" +
                "Apple Watch Series 7 sở hữu màn hình lớn hơn tới 20% so với thế hệ Series 6, hơn 50% so với Series 3, nhưng điều đặc biệt là kích thước tổng thể của đồng hồ không hề thay đổi. Để có được kết quả này, đội ngũ kỹ sư của Apple đã thiết kế lại hoàn toàn màn hình giúp giảm tới 40% diện tích phần viền, tạo thành một màn hình tràn viền đầy quyến rũ, nơi bạn xem được nhiều nội dung hơn, hình ảnh hấp dẫn hơn.\n" +
                "\n" +
                "Màn hình Retina Always-On tuyệt đẹp\n" +
                "Bạn không cần phải đưa tay lên hay chạm vào màn hình để xem giờ hoặc các thông tin khác nữa, đơn giản vì màn hình Apple Watch Series 7 luôn bật. Màn hình Retina Always-On của Apple Watch 7 giờ đây còn sáng hơn tới 70%, cho hình ảnh luôn hiển thị sáng đẹp và nổi bật.\n" +
                "\n" +
                "Màn hình lớn cho bạn làm được nhiều việc hơn\n" +
                "Không chỉ hiển thị được nhiều nội dung hơn, màn hình lớn của Apple Watch Series 7 còn cho bạn một trải nghiệm hoàn toàn mới, ngay từ các ứng dụng cơ bản. Apple đã thiết kế lại giao diện của máy tính, đồng hồ bấm giờ, bàn phím Qwerty và nhiều ứng dụng khác để tận dụng lợi thế của màn hình lớn, giúp bạn sử dụng đồng hồ trực quan và dễ dàng hơn bao giờ hết.\n" +
                "\n" +
                "Bền bỉ, cao cấp và tinh tế\n" +
                "Kiểu dáng sang trọng của dòng Apple Watch đã mang tính thương hiệu trong suốt nhiều năm qua. Giờ đây mọi thứ còn hoàn hảo hơn khi Apple tập trung về độ bền và những thay đổi đầy tinh tế. Phần màn hình được làm cong nhẹ với hiệu ứng ánh sáng khúc xạ rìa màn hình, tạo kết nối liền mạch với khung kim loại sắc sảo.\n" +
                "\n" +
                "Hơn nữa, đồng hồ Apple Watch Series 7 còn có khả năng chống bụi, chống nước và màn hình cứng cáp nhất từ trước đến nay với tinh thể pha lê dày gấp đôi Series 6. Sẽ có 5 màu sắc mới tươi trẻ thời thượng dành cho bạn lựa chọn trên phiên bản Apple Watch 7.\n" +
                "\n" +
                "Đo nồng độ oxy trong máu\n" +
                "Chỉ số oxy trong máu SpO2 là một chỉ số rất quan trọng về sức khỏe tổng quát của mỗi người. Nếu chỉ số này thấp hơn mức bình thường, nghĩa là sức khỏe của bạn đang tổn thương nghiêm trọng. Cảm biến thông minh và ứng dụng trực quan trên Apple Watch Series 7 cho phép bạn đo nồng độ oxy trong máu cùng các chỉ số nền liên quan bất cứ lúc nào, dù ngày hay đêm.\n" +
                "\n" +
                "Chụp điện tâm đồ chuyên nghiệp\n" +
                "Trên Apple Watch Series 7, bạn có thể tạo ra những bảng điện tâm đồ ECG tương tự các thiết bị y tế chuyên nghiệp. Đây là một thành tựu đột phá trên đồng hồ thông minh, có thể cung cấp dữ liệu quan trọng cho bản thân bạn và cả các bác sĩ, giúp bạn yên tâm hơn, đồng thời phát hiện sức khỏe bất thường kịp thời.\n" +
                "\n" +
                "Cảm biến nhịp tim chính xác\n" +
                "Chỉ cần chạm ngón tay vào vòng xoay Digital Crown trên Apple Watch Series 7, đồng hồ sẽ tạo ra sóng ECG trong 30 giây và cho biết chính xác nhịp tim của bạn. Qua đó bạn sẽ kiểm soát được nhịp tim, nếu nhịp tim đều nghĩa là sức khỏe của bạn đang bình thường còn nếu nhịp tim không đều, có thể bạn đang có dấu hiệu rung nhĩ và cần phải gặp bác sĩ.\n" +
                "\n" +
                "Công nghệ sạc nhanh tiện lợi\n" +
                "Hệ thống sạc trên Apple Watch Series 7 đã được nâng cấp để tốc độ sạc nhanh hơn bao giờ hết. Với kiến trúc sạc mới và cáp sạc nhanh USB-C, bạn chỉ mất 45 phút để sạc được từ 0 lên 80%, nhanh hơn tới 33% so với Series 6.\n" +
                "\n" +
                "Rèn luyện sức khỏe mọi lúc mọi nơi\n" +
                "Apple Watch Series 7 là động lực để bạn tập luyện, nâng cao sức khỏe mỗi ngày. Trên Apple Watch có một hệ thống các bài tập thể dục tại chỗ để bạn tranh thủ tập luyện ở bất cứ đâu. Với màn hình lớn hơn, bạn sẽ thấy thống kê chi tiết các chỉ số tập luyện một cách trực quan hơn, qua đó quá trình tập luyện sẽ hiệu quả và thú vị hơn.\n" +
                "\n" +
                "ĐẶC ĐIỂM NỔI BẬT\n" +
                "- Chức năng màn hình luôn bật giữ cho chức năng xem giờ luôn hoạt động,tiết kiệm pin hơn\n" +
                "- Thoải mái sử dụng ở hồ bơi hay ngoài trời với chuẩn kháng bụi IP6X ,chống nước đến 50m\n" +
                "- Đo nhịp tim,oxy trong máu,theo dõi giấc ngủ cùng nhiều tính năng sức khoẻ tích hợp sẵn\n" +
                "- Trải nghiệm âm nhạc với bộ nhớ trong 32GB cùng khả năng kết nối tai nghe bluetooth\n" +
                "- Cổng sạc Type C,sạc nhanh 45 phút cho 80% pin\n" +
                "\n" +
                "Thông số kỹ thuật\n" +
                "Công nghệ màn hình: LTPO OLED display (1000 nits)\n" +
                "Kích thước màn hình: 45mm\n" +
                "CPU: Apple S7\n" +
                "Bộ nhớ trong: 32GB\n" +
                "Hệ điều hành: watchOS 8\n" +
                "Kết nối được với hệ điều hành: iPhone 6s trở lên dùng iOS mới nhất\n" +
                "Chất liệu mặt: Ion-X strengthened glass\n" +
                "Chất liệu dây: Cao su\n" +
                "Chống nước: Có, độ sâu dưới 50m\n" +
                "Thời gian sử dụng pin: Sử dụng đến 18h\n" +
                "Kết nối: Wi-Fi, Cellular, Bluetooth\n" +
                "\n" +
                "Thông tin bảo hành\n" +
                "Sản phẩm được bảo hành bằng hóa đơn \n" +
                "Bảo hành: 12 tháng kể từ ngày xuất hóa đơn \n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất: \n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "Bước 2: Chọn sản phẩm. \n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu. \n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/c42123a453771f56de2c938a6ee8af6d");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));

        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/ac1e535a386002f69d24cf59a929da62");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/d992ed6a1dfe51d755750689b0b094b9");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLUE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(910));
        productVariantList.add(variant_1);
        productVariantList.add(variant_2);
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


}
