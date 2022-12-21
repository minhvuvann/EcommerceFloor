import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vn.mellow.ecom.ecommercefloor.EcommerceFloorApplication;
import vn.mellow.ecom.ecommercefloor.base.exception.ServiceException;
import vn.mellow.ecom.ecommercefloor.base.logs.ActivityUser;
import vn.mellow.ecom.ecommercefloor.controller.*;
import vn.mellow.ecom.ecommercefloor.enums.*;
import vn.mellow.ecom.ecommercefloor.manager.GeoManager;
import vn.mellow.ecom.ecommercefloor.manager.ProductManager;
import vn.mellow.ecom.ecommercefloor.model.cart.CartDetail;
import vn.mellow.ecom.ecommercefloor.model.geo.Address;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
    private CartController cartController;

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

    public void t() {

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
    public void testCartDetail() {
        CartDetail cartDetail = null;
        try {
            cartDetail = cartController.getCartDetail("1671610208305328");

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        assertNotNull(cartDetail != null);
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

    //apple store
    @Test
    public void testProduct_602329_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple Watch Series 7 45mm GPS Sport Band");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/decaf47f7cb7a2df555dbff0deeed698");
        images.add("https://cf.shopee.vn/file/a5dc7787667f07f10c26456c04a51d34");
        images.add("https://cf.shopee.vn/file/5fa46c2de37a681744f74bc59ed5005e");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/c42123a453771f56de2c938a6ee8af6d");
        //thêm mô tả
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
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/ac1e535a386002f69d24cf59a929da62");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/d992ed6a1dfe51d755750689b0b094b9");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLUE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(910));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple AirPods with Charging ");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/74f393af9467db98f825d50c3c71304c_tn");
        images.add("https://cf.shopee.vn/file/77fcdcf6e091441846fb0f9cae6439a5_tn");
        images.add("https://cf.shopee.vn/file/1553529b098197d0493e41cde92c011e_tn");
        images.add("https://cf.shopee.vn/file/cff5850e5abc59ab782c8c2f88ce45e7_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/a256a89d3b7b4b80224e768d9ff6fdff_tn");
        //thêm mô tả
        productInput.setDescription("Với thời gian đàm thoại tối ưu, công nghệ đột phá và kết hợp cùng Hộp Sạc, AirPods đem đến trải nghiệm \n" +
                "tai nghe không dây tuyệt vời chưa từng có. Tai nghe có thể sử dụng được với tất cả các thiết bị của bạn (2). \n" +
                "Sau khi nhét vào tai, tai nghe sẽ kết nối ngay lập tức, đưa bạn hòa mình vào âm thanh phong phú và \n" +
                "có độ trung thực cao. Đều tuyệt vời. \n" +
                "\n" +
                "Tính năng nổi bật \n" +
                "•        Vừa vặn với mọi kích cỡ, thoải mái cả ngày dài \n" +
                "•        Tự động bật, tự động kết nối \n" +
                "•        Dễ dàng kết nối với tất cả thiết bị Apple của bạn (2)\n" +
                "•        Điều khiển nhạc và cuộc gọi của bạn từ AirPods \n" +
                "•        Chuyển đổi mượt mà giữa các thiết bị (2) \n" +
                "•        Hộp sạc đem đến thời lượng pin hơn 24 giờ (1)\n" +
                " \n" +
                "Pháp lý \n" +
                "(1) Thời lượng pin khác nhau tùy theo cách sử dụng và cấu hình. Truy cập apple.com/batteries \n" +
                "để biết thêm thông tin. \n" +
                "(2) Cần có tài khoản iCloud và macOS 10.14.4, iOS 12.2, iPadOS, watchOS 5.2 hoặc tvOS 13.2 trở lên. \n" +
                " \n" +
                "Bộ sản phẩm bao gồm: \n" +
                "•        Tai nghe\n" +
                "•        Sách hướng dẫn\n" +
                "•        Hộp sạc: Lightning\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Sản phẩm được bảo hành bằng hóa đơn\n" +
                "Bảo hành: 12 tháng kể từ ngày xuất hóa đơn\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất..");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/74f393af9467db98f825d50c3c71304c_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(950));
        productVariantList.add(variant_1);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple Watch Series 3 38mm GPS Sport Band");

        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/baa6bf0f0db1fe474a5ab815ec189f25_tn");
        images.add("https://cf.shopee.vn/file/4b1df878ee257bd895c0b6b0e8bdd5d2_tn");
        images.add("https://cf.shopee.vn/file/80c2d5fac406bff413bfda3c8f31eca2_tn");
        images.add("https://cf.shopee.vn/file/b229b6c671e963d5ed9f3954b860d02f_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/982a277b97e49732507d9d68341267c6_tn");
        //thêm mô tả
        productInput.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Kiểm tra nhịp tim và nhận thông báo khi nhịp tim nhanh và chậm. Đo lường các bài luyện tập, theo dõi và \n" +
                "chia sẻ hoạt động của bạn. Nhận cuộc gọi, trả lời tin nhắn và đồng bộ nhạc và podcast yêu thích. \n" +
                "Với Apple Watch Series 3, tất cả đều gần trong tầm tay.\n" +
                "\n" +
                "Tính năng nổi bật\n" +
                "•        Xem nhanh thông tin quan trọng trên màn hình Retina\n" +
                "•        Theo dõi nhịp tim bất cứ lúc nào với ứng dụng Nhịp Tim\n" +
                "•        Nhận thông báo về nhịp tim nhanh và chậm\n" +
                "•        Nhận cuộc gọi và trả lời tin nhắn ngay từ cổ tay\n" +
                "•        Đồng bộ nhạc và podcast yêu thích\n" +
                "•        Theo dõi hoạt động hàng ngày của bạn trên Apple Watch và xem xu hướng của bạn trong \n" +
                "ứng dụng Thể Dục trên iPhone\n" +
                "•        Đo lường các hoạt động thể dục của bạn như chạy, đi bộ, đạp xe, tập yoga, bơi lội và khiêu vũ\n" +
                "•        Thiết kế chống thấm nước khi bơi lội (1)\n" +
                "•        SOS Khẩn Cấp giúp bạn gọi xin trợ giúp ngay từ cổ tay (2)\n" +
                "•        S3 có bộ xử lý lõi kép\n" +
                "•        watchOS 7 sở hữu tính năng theo dõi giấc ngủ, chỉ đường khi đi xe đạp và mặt đồng hồ có thể \n" +
                "tùy chỉnh mới\n" +
                "•        Vỏ nhôm hiện có hai màu\n" +
                "\n" +
                "Pháp lý\n" +
                "Apple Watch Series 3 tương thích với iPhone 6s hoặc các phiên bản cao hơn sử dụng iOS 14 hoặc \n" +
                "các bản cập nhật cao hơn. \n" +
                "(1) Tiêu chuẩn ISO 22810:2010. Phù hợp cho các hoạt động dưới nước nông như bơi lội. Khuyến nghị \n" +
                "không sử dụng sản phẩm dưới độ sâu cho phép và khi tham gia các hoạt động có tốc độ cao dưới nước.\n" +
                "(2) Để sử dụng SOS khẩn cấp, iPhone phải ở gần bạn. Nếu iPhone không ở gần bạn, Apple Watch cần \n" +
                "được kết nối với mạng Wi-Fi đã xác định và bạn phải thiết lập Cuộc Gọi Wi-Fi.\n" +
                "\n" +
                "Bộ sản phẩm bao gồm: \n" +
                "•        Đồng hồ\n" +
                "•        Hộp\n" +
                "•        Dây đế sạc Apple\n" +
                "•        Sách hướng dẫn\n" +
                "•        Dây đeo dự phòng\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Sản phẩm được bảo hành bằng hóa đơn\n" +
                "Bảo hành: 12 tháng kể từ ngày xuất hóa đơn\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&amp;locale=en_VN\n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.\n" +
                "Note : Silver Aluminium Case with White Sport Band, Space Gray Aluminum Black Sport");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/74f393af9467db98f825d50c3c71304c_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.SILVER);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1200));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/4b1df878ee257bd895c0b6b0e8bdd5d2_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.GRAY);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_2);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple iPhone 14 128GB");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/39c9bb82bbf0dbe7d573ebec7c0bbfac_tn");
        images.add("https://cf.shopee.vn/file/8b299a01bed582fe9b285405833dff97_tn");
        images.add("https://cf.shopee.vn/file/ef7e86e59bc3f59667abe75b2cb523b4_tn");
        images.add("https://cf.shopee.vn/file/5d40817b8aa13d6685a825e06cc9d932_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/0982de1d517eed28495a9bbcaced5881_tn");
        //thêm mô tả
        productInput.setDescription("IPhone 14. Với hệ thống camera kép tiên tiến nhất từng có trên iPhone. Chụp những bức ảnh tuyệt đẹp \n" +
                "trong điều kiện từ thiếu sáng đến dư sáng. Phát hiện Va Chạm,1 một tính năng an toàn mới, t\n" +
                "hay bạn gọi trợ giúp khi cần kíp.\n" +
                "\n" +
                "Tính năng nổi bật\n" +
                "•        Màn hình Super Retina XDR 6,1 inch2\n" +
                "•        Hệ thống camera tiên tiến cho chất lượng ảnh đẹp hơn trong mọi điều kiện ánh sáng\n" +
                "•        Chế độ Điện Ảnh nay đã hỗ trợ 4K Dolby Vision tốc độ lên đến 30 fps\n" +
                "•        Chế độ Hành Động để quay video cầm tay mượt mà, ổn định\n" +
                "•        Công nghệ an toàn quan trọng - Phát Hiện Va Chạm1 thay bạn gọi trợ giúp khi cần kíp\n" +
                "•        Thời lượng pin cả ngày và thời gian xem video lên đến 20 giờ3\n" +
                "•        Chip A15 Bionic với GPU 5 lõi để đạt hiệu suất siêu nhanh. Mạng di động 5G siêu nhanh4\n" +
                "•        Các tính năng về độ bền dẫn đầu như Ceramic Shield và khả năng chống nước5\n" +
                "•        iOS 16 đem đến thêm nhiều cách để cá nhân hóa, giao tiếp và chia sẻ6\n" +
                "\n" +
                "Pháp lý\n" +
                "1SOS Khẩn Cấp sử dụng kết nối mạng di động hoặc Cuộc Gọi Wi-Fi.\n" +
                "2Màn hình có các góc bo tròn. Khi tính theo hình chữ nhật chuẩn, kích thước màn hình theo \n" +
                "đường chéo là 6,06 inch. Diện tích hiển thị thực tế nhỏ hơn.\n" +
                "3Thời lượng pin khác nhau tùy theo cách sử dụng và cấu hình; truy cập apple.com/batteries để biết \n" +
                "thêm thông tin.\n" +
                "4Cần có gói cước dữ liệu. Mạng 5G chỉ khả dụng ở một số thị trường và được cung cấp qua một \n" +
                "số nhà mạng. \n" +
                "Tốc độ có thể thay đổi tùy địa điểm và nhà mạng. Để biết thông tin về hỗ trợ mạng 5G, vui lòng liên hệ \n" +
                "nhà mạng và truy cập apple.com/iphone/cellular.\n" +
                "5iPhone 14 có khả năng chống tia nước, chống nước và chống bụi. Sản phẩm đã qua kiểm nghiệm \n" +
                "trong điều kiện phòng thí nghiệm có kiểm soát đạt mức IP68 theo tiêu chuẩn IEC 60529 (chống nước \n" +
                "ở độ sâu tối đa 6 mét trong vòng tối đa 30 phút). Khả năng chống tia nước, chống nước và chống bụi \n" +
                "không phải là các điều kiện vĩnh viễn. Khả năng này có thể giảm do hao mòn thông thường. Không sạc pin \n" +
                "khi iPhone đang bị ướt. Vui lòng tham khảo hướng dẫn sử dụng để biết cách lau sạch và làm khô máy. \n" +
                "Không bảo hành sản phẩm bị hỏng do thấm chất lỏng. \n" +
                "6Một số tính năng không khả dụng tại một số quốc gia hoặc khu vực. \n" +
                "\n" +
                "Thông số kỹ thuật\n" +
                "Truy cập apple.com/iphone/compare để xem cấu hình đầy đủ. \n" +
                "\n" +
                "\n" +
                "\n" +
                "Bộ sản phẩm bao gồm: \n" +
                "•        Điện thoại \n" +
                "•        Dây sạc\n" +
                "•        HDSD Bảo hành điện tử 12 tháng.\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Bảo hành: 12 tháng kể từ ngày kích hoạt sản phẩm.\n" +
                "Kích hoạt bảo hành tại: https://checkcoverage.apple.com/vn/en/\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/39c9bb82bbf0dbe7d573ebec7c0bbfac_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.PURPLE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1300));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/8b299a01bed582fe9b285405833dff97_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1200));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/ef7e86e59bc3f59667abe75b2cb523b4_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLUE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple iPhone 14 128GB");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/3af324bc17f502e8b1898fd5a2b01844_tn");
        images.add("https://cf.shopee.vn/file/b7addff24c257ae552ee31f16a23af7c_tn");
        images.add("https://cf.shopee.vn/file/bdda15455bac30f5bc2b404752bbe3ba_tn");
        images.add("https://cf.shopee.vn/file/198720e37eeccfcdc98c6ea87f5decaa_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/93b3f6d4799b06f9279f817540daa4d1_tn");
        //thêm mô tả
        productInput.setDescription("Điện thoại iPhone 14 Pro sở hữu trọng lượng 206g cùng thiết kế nhỏ gọn cho khả năng cầm nắm thoải mái. \n" +
                "Về thông số màn hình, điện thoại được trang bị màn hình có độ phân giải 2556 x 1179 pixel và \n" +
                "mật độ điểm ảnh 2556 x 1179 pixel mang lại khả năng hiển thị ấn tượng.\n" +
                "\n" +
                "Điện thoại được trang bị màn hình Dynamic Island siêu ấn tượng với khả năng tuy biến thành nhiều dạng\n" +
                " theo điều khiển của người dùng. Nhờ đó người dùng có thể theo dõi và hiển thị nhanh các thông tin như \n" +
                "cuộc gọi, chỉ đường, hẹn giờ,...\n" +
                "\n" +
                "Trên điện thoại iPhone 14 Pro, lần đầu Apple trang bị cho sản phẩm của mình camera cảm biến lớn. \n" +
                "Theo đó, thiết bị đã được nâng cấp camera chính lên độ phân giải 48MP, kết hợp công ngệ pixel-pinning \n" +
                "hỗ trợ nâng cao khả năng chụp hình trong điều kiện thiếu sáng.\n" +
                "\n" +
                "Điện thoại được trang bị viên pin dung lượng lớn cùng con chip Apple A16, thiết bị mang lại thời gian \n" +
                "phát video trực tuyến lên tới 20 giờ hoặc 75 giờ phát nhạc.\n" +
                "\n" +
                "Bộ sản phẩm bao gồm: \n" +
                "•        Điện thoại \n" +
                "•        Dây sạc\n" +
                "•        HDSD Bảo hành điện tử 12 tháng.\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Bảo hành: 12 tháng kể từ ngày kích hoạt sản phẩm.\n" +
                "Kích hoạt bảo hành tại: https://checkcoverage.apple.com/vn/en/\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.\n" +
                "\n" +
                "Bộ sản phẩm bao gồm: \n" +
                "•        Điện thoại \n" +
                "•        Dây sạc\n" +
                "•        HDSD Bảo hành điện tử 12 tháng.\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Bảo hành: 12 tháng kể từ ngày kích hoạt sản phẩm.\n" +
                "Kích hoạt bảo hành tại: https://checkcoverage.apple.com/vn/en/\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/3af324bc17f502e8b1898fd5a2b01844_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.PURPLE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1500));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/b7addff24c257ae552ee31f16a23af7c_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.SILVER);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1300));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/198720e37eeccfcdc98c6ea87f5decaa_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLACK);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple Macbook Air (2022) M2 chip, 13.6 inches, 8GB, 256GB SSD");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/b9245889043f87930168547032a5843e_tn");
        images.add("https://cf.shopee.vn/file/b09430e3c8c8dba7b6dc87d25cd8cb0e_tn");
        images.add("https://cf.shopee.vn/file/d3ebf604bcc0fcc5420cd0e680ef501a_tn");
        images.add("https://cf.shopee.vn/file/26b018e5decbecf17fb8f72117f8ea10_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/1cba156175a5ef0b0d356b52451b4c42_tn");
        //thêm mô tả
        productInput.setDescription("Apple Macbook Air (2022) M2 chip  (năm SX 2022), 13.6 inches, 8GB, 256GB SSD\n" +
                "SPG (Space Gray), STL (Starlight), MDN (Midnight). \n" +
                "Bộ xử lý\n" +
                "Hãng CPU: Apple\n" +
                "Công nghệ CPU: M2\n" +
                "Loại CPU: 8 - Core\n" +
                "Tốc độ CPU: 100GB/s memory bandwidth\n" +
                "\n" +
                "RAM & Ổ cứng\n" +
                "Dung lượng RAM: 8 GB\n" +
                "Dung lượng ổ cứng: 256GB SSD\n" +
                "\n" +
                "Màn hình\n" +
                "Kích thước màn hình: 13.6 inches\n" +
                "Công nghệ màn hình: Liquid Retina, True Tone Technology, Wide color (P3)\n" +
                "Độ phân giải: 2560 x 1664 Pixels\n" +
                "Loại màn hình: LED\n" +
                "Độ sáng: 500 nits\n" +
                "Colors: 1 triệu màu\n" +
                "\n" +
                "Đồ họa\n" +
                "Card onboard\n" +
                "Hãng: Apple\n" +
                "Model GPU: 8 - Core\n" +
                "\n" +
                "Bảo mật\n" +
                "Mở khóa vân tay\n" +
                "Mật khẩu\n" +
                "\n" +
                "Giao tiếp & kết nối\n" +
                "Cổng giao tiếp        \n" +
                "2 x Thunderbolt 3/USB 4\n" +
                "1 x Jack tai nghe 3.5 mm\n" +
                "1 x MagSafe 3\n" +
                "\n" +
                "Wifi: 802.11ax\n" +
                "Bluetooth: v5.0\n" +
                "Webcam: 1080p FaceTime HD camera\n" +
                "\n" +
                " m thanh\n" +
                "Số lượng loa: 4\n" +
                "Số lượng microphones: 3\n" +
                "Công nghệ âm thanh: Spatial Audio, Dolby Atmos\n" +
                "\n" +
                "Bàn phím & TouchPad\n" +
                "Kiểu bàn phím: Backlit Magic Keyboard\n" +
                "Bàn phím số: Không\n" +
                "Đèn bàn phím: Có\n" +
                "Touch ID: Có\n" +
                "TouchPad: Multi-touch touchpad\n" +
                "\n" +
                "Thông tin pin & Sạc\n" +
                "Loại PIN: Lithium polymer\n" +
                "Nguồn vào: 30W USB-C Power Adapter \n" +
                "Dung lượng pin: \n" +
                "- Lên tới 18 giờ Apple TV app movie playback\n" +
                "- Lên tới 15 giờ wireless web\n" +
                "\n" +
                "Hệ điều hành\n" +
                "OS: macOS\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Bảo hành: 12 tháng kể từ ngày kích hoạt sản phẩm.\n" +
                "Kích hoạt bảo hành tại: https://checkcoverage.apple.com/vn/en/\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/b9245889043f87930168547032a5843e_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.GRAY);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(2000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/b09430e3c8c8dba7b6dc87d25cd8cb0e_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.SILVER);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1500));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/d3ebf604bcc0fcc5420cd0e680ef501a_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLACK);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1200));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple AirPods Pro");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a188dbde7bf8b549535fb5b2dc99902e_tn");
        images.add("https://cf.shopee.vn/file/dd89f8691815c4b874ffb3772fa0f62f_tn");
        images.add("https://cf.shopee.vn/file/883547817583605ccfb1fb925a1f950e_tn");
        images.add("https://cf.shopee.vn/file/89a436849bfefddcd542dfba60ec955a_tn");
        images.add("https://cf.shopee.vn/file/82037f8c1b7dfdcfb543072e8c2f5b85_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/5b0ee8b80d604dd93e2b77278b770928_tn");
        //thêm mô tả
        productInput.setDescription("Tính năng Chủ Động Khử Tiếng Ồn tạo ra âm thanh sống động. Chế Độ Xuyên Âm giúp bạn nghe và kết nối \n" +
                "với thế giới xung quanh. Và kích cỡ tai nghe tùy chỉnh tạo sự thoải mái cả ngày dài. Chống mồ hôi và \n" +
                "chống nước (1). Tất cả trong một chiếc tai nghe siêu nhẹ, dễ thiết lập với mọi thiết bị Apple của bạn (2). \n" +
                "\n" +
                "Tính năng nổi bật\n" +
                "•        Tính năng Chủ Động Khử Tiếng Ồn tạo ra âm thanh sống động\n" +
                "•        Chế Độ Xuyên Âm giúp bạn nghe và kết nối với thế giới xung quanh\n" +
                "•        Đầu silicon mềm mại với ba kích cỡ khác nhau tạo sự thoải mái và ôm khít\n" +
                "•        Chống mồ hôi và chống nước (1)\n" +
                "•        EQ thích ứng sẽ tự động điều chỉnh nhạc theo kích thước tai của bạn\n" +
                "•        Dễ dàng kết nối với tất cả thiết bị Apple của bạn (2)\n" +
                "•        Điều khiển nhạc và cuộc gọi của bạn từ AirPods\n" +
                "•        Hộp Sạc Không Dây đem đến thời lượng pin hơn 24 giờ (3)\n" +
                "\n" +
                "Pháp lý\n" +
                "(1) AirPods Pro có khả năng chống mồ hôi và chống nước, có thể sử dụng trong các môn thể thao và \n" +
                "luyện tập không liên quan đến nước. Tai nghe cũng đạt chuẩn IPX4. Khả năng chống mồ hôi và chống \n" +
                "nước không phải là điều kiện vĩnh viễn. Hộp sạc không có khả năng chống mồ hôi hoặc chống nước.\n" +
                "(2) Cần có tài khoản iCloud và macOS 10.14.4, iOS 12.2, iPadOS, watchOS 5.2 hoặc tvOS 13.2 trở lên.\n" +
                "(3) Thời lượng pin khác nhau tùy theo cách sử dụng và cấu hình. Truy cập apple.com/batteries để biết \n" +
                "thêm thông tin.\n" +
                "\n" +
                "Bộ sản phẩm bao gồm: \n" +
                "•        Tai nghe\n" +
                "•        Sách hướng dẫn\n" +
                "•        Hộp sạc\n" +
                "•        Cáp Type C - Lightning\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Sản phẩm được bảo hành bằng hóa đơn\n" +
                "Bảo hành: 12 tháng kể từ ngày xuất hóa đơn\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/a188dbde7bf8b549535fb5b2dc99902e_tn");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        //sửa màu
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);

        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple iPad Gen 9th 10.2-inch Wi-Fi 64GB");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/b46eef299808bc1751af1b5e9dce6cde_tn");
        images.add("https://cf.shopee.vn/file/5e1b81fa44eb632009b2eeec30edef2f_tn");
        images.add("https://cf.shopee.vn/file/ae538aa64c0ce480fd22bb5f0e8430d7_tn");
        images.add("https://cf.shopee.vn/file/4869283ee2d5359f3faadacf22bcc19b_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/38a51fee63a623600932f1aea78d76a9_tn");
        //thêm mô tả
        productInput.setDescription("Mạnh mẽ. Dễ sử dụng. Đa năng. iPad mới có màn hình Retina tuyệt đẹp, chip A13 Bionic mạnh mẽ, \n" +
                "camera trước Ultra Wide có tính năng Trung Tâm Màn Hình, tương thích với Apple Pencil và \n" +
                "Smart Keyboard (1). iPad giúp bạn dễ dàng làm được nhiều việc hơn nữa. Tất cả tính năng \n" +
                "với mức giá ấn tượng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Tính năng nổi bật\n" +
                "\n" +
                "•        Màn hình Retina 10.2 inch sống động với True Tone\n" +
                "\n" +
                "•        Chip A13 Bionic với Neural Engine\n" +
                "\n" +
                "•        Camera sau Wide 8MP, camera trước Ultra Wide 12MP với tính năng Trung Tâm Màn Hình\n" +
                "\n" +
                "•        Ổ lưu trữ lên tới 64GB\n" +
                "\n" +
                "•        Loa stereo\n" +
                "\n" +
                "•        Xác thực bảo mật với Touch ID\n" +
                "\n" +
                "•        Wi-Fi 802.11ac và dữ liệu di động LTE chuẩn Gigabit (2)\n" +
                "\n" +
                "•        Thời lượng pin lên tới 10 giờ (3)\n" +
                "\n" +
                "•        Cổng kết nối Lightning để sạc và kết nối phụ kiện\n" +
                "\n" +
                "•        Tương thích với Apple Pencil (thế hệ thứ 1) và Smart Keyboard (1)\n" +
                "\n" +
                "•        iPadOS 15 sở hữu sức mạnh độc đáo, dễ sử dụng và được thiết kế cho tính đa năng của iPad\n" +
                "\n" +

                "Pháp lý \n" +
                "\n" +
                "Ứng dụng có sẵn trên App Store. Nội dung được cung cấp có thể thay đổi.\n" +
                "\n" +
                "(1) Phụ kiện được bán riêng. Khả năng tương thích tùy thuộc thế hệ sản phẩm.\n" +
                "\n" +
                "(2) Cần có gói cước dữ liệu. Mạng LTE chuẩn Gigabit, 4G LTE Advanced, 4G LTE và gọi Wi-Fi chỉ khả dụng \n" +
                "ở một số thị trường và được cung cấp qua một số nhà mạng. Tốc độ phụ thuộc vào thông lượng lý thuyết và \n" +
                "có thể thay đổi tùy địa điểm và nhà mạng. Để biết thông tin về hỗ trợ mạng LTE, vui lòng liên hệ nhà mạng \n" +
                "và truy cập apple.com/ipad/cellular.\n" +
                "\n" +
                "(3) Thời lượng pin khác nhau tùy theo cách sử dụng và cấu hình. Truy cập apple.com/batteries \n" +
                "để biết thêm thông tin.\n" +
                "\n" +
                "Bộ sản phẩm bao gồm: \n" +
                "\n" +
                "•        iPad\n" +
                "\n" +
                "•        Dây sạc Lighting to USB-C\n" +
                "\n" +
                "•        20W USB Power Adaper\n" +
                "\n" +
                "•        HDSD Bảo hành điện tử 12 tháng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "\n" +
                "Bảo hành: 12 tháng kể từ ngày kích hoạt sản phẩm.\n" +
                "\n" +
                "Kích hoạt bảo hành tại: https://checkcoverage.apple.com/vn/en/\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "\n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.\n" +
                "\n" +

                "Model: A2602 : 10.2-inch");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/4869283ee2d5359f3faadacf22bcc19b_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1500));
        productVariantList.add(variant_1);

        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("ttps://cf.shopee.vn/file/5e1b81fa44eb632009b2eeec30edef2f_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.SILVER);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1300));
        productVariantList.add(variant_2);

        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple iPhone 11 64GB");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/3af324bc17f502e8b1898fd5a2b01844_tn");
        images.add("https://cf.shopee.vn/file/b7addff24c257ae552ee31f16a23af7c_tn");
        images.add("https://cf.shopee.vn/file/bdda15455bac30f5bc2b404752bbe3ba_tn");
        images.add("https://cf.shopee.vn/file/198720e37eeccfcdc98c6ea87f5decaa_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/93b3f6d4799b06f9279f817540daa4d1_tn");
        //thêm mô tả
        productInput.setDescription("Lưu ý: Các sản phẩm sản xuất sau tháng 10/2020 sẽ không có củ sạc và tai nghe trong bộ sản phẩm.\n" +
                "\n" +
                "Quay video 4K, chụp ảnh chân dung tuyệt đẹp và chụp ảnh phong cảnh rộng với hệ thống camera kép \n" +
                "hoàn toàn mới. Chụp ảnh tuyệt đẹp trong điều kiện ánh sáng yếu với chế độ Ban Đêm. Xem ảnh, video và\n" +
                "chơi game màu sắc chân thực trên màn hình Liquid Retina 6.1 inch (3). Trải nghiệm hiệu năng chưa từng có\n" +
                "với chip A13 Bionic cho game, thực tế ảo tăng cường (AR) và chụp ảnh. Làm được nhiều việc hơn và \n" +
                "sạc ít hơn với thời lượng pin bền bỉ cả ngày (2). Và bớt phải lo lắng nhờ khả năng chống nước \n" +
                "ở độ sâu tối đa 2 mét trong vòng 30 phút (1).\n" +
                "\n" +
                "Tính năng nổi bật \n" +
                "•        Màn hình Liquid Retina HD LCD 6.1 inch (3)  \n" +
                "•        Chống nước và chống bụi (chống nước ở độ sâu 2 mét trong vòng tối đa 30 phút, đạt mức IP68) (1) \n" +
                "•        Hệ thống camera kép 12MP với camera Ultra Wide và Wide; chế độ Ban Đêm, chế độ Chân Dung và \n" +
                "video 4K tốc độ tối đa 60fps \n" +
                "•        Camera trước TrueDepth 12MP với chế độ Chân Dung, quay video 4K và quay video chậm \n" +
                "•        Xác thực bảo mật với Face ID \n" +
                "•        Chip A13 Bionic với Neural Engine thế hệ thứ ba \n" +
                "•        Khả năng sạc nhanh\n" +
                "•        Sạc không dây (4) \n" +
                "•        iOS 13 cùng với chế độ Dark Mode, các công cụ chỉnh ảnh và quay video mới, và \n" +
                "các tính năng bảo mật hoàn toàn mới \n" +
                "\n" +
                "Pháp lý \n" +
                "(1) iPhone 11 có khả năng chống tia nước, chống nước và chống bụi. Sản phẩm đã qua kiểm nghiệm \n" +
                "trong điều kiện phòng thí nghiệm có kiểm soát đạt mức IP68 theo tiêu chuẩn IEC 60529 (chống nước \n" +
                "ở độ sâu tối đa 2 mét trong vòng tối đa 30 phút). Khả năng chống tia nước, chống nước, và chống bụi \n" +
                "không phải là các điều kiện vĩnh viễn và khả năng này có thể giảm do hao mòn thông thường. \n" +
                "Không sạc pin khi iPhone đang bị ướt. Vui lòng tham khảo hướng dẫn sử dụng để biết cách lau sạch và \n" +
                "làm khô máy. Không bảo hành sản phẩm bị hỏng do thấm chất lỏng.\n" +
                "(2) Thời lượng pin khác nhau tùy theo cách sử dụng và cấu hình. Truy cập www.apple.com/batteries \n" +
                "để biết thêm thông tin. \n" +
                "(3) Màn hình có các góc bo tròn. Khi tính theo hình chữ nhật, kích thước màn hình iPhone 11 là\n" +
                " 6.06 inch theo đường chéo. Diện tích hiển thị thực tế nhỏ hơn.\n" +
                "(4) Không bán kèm bộ sạc không dây Qi. \n" +
                "\n" +
                "Bộ sản phẩm bao gồm: \n" +
                "•        Điện thoại \n" +
                "•        Cáp sạc\n" +
                "•        Củ sạc + Tai nghe (Với sản phẩm sản xuất trước 10/2020)\n" +
                "•        HDSD Bảo hành điện tử 12 tháng.\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Bảo hành: 12 tháng kể từ ngày kích hoạt sản phẩm.\n" +
                "Kích hoạt bảo hành tại: https://checkcoverage.apple.com/vn/en/\n" +
                "\n" +
                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/4a10e843c4fc5d42876d7822a0548cf1_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.RED);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/fa1c711b31f401fda182f8d43b251fa7_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.SILVER);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1300));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/ab149f4798333a55d3d890c93a6ef86f_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLACK);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(950));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602329_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Apple Macbook Pro (2022) M2 chip, 13.3 inches, 8GB, 512GB SSD");
        //mã shop sửa lại
        productInput.setShopId(602329);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420749648");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productInput.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/6f65b108bcdcabae207d8255360e6f61_tn");
        images.add("https://cf.shopee.vn/file/7492878c607c83190a1c33f74fea076d_tn");
        images.add("https://cf.shopee.vn/file/decaf47f7cb7a2df555dbff0deeed698_tn");
        images.add("https://cf.shopee.vn/file/a5dc7787667f07f10c26456c04a51d34_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/a5e087409cca78fb6bdd281da177ff88_tn");
        //thêm mô tả
        productInput.setDescription("Apple Macbook Pro (2022) M2 chip, 13.3 inches, 8GB, 512GB SSD\n" +
                "( SPG : màu Space Gray; SLV: màu Silver)\n" +
                "Bộ xử lý\n" +
                "Hãng CPU: Apple\n" +
                "Công nghệ CPU: M2\n" +
                "\n" +
                " Loại CPU: 8 - Core\n" +
                "\n" +
                "Tốc độ CPU: 2.40 GHz\n" +
                "\n" +
                "\n" +
                "\n" +
                "RAM\n" +
                "\n" +
                "Dung lượng RAM: 8 GB\n" +
                "\n" +
                "Loại RAM: LPDDR4\n" +
                "\n" +
                "Tốc độ RAM: 3200 MHz\n" +
                "\n" +
                "Số khe cắm rời: 0\n" +
                "\n" +
                "Số khe RAM còn lại: 0\n" +
                "\n" +
                "Số RAM onboard: 1\n" +
                "\n" +
                "Hỗ trợ RAM tối đa: 8 GB\n" +
                "\n" +
                "\n" +
                "\n" +
                "Màn hình\n" +
                "\n" +
                " Kích thước màn hình: 13.3 inches\n" +
                "\n" +
                "Công nghệ màn hình: Retina\n" +
                "\n" +
                "Độ phân giải: 2560 x 1600 Pixels\n" +
                "\n" +
                "Loại màn hình: LED\n" +
                "\n" +
                "Độ sáng: 500 nits\n" +
                "\n" +
                "Màn hình cảm ứng: Không\n" +
                "\n" +
                "\n" +
                "\n" +
                "Đồ họa\n" +
                "\n" +
                "Card onboard\n" +
                "\n" +
                "Hãng: Apple\n" +
                "\n" +
                "Model GPU: 10 nhân\n" +
                "\n" +
                "\n" +
                "\n" +
                "Lưu trữ        \n" +
                "\n" +
                "Loại SSD: M2. PCIe\n" +
                "\n" +
                "Dung lượng: 512 GB\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bảo mật\n" +
                "\n" +
                "Mở khóa vân tay\n" +
                "\n" +
                "Mật khẩu\n" +
                "\n" +
                "\n" +
                "\n" +
                "Giao tiếp & kết nối\n" +
                "\n" +
                "Cổng giao tiếp        \n" +
                "\n" +
                "1 Jack 3.5 mm\n" +
                "\n" +
                "2 Type C\n" +
                "\n" +
                "\n" +
                "\n" +
                "Wifi: 802.11 ax\n" +
                "\n" +
                "Bluetooth: v5.0\n" +
                "\n" +
                "Webcam: HD Webcam (720p Webcam)\n" +
                "\n" +
                "\n" +
                "\n" +
                "Âm thanh\n" +
                "\n" +
                "Số lượng loa: 2\n" +
                "\n" +
                "Công nghệ âm thanh: Stereo speakers with high dynamic range\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bàn phím & TouchPad\n" +
                "\n" +
                "Kiểu bàn phím: English International Backlit Keyboard\n" +
                "\n" +
                "Bàn phím số: Không\n" +
                "\n" +
                "Đèn bàn phím: LED\n" +
                "\n" +
                "Công nghệ đèn bàn phím: Đơn sắc\n" +
                "\n" +
                "Màu đèn LED: Trắng\n" +
                "\n" +
                "TouchPad: Multi-touch touchpad\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thông tin pin & Sạc\n" +
                "\n" +
                "Loại PIN: Lithium polymer\n" +
                "\n" +
                "Power Supply: 67 W\n" +
                "\n" +
                "Dung lượng pin: 20 Giờ\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hệ điều hành\n" +
                "\n" +
                "OS: macOS\n" +
                "\n" +
                "Version: macOS 12\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "\n" +
                "Bảo hành: 12 tháng kể từ ngày kích hoạt sản phẩm.\n" +
                "\n" +
                "Kích hoạt bảo hành tại: https://checkcoverage.apple.com/vn/en/\n" +

                "Hướng dẫn kiểm tra địa điểm bảo hành gần nhất:\n" +
                "\n" +
                "Bước 1: Truy cập vào đường link https://getsupport.apple.com/?caller=grl&locale=en_VN \n" +
                "\n" +
                "Bước 2: Chọn sản phẩm.\n" +
                "\n" +
                "Bước 3: Điền Apple ID, nhập mật khẩu.\n" +
                "\n" +
                "Sau khi hoàn tất, hệ thống sẽ gợi ý những trung tâm bảo hành gần nhất.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/a5dc7787667f07f10c26456c04a51d34_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1500));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/7492878c607c83190a1c33f74fea076d_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.SILVER);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1300));
        productVariantList.add(variant_2);

        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //Vinfastore --------------------
    @Test
    public void testProduct_602349_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe máy điện VinFast Feliz S");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0a7642dc53b2a96509f5998a705b51ea_tn");
        images.add("https://cf.shopee.vn/file/4e1236dade3e74c798b01b64f990c2d5_tn");
        images.add("https://cf.shopee.vn/file/75cfbb02cbc1894ae434afd56db0e942_tn");
        images.add("https://cf.shopee.vn/file/843a3bd801e25b3a63f6df3d349249b1_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/953e2723509b9d4941e2a9b6daeb6597_tn");
        //thêm mô tả
        productInput.setDescription("Giá niêm yết Feliz S: 29,900,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +
                "\n" +
                "----------------------------------------------------------------------------------------\n" +
                "\n" +
                "2. Giới thiệu về công nghệ Pin thế hệ mới\n" +
                "\n" +
                "Với tầm nhìn dài hạn về việc kiến tạo tương lai di chuyển xanh, VinFast đã làm chủ được công nghệ sản xuất pin, từ việc nghiên cứu thị trường, thiết kế kiểu dáng công nghiệp,\n" +
                " phần cứng và đặc biệt là phần mềm quản lý pin BMS. Chỉ sau 9 tháng cùng hợp tác, VinFast cùng Gotion Hightech đã phát triển thành công loại cell pin LFP mới cho tuổi thọ,\n" +
                " độ ổn định và an toàn vượt trội. Theo đó tuổi thọ pin lên tới hơn 2000 lần sạc/xả vẫn còn dung lượng tới 70%. Pin LFP có khả năng chống cháy nổ trong mọi trường hợp và\n" +
                " giúp giảm tác động tới môi trường do không chứa thành phần kim loại hiếm như Co-ban, Ni-ken.\n" +
                "\n" +
                "Đặc biệt, nhờ việc tối ưu phần mềm qua 4 chức năng chính gồm Thu thập, Điều khiển, Truyền thông tin và Đảm bảo an toàn, các mẫu xe máy điện sử dụng pin LFP \n" +
                "mới của VinFast có thể đạt được quãng đường di chuyển tối đa sau 1 lần sạc đầy lên tới khoảng 200km.\n" +
                "\n" +
                "----------------------------------------------------------------------------------------\n" +
                "\n" +
                "3. Thông số kỹ thuật Feliz S\n" +
                "\n" +
                "- Công suất 1800W\n" +
                "\n" +
                "- Thời gian sạc đầy 6h\n" +
                "\n" +
                "- Động cơ Inhub/ Pin LFP\n" +
                "\n" +
                "- Giảm xóc trước: ống lồng - giảm chấn thủy lực; Giảm xóc sau: đôi - giảm chấn thủy lực\n" +
                "\n" +
                "- Max speed: 78km/h\n" +
                "\n" +
                "- Trọng lượng xe: 110kg (bao gồm pin)\n" +
                "\n" +
                "- Quãng đường 1 lần sạc: 198km ở điều kiện tiêu chuẩn\n" +
                "\n" +
                "- Phanh trước: đĩa; phanh sau: cơ\n" +
                "\n" +
                "- Chiều cao yên: 770mm\n" +
                "\n" +
                "- Chuẩn chống nước IP67\n" +
                "\n" +
                "- Đèn pha trước LED projector\n" +
                "\n" +
                "- Đèn xi nhan, đèn hậu: Full LED\n" +
                "\n" +
                "- Thể tích cốp: 25 lít\n" +
                "\n" +
                "- Tìm xe bằng chìa khóa: Có\n" +
                "\n" +
                "------------------------------------------------------------------------------------------\n" +
                "\n" +
                "4. Các gói thuê bao pin:\n" +
                "\n" +
                "Gói thuê Pin linh hoạt: Khách hàng trả phí thuê bao pin hàng tháng cho quãng đường 500km/ tháng, nếu Khách hàng sử dụng nhiều hơn 500 km/ tháng\n" +
                " thì cần trả thêm số tiền bằng quãng đường vượt nhân với đơn giá thuê bao pin/km.\n" +
                "\n" +
                "Gói thuê Pin cố định: Khách hàng trả chi phí thuê cố định mà không bị tính chi phí phụ trội khi vượt số km/tháng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Khách hàng lựa chọn thủ tục thuê pin / mua pin tại địa chỉ Showroom gần nhất khi nhận bàn giao xe.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Chi phí gói thuê Pin cố định: 350.000 VNĐ / tháng.\n" +
                "\n" +
                "Giá bán Pin: 19.900.000 VNĐ / 01 pin.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/0a7642dc53b2a96509f5998a705b51ea_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/4e1236dade3e74c798b01b64f990c2d5_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1020));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/75cfbb02cbc1894ae434afd56db0e942_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLUE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe máy điện VinFast Theon S");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a479458db484ed58d9fa8f0ffa64b8a2_tn");
        images.add("https://cf.shopee.vn/file/47606d82a6c1ddd096d1d8a326cc52fc_tn");
        images.add("https://cf.shopee.vn/file/254a7d52641844f889b7ec65cd4a70dd_tn");
        images.add("https://cf.shopee.vn/file/636d97db0e0982406e9a76f2036a7421_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/37c3fa447dd076c8503ed496bd97d37e_tn");
        //thêm mô tả
        productInput.setDescription("Giá niêm yết Theon S: 69,900,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +
                "\n" +
                "1. Giới thiệu về công nghệ Pin thế hệ mới\n" +
                "\n" +
                "Với tầm nhìn dài hạn về việc kiến tạo tương lai di chuyển xanh, VinFast đã làm chủ được công nghệ sản xuất pin, từ việc nghiên cứu thị trường, thiết kế kiểu dáng công nghiệp,\n" +
                " phần cứng và đặc biệt là phần mềm quản lý pin BMS. Chỉ sau 9 tháng cùng hợp tác, VinFast cùng Gotion Hightech đã phát triển thành công loại cell pin LFP mới cho tuổi thọ, \n" +
                "độ ổn định và an toàn vượt trội. Theo đó tuổi thọ pin lên tới hơn 2000 lần sạc/xả vẫn còn dung lượng tới 70%. Pin LFP có khả năng chống cháy nổ trong mọi trường hợp và \n" +
                "giúp giảm tác động tới môi trường do không chứa thành phần kim loại hiếm như Co-ban, Ni-ken.\n" +
                "\n" +
                "Đặc biệt, nhờ việc tối ưu phần mềm qua 4 chức năng chính gồm Thu thập, Điều khiển, Truyền thông tin và Đảm bảo an toàn, các mẫu xe máy điện sử dụng pin LFP mới của VinFast\n" +
                " có thể đạt được quãng đường di chuyển tối đa sau 1 lần sạc đầy lên tới khoảng 200km.\n" +
                "\n" +
                "----------------------------------------------------------------------------------------\n" +
                "\n" +
                "2. Thông số kỹ thuật Theon S\n" +
                "\n" +
                "- Công suất 3500W\n" +
                "\n" +
                "- Thời gian sạc đầy khoảng 6h\n" +
                "\n" +
                "- Động cơ Center motor / Pin LFP\n" +
                "\n" +
                "- Giảm xóc trước: ống lồng - giảm chấn thủy lực; Giảm xóc sau: đôi - giảm chấn thủy lực\n" +
                "\n" +
                "- Max speed: 99km/h\n" +
                "\n" +
                "- Trọng lượng xe: 145kg (bao gồm pin)\n" +
                "\n" +
                "- Quãng đường 1 lần sạc: 150km ở điều kiện tiêu chuẩn\n" +
                "\n" +
                "- Phanh đĩa ABS 2 kênh\n" +
                "\n" +
                "- Chiều cao yên: 780mm\n" +
                "\n" +
                "- Chuẩn chống nước IP67\n" +
                "\n" +
                "- Đèn pha trước LED projector\n" +
                "\n" +
                "- Đèn xi nhan, đèn hậu: Full LED\n" +
                "\n" +
                "- Thể tích cốp: 24 lít\n" +
                "\n" +
                "- Tìm xe bằng chìa khóa: Có\n" +
                "\n" +
                "------------------------------------------------------------------------------------------\n" +
                "\n" +
                "3. Các gói thuê bao pin:\n" +
                "\n" +
                "Gói thuê Pin linh hoạt: Khách hàng trả phí thuê bao pin hàng tháng cho quãng đường 500km/ tháng, nếu Khách hàng sử dụng nhiều hơn 500 km/ tháng\n" +
                " thì cần trả thêm số tiền bằng quãng đường vượt nhân với đơn giá thuê bao pin/km.\n" +
                "\n" +
                "Gói thuê Pin cố định: Khách hàng trả chi phí thuê cố định mà không bị tính chi phí phụ trội khi vượt số km/tháng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Khách hàng lựa chọn thủ tục thuê pin / mua pin tại địa chỉ Showroom gần nhất khi nhận bàn giao xe.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Chi phí gói thuê Pin cố định: 350.000 VNĐ / tháng.\n" +
                "\n" +
                "Giá bán Pin: 19.900.000 VNĐ / 01 pin.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/636d97db0e0982406e9a76f2036a7421_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/a479458db484ed58d9fa8f0ffa64b8a2_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1020));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/47606d82a6c1ddd096d1d8a326cc52fc_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe máy điện VinFast Vento S 2022");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/c70249b922f372ac563b7f8c5cc65179_tn");
        images.add("https://cf.shopee.vn/file/097aa132cd572fe6e22f8adb703f5da9_tn");
        images.add("https://cf.shopee.vn/file/75cfbb02cbc1894ae434afd56db0e942_tn");
        images.add("https://cf.shopee.vn/file/140cd1faa9925d3787c8ded7a6fabda5_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/ec75dab542a10958575c26c15f99ac4e_tn");
        //thêm mô tả
        productInput.setDescription("Giá niêm yết Vento S: 56,000,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +
                "\n" +
                "\n" +
                "\n" +
                "1. Giới thiệu về công nghệ Pin thế hệ mới\n" +
                "\n" +
                "Với tầm nhìn dài hạn về việc kiến tạo tương lai di chuyển xanh, VinFast đã làm chủ được công nghệ sản xuất pin, từ việc nghiên cứu thị trường, \n" +
                "thiết kế kiểu dáng công nghiệp, phần cứng và đặc biệt là phần mềm quản lý pin BMS. Chỉ sau 9 tháng cùng hợp tác, VinFast cùng Gotion Hightech \n" +
                "đã phát triển thành công loại cell pin LFP mới cho tuổi thọ, độ ổn định và an toàn vượt trội. Theo đó tuổi thọ pin lên tới hơn 2000 lần sạc/xả \n" +
                "vẫn còn dung lượng tới 70%. Pin LFP có khả năng chống cháy nổ trong mọi trường hợp và giúp giảm tác động tới môi trường do không chứa \n" +
                "thành phần kim loại hiếm như Co-ban, Ni-ken.\n" +
                "\n" +
                "Đặc biệt, nhờ việc tối ưu phần mềm qua 4 chức năng chính gồm Thu thập, Điều khiển, Truyền thông tin và Đảm bảo an toàn, \n" +
                "các mẫu xe máy điện sử dụng pin LFP mới của VinFast có thể đạt được quãng đường di chuyển tối đa sau 1 lần sạc đầy lên tới khoảng 200km.\n" +
                "\n" +
                "----------------------------------------------------------------------------------------\n" +
                "\n" +
                "2. Thông số kỹ thuật Vento S\n" +
                "\n" +
                "- Công suất 3000W\n" +
                "\n" +
                "- Thời gian sạc đầy 6h\n" +
                "\n" +
                "- Động cơ Mô tơ điện đặt bên, truyền động bằng bánh răng/ Pin LFP\n" +
                "\n" +
                "- Giảm xóc trước: ống lồng - lò xo; Giảm xóc sau: đôi - giảm chấn thủy lực\n" +
                "\n" +
                "- Max speed: 89km/h\n" +
                "\n" +
                "- Trọng lượng xe: 122kg (bao gồm pin)\n" +
                "\n" +
                "- Quãng đường 1 lần sạc: 160km ở điều kiện tiêu chuẩn\n" +
                "\n" +
                "- Phanh trước: phanh đĩa ABS Continental; phanh sau: đĩa\n" +
                "\n" +
                "- Chiều cao yên: 780mm\n" +
                "\n" +
                "- Chuẩn chống nước IP67\n" +
                "\n" +
                "- Đèn pha trước LED projector\n" +
                "\n" +
                "- Đèn xi nhan, đèn hậu: Full LED\n" +
                "\n" +
                "- Thể tích cốp: 25 lít\n" +
                "\n" +
                "- Tìm xe bằng chìa khóa: Có\n" +
                "\n" +
                "------------------------------------------------------------------------------------------\n" +
                "\n" +
                "3. Các gói thuê bao pin:\n" +
                "\n" +
                "Gói thuê Pin linh hoạt: Khách hàng trả phí thuê bao pin hàng tháng cho quãng đường 500km/ tháng, nếu Khách hàng sử dụng nhiều hơn 500 km/ tháng\n" +
                " thì cần trả thêm số tiền bằng quãng đường vượt nhân với đơn giá thuê bao pin/km.\n" +
                "\n" +
                "Gói thuê Pin cố định: Khách hàng trả chi phí thuê cố định mà không bị tính chi phí phụ trội khi vượt số km/tháng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Khách hàng lựa chọn thủ tục thuê pin / mua pin tại địa chỉ Showroom gần nhất khi nhận bàn giao xe.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Chi phí gói thuê Pin cố định: 350.000 VNĐ / tháng.\n" +
                "\n" +
                "Giá bán Pin: 19.900.000 VNĐ / 01 pin.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/c70249b922f372ac563b7f8c5cc65179_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/097aa132cd572fe6e22f8adb703f5da9_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1020));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/140cd1faa9925d3787c8ded7a6fabda5_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLUE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe máy điện VinFast Klara S (2022)");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/cdece7d22791617fbbb8d7b5f3407136_tn");
        images.add("https://cf.shopee.vn/file/2ac450d37a7e1e8f74d797bcb56179b1_tn");
        images.add("https://cf.shopee.vn/file/df928df3782c04806254be40c4d55f50_tn");
        images.add("https://cf.shopee.vn/file/a5e661fc72d5478445e647f8c54fe225_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/3778e7fecc28f0879ed9655d61cc34e0_tn");
        //thêm mô tả
        productInput.setDescription("Giá niêm yết Klara S (2022): 36,900,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +

                "\n" +
                "1. Giới thiệu về công nghệ Pin thế hệ mới\n" +
                "\n" +
                "Với tầm nhìn dài hạn về việc kiến tạo tương lai di chuyển xanh, VinFast đã làm chủ được công nghệ sản xuất pin, từ việc nghiên cứu thị trường, \n" +
                "thiết kế kiểu dáng công nghiệp, phần cứng và đặc biệt là phần mềm quản lý pin BMS. Chỉ sau 9 tháng cùng hợp tác, VinFast cùng Gotion Hightech \n" +
                "đã phát triển thành công loại cell pin LFP mới cho tuổi thọ, độ ổn định và an toàn vượt trội. Theo đó tuổi thọ pin lên tới hơn 2000 lần sạc/xả \n" +
                "vẫn còn dung lượng tới 70%. Pin LFP có khả năng chống cháy nổ trong mọi trường hợp và giúp giảm tác động tới môi trường do không chứa thành \n" +
                "phần kim loại hiếm như Co-ban, Ni-ken.\n" +
                "\n" +
                "Đặc biệt, nhờ việc tối ưu phần mềm qua 4 chức năng chính gồm Thu thập, Điều khiển, Truyền thông tin và Đảm bảo an toàn,\n" +
                " các mẫu xe máy điện sử dụng pin LFP mới của VinFast có thể đạt được quãng đường di chuyển tối đa sau 1 lần sạc đầy lên tới khoảng 200km.\n" +
                "\n" +
                "----------------------------------------------------------------------------------------\n" +
                "\n" +
                "2. Thông số kỹ thuật Klara S (2022)\n" +
                "\n" +
                "- Công suất 1800W\n" +
                "\n" +
                "- Thời gian sạc đầy 6h\n" +
                "\n" +
                "- Động cơ Inhub/ Pin LFP\n" +
                "\n" +
                "- Giảm xóc trước: ống lồng - giảm chấn thủy lực; Giảm xóc sau: đôi - giảm chấn thủy lực\n" +
                "\n" +
                "- Max speed: 78km/h\n" +
                "\n" +
                "- Trọng lượng xe: 112kg (bao gồm pin)\n" +
                "\n" +
                "- Quãng đường 1 lần sạc: 194km ở điều kiện tiêu chuẩn\n" +
                "\n" +
                "- Phanh trước: đĩa; phanh sau: đĩa\n" +
                "\n" +
                "- Chiều cao yên: 760mm\n" +
                "\n" +
                "- Chuẩn chống nước IP67\n" +
                "\n" +
                "- Đèn pha trước LED \n" +
                "\n" +
                "- Đèn xi nhan, đèn hậu: Full LED\n" +
                "\n" +
                "- Thể tích cốp: 23 lít\n" +
                "\n" +
                "- Tìm xe bằng chìa khóa: Có\n" +
                "\n" +
                "------------------------------------------------------------------------------------------\n" +
                "\n" +
                "3. Các gói thuê bao pin:\n" +
                "\n" +
                "Gói thuê Pin linh hoạt: Khách hàng trả phí thuê bao pin hàng tháng cho quãng đường 500km/ tháng, nếu Khách hàng sử dụng nhiều hơn 500 km/ tháng\n" +
                " thì cần trả thêm số tiền bằng quãng đường vượt nhân với đơn giá thuê bao pin/km.\n" +
                "\n" +
                "Gói thuê Pin cố định: Khách hàng trả chi phí thuê cố định mà không bị tính chi phí phụ trội khi vượt số km/tháng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Khách hàng lựa chọn thủ tục thuê pin / mua pin tại địa chỉ Showroom gần nhất khi nhận bàn giao xe.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Chi phí gói thuê Pin cố định: 350.000 VNĐ / tháng.\n" +
                "\n" +
                "Giá bán Pin: 19.900.000 VNĐ / 01 pin.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/cdece7d22791617fbbb8d7b5f3407136_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/a5e661fc72d5478445e647f8c54fe225_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1020));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/2ac450d37a7e1e8f74d797bcb56179b1_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe máy điện VinFast Ludo");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/3c8c8a8d155314743e1eab895bad2abc_tn");
        images.add("https://cf.shopee.vn/file/ef7633e1d0f1b2cc5e72d816ce098cf4_tn");
        images.add("https://cf.shopee.vn/file/e3f67fb013276465bfe16387a7755562_tn");
        images.add("https://cf.shopee.vn/file/96ade7c891c63a01f27b251a48d7a7d7_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/c71e0a97e0236c7ca7f1350ebad34f47_tn");
        //thêm mô tả
        productInput.setDescription("Giá bán xe đã bao gồm VAT, đã bao gồm sạc và chưa bao gồm pin, lệ phí thuế trước bạ, làm giấy tờ, biển số.\n" +
                "Khách hàng đến nhận xe tại hệ thống hơn 70 showroom trên toàn quốc, tra cứu địa chỉ nhận xe gần nhất tại link : \n" +
                "https://xemaydien.vinfast.vn/he-thong-phan-phoi/\n" +
                "Sản phẩm không cho phép đổi trả sau khi xuất hóa đơn.\n" +
                "Bảo hành xe 03 năm, không giới hạn số km.\n" +
                "\n" +
                "Thiết kế năng động, cá tính\n" +
                "\n" +
                "An toàn vượt trội\n" +
                "Xe được trang bị phanh đĩa trước và phanh tang trống phía sau giúp đảm bảo an toàn tuyệt đối cho người sử dụng, lốp không săm giúp xe bám đường tốt, \n" +
                "khung xe siêu bền được thử nghiệm qua hàng loạt các hạng mục kiểm tra vô cùng nghiêm ngặt giúp xe đạt độ bền rất cao và chịu được tải trọng lớn.\n" +
                " Hệ thống giảm sóc đôi phía trước, giảm sóc đơn phía sau giúp xe vận hành êm ái trên mọi điều kiện địa hình.\n" +
                "\n" +
                "Vận hành ưu việt\n" +
                "Vinfast Ludo được trang bị động cơ Bosch chống nước tiêu chuẩn IP57 cho xe chạy mượt mà. Dòng xe Ludo với tùy chọn lái xe Sport có công suất hoạt động 500W,\n" +
                " tốc độ tối đa 35 Km/h và quãng đường đi khoảng 70km mới đổi pin 1 lần.\n" +
                "\n" +
                "Xe máy điện thông minh\n" +
                "Nhờ tích hợp eSIM, xe được kết nối điện thoại thông minh qua Mobile App, giúp LUDO sở hữu tính năng thông minh như:\n" +
                "\n" +
                "Cảnh báo chống trộm: Khi xe bị dắt đi một khoảng nhất định hoặc ổ khóa điện bị mở khóa trái phép, xe sẽ rung lắc và cảnh báo bằng còi.\n" +
                "Định vị xe (GPS): Người dùng dễ dàng kiểm tra vị trí hiện tại của xem hay tìm xe trong bãi đỗ.\n" +
                "Kiểm tra lịch sử di chuyển của xe: Có thể xem lại lịch sử di chuyển của xe trong khoảng thời gian 01 tháng gần nhất.\n" +
                "Kiểm tra tình trạng xe: Thông qua App có thể kiểm tra dung lượng còn lại của Pin.\n" +
                "Xe máy điện Vinfast Ludo\n" +
                "\n" +
                "Hệ thống trạm đổi pin tiên tiến\n" +
                "Với hệ thống hàng ngàn trạm đổi pin hiện đại phủ khắp Việt Nam, khách hàng có thể dễ dàng tìm, tiếp cận trạm đổi pin gần nhất và \n" +
                "nhanh chóng đổi pin để tiếp tục hành trình.\n" +
                "\n" +
                "ĐỘNG CƠ         \n" +
                "Công suất hữu ích lớn nhất (Công suất lớn nhất )        1100 W\n" +
                "Công suất danh định        500 W\n" +
                "Loại động cơ        Động cơ điện một chiều không chổi than\n" +
                "Tốc độ tối đa        33,5±5% Km/h\n" +
                "HỆ THỐNG ẮC QUY         \n" +
                "Loại Ắc-Quy        Ắc quy li-ion\n" +
                "Dung lượng        22 Ampe/Giờ\n" +
                "Trọng lượng trung bình        7.8 Kg\n" +
                "HỆ THỐNG KHUNG/GIẢM XÓC/PHANH         \n" +
                "Giảm Xóc trước        Giảm chấn lò xo dầu\n" +
                "Giảm Xóc sau        Giảm xóc đôi (Kép)\n" +
                "Phanh trước        Phanh Đĩa, dẫn động thủy lực, điều khiển bằng tay\n" +
                "Phanh sau        Phanh tang chống, dẫn động cơ khí, điều khiển bằng tay\n" +
                "KÍCH THƯỚC CƠ BẢN         \n" +
                "Khoảng cách trục bánh Trước-sau        1157 mm\n" +
                "Dài x Rộng x Cao (mm)        1700 x 715 x 1070\n" +
                "Khoảng sáng gầm        147 mm\n" +
                "Chiều cao Yên        750");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/3c8c8a8d155314743e1eab895bad2abc_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.WHITE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/e3f67fb013276465bfe16387a7755562_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1020));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/96ade7c891c63a01f27b251a48d7a7d7_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.GRAY);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("VinFast - Xe máy điện Impes Red");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/13f47dcf9115a7d7666b7ea6b1f35650_tn");
        images.add("https://cf.shopee.vn/file/799f2bac92b3b08cc405d06684dd8bca_tn");
        images.add("https://cf.shopee.vn/file/4d12c80fbeaf66d918368b01a5017c8b_tn");
        images.add("https://cf.shopee.vn/file/681fcbc7ffc943236add3fb6dc0865d4_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/6a1357a8f75e74349acacd4418eaefaa_tn");
        //thêm mô tả
        productInput.setDescription("Giá bán đã bao gồm VAT, bao gồm sạc và không bao gồm pin, phí đăng ký, đăng kiểm, biển số, lệ phí trước bạ.\n" +
                "Khách hàng đến nhận xe tại hệ thống hơn 70 showroom trên toàn quốc, tra cứu địa chỉ nhận xe gần nhất tại link\n" +
                " : https://xemaydien.vinfast.vn/he-thong-phan-phoi/\n" +
                "Sản phẩm không cho phép đổi trả sau khi xuất hóa đơn.\n" +
                "Bảo hành xe 03 năm, không giới hạn số km.\n" +
                "\n" +
                "Thông số kỹ thuật\n" +
                "ĐỘNG CƠ         \n" +
                "Công suất hữu ích lớn nhất (Công suất lớn nhất )        1700 W\n" +
                "Công suất danh định        1200 W\n" +
                "Loại động cơ        Động cơ điện một chiều không chổi than\n" +
                "Tốc độ tối đa        49 Km/h\n" +
                "HỆ THỐNG ẮC QUY         \n" +
                "Loại Ắc-Quy        Ắc quy li-ion\n" +
                "Dung lượng        22 Ampe/Giờ\n" +
                "Trọng lượng trung bình        7.8 Kg\n" +
                "HỆ THỐNG KHUNG/GIẢM XÓC/PHANH         \n" +
                "Giảm Xóc trước        Giảm chấn lò xo dầu\n" +
                "Giảm Xóc sau        Giảm xóc đơn\n" +
                "Phanh trước        Phanh Đĩa, dẫn động thủy lực, điều khiển bằng tay\n" +
                "Phanh sau        Phanh tang chống, dẫn động cơ khí, điều khiển bằng tay\n" +
                "KÍCH THƯỚC CƠ BẢN         \n" +
                "Khoảng cách trục bánh Trước-sau        1300 mm\n" +
                "Dài x Rộng x Cao (mm)        1800 x 710 x 1070\n" +
                "Khoảng sáng gầm        155 mm\n" +
                "Chiều cao Yên        733 mm\n" +
                "Kích thước lốp trước | sau        90/90 - 12 44J | 90/90 - 12 44J\n" +
                "TRỌNG LƯỢNG         \n" +
                "Xe và Ắc-Quy Li-On        75kg\n" +
                "HỆ THỐNG CHIẾU SÁNG         \n" +
                "Đèn pha trước        LED\n" +
                "Đèn xi nhan, Đèn Hậu        LED");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/13f47dcf9115a7d7666b7ea6b1f35650_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.RED);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);

        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Motor Điện VinFast EVO 200");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0a7642dc53b2a96509f5998a705b51ea_tn");
        images.add("https://cf.shopee.vn/file/4e1236dade3e74c798b01b64f990c2d5_tn");
        images.add("https://cf.shopee.vn/file/75cfbb02cbc1894ae434afd56db0e942_tn");
        images.add("https://cf.shopee.vn/file/843a3bd801e25b3a63f6df3d349249b1_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/953e2723509b9d4941e2a9b6daeb6597_tn");
        //thêm mô tả
        productInput.setDescription("Giá niêm yết EVO 200: 22,000,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +
                "\n" +
                "\n" +
                "\n" +
                "1. Chính sách bán hàng:\n" +
                "\n" +
                "\n" +
                "\n" +
                "Khách hàng đặt cọc/mua xe trong khoảng thời gian từ 21/9/2022 - 21/10/2022 hoặc 20.000 đơn hàng đầu tiên (tùy điều kiện nào đến trước) \n" +
                "sẽ nhận được ưu đãi gói thuê bao pin miễn phí 12 tháng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thời gian giao xe dự kiến: Bắt đầu trả xe từ tháng 10. \n" +
                "\n" +
                "\n" +
                "\n" +
                "----------------------------------------------------------------------------------------\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "2. Giới thiệu về công nghệ Pin thế hệ mới\n" +
                "\n" +
                "\n" +
                "Với tầm nhìn dài hạn về việc kiến tạo tương lai di chuyển xanh, VinFast đã làm chủ được công nghệ sản xuất pin, \n" +
                "từ việc nghiên cứu thị trường, thiết kế kiểu dáng công nghiệp, phần cứng và đặc biệt là phần mềm quản lý pin BMS. \n" +
                "Chỉ sau 9 tháng cùng hợp tác, VinFast cùng Gotion Hightech đã phát triển thành công loại cell pin LFP mới cho tuổi thọ, \n" +
                "độ ổn định và an toàn vượt trội. Theo đó tuổi thọ pin lên tới hơn 2000 lần sạc/xả vẫn còn dung lượng tới 70%. \n" +
                "Pin LFP có khả năng chống cháy nổ trong mọi trường hợp và giúp giảm tác động tới môi trường do không chứa thành phần \n" +
                "kim loại hiếm như Co-ban, Ni-ken.\n" +
                "\n" +
                "Đặc biệt, nhờ việc tối ưu phần mềm qua 4 chức năng chính gồm Thu thập, Điều khiển, Truyền thông tin và Đảm bảo an toàn,\n" +
                " các mẫu xe máy điện sử dụng pin LFP mới của VinFast có thể đạt được quãng đường di chuyển tối đa sau 1 lần sạc đầy lên tới khoảng 200km.\n" +
                "\n" +
                "\n" +
                "\n" +
                "----------------------------------------------------------------------------------------\n" +
                "\n" +
                "\n" +
                "\n" +
                "3. Thông số kỹ thuật EVO 200\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Công suất tối đa 2500W\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Thời gian sạc đầy:\n" +
                "\n" +
                "\n" +
                "   + Sạc tiêu chuẩn: 10h (0%-100%)\n" +
                "\n" +
                "\n" +
                "   + Sạc nhanh: 4h (0%-100%)\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Động cơ Inhub/ Pin LFP\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Giảm xóc trước: ống lồng - giảm chấn thủy lực; Giảm xóc sau: đôi - giảm chấn thủy lực\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Max speed: 70km/h\n" +
                "\n" +
                "\n" +
                "- Trọng lượng xe: 97kg (bao gồm pin)\n" +
                "\n" +
                "\n" +
                "- Quãng đường 1 lần sạc: 203km ở điều kiện tiêu chuẩn\n" +
                "\n" +
                "\n" +
                "- Phanh trước: đĩa; phanh sau: cơ\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Chiều cao yên: 750mm\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Chuẩn chống nước IP67\n" +
                "\n" +
                "\n" +
                "- Đèn pha trước LED projector\n" +
                "\n" +
                "\n" +
                "- Đèn xi nhan, đèn hậu: LED\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Thể tích cốp: 22 lít\n" +
                "\n" +
                "\n" +
                "- Tìm xe bằng chìa khóa: Không (có thể lắp thêm)\n" +
                "\n" +
                "\n" +
                "------------------------------------------------------------------------------------------\n" +
                "\n" +
                "\n" +
                "\n" +
                "4. Các gói thuê bao pin:\n" +
                "\n" +
                "\n" +
                "Gói thuê Pin linh hoạt: Khách hàng trả phí thuê bao pin hàng tháng cho quãng đường 500km/ tháng, nếu Khách hàng sử dụng \n" +
                "nhiều hơn 500 km/ tháng thì cần trả thêm số tiền bằng quãng đường vượt nhân với đơn giá thuê bao pin/km.\n" +
                "\n" +
                "Gói thuê Pin cố định: Khách hàng trả chi phí thuê cố định mà không bị tính chi phí phụ trội khi vượt số km/tháng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Khách hàng lựa chọn thủ tục thuê pin / mua pin tại địa chỉ Showroom gần nhất khi nhận bàn giao xe.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Chi phí gói thuê Pin linh hoạt: 189.000 VNĐ / tháng (Chi phí cho mỗi KM phụ trội: 374 VNĐ / km).\n" +
                "\n" +
                "Chi phí gói thuê Pin cố định: 350.000 VNĐ / tháng.\n" +
                "\n" +
                "Giá bán Pin: 19.900.000 VNĐ / 01 pin.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Từ 1/10/2022, VinFast áp dụng tính phí chuyển đổi gói cước thuê bao pin cho tất cả các dòng xe máy điện với mức phí 600.000 đ/ 1 lần\n" +
                " chuyển đổi.");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/0a7642dc53b2a96509f5998a705b51ea_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/4e1236dade3e74c798b01b64f990c2d5_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1020));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/75cfbb02cbc1894ae434afd56db0e942_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLUE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe motor điện VinFast Theon");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/bbcb49babefa093103ee24ef0cc9a63b_tn");
        images.add("https://cf.shopee.vn/file/8590194cd9414ce66134a5b14d26ec80_tn");
        images.add("https://cf.shopee.vn/file/11d547c80f64493da0c4fd1c80c00cdb_tn");
        images.add("https://cf.shopee.vn/file/ee486b342fcbb8c0caad2c2b46df1438_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/d43ee70b8d602964f17c9724b2b36e38_tn");
        //thêm mô tả
        productInput.setDescription("\uD83D\uDD31 Thông tin chung:\n" +
                "\n" +
                "\uD83D\uDD38 Giá bán xe đã bao gồm VAT và 01 bộ sạc kèm xe, chưa bao gồm pin, lệ phí thuế trước bạ, làm giấy tờ, biển số.\n" +
                "\uD83D\uDD38 Khách hàng nhận xe và làm thủ tục giấy tờ tại hệ thống showroom VinFast trên toàn quốc. Thời hạn nhận xe: \n" +
                "trong vòng tối đa 12 ngày tính từ thời điểm thanh toán thành công (không bao gồm T7, CN, Lễ Tết).\n" +
                "\uD83D\uDD38 Sản phẩm không cho phép đổi trả sau khi xuất hóa đơn.\n" +
                "\uD83D\uDD38 Bảo hành xe 03 năm, không giới hạn số km.\n" +
                "\n" +
                "\uD83D\uDD31 Thông số kỹ thuật:\n" +
                ".\n" +
                "\uD83C\uDFC6 Thiết kế hài hòa theo tiêu chuẩn của Kriska - Ý\n" +
                "\uD83C\uDFC6 Tiêu chuẩn chống nước IP67 vượt trội ( có thể lội nước 0,5m trong thời gian 30 phút)\n" +
                "\uD83C\uDFC6 Tốc độ tối đa 90km/h\n" +
                "\uD83C\uDFC6 Công suất 3500W - Pin lithium thương hiệu Samsung\n" +
                "\uD83C\uDFC6 Động cơ điện đặt giữa\n" +
                "\uD83C\uDFC6 Quãng đường đi được 1 lần sạc: 101 km\n" +
                "\uD83C\uDFC6 Thời gian sạc: 5,5 - 06 tiếng \n" +
                "\uD83C\uDFC6 Dung lượng pin: 24,8 Ah, không swap pin\n" +
                "\uD83C\uDFC6 Phanh xe: Phanh thương hiệu Nissin, trang bị ABS 2 kênh Continental \n" +
                "\uD83C\uDFC6 Khoảng sáng gầm xe: 160 mm, vượt ổ gà lội ngập nước dễ dàng\n" +
                "\uD83C\uDFC6 Chiều cao yên: 780 mm\n" +
                "\uD83C\uDFC6 Giảm sóc đôi thương hiệu Showa, giảm chấn lò xo dầu, giảm chấn thủy lực\n" +
                "\uD83C\uDFC6 Đèn pha LED projector thiết kế thời trang hiện đại công nghệ, đèn xi nhan/đèn hậu: LED\n" +
                "\uD83C\uDFC6 Hiển thị thông tin xe liên tục, theo dõi tình trạng xe. Có kế hoạch thay thế bảo dưỡng, phụ tùng phù hợp và kịp thời\n" +
                "\uD83C\uDFC6 Chức năng tìm xe trong bãi đỗ bằng ứng dụng (App)\n" +
                "\uD83C\uDFC6 Bật/tắt tính năng chống trộm, khóa/ mở khóa xe từ xa qua app  \n" +
                "\uD83C\uDFC6 Thể tích cốp: 17 Lít\n" +
                "\uD83C\uDFC6 Khóa xe: Smart key. Khóa cổ xe từ xa, tìm xe trong bãi đỗ\n" +
                "\uD83C\uDFC6 Chức năng định vị, tìm xe dễ dàng khi bị mất\n" +
                "\uD83C\uDFC6 Thống kê lịch sử hành trình, quãng đường, thiết lập vùng an toàn\n" +
                "\uD83C\uDFC6 Sạc pin với trạm sạc VinFast: Có\n" +
                "\uD83C\uDFC6 Chức năng tự động chuẩn đoán và cảnh báo lỗi \n" +
                "\uD83C\uDFC6 Chức năng mở khóa bằng tay 1 chạm \n" +
                "\uD83C\uDFC6 Cập nhật phần mềm từ xa");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/8590194cd9414ce66134a5b14d26ec80_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/bbcb49babefa093103ee24ef0cc9a63b_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1500));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/ee486b342fcbb8c0caad2c2b46df1438_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1100));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe máy điện VinFast Klara A2 - 2021");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/211d03dbff65d40fd94d57d54ba32a57_tn");
        images.add("https://cf.shopee.vn/file/7ec2baeef5092b2805e45b64ab75d35a_tn");
        images.add("https://cf.shopee.vn/file/f7edf6e9c2c736a779f1ac6ae277a899_tn");
        images.add("https://cf.shopee.vn/file/bb3f3a5b33da4e33e27b835d76c95362_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/8f80fc07aa046ab6ca62c29dcff620d3_tn");
        //thêm mô tả
        productInput.setDescription("\n" +
                "MÔ TẢ SẢN PHẨM\n" +
                "THÔNG SỐ KỸ THUẬT\n" +
                "\n" +
                "Chiều cao yên: 757 mm\n" +
                "\n" +
                "Khoảng sáng gầm: 125 mm \n" +
                "\n" +
                "Dài x rộng x cao: 1890 mm x 684 mm x 1125 mm\n" +
                "\n" +
                "Đèn pha trước: Halogen \n" +
                "\n" +
                "Đèn xi nhan, đèn hậu: LED\n" +
                "\n" +
                "Tải trọng xe và ắc quy: 127 kg (gồm ắc quy Leoch)\n" +
                "\n" +
                "Giảm xóc trước và sau: Giảm chấn lò xo dầu, giảm xóc đôi, giảm chấn thủy lực \n" +
                "\n" +
                "Phanh trước và sau: Phanh đĩa / Cơ \n" +
                "\n" +
                "Động cơ 1 chiều không chổi than tích hợp bánh sau (BLDC) \n" +
                "\n" +
                "Kích thước lốp: Trước 90/90  - 14 46P.  Sau 120/70 12 58P. Nhà sản xuất lốp: IRC \n" +
                "\n" +
                "Vành bánh xe: Trước MT2. 15 x 14 , vành hợp kim nhôm\n" +
                "\n" +
                "Sau: MT3.5 x 12, vành thép \n" +
                "\n" +
                "Động cơ VinFast, một chiều không chổi than \n" +
                "\n" +
                "Động cơ tích hợp trong vành sau \n" +
                "\n" +
                "Bộ truyền động trực tiếp \n" +
                "\n" +
                "Ắc quy chì axit kín khí, 6 bình nối tiếp \n" +
                "\n" +
                "Dung lượng ắc quy: 20 - 20 Ah \n" +
                "\n" +
                "Trọng lượng ắc quy: 40.5 kg  (6.75 kg/ bình ắc quy) \n" +
                "\n" +
                "Loại sạc: Sạc di động 72V \n" +
                "\n" +
                "Thời gian sạc: 8h được 80%, 9h được 95%, 11h được 100% \n" +
                "\n" +
                "Công suất danh định: 1200 W \n" +
                "\n" +
                "Mô men xoắn lớn nhất 98 Nm@ 100 rpm\n" +
                "\n" +
                "Gia tốc tăng tốc 0 - 50 km / h (1 người 65kg)  trong vòng 14 giây \n" +
                "\n" +
                "Tốc độ tối đa: 60km / 1h\n" +
                "\n" +
                "Quãng đường đi được tối đa: 90 km trên 1 lần sạc.\n" +
                "\n" +
                "Chế độ vận hành: Eco và Sport\n" +
                "\n" +
                "Chế độ parking: Có \n" +
                "\n" +
                "Khả năng leo dốc 20% khi chở người 65 kg đạt hiệu suất 9km/ h\n" +
                "\n" +
                "Dung tích cốp 20 lít, có thể chứa 2 mũ bảo hiểm nửa đầu.\n" +
                "\n" +
                "Chìa khóa RF, bao gồm: Nút khóa, nút mở xe, nút tìm xe\n" +
                "\n" +
                "Chứng nhận an toàn ISO 9001:2015\n" +
                "\n" +
                "Đạt tiêu chuẩn chống nước cho động cơ IP65 \n" +
                "\n" +
                "Đạt tiêu chuẩn chống nước cho ắc quy IP67  \n" +
                "\n" +
                "Chế độ bảo hành ắc quy: 1 năm, bảo hành xe: 3 năm\n" +
                "\n" +
                "Lưu ý vận hành:\n" +
                "\n" +
                "1. Nên sạc ắc quy đều đặn, không nên để dưới 10% gây kiệt ắc quy.\n" +
                "\n" +
                "2. Khi đi về, nên đợi 30 phút để ắc quy nguội rồi sạc\n" +
                "\n" +
                "3. Không nên chở 3 người, chở nặng quá gây quá tải ắc quy\n" +
                "\n" +
                "4. Khi không sử dụng trong thời gian dài, nên sạc ~50% và ngắt nguồn điện, để chỗ khô mát\n" +
                "\n" +
                "Lưu ý về chính sách bán hàng qua sàn TMĐT:\n" +
                "\n" +
                "Giá bán xe đã bao gồm VAT, 01 bộ sạc theo xe, chưa bao gồm lệ phí thuế trước bạ, làm giấy tờ, biển số. Xe không sử dụng Pin lithium, \n" +
                "thay vào đó xe sử dụng Ắc quy chì. \n" +
                "Sản phẩm không cho phép đổi trả sau khi xuất hóa đơn. \n" +
                "Khách hàng nhận xe và hoàn tất thủ tục giấy tờ tại hệ thống các showroom VinFast trên toàn quốc.\n" +
                "Đối với đơn hàng thanh toán trả sau (COD), sau 07 ngày kể từ ngày Đặt hàng thành công, khách hàng không đến showroom VinFast \n" +
                "nhận xe và thanh toán - Đơn hàng sẽ tự động Hủy.\n" +
                "Trường hợp Khách hàng muốn nhận xe tại nhà, cần đảm bảo thanh toán đủ chi phí liên quan và chấp nhận rủi ro trầy xước trong quá trình vận chuyển.\n" +
                "\n" +
                "Để bật chế độ hỗ trợ đọc màn hình, nhấn Ctrl+Alt+Z Để tìm hiểu thêm về các phím tắt, nhấn Ctrl+dấu gạch chéo\n" +
                " \n" +
                " \n" +
                " \t\t\n" +
                "MÔ TẢ SẢN PHẨM\n" +
                "THÔNG SỐ KỸ THUẬT\n" +
                "\n" +
                "Chiều cao yên: 757 mm\n" +
                "\n" +
                "Khoảng sáng gầm: 125 mm \n" +
                "\n" +
                "Dài x rộng x cao: 1890 mm x 684 mm x 1125 mm\n" +
                "\n" +
                "Đèn pha trước: Halogen \n" +
                "\n" +
                "Đèn xi nhan, đèn hậu: LED\n" +
                "\n" +
                "Tải trọng xe và ắc quy: 127 kg (gồm ắc quy Leoch)\n" +
                "\n" +
                "Giảm xóc trước và sau: Giảm chấn lò xo dầu, giảm xóc đôi, giảm chấn thủy lực \n" +
                "\n" +
                "Phanh trước và sau: Phanh đĩa / Cơ \n" +
                "\n" +
                "Động cơ 1 chiều không chổi than tích hợp bánh sau (BLDC) \n" +
                "\n" +
                "Kích thước lốp: Trước 90/90  - 14 46P.  Sau 120/70 12 58P. Nhà sản xuất lốp: IRC \n" +
                "\n" +
                "Vành bánh xe: Trước MT2. 15 x 14 , vành hợp kim nhôm\n" +
                "\n" +
                "Sau: MT3.5 x 12, vành thép \n" +
                "\n" +
                "Động cơ VinFast, một chiều không chổi than \n" +
                "\n" +
                "Động cơ tích hợp trong vành sau \n" +
                "\n" +
                "Bộ truyền động trực tiếp \n" +
                "\n" +
                "Ắc quy chì axit kín khí, 6 bình nối tiếp \n" +
                "\n" +
                "Dung lượng ắc quy: 20 - 20 Ah \n" +
                "\n" +
                "Trọng lượng ắc quy: 40.5 kg  (6.75 kg/ bình ắc quy) \n" +
                "\n" +
                "Loại sạc: Sạc di động 72V \n" +
                "\n" +
                "Thời gian sạc: 8h được 80%, 9h được 95%, 11h được 100% \n" +
                "\n" +
                "Công suất danh định: 1200 W \n" +
                "\n" +
                "Mô men xoắn lớn nhất 98 Nm@ 100 rpm\n" +
                "\n" +
                "Gia tốc tăng tốc 0 - 50 km / h (1 người 65kg)  trong vòng 14 giây \n" +
                "\n" +
                "Tốc độ tối đa: 60km / 1h\n" +
                "\n" +
                "Quãng đường đi được tối đa: 90 km trên 1 lần sạc.\n" +
                "\n" +
                "Chế độ vận hành: Eco và Sport\n" +
                "\n" +
                "Chế độ parking: Có \n" +
                "\n" +
                "Khả năng leo dốc 20% khi chở người 65 kg đạt hiệu suất 9km/ h\n" +
                "\n" +
                "Dung tích cốp 20 lít, có thể chứa 2 mũ bảo hiểm nửa đầu.\n" +
                "\n" +
                "Chìa khóa RF, bao gồm: Nút khóa, nút mở xe, nút tìm xe\n" +
                "\n" +
                "Chứng nhận an toàn ISO 9001:2015\n" +
                "\n" +
                "Đạt tiêu chuẩn chống nước cho động cơ IP65 \n" +
                "\n" +
                "Đạt tiêu chuẩn chống nước cho ắc quy IP67  \n" +
                "\n" +
                "Chế độ bảo hành ắc quy: 1 năm, bảo hành xe: 3 năm\n" +
                "\n" +
                "Lưu ý vận hành:\n" +
                "\n" +
                "1. Nên sạc ắc quy đều đặn, không nên để dưới 10% gây kiệt ắc quy.\n" +
                "\n" +
                "2. Khi đi về, nên đợi 30 phút để ắc quy nguội rồi sạc\n" +
                "\n" +
                "3. Không nên chở 3 người, chở nặng quá gây quá tải ắc quy\n" +
                "\n" +
                "4. Khi không sử dụng trong thời gian dài, nên sạc ~50% và ngắt nguồn điện, để chỗ khô mát\n" +
                "\n" +
                "Lưu ý về chính sách bán hàng qua sàn TMĐT:\n" +
                "\n" +
                "Giá bán xe đã bao gồm VAT, 01 bộ sạc theo xe, chưa bao gồm lệ phí thuế trước bạ, làm giấy tờ, biển số. Xe không sử dụng Pin lithium, \n" +
                "thay vào đó xe sử dụng Ắc quy chì. \n" +
                "Sản phẩm không cho phép đổi trả sau khi xuất hóa đơn. \n" +
                "Khách hàng nhận xe và hoàn tất thủ tục giấy tờ tại hệ thống các showroom VinFast trên toàn quốc.\n" +
                "Đối với đơn hàng thanh toán trả sau (COD), sau 07 ngày kể từ ngày Đặt hàng thành công, khách hàng không đến showroom VinFast \n" +
                "nhận xe và thanh toán - Đơn hàng sẽ tự động Hủy.\n" +
                "Trường hợp Khách hàng muốn nhận xe tại nhà, cần đảm bảo thanh toán đủ chi phí liên quan và chấp nhận rủi ro trầy xước trong quá trình vận chuyển.\n" +
                "\n");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/211d03dbff65d40fd94d57d54ba32a57_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/7ec2baeef5092b2805e45b64ab75d35a_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.RED);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/bb3f3a5b33da4e33e27b835d76c95362_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.BLUE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602349_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe máy điện VinFast Ludo Mint To Be - Phiên bản giới hạn");
        //mã shop sửa lại
        productInput.setShopId(602349);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420765418");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/ee82a7088fbb45c8147feb6dbca080f6_tn");
        images.add("https://cf.shopee.vn/file/3dd12a5700320cd2770645d20477595b_tn");
        images.add("https://cf.shopee.vn/file/e06118487edeb7a0900bd7b4603bd55e_tn");
        images.add("https://cf.shopee.vn/file/075c3b4725104a87fad2d50e4fb4ecda_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/15b7bfa4253d613966fabffb7bc0c5e3_tn");
        //thêm mô tả
        productInput.setDescription("➡️Giá bán xe đã bao gồm VAT, đã bao gồm sạc & pin & Không bao gồm lệ phí thuế trước bạ, làm giấy tờ, biển số.\n" +
                "\n" +
                "➡️ Thông tin chung:\n" +
                "\uD83D\uDD38 Giá bán xe đã bao gồm VAT, đã bao gồm sạc & pin,  chưa bao gồm lệ phí thuế trước bạ, làm giấy tờ, biển số.\n" +
                "\uD83D\uDD38 Khách hàng nhận xe và làm thủ tục giấy tờ tại toàn bộ các showroom VinFast trên toàn quốc.\n" +
                "\uD83D\uDD38 Sản phẩm không cho phép đổi trả sau khi xuất hóa đơn.\n" +
                "\uD83D\uDD38 Bảo hành xe 03 năm, không giới hạn số km.\n" +
                "\n" +
                "➡️ - Bảo hành 03 năm.\n" +
                "      - Phí cứu hộ pin: 50.000/lần/pin\n" +
                "\n" +
                "➡️ VinFast Ludo Mint To Be - Phiên bản giới hạn\n" +
                "“Mint to be” là cách chơi chữ từ “Meant to be”, nghĩa là luôn tạo ra điều gì đó có ý nghĩa tại mọi thời điểm. Với màu xanh tươi sáng và tràn đầy hy vọng, \n" +
                "Ludo Mint to be ra đời để song hành cùng bạn trong những năm tháng tràn đầy ý nghĩa của thời học sinh, để mỗi ngày của bạn luôn là ngày vui nhất.\n" +
                "\n" +
                "➡️Thông số kỹ thuật\n" +
                "ĐỘNG CƠ         \n" +
                "Công suất hữu ích lớn nhất (Công suất lớn nhất ): 1700 W\n" +
                "Công suất danh định: 1200 W\n" +
                "Loại động cơ: Động cơ điện một chiều không chổi than\n" +
                "Tốc độ tối đa: 33.5 +-5% Km/h\n" +
                "HỆ THỐNG ẮC QUY         \n" +
                "Loại Ắc-Quy: Ắc quy li-ion\n" +
                "Dung lượng: 22 Ampe/Giờ\n" +
                "Trọng lượng trung bình: 7.8 Kg\n" +
                "HỆ THỐNG KHUNG/GIẢM XÓC/PHANH         \n" +
                "Giảm Xóc trước: Giảm chấn lò xo dầu\n" +
                "Giảm Xóc sau: Giảm xóc đơn\n" +
                "Phanh trước: Phanh Đĩa, dẫn động thủy lực, điều khiển bằng tay\n" +
                "Phanh sau: Phanh tang chống, dẫn động cơ khí, điều khiển bằng tay\n" +
                "KÍCH THƯỚC CƠ BẢN         \n" +
                "Khoảng cách trục bánh Trước-sau: 1300 mm\n" +
                "Dài x Rộng x Cao (mm): 1800 x 710 x 1070\n" +
                "Khoảng sáng gầm: 155 mm\n" +
                "Chiều cao Yên: 733 mm\n" +
                "Kích thước lốp trước | sau: 90/90 - 12 44J | 90/90 - 12 44J\n" +
                "TRỌNG LƯỢNG         \n" +
                "Xe và Ắc-Quy Li-On: 75kg\n" +
                "HỆ THỐNG CHIẾU SÁNG         \n" +
                "Đèn pha trước: LED\n" +
                "Đèn xi nhan, Đèn Hậu: LED");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/ee82a7088fbb45c8147feb6dbca080f6_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.GREEN);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //Yamaha
    @Test
    public void testProduct_602350_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Máy Yamaha NVX 155 ABS VVA");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/97269174e6606f9219cbed9155ce5b5d_tn");
        images.add("https://cf.shopee.vn/file/921543f18e57726d32bdf821bd32c600_tn");
        images.add("https://cf.shopee.vn/file/937fb199a71873b79d982a0e417cd8be_tn");
        images.add("https://cf.shopee.vn/file/9b96c2d057ef61e4218d342b99be548f_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/aa0643920f4780e3e916bdcb7850ccf6_tn");
        //thêm mô tả
        productInput.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
                "Bố trí xi lanh        Xy lanh đơn\n" +
                "Dung tích xy lanh (CC)        155,1 cc\n" +
                "Đường kính và hành trình piston        58 x 58,7mm\n" +
                "Tỷ số nén        11,6:1\n" +
                "Công suất tối đa        11,3kW (15,4 PS)/8.000 vòng/phút\n" +
                "Mô men cực đại        13,9 N.m (1,4kgf.m)/6.500 vòng/phút\n" +
                "Hệ thống khởi động        Điện\n" +
                "Dung tích bình xăng        5,5 lít\n" +
                "Mức tiêu thụ nhiên liệu (l/100km)        2,19\n" +
                "Tỷ số truyền động        2,300-0,724:1\n" +
                "Kiểu hệ thống truyền lực        CVT\n" +
                "Khung xe\n" +
                "Kích thước\n" +
                "Bảo hành\n" +
                "Hệ thống điện\n" +
                "Lưu ý:thanh toán bằng thẻ tín dụng hay tất cả hình thức khác trên shopee \n" +
                "đều có phí 3% thanh toán khi nhận xe");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/97269174e6606f9219cbed9155ce5b5d_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.ORANGE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);

        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/921543f18e57726d32bdf821bd32c600_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.GRAY);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/937fb199a71873b79d982a0e417cd8be_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(910));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Máy Yamaha Latte Phiên Bản Chuẩn");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-55p67jeo9mjv00_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-s4sl9e1bq3kva9_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-lhelif1bq3kv3b_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/aa0643920f4780e3e916bdcb7850ccf6_tn");
        //thêm mô tả
        productInput.setDescription("Yamaha Grande là mẫu xe tay ga tiết kiệm xăng số 1 Việt Nam với mức tiêu thụ chỉ 1,69 lít/100km, \n" +
                "theo số liệu thống kê và phân tích được Cục Đăng kiểm Việt Nam công bố, xác nhận bởi Báo Giao thông. \n" +
                "Bên cạnh thiết kế thời trang, thanh lịch, ở phiên bản Grande Bluecore Hybrid mới còn sở hữu cốp xe siêu rộng 27 lít và\n" +
                " được trang bị hàng loạt tính năng hiện đại như trợ lực Hybrid, Smart key, hệ thống Stop & Start System, phanh ABS,.. \n" +
                "với mức giá rất hấp dẫn.\n" +
                "\n" +
                "QUY TRÌNH ĐẶT XE :\n" +
                "B1: Chọn xe/ Màu xe cho vào giỏ hàng\n" +
                "B2 : Chat cùng cửa hàng để biết thêm thông tin về sản phẩm và các chi phí phát sinh kèm theo.\n" +
                "B3: Khách đồng ý -> đặt xe -> cửa hàng nhận đơn->giao xe cho khách. \n" +
                "Hotline Zalo : 0904505045\n" +
                "Giá bán xe đã bao gồm thuế VAT, chưa bao gồm thuế trước bạ, biển số, và các chi phí khác\n" +
                "Cửa hàng có hỗ trợ về thủ tục đăng ký, khách hàng mua xe có thể tự đi đăng ký.\n" +
                "Khách đến nhận xe tại cửa hàng.\n" +
                "Thời hạn bảo hành: 3 năm\n" +
                "*Sau khi đặt hàng thành công, cửa hàng sẽ liên hệ và thông báo thời gian nhận xe trong vòng 24h.\n" +
                "Đại lý Yamaha Xe Máy 74 \n" +
                "Địa chỉ: 74 Khâm Thiên, quận Đống Đa, Hà Nội\n" +
                "\uD83D\uDCDE Tư vấn bán hàng: 02438510610 \n" +
                "\uD83D\uDCDE Di động: 0904505045");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22110-55p67jeo9mjv00_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.RED);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);

        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-s4sl9e1bq3kva9_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.GRAY);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-lhelif1bq3kv3b_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Máy YAMAHA Janus ");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-diounf5ji8ivcf_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-yx2ubh5ji8ivc2_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-n8vynf5ji8iv2f_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-0uzojrmzonivd8_tn");
        //thêm mô tả
        productInput.setDescription("Giá bán xe đã bao gồm thuế VAT, chưa bao gồm thuế trước bạ, biển số, \n" +
                "chi phí thanh toán bằng thẻ tín dụng của Shopee.\n" +
                "YAMAHA 74 KHÂM THIÊN    có hỗ trợ về thủ tục đăng ký, khách hàng mua xe\n" +
                " có thể TỰ ĐI ĐĂNG KÍ \n" +
                "Khách hàng nhận xe tại cửa hàng.\n" +
                "Thời hạn bảo hành: 3 năm\n" +
                "\n" +
                "\n" +
                "QUY TRÌNH ĐẶT XE :\n" +
                "B1: Chọn xe/ Màu xe cho vào giỏ hàng\n" +
                "B2 : Chat cùng cửa hàng để biết thêm thông tin về sản phẩm và các chi phí phát sinh kèm theo.\n" +
                "B3: Khách đồng ý -> đặt xe -> cửa hàng nhận đơn->giao xe cho khách. \n" +
                "Hotline Zalo : 0904505045\n" +
                "Giá bán xe đã bao gồm thuế VAT, chưa bao gồm thuế trước bạ, biển số, và các chi phí khác\n" +
                "Cửa hàng có hỗ trợ về thủ tục đăng ký, khách hàng mua xe có thể tự đi đăng ký.\n" +
                "Khách đến nhận xe tại cửa hàng.\n" +
                "Thời hạn bảo hành: 3 năm\n" +
                "*Sau khi đặt hàng thành công, cửa hàng sẽ liên hệ và thông báo thời gian nhận xe trong vòng 24h.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Đại lý Yamaha Xe Máy 74 \n" +
                "Địa chỉ: 74 Khâm Thiên, quận Đống Đa, Hà Nội\n" +
                "\uD83D\uDCDE Tư vấn bán hàng: 02438510610 \n" +
                "\uD83D\uDCDE Di động: 0904505045");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-diounf5ji8ivcf_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);

        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-yx2ubh5ji8ivc2_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.GRAY);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-n8vynf5ji8iv2f_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(910));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe máy Yamaha Janus Phiên Bản Giới Hạn");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/970938f58e68e37ddbeeaffe2ba7bcc6_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-e0z1qa0h9mjvb6_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-hdrwoc0h9mjv2b_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/73f1f6763965aabf62c0f7cc9398a8b3_tn");
        //thêm mô tả
        productInput.setDescription("Yamaha Grande là mẫu xe tay ga tiết kiệm xăng số 1 Việt Nam với mức tiêu thụ chỉ 1,69 lít/100km, \n" +
                "theo số liệu thống kê và phân tích được Cục Đăng kiểm Việt Nam công bố, xác nhận bởi Báo Giao thông. \n" +
                "Bên cạnh thiết kế thời trang, thanh lịch, ở phiên bản Grande Bluecore Hybrid mới còn sở hữu cốp xe siêu rộng 27 lít và\n" +
                " được trang bị hàng loạt tính năng hiện đại như trợ lực Hybrid, Smart key, hệ thống Stop & Start System, phanh ABS,.. \n" +
                "với mức giá rất hấp dẫn.\n" +
                "\n" +
                "QUY TRÌNH ĐẶT XE :\n" +
                "B1: Chọn xe/ Màu xe cho vào giỏ hàng\n" +
                "B2 : Chat cùng cửa hàng để biết thêm thông tin về sản phẩm và các chi phí phát sinh kèm theo.\n" +
                "B3: Khách đồng ý -> đặt xe -> cửa hàng nhận đơn->giao xe cho khách. \n" +
                "Hotline Zalo : 0904505045\n" +
                "Giá bán xe đã bao gồm thuế VAT, chưa bao gồm thuế trước bạ, biển số, và các chi phí khác\n" +
                "Cửa hàng có hỗ trợ về thủ tục đăng ký, khách hàng mua xe có thể tự đi đăng ký.\n" +
                "Khách đến nhận xe tại cửa hàng.\n" +
                "Thời hạn bảo hành: 3 năm\n" +
                "*Sau khi đặt hàng thành công, cửa hàng sẽ liên hệ và thông báo thời gian nhận xe trong vòng 24h.\n" +
                "Đại lý Yamaha Xe Máy 74 \n" +
                "Địa chỉ: 74 Khâm Thiên, quận Đống Đa, Hà Nội\n" +
                "\uD83D\uDCDE Tư vấn bán hàng: 02438510610 \n" +
                "\uD83D\uDCDE Di động: 0904505045");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/970938f58e68e37ddbeeaffe2ba7bcc6_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);

        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22110-e0z1qa0h9mjvb6_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.GRAY);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(1200));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22110-hdrwoc0h9mjv2b_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Máy Yamaha Grande Phiên Bản Giới Hạn");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/4c5ea8bc9c8bbfbae25c96f83ca59fdf_tn");
        images.add("https://cf.shopee.vn/file/77d27b55eb2eda0d3b188f0b478471ac_tn");
        images.add("https://cf.shopee.vn/file/2c586c78f19bae7bc35f69a2ae5610bf_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/509cf4e0d80675cb95cae3ea41e6c3f4_tn");
        //thêm mô tả
        productInput.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
                "Bố trí xi lanh        Xy lanh đơn\n" +
                "Dung tích xy lanh (CC)        155,1 cc\n" +
                "Đường kính và hành trình piston        58 x 58,7mm\n" +
                "Tỷ số nén        11,6:1\n" +
                "Công suất tối đa        11,3kW (15,4 PS)/8.000 vòng/phút\n" +
                "Mô men cực đại        13,9 N.m (1,4kgf.m)/6.500 vòng/phút\n" +
                "Hệ thống khởi động        Điện\n" +
                "Dung tích bình xăng        5,5 lít\n" +
                "Mức tiêu thụ nhiên liệu (l/100km)        2,19\n" +
                "Tỷ số truyền động        2,300-0,724:1\n" +
                "Kiểu hệ thống truyền lực        CVT\n" +
                "Khung xe\n" +
                "Kích thước\n" +
                "Bảo hành\n" +
                "Hệ thống điện\n" +
                "Lưu ý:thanh toán bằng thẻ tín dụng hay tất cả hình thức khác trên shopee \n" +
                "đều có phí 3% thanh toán khi nhận xe");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/2c586c78f19bae7bc35f69a2ae5610bf_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.ORANGE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/77d27b55eb2eda0d3b188f0b478471ac_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.GRAY);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/4c5ea8bc9c8bbfbae25c96f83ca59fdf_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(910));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Máy Yamaha Exciter 150 Phiên Bản Gp và Rc");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/7803aa9c3e0d2c64d6f8f9bc5f4f2925_tn");
        images.add("https://cf.shopee.vn/file/020f9b4704b1366331c3ffa780cee8b7_tn");
        images.add("https://cf.shopee.vn/file/043df65b14e76f116a3f5e4bd1520f48_tn");

        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/b11e3fcd26b8d03818387c6e15ab41af_tn");
        //thêm mô tả
        productInput.setDescription("Yamaha Grande là mẫu xe tay ga tiết kiệm xăng số 1 Việt Nam với mức tiêu thụ chỉ 1,69 lít/100km, \n" +
                "theo số liệu thống kê và phân tích được Cục Đăng kiểm Việt Nam công bố, xác nhận bởi Báo Giao thông. \n" +
                "Bên cạnh thiết kế thời trang, thanh lịch, ở phiên bản Grande Bluecore Hybrid mới còn sở hữu cốp xe siêu rộng 27 lít và\n" +
                " được trang bị hàng loạt tính năng hiện đại như trợ lực Hybrid, Smart key, hệ thống Stop & Start System, phanh ABS,.. \n" +
                "với mức giá rất hấp dẫn.\n" +
                "\n" +
                "QUY TRÌNH ĐẶT XE :\n" +
                "B1: Chọn xe/ Màu xe cho vào giỏ hàng\n" +
                "B2 : Chat cùng cửa hàng để biết thêm thông tin về sản phẩm và các chi phí phát sinh kèm theo.\n" +
                "B3: Khách đồng ý -> đặt xe -> cửa hàng nhận đơn->giao xe cho khách. \n" +
                "Hotline Zalo : 0904505045\n" +
                "Giá bán xe đã bao gồm thuế VAT, chưa bao gồm thuế trước bạ, biển số, và các chi phí khác\n" +
                "Cửa hàng có hỗ trợ về thủ tục đăng ký, khách hàng mua xe có thể tự đi đăng ký.\n" +
                "Khách đến nhận xe tại cửa hàng.\n" +
                "Thời hạn bảo hành: 3 năm\n" +
                "*Sau khi đặt hàng thành công, cửa hàng sẽ liên hệ và thông báo thời gian nhận xe trong vòng 24h.\n" +
                "Đại lý Yamaha Xe Máy 74 \n" +
                "Địa chỉ: 74 Khâm Thiên, quận Đống Đa, Hà Nội\n" +
                "\uD83D\uDCDE Tư vấn bán hàng: 02438510610 \n" +
                "\uD83D\uDCDE Di động: 0904505045");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/043df65b14e76f116a3f5e4bd1520f48_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLUE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/020f9b4704b1366331c3ffa780cee8b7_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.BLACK);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/7803aa9c3e0d2c64d6f8f9bc5f4f2925_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(910));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Máy Yamaha MT15  ");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/f804603ed99d44755cd288d69e181fb6_tn");
        images.add("https://cf.shopee.vn/file/c3f97d1cfcd6c61f9268e4df12245d62_tn");
        images.add("https://cf.shopee.vn/file/f8b5e3f82631338c344f32412cf2dc6d_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/54f2a3a01d31df919c1ee5ec924dc121_tn");
        //thêm mô tả
        productInput.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
                "Bố trí xi lanh        Xy lanh đơn\n" +
                "Dung tích xy lanh (CC)        155,1 cc\n" +
                "Đường kính và hành trình piston        58 x 58,7mm\n" +
                "Tỷ số nén        11,6:1\n" +
                "Công suất tối đa        11,3kW (15,4 PS)/8.000 vòng/phút\n" +
                "Mô men cực đại        13,9 N.m (1,4kgf.m)/6.500 vòng/phút\n" +
                "Hệ thống khởi động        Điện\n" +
                "Dung tích bình xăng        5,5 lít\n" +
                "Mức tiêu thụ nhiên liệu (l/100km)        2,19\n" +
                "Tỷ số truyền động        2,300-0,724:1\n" +
                "Kiểu hệ thống truyền lực        CVT\n" +
                "Khung xe\n" +
                "Kích thước\n" +
                "Bảo hành\n" +
                "Hệ thống điện\n" +
                "Lưu ý:thanh toán bằng thẻ tín dụng hay tất cả hình thức khác trên shopee \n" +
                "đều có phí 3% thanh toán khi nhận xe");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/f804603ed99d44755cd288d69e181fb6_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLUE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/c3f97d1cfcd6c61f9268e4df12245d62_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.BLACK);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));
        productVariantList.add(variant_2);

        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Máy Yamaha Sirius Phanh Cơ");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/4c5ea8bc9c8bbfbae25c96f83ca59fdf_tn");
        images.add("https://cf.shopee.vn/file/77d27b55eb2eda0d3b188f0b478471ac_tn");
        images.add("https://cf.shopee.vn/file/2c586c78f19bae7bc35f69a2ae5610bf_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/509cf4e0d80675cb95cae3ea41e6c3f4_tn");
        //thêm mô tả
        productInput.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
                "Bố trí xi lanh        Xy lanh đơn\n" +
                "Dung tích xy lanh (CC)        155,1 cc\n" +
                "Đường kính và hành trình piston        58 x 58,7mm\n" +
                "Tỷ số nén        11,6:1\n" +
                "Công suất tối đa        11,3kW (15,4 PS)/8.000 vòng/phút\n" +
                "Mô men cực đại        13,9 N.m (1,4kgf.m)/6.500 vòng/phút\n" +
                "Hệ thống khởi động        Điện\n" +
                "Dung tích bình xăng        5,5 lít\n" +
                "Mức tiêu thụ nhiên liệu (l/100km)        2,19\n" +
                "Tỷ số truyền động        2,300-0,724:1\n" +
                "Kiểu hệ thống truyền lực        CVT\n" +
                "Khung xe\n" +
                "Kích thước\n" +
                "Bảo hành\n" +
                "Hệ thống điện\n" +
                "Lưu ý:thanh toán bằng thẻ tín dụng hay tất cả hình thức khác trên shopee \n" +
                "đều có phí 3% thanh toán khi nhận xe");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/2c586c78f19bae7bc35f69a2ae5610bf_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(800));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/77d27b55eb2eda0d3b188f0b478471ac_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.GRAY);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(820));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/2c586c78f19bae7bc35f69a2ae5610bf_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(810));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe yamaha YZF-R15-V4 ");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-eis02rk4ohiv6a_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-sus73rcrmuiv82_tn");

        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-eis02rk4ohiv6a_tn");
        //thêm mô tả
        productInput.setDescription("Yamaha Grande là mẫu xe tay ga tiết kiệm xăng số 1 Việt Nam với mức tiêu thụ chỉ 1,69 lít/100km, \n" +
                "theo số liệu thống kê và phân tích được Cục Đăng kiểm Việt Nam công bố, xác nhận bởi Báo Giao thông. \n" +
                "Bên cạnh thiết kế thời trang, thanh lịch, ở phiên bản Grande Bluecore Hybrid mới còn sở hữu cốp xe siêu rộng 27 lít và\n" +
                " được trang bị hàng loạt tính năng hiện đại như trợ lực Hybrid, Smart key, hệ thống Stop & Start System, phanh ABS,.. \n" +
                "với mức giá rất hấp dẫn.\n" +
                "\n" +
                "QUY TRÌNH ĐẶT XE :\n" +
                "B1: Chọn xe/ Màu xe cho vào giỏ hàng\n" +
                "B2 : Chat cùng cửa hàng để biết thêm thông tin về sản phẩm và các chi phí phát sinh kèm theo.\n" +
                "B3: Khách đồng ý -> đặt xe -> cửa hàng nhận đơn->giao xe cho khách. \n" +
                "Hotline Zalo : 0904505045\n" +
                "Giá bán xe đã bao gồm thuế VAT, chưa bao gồm thuế trước bạ, biển số, và các chi phí khác\n" +
                "Cửa hàng có hỗ trợ về thủ tục đăng ký, khách hàng mua xe có thể tự đi đăng ký.\n" +
                "Khách đến nhận xe tại cửa hàng.\n" +
                "Thời hạn bảo hành: 3 năm\n" +
                "*Sau khi đặt hàng thành công, cửa hàng sẽ liên hệ và thông báo thời gian nhận xe trong vòng 24h.\n" +
                "Đại lý Yamaha Xe Máy 74 \n" +
                "Địa chỉ: 74 Khâm Thiên, quận Đống Đa, Hà Nội\n" +
                "\uD83D\uDCDE Tư vấn bán hàng: 02438510610 \n" +
                "\uD83D\uDCDE Di động: 0904505045");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-eis02rk4ohiv6a_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLUE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-sus73rcrmuiv82_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.BLACK);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));
        productVariantList.add(variant_2);

        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602350_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        //sửa tên sản phâm
        productInput.setName("Xe Máy Yamaha Sirius Vành Đúc");
        //mã shop sửa lại
        productInput.setShopId(602350);
        //mã thương hiệu
        productInput.setTradeMarkId("1671551420766767");
        //sửa ngành hàng
        productInput.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productInput.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/d8f4c895a58b8a01039ab6b829998190_tn");
        images.add("https://cf.shopee.vn/file/793bc35b35e52a4886a283fc7b1a0405_tn");
        images.add("https://cf.shopee.vn/file/69820f0835581834e1f684c357c8d13b_tn");
        productInput.setImageUrls(images);
        //thêm link ảnh chính vô
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/c67245663241a658918b11dc79445023_tn");
        //thêm mô tả
        productInput.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
                "Bố trí xi lanh        Xy lanh đơn\n" +
                "Dung tích xy lanh (CC)        155,1 cc\n" +
                "Đường kính và hành trình piston        58 x 58,7mm\n" +
                "Tỷ số nén        11,6:1\n" +
                "Công suất tối đa        11,3kW (15,4 PS)/8.000 vòng/phút\n" +
                "Mô men cực đại        13,9 N.m (1,4kgf.m)/6.500 vòng/phút\n" +
                "Hệ thống khởi động        Điện\n" +
                "Dung tích bình xăng        5,5 lít\n" +
                "Mức tiêu thụ nhiên liệu (l/100km)        2,19\n" +
                "Tỷ số truyền động        2,300-0,724:1\n" +
                "Kiểu hệ thống truyền lực        CVT\n" +
                "Khung xe\n" +
                "Kích thước\n" +
                "Bảo hành\n" +
                "Hệ thống điện\n" +
                "Lưu ý:thanh toán bằng thẻ tín dụng hay tất cả hình thức khác trên shopee \n" +
                "đều có phí 3% thanh toán khi nhận xe");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/d8f4c895a58b8a01039ab6b829998190_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.RED);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setSize(SizeType.L);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);


        ProductVariant variant_2 = new ProductVariant();
        variant_2.setImageUrl("https://cf.shopee.vn/file/793bc35b35e52a4886a283fc7b1a0405_tn");
        variant_2.setWeight(400.0);
        variant_2.setColor(ColorProduct.GRAY);
        variant_2.setDimension(dimensionMeasurement);
        variant_2.setRequiresShipping(true);
        variant_2.setPrice(MoneyCalculateUtils.getMoney(920));
        productVariantList.add(variant_2);

        ProductVariant variant_3 = new ProductVariant();
        variant_3.setImageUrl("https://cf.shopee.vn/file/69820f0835581834e1f684c357c8d13b_tn");
        variant_3.setWeight(400.0);
        variant_3.setColor(ColorProduct.WHITE);
        variant_3.setDimension(dimensionMeasurement);
        variant_3.setRequiresShipping(true);
        variant_3.setPrice(MoneyCalculateUtils.getMoney(910));
        productVariantList.add(variant_3);
        createProductInput1.setProductVariants(productVariantList);


        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //Inistree
    @Test
    public void testProduct_602334_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Phấn phủ bột kiềm dầu innisfree No Sebum Mineral Powder 5g");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/8a0c878f58e08c86a127040ac89bb63d");
        images.add("https://cf.shopee.vn/file/66057aee11843ae46ea38458f022453c");
        images.add("https://cf.shopee.vn/file/c69e636e738288be7fb3248253aa93b9");
        images.add("https://cf.shopee.vn/file/2f45bd462cd17aac30582d22946c545c");
        images.add("https://cf.shopee.vn/file/e74f80862cbd52df6a16e4d65a4c7a8c");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-vuwhm6d3salv21");
        productInput.setDescription("PHẤN PHỦ BỘT KIỀM DẦU INNISFREE NO SEBUM MINERAL POWDER 5G\n" +
                "\n" +
                "Phấn phủ bột kiềm dầu innisfree No Sebum Mineral Powder chiết xuất từ bạc hà và khoáng chất tự nhiên đảo Jeju, kiểm soát bã nhờn và dầu thừa để lớp nền luôn khô thoáng và mịn màng. Phấn phủ đa năng này còn có thể dùng như dầu gội khô hay làm phấn lót giữ màu mắt lâu trôi.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần & Công dụng\n" +
                "\n" +
                "1. Hút sạch và kiểm soát dầu thừa\n" +
                "Phấn phủ kiềm dầu vượt trội, mang lại làn da khô thoáng và mịn màng như da em bé nhờ thành phần bạc hà và dầu khoáng tự nhiên đảo Jeju. Phấn phủ không màu, tạo độ che phủ tự nhiên, cho lớp nền mượt mà hơn và xóa tan cảm giác nhờn rít khó chịu.\n" +
                "\n" +
                "2. Duy trì làn da rạng rỡ\n" +
                "Bên cạnh tác dụng kiểm soát dầu thừa hiệu quả, phấn phủ chiết xuất thiên nhiên còn giúp duy trì làn da rạng rỡ và hạn chế lớp nền bị xuống tông.\n" +
                "\n" +
                "3. Đa năng và tiện lợi\n" +
                "Ngoài sử dụng cho lớp nền, phấn phủ đa năng còn có thể được dùng để hút sạch bã nhờn trên da đầu hay lót dưới phấn mắt để giữ màu bền lâu. Thiết kế nhỏ gọn, thông minh, tiện lợi khi mang theo bên mình mỗi ngày.\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "Dùng ở bước cuối cùng của quy trình chăm sóc da. Thoa phấn lên vùng da nhiều dầu, có thể dặm lại khi cần thiết.\n" +
                "\n" +
                "Lưu ý\n" +
                "1. Chỉ sử dụng ngoài da. \n" +
                "2. Tránh tiếp xúc trực tiếp với mắt. \n" +
                "3. Rửa sạch ngay với nước nếu tiếp xúc trực tiếp với mắt. \n" +
                "4. Ngưng sử dụng sản phẩm và tham khảo ngay ý kiến bác sĩ khi thấy có dấu hiệu bất thường. \n" +
                "5. Tránh xa tầm tay trẻ em.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "--\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "--\n" +
                "Xuất xứ: Hàn Quốc \n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất in trên bao bì sản phẩm.\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #phanphu #phankiemdau #nosebummineralpowder #nosebum");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-vuwhm6d3salv21");
        variant_1.setWeight(5.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);
        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Kem dưỡng da tay innisfree Jeju Life Perfumed Hand Cream 30ml");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0eae41afd7c614d5c7cf33e2f5de0a24");
        images.add("https://cf.shopee.vn/file/e55a17b5d0a73c919f0f1d509b237bb9");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/8c603c56d978cf5fe557545bb226baff");
        productInput.setDescription("Kem dưỡng da tay innisfree Jeju Life Perfumed Hand Cream 30ml\n" +
                "\n" +
                "Một đôi tay luôn mềm mại và toả hương thơm nhè nhẹ sẽ là điểm thu hút quyến rũ của bạn. Hãy để innisfree giúp bạn làm điều đó, với kem dưỡng da tay mang phong cách Jeju, với mùi thơm lãng mạn mang đậm khung cảnh Jeju, dưỡng ẩm và dễ dàng thẩm thấu vào da đem lại day tay mềm mại ngát hương\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần, công dụng\n" +
                "- Chuẩn phong cách với những mùi hương đa dạng:  10 loại mùi hương dưỡng da tay mang phong cách Jeju, với mùi thơm lãng mạn mang đậm khung cảnh Jeju.\n" +
                "- Dạng kem dưỡng ẩm ôm trọn lấy đôi bàn tay. Giúp đôi bàn tay mềm mại, không gây bết dính, thẩm thấu nhanh và không để lại vết bóng dầu\n" +
                "- Hương thơm bền lâu với kĩ thuật semi-wax trap, giúp hương thơm được bền lâu\n" +
                "\n" +
                "Phù hợp với mọi làn da tay, kể cả da nhạy cảm\n" +
                "\n" +
                "Gồm 10 hương\n" +
                "- Hương Tropic Sherbet\n" +
                "- Hương Lilac Path\n" +
                "- Hương Sky Surfing\n" +
                "- Hương Snow Love\n" +
                "- Hương Guesthouse\n" +
                "- Hương Small Wedding\n" +
                "- Hương Sunshine Wildberry\n" +
                "- Hương Peach\n" +
                "- Hương Pink Coral\n" +
                "- Hương Yuja Tea\n" +
                "\n" +
                "Thiết kế: tuýp 30ml\n" +
                "\n" +
                "Hướng dẫn sử dụng:\n" +
                "- Sau khi rửa tay, lấy lượng vừa đủ và thao đều lên bàn tay.\n" +
                "\n" +
                "Hướng dẫn bảo quản\n" +
                "- Đóng nắp sau khi sử dụng\n" +
                "- Bảo quản nơi khô thoáng mát. \n" +
                "- Không bảo quản nơi có nhiệt độ quá cao hoặc quá thấp, nơi có ánh sáng trực tiếp.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "--\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "--\n" +
                "Xuất xứ: Hàn Quốc.\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất.\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "#innisfreeofficial #jejuhandcream #kemduongtay");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/8c603c56d978cf5fe557545bb226baff");
        variant_1.setWeight(30.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(700));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Mặt nạ innisfree My Real Squeeze Mask 20ml");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/7a965de5f7973b690724dbbeba4e98cb");
        images.add("https://cf.shopee.vn/file/24555648e8707398cc4b513c912dc9c3");
        images.add("https://cf.shopee.vn/file/259aa948870d92e272e741168fdf5298");
        images.add("https://cf.shopee.vn/file/0bd0302033765d5df371f3f9b8109f45");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-0e8fdlsl83iv4a");
        productInput.setDescription("Mặt nạ innisfree My Real Squeeze Mask 20ml\n" +
                "\n" +
                "Mặt nạ My Real Squeeze Mask 20ml chiết xuất từ nước ép tự nhiên đảo Jeju, giải quyết các vấn đề khác nhau của làn da. Làm từ 100% cellulose lành tính, mặt nạ ứng dụng công nghệ vắt - ép lạnh để tối ưu hóa dưỡng chất. Mặt nạ dưa leo phục hồi nước tức thì cho da.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần, công dụng\n" +
                "1. Chiết xuất nước ép tươi với công nghệ vắt hiện đại \n" +
                "Mặt nạ dồi dào năng lượng thiên nhiên từ các loại hoa quả tại hòn đảo Jeju trong lành. Dưỡng chất được lưu giữ trọn vẹn nhờ ứng dụng công nghệ vắt và ép lạnh ở nhiệt độ thấp.\n" +
                "\n" +
                "2. Làm từ 100% cellulose nguyên chất\n" +
                "Mặt nạ với chất liệu cellulose từ cây bạch đàn cho thiết kế mỏng nhẹ và bền chắc. Mặt nạ lành tính, ôm nhẹ và bám tốt trên mặt, giúp đưa dưỡng chất vào da hiệu quả hơn. Thiết kế mới Easy-peel có hai tai gấu bên trên cho thao tác tiện lợi hơn.\n" +
                "*Được làm từ nguyên liệu tự phân hủy, đây là mặt nạ giấy thân thiện với môi trường.\n" +
                "\n" +
                "3. Ba loại dưỡng chất chăm sóc da toàn diện:\n" +
                "A. Dạng nước tinh chất dịu mát:\n" +
                "- Tea Tree (Cây tràm trà): Làm mượt và kháng khuẩn.\n" +
                "- Bamboo (Tre): Cấp nước và làm dịu.\n" +
                "- Lime (Chanh): Làm sáng da.\n" +
                "- Green Tea (trà xanh): Cấp nước và làm diu da.\n" +
                "- Rose (hoa hồng): Cấp nước và làm sáng da.\n" +
                "B. Dạng tinh chất dưỡng ẩm: \n" +
                "- Fig (Trái phỉ): Thanh lọc da.\n" +
                "- Pomergranate (Lựu): Làm mịn và sáng da.\n" +
                "- Honey (Mật ong manuka): Dưỡng ẩm và phục hồi da.\n" +
                "- Cucumber (Dưa leo): Phục hồi nước.\n" +
                "- Aloe (Nha đam): Cấp nước và làm dịu da.\n" +
                "C. Dạng kem giàu dưỡng chất: \n" +
                "- Rice (Gạo): Làm sáng và mịn da.\n" +
                "- Acai Berry: Phục hồi và giúp da săn chắc.\n" +
                "- Ginseng (Nhân sâm Hàn Quốc): phục hồi da và nuôi dưỡng.\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "1) Sau khi rửa mặt, thoa nước cân bằng làm dịu da.\n" +
                "2) Lấy mặt nạ ra khỏi bao bì và đắp lên mặt. Lưu ý tránh vùng mắt, môi.\n" +
                "3) Đắp mặt nạ từ 10-20 phút.\n" +
                "4) Tháo mặt nạ ra. Vỗ nhẹ để tăng cường khả năng thấm thấu.\n" +
                "\n" +
                "Lưu ý\n" +
                "1) Ngưng sử dụng ngay khi có dấu hiệu bất thường.\n" +
                "2) Sử dụng ngay sau khi mở nắp.\n" +
                "3) Tránh tiếp xúc vùng mắt.\n" +
                "4) Lưu ý với người dễ bị kích ứng với cao dán và khăn ướt. \n" +
                "5) Tránh xa tầm tay trẻ em.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "--\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "--\n" +
                "Xuất xứ: Hàn Quốc.\n" +
                "Hạn sử dụng: 2 năm kể từ ngày sản xuất.\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #matna #matnagiay \n" +
                "#innisfreemyrealsqueezemask #myrealsqueezemask #mask #sheetmask");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-0e8fdlsl83iv4a");
        variant_1.setWeight(20.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(800));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Kem dưỡng ẩm sáng da innisfree Cherry Blossom Tone Up Cream 50ml");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/2320796afe1a33e86d1621e0ccd9e60f");
        images.add("https://cf.shopee.vn/file/26d222e0e42ee310149142bf0acc694d");
        images.add("https://cf.shopee.vn/file/e26152fd98df94fff515a3fd816d00f5");
        images.add("https://cf.shopee.vn/file/798e1a8263f7dae0f0b807a9ae57925d");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-80ysxexje6kv7a");
        productInput.setDescription("Kem dưỡng ẩm sáng da innisfree Cherry Blossom Tone Up Cream 50ml\n" +
                "\n" +
                "Kem dưỡng ẩm sáng da innisfree Cherry Blossom Tone Up Cream cho làn da tinh khiết, trắng sáng rạng rỡ tức thì. Có thể sử dụng thay kem nền khi muốn trang điểm nhẹ nhàng.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần & Công dụng\n" +
                "1. Làm sáng da hiệu quả với Jeju Cherry Blossom, không gây mỏng da\n" +
                "Khả năng dưỡng sáng hiệu quả của chiết xuất cánh hoa anh đào Jeju được nâng tầm bằng phương pháp ủ lạnh với nước biển lava đảo Jeju trong 3 tuần. Làn da hấp thụ tinh hoa dưỡng chất sẽ trở nên tươi sáng, trong veo và tràn đầy sức sống. \n" +
                "\n" +
                "2. Cung cấp và duy trì độ ẩm cho làn da mịn màng, không còn khô ráp\n" +
                "Bổ sung thêm Betaine từ củ dền với khả năng dưỡng ẩm dồi dào, mang đến vẻ mềm mại, mịn màng và căng mọng cho làn da.\n" +
                "\n" +
                "3. Nâng tone tự nhiên, tự tin với làn da mộc\n" +
                "Cánh hoa anh đào từ Jeju với dưỡng chất giúp nâng tông da tức thì, đồng thời dưỡng sáng da từ bên trong. Có thể sử dụng thay kem nền khi muốn trang điểm nhẹ nhàng vào mùa nóng.\n" +
                "\n" +
                "Tips\n" +
                "- Kết hợp cùng với Cherry Blossom Jelly Cream (tuỳ theo tỉ lệ) cho làn da thêm ẩm mượt rạng rỡ.\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "- Ở bước cuối cùng của quá trình chăm sóc da, thoa một lượng thích hợp lên mặt và cổ. Để sản phẩm thẩm thấu, sau đó thoa thêm một lớp mỏng để tăng hiệu quả dưỡng sáng da nếu cần.\n" +
                "\n" +
                "Lưu ý\n" +
                "1. Chỉ dùng ngoài da.\n" +
                "2. Tránh tiếp xúc trực tiếp với mắt.\n" +
                "3. Rửa sạch ngay với nước nếu sản phẩm tiếp xúc với mắt.\n" +
                "4. Ngưng sử dụng sản phẩm và tham khảo ngay ý kiến bác sĩ khi thấy da có dấu hiệu bất thường.\n" +
                "5. Tránh xa tầm tay trẻ em.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "--\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "--\n" +
                "Xuất xứ: Hàn Quốc.\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất.\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #cherryblossom #cream");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-80ysxexje6kv7a");
        variant_1.setWeight(50.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Sữa rửa mặt dành cho da mụn innisfree Bija Trouble Facial Foam 150g");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/d97f9b0fdf880a3a9f72aab08869421e");
        images.add("https://cf.shopee.vn/file/253b6b4b1af4923fe61bd7bda237d4d3");
        images.add("https://cf.shopee.vn/file/7c5109c8b0298450ba1bb545853ff3ee");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-ia7mb1f4e6kv8b");
        productInput.setDescription("Sữa rửa mặt dành cho da mụn innisfree Bija Trouble Facial Foam 150g\n" +
                "\n" +
                "Sữa rửa mặt chiết xuất từ dầu Bija Jeju giúp làm sạch hoàn hảo bụi bẩn từ sâu bên trong lỗ chân lông, đặc biệt phù hợp cho làn da đang gặp vấn đề về mụn.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần, công dụng\n" +
                "\n" +
                "1. Hạt Bija tăng sức đề kháng cho da\n" +
                "Bija hấp thụ tinh hoa thiên nhiên suốt một khoảng thời gian dài, nhẹ nhàng làm dịu và tăng sức đề kháng cho da. Từ đó, tình trạng làn da được cải thiện đáng kể. innisfree lựa chọn hình thức thương mại công bằng, thu mua Bija được trồng tại Songdang-ri, Jeju. Nhờ vậy, innisfree đã mang lại nguồn thu nhập mới và thúc đẩy phát triển cộng đồng nơi đây.\n" +
                "\n" +
                "*Sản phẩm đạt chứng nhận 6 không (Không parabens, không màu tổng hợp, không dầu khoáng, không dầu động vật, không mùi hương nhân tạo, không imidazolidinyl urea).\n" +
                "\n" +
                "2. Sạch thoáng lỗ chân lông và tế bào chết\n" +
                "Thành phần Acid Salicylic BHA giúp loại bỏ tế bào chết trên da và làm sạch sâu tạp chất bên trong lỗ chân lông.\n" +
                "\n" +
                "3. An toàn cho da mụn và dầu mụn\n" +
                "Sản phẩm đạt kết quả thử nghiệm Noncomedogenic, an toàn cho da mụn. Dầu hạt Bija Jeju giúp làm dịu và bảo vệ vùng da gặp rắc rối về vấn đề mụn. Bọt mịn tạo cảm giác sảng khoái cho làn da dầu.\n" +
                "\n" +
                "Loại da phù hợp\n" +
                "- Da gặp vấn đề về mụn\n" +
                "\n" +
                "Thiết kế sản phẩm\n" +
                "- Dung tích 150g\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "Cho một lượng thích hợp vào lòng bàn tay, tạo bọt vừa đủ rồi thoa nhẹ lên da. Mát-xa toàn mặt, sau đó rửa mặt sạch bằng nước ấm.\n" +
                "\n" +
                "Hướng dẫn bảo quản\n" +
                "1. Chỉ sử dụng ngoài da.\n" +
                "2. Tránh tiếp xúc trực tiếp với mắt.\n" +
                "3. Rửa sạch ngay với nước nếu tiếp xúc trực tiếp với mắt.\n" +
                "4. Ngưng sử dụng sản phẩm và tham khảo ngay ý kiến bác sĩ khi thấy có dấu hiệu bất thường.\n" +
                "5. Tránh xa tầm tay trẻ em.\n" +
                "\n" +
                "*Cảnh báo rám nắng : Sản phẩm này chứa Alpha hydroxy acid (AHA) có thể làm tăng sự nhạy cảm của da với nắng cụ thể là có thể bị rám nắng. Hãy sử dụng các dụng cụ che nắng và hạn chế phơi nắng khi sử dụng Sản phẩm này cho đến sau đó một tuần.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "--\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "--\n" +
                "Xuất xứ: Hàn Quốc.\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất.\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #suaruamat #facialfoam #bija");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-ia7mb1f4e6kv8b");
        variant_1.setWeight(150.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));

        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Mặt nạ lột mụn đầu đen innisfree Jeju Volcanic Nose Pack 1 Miếng");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0b95f700dff9fba598b5074f6e0ac180");
        images.add("https://cf.shopee.vn/file/d627dae05cbae67486c615b5059482a3");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/ddb18f1f06bc188a31395525f5a0e49d");
        productInput.setDescription("Mặt nạ lột mụn đầu đen innisfree Jeju Volcanic Nose Pack 1 Miếng\n" +
                "\n" +
                "\n" +
                "Mặt nạ dành cho da vùng mũi chiết xuất từ tro núi lửa Jeju giúp làm sạch chất nhờn và loại bỏ mụn đầu đen.\n" +
                "\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần, công dụng\n" +
                "\n" +
                "- Chứa thành phần tro núi lửa Jeju có tác dụng loại bỏ bụi bẩn và đánh bay bã nhờn, làm sạch tận sâu lỗ chân lông.\n" +
                "\n" +
                "- Loại bỏ mụn đầu đen cứng đầu \"đóng quân\" quanh vùng mũi và lớp bã nhờn bám sâu lỗ chân lông một cách hiệu quả.\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "\n" +
                "- Sau khi rửa sạch, không dùng sữa dưỡng hoặc kem dưỡng. Thoa ít nước làm ướt mũi.\n" +
                "\n" +
                "- Gỡ bỏ tấm phim trong suốt, sau đó nhẹ nhàng dán mặt mịn miếng dán lội mụn vào giữa mũi.\n" +
                "\n" +
                "- Sau khoảng 15 phút, khi miếng dán khô hoàn toàn, từ từ gỡ bỏ miếng dán từ ngoài mép vào trong.\n" +
                "\n" +
                "- Làm sạch lại vùng mũi bằng bông cotton thấm nước cân bằng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn bảo quản\n" +
                "\n" +
                "- Đậy nắp lại sau khi sử dụng.\n" +
                "\n" +
                "- Không bảo quản nơi có nhiệt độ quá cao hoặc quá thấp, nơi có ánh sáng trực tiếp.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "\n" +
                "--\n" +
                "\n" +
                "Xuất xứ: Hàn Quốc.\n" +
                "\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất.\n" +
                "\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #matna #jejuvolcanicnosepack #maskMặt nạ lột mụn đầu đen innisfree Jeju Volcanic Nose Pack 1 Miếng\n" +
                "\n");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/ddb18f1f06bc188a31395525f5a0e49d");
        variant_1.setWeight(5.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(600));

        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Kem lót kiềm dầu làm mờ lỗ chân lông innisfree No Sebum Blur Primer 25ml");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/f9621851e40429c20b209966deced0df");
        images.add("https://cf.shopee.vn/file/0600e6c0f46ceda952453d3b223d9f51");
        images.add("https://cf.shopee.vn/file/0b25b1b9ce96ece6bbad343df283de23");
        images.add("https://cf.shopee.vn/file/babde95ab07ab6986ac4868d21d3d932");
        images.add("https://cf.shopee.vn/file/d011f3e709a41ab4b380f87a09bb5ed1");
        images.add("https://cf.shopee.vn/file/42670eb64085334b6308c9b6e7461a94");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/1c4200a6646886acac0750a6f0aea696");
        productInput.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Kem lót kiềm dầu làm mờ lỗ chân lông innisfree No Sebum Blur Primer 25ml\n" +
                "\n" +
                "Kem lót kiềm dầu làm mờ lỗ chân lông innisfree No Sebum Blur Primer giúp kiềm dầu, đồng thời che phủ lỗ chân lông, làm mờ các nếp nhăn và tạo lớp lót mịn màng bảo vệ da.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần & công dụng\n" +
                "\n" +
                "1. Kiềm dầu cho lớp nền lâu trôi\n" +
                "\n" +
                "Thành phần bạc hà và khoáng chất tự nhiên giúp cân bằng dầu và độ ẩm trên da, giúp làn da khô thoáng và và duy trì lớp nền bền lâu.\n" +
                "\n" +
                "2. Che phủ lỗ chân lông và làm mờ nếp nhăn\n" +
                "\n" +
                "Kem lót che phủ lỗ chân lông, làm mờ các nếp nhăn tạo lớp lót mịn màng, lý tưởng trước khi trang điểm.\n" +
                "\n" +
                "3. Chăm sóc làn da khỏe khoắn với thành phần thiên nhiên \n" +
                "\n" +
                "Chiết xuất từ hồng xanh Jeju giúp chăm sóc làn da mịn màng và tạo lớp bảo vệ da trước khi trang điểm.\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "\n" +
                "- Sau bước chăm sóc da, thoa một lượng mỏng thích hợp lên vùng cần che phủ lỗ chân lông rồi vỗ nhẹ để tạo lớp lót tự nhiên.\n" +
                "\n" +
                "Lưu ý\n" +
                "\n" +
                "1. Chỉ sử dụng ngoài da.\n" +
                "\n" +
                "2. Tránh tiếp xúc trực tiếp với mắt.\n" +
                "\n" +
                "3. Rửa sạch ngay với nước nếu sản phẩm tiếp xúc với mắt.\n" +
                "\n" +
                "4. Ngưng sử dụng sản phẩm khi có dấu hiệu bất thường.\n" +
                "\n" +
                "5. Tránh xa tầm tay trẻ em.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "\n" +
                "-- \n" +
                "\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "\n" +
                "--\n" +
                "\n" +
                "Xuất xứ: Hàn Quốc\n" +
                "\n" +
                "Hạn sử dụng: 03 năm kể từ ngày sản xuất\n" +
                "\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #kemlottrangdiem #kemlot #primer");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/1c4200a6646886acac0750a6f0aea696");
        variant_1.setWeight(25.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(750));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);

        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Sữa rửa mặt làm sáng và sạch da innisfree Brightening Pore Facial Cleanser 150ml");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/781b62e50f6a3de125ed6f0e32802523");
        images.add("https://cf.shopee.vn/file/f968e10c54259f170d3b53450eae90b7");
        images.add("https://cf.shopee.vn/file/98a998633ee1a1f18ff3367204a35d8c");
        images.add("https://cf.shopee.vn/file/7c129599dfe90b5c625516b180d575f1");
        images.add("https://cf.shopee.vn/file/7c1ca026bf38183aa2beee81ea61d15c");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-24oalix8e6kv05");
        productInput.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sữa rửa mặt làm sáng và sạch da innisfree Brightening Pore Facial Cleanser 150ml\n" +
                "\n" +
                "Sữa rửa mặt làm sáng và sạch da innisfree Brightening Pore Facial Cleanser giúp cải thiện sắc da, thu nhỏ lỗ chân lông và cấp ẩm bằng cách loại bỏ các tạp chất bên trong da nhờ vào lớp bọt dày mịn\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM \n" +
                "\n" +
                "Thành phần công dụng \n" +
                "- Chăm sóc đồng thời các đốm tối màu + lỗ chân lông + dưỡng ẩm: Được chiết xuất từ vỏ quýt Hallabong và dẫn xuất 3 loại vitamin C, B3, B5 giúp kiểm soát các đốm tối màu và làm thu nhỏ lỗ chân lông để mang lại làn da sáng đều màu không khuyết điểm\n" +
                "- Cảm giác ẩm mượt kéo dài kể cả sau khi rửa mặt: Lớp bọt mềm mịn và dày được làm từ kết cấu dạng sữa giúp loại bỏ các chất cặn bã hiệu quả nhưng vẫn lưu lại cảm giác ẩm mướt trong thời gian dài \n" +
                "\n" +
                "Phù hợp với da đang gặp vấn đề về sắc tố và khuyết điểm\n" +
                "\n" +
                "Thiết kế sản phẩm\n" +
                "- Dung tích 150ml\n" +
                "\n" +
                "Cách sử dụng\n" +
                "- Lấy 1 lượng vừa đủ ra tay và tạo bọt \n" +
                "- Massage lên da mặt và rửa sạch lại với nước ấm\n" +
                "\n" +
                "Hướng dẫn bảo quản\n" +
                "- Đóng nắp sau khi sử dụng\n" +
                "- Bảo quản nơi thoáng mát\n" +
                "- Không bảo quản nơi có nhiệt độ quá cao hoặc quá thấp, nơi có ánh sáng trực tiếp\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "--\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "--\n" +
                "Xuất xứ: Hàn Quốc\n" +
                "Hạn sử dụng: 03 năm kể từ ngày sản xuất\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #brighteningpore #cleanser#suaruamat");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-24oalix8e6kv05");
        variant_1.setWeight(150.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(850));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Mặt nạ tẩy tế bào da chết innisfree Green Barley Gommage Mask 120ml");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/585024f559681d38fac9464b2a38d1dd");
        images.add("https://cf.shopee.vn/file/cb09d6fa95cb1cc13643dca518a732cc");
        images.add("https://cf.shopee.vn/file/efd15cc5e928325c365160c9fe1af0d3");
        images.add("https://cf.shopee.vn/file/c4fbdda25c85a29c8ffc6c52f66b7993");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/585027f559681d38fac9464b2a38d1dd");
        productInput.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Mặt nạ tẩy tế bào da chết innisfree Green Barley Gommage Mask 120ml\n" +
                "\n" +
                "Mặt nạ tẩy tế bào da chết innisfree Green Barley Gommage Mask chiết xuất từ lúa mạch xanh kết hợp hiệu quả tẩy da chết hóa học và vật lý Cellulose mang lại làn da sạch khỏe và láng mịn.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần & Công dụng\n" +
                "1. Năng lượng làm sạch hiệu quả từ lúa mạch xanh trên đảo Gapado\n" +
                "Lúa mạch xanh không hóa chất thu hoạch từ đảo Gapado chứa hàm lượng chất xơ và các thành phần dinh dưỡng khác (protein, vitamin, …) dồi dào hiệu quả trong việc làm sạch và sáng da.\n" +
                "Áp dụng phương pháp lên men 3 bước lúa mạch xanh tạo ra giấm lúa mạch xanh có tác dụng loại bỏ tế bào chết hiệu quả.\n" +
                "3 loại AHA có nguồn gốc thiên nhiên chiết xuất từ mầm lúa mạch xanh đảo Jeju giúp lấy đi tế bào chết hiệu quả cho làn da sạch mướt và tươi sáng\n" +
                "\n" +
                "2. Loại bỏ tế bào chết trên da 2 trong 1\n" +
                "Hiệu quả làm sạch và loại bỏ tế bào chết trên da gấp 2 lần nhờ kết hợp chức năng tẩy tế bào chết hóa học từ AHA, BHA và tẩy tế bào chết vật lý của Cellulose tự nhiên.\n" +
                "\n" +
                "3. Làn da láng mịn mà không khô căng\n" +
                "Chiết xuất từ lúa mạch xanh giàu chất xơ và protein giúp tăng cường khả năng loại bỏ tế bào chết, cải thiện bề mặt da mà không gây cảm giác khô căng trên da.\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "- Sau khi rửa mặt và lau khô nước, lấy một lượng thích hợp khoảng bằng đồng xu thoa đều lên mặt, tránh vùng mắt và môi. Sau 3 phút, dùng đầu ngón tay miết nhẹ lên da rồi rửa sạch bằng nước. (Dùng 1-2 lần/tuần).\n" +
                "\n" +
                "Lưu ý\n" +
                "- Chỉ dùng ngoài da. \n" +
                "- Tránh tiếp xúc trực tiếp với mắt. \n" +
                "- Rửa sạch ngay với nước nếu sản phẩm tiếp xúc trực tiếp với mắt. \n" +
                "- Ngưng sử dụng sản phẩm và tham khảo ngay ý kiến bác sĩ nếu có dấu hiệu bất thường. \n" +
                "- Tránh xa tầm tay trẻ em. \n" +
                "- Không sử dụng cho trẻ em dưới 3 tuổi.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "--\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "--\n" +
                "Xuất xứ: Hàn Quốc\n" +
                "Hạn sử dụng: 03 năm kể từ ngày sản xuất\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #taytebaochet #peeling");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/585027f559681d38fac9464b2a38d1dd");
        variant_1.setWeight(120.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(800));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_602334_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductInput createProductInput1 = new CreateProductInput();
        Product productInput = new Product();
        productInput.setName("Nước cân bằng làm sạch tế bào da chết innisfree Green Barley Peeling Toner 250ml");
        productInput.setShopId(602334);
        productInput.setTradeMarkId("1671551420753844");
        productInput.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productInput.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/8dbf984abddb1cfbd88fedd158b20c58");
        images.add("https://cf.shopee.vn/file/87f25622cf6e2307a98f70cf95361af1");
        images.add("https://cf.shopee.vn/file/7901c396a4c63a038992759fefdeab08");
        images.add("https://cf.shopee.vn/file/6ba9996ab0846e18d11dfc1d40cb8923");
        images.add("https://cf.shopee.vn/file/0301aeb6125d9c7af2a6c075a7aeb470");
        images.add("https://cf.shopee.vn/file/900ca46ca7f6f0a9d3110d64d522053d");
        productInput.setImageUrls(images);
        productInput.setFeaturedImageUrl("https://cf.shopee.vn/file/beb93ed4a0df465a3f474891d2daeb5c");
        productInput.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Nước cân bằng làm sạch tế bào da chết innisfree Green Barley Peeling Toner 250ml\n" +
                "\n" +
                "Nước cân bằng làm sạch tế bào da chết innisfree Green Barley Peeling Toner chiết xuất từ lúa mạch xanh giúp cân bằng độ ẩm và nhẹ nhàng loại bỏ tế bào chết mang lại làn da ẩm mềm, tươi mát.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thành phần & Công dụng\n" +
                "1. Năng lượng làm sạch hiệu quả từ lúa mạch xanh trên đảo Gapado gần Jeju\n" +
                "Lúa mạch xanh không hóa chất thu hoạch từ đảo Gapado chứa hàm lượng chất xơ và các thành phần dinh dưỡng khác (protein, vitamin, …) dồi dào hiệu quả trong việc làm sạch và sáng da.\n" +
                "Áp dụng phương pháp lên men 3 bước lúa mạch xanh tạo ra giấm lúa mạch xanh có tác dụng loại bỏ tế bào chết hiệu quả.\n" +
                "3 loại AHA có nguồn gốc thiên nhiên chiết xuất từ mầm lúa mạch xanh đảo Jeju giúp lấy đi tế bào chết hiệu quả cho làn da sạch mướt và tươi sáng\n" +
                "\n" +
                "2. Bổ sung độ ẩm và cân bằng da.\n" +
                "Công thức dạng nước phân tử nhỏ chứa Hyaluronic Acid giúp bổ sung độ ẩm nhanh chóng và cân bằng lượng dầu – nước mang lại làn da ẩm mềm, láng mịn, không gây nhờn dính.\n" +
                "\n" +
                "3. Loại bỏ tế bào chết dịu nhẹ\n" +
                "Chiết xuất từ lúa mạch xanh chứa AHA giúp nhẹ nhàng loại bỏ tế bào chết trên da, làm sạch bã nhờn dư thừa mang lại làn da sạch thoáng, láng mịn.\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "- Cho một lượng sản phẩm thích hợp lên miếng bông cotton và nhẹ nhàng lau đều toàn mặt. Vỗ nhẹ để tăng khả năng thẩm thấu.\n" +
                "- Với làn da bị mất nước, có thể dùng thay cho rửa mặt vào buổi sáng.\n" +
                "\n" +
                "Lưu ý\n" +
                "- Chỉ dùng ngoài da. \n" +
                "- Tránh tiếp xúc trực tiếp với mắt. \n" +
                "- Rửa sạch ngay với nước nếu sản phẩm tiếp xúc trực tiếp với mắt. \n" +
                "- Ngưng sử dụng sản phẩm và tham khảo ngay ý kiến bác sĩ nếu có dấu hiệu bất thường. \n" +
                "- Tránh xa tầm tay trẻ em.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Innisfree là thương hiệu chia sẻ những lợi ích của thiên nhiên từ hòn đảo Jeju tinh khiết, mang đến vẻ đẹp của cuộc sống xanh, thân thiện với môi trường nhằm bảo vệ cân bằng hệ sinh thái.\n" +
                "Mục tiêu innisfree muốn mang lại là vẻ đẹp khoẻ mạnh thực sự cho khách hàng thông qua những ưu đãi từ thiên nhiên.\n" +
                "--\n" +
                "Xuất xứ: Hàn Quốc\n" +
                "Hạn sử dụng: 03 năm kể từ ngày sản xuất\n" +
                "Công ty chịu trách nhiệm nhập khẩu và phân phối độc quyền: Công ty TNHH AmorePacific Việt Nam.\n" +
                "Mẫu mã bao bì sản phẩm sẽ thay đổi tùy vào đợt nhập hàng.\n" +
                "--\n" +
                "Cảm ơn bạn đã đồng hành cùng innisfree Việt Nam.\n" +
                "\n" +
                "#innisfree #innisfreeVietnam #chinhhang #toner");
        createProductInput1.setProduct(productInput);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/beb93ed4a0df465a3f474891d2daeb5c");
        variant_1.setWeight(250.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        try {
            productController.createProductVariant(createProductInput1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
