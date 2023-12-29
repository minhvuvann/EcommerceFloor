
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vn.mellow.ecom.EcommerceApiApplication;
import vn.mellow.ecom.base.exception.ServiceException;
import vn.mellow.ecom.base.logs.ActivityUser;
import vn.mellow.ecom.model.enums.*;
import vn.mellow.ecom.manager.GeoManager;
import vn.mellow.ecom.manager.ProductManager;
import vn.mellow.ecom.model.cart.CartDetail;
import vn.mellow.ecom.model.geo.Address;
import vn.mellow.ecom.model.geo.Geo;
import vn.mellow.ecom.model.industrial.IndustrialProduct;
import vn.mellow.ecom.model.input.*;
import vn.mellow.ecom.model.product.Product;
import vn.mellow.ecom.model.product.ProductDetail;
import vn.mellow.ecom.model.product.ProductVariant;
import vn.mellow.ecom.model.product.Trademark;
import vn.mellow.ecom.model.size.DimensionMeasurement;
import vn.mellow.ecom.model.user.DataRegister;
import vn.mellow.ecom.model.user.User;
import vn.mellow.ecom.restcontroller.*;
import vn.mellow.ecom.utils.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = EcommerceApiApplication.class)
@AutoConfigureMockMvc
public class EcommerceManagerApplicationTests {
    @Autowired
    private UserRestController userRestController;

    @Autowired
    private RegisterRestController registerRestController;

    @Autowired
    private ShopRestController shopRestController;

    @Autowired
    private ProductRestController productRestController;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private CartRestController cartRestController;

    @Autowired
    private GeoManager geoManager;

    private ActivityUser byUser;

    private List<String> fullNames = new ArrayList<String>();


    public EcommerceManagerApplicationTests() {
        this.byUser = new ActivityUser();
        byUser.setUserId("adminVVM");
        byUser.setUserName("adminTest001");
        byUser.setEmail("adminTest001@gmail.com");
        byUser.setPhone("0927382733");
        fullNames.add("Bùi Anh Tú");
        fullNames.add("Nguyễn Duy Sơn");
        fullNames.add("Nguyễn Thị Hoa");
        fullNames.add("Huỳnh Thị Nhi");
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
        fullNames.add("An Trường");
        fullNames.add("Noo Phước Thịnh");
        fullNames.add("ChiPu");
        fullNames.add("Thuỷ Tiên");
        fullNames.add("Ngọc Trinh");
        fullNames.add("Thuỷ Chi");
    }

    @Test
    public void testPrice() throws Exception {
        List<Product> products = productManager.getProductALLs();
        for (Product product : products) {
            int min = 5;
            int max = 50;
            int randomNumber = new Random().nextInt(max - min + 1) + min;
            productManager.updateDisCount(product.getId(), randomNumber);
            int min2 = 100000;
            int max2 = 5000000;

            // Tạo số nguyên ngẫu nhiên trong phạm vi từ 100,000 đến 5,000,000
            int rd2 = new Random().nextInt(max2 - min2 + 1) + min;
            int mediaPrice = new BigDecimal(rd2).setScale(-3, RoundingMode.FLOOR).intValue();
            productManager.updateMediumPrice(product.getId(), mediaPrice);
            int sale = DiscountUtils.calculateSalePrice(mediaPrice, randomNumber);
            productManager.updateSalePrice(product.getId(), (int) sale);
            product = productManager.getProduct(product.getId());
            ProductDetail detail = productManager.getProductDetail(
                    product.getId()
            );

            detail.getVariants().forEach(productVariant ->
            {
                productManager.updateVariantDisCount(productVariant.getId(), randomNumber);
                productManager.updateVariantPrice(productVariant.getId(), mediaPrice);
                productManager.updateVariantSalePrice(productVariant.getId(), sale);
            });
        }
    }

    @Test
    public void testUser() throws ServiceException {
        List<Trademark> trademarks = productRestController.getListTrademark();
        for (Trademark t : trademarks) {
            String fullName = fullNames.get(trademarks.indexOf(t));
            CreateUserDTO createUserDTO = new CreateUserDTO();
            UserDTO user = new UserDTO();
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
            address.setWardCode(Integer.parseInt(mapGeo.get("ward")));
            Geo geoWard = geoManager.getGeoGHN_ID(GeoType.WARD, Integer.parseInt(mapGeo.get("ward")));
            address.setWard(geoWard.getName());
            Geo geoDistrict = geoManager.getGeoGHN_ID(GeoType.DISTRICT, Integer.parseInt(mapGeo.get("district")));
            address.setDistrict(geoDistrict.getName());
            address.setDistrictCode(Integer.valueOf(mapGeo.get("district")));
            Geo geoProvince = geoManager.getGeoGHN_ID(GeoType.PROVINCE, Integer.valueOf(mapGeo.get("province")));
            address.setProvince(geoProvince.getName());
            address.setProvinceCode(Integer.valueOf(mapGeo.get("province")));
            user.setAddress(address);
            createUserDTO.setUser(user);
            KeyPasswordDTO password = new KeyPasswordDTO();
            password.setPasswordStatus(PasswordStatus.NEW);
            password.setPassword("MTIzNDU2");
            createUserDTO.setPassword(password);
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setRoleType(RoleType.PERSONAL);
            createUserDTO.setRole(roleDTO);
            DataRegister dataRegister;
            try {
                //Đăng kí tài khoản khách hàng (mua)
                dataRegister = (DataRegister) registerRestController.registerUser(createUserDTO).getData();
                HashMap<String, String> mapGeoShop = GeoUtils.generateGeo();
                Address addressShop = new Address();
                addressShop.setAddress1(mapGeoShop.get("address"));
                addressShop.setWardCode(Integer.parseInt(mapGeoShop.get("ward")));
                Geo geoWardShop = geoManager.getGeoGHN_ID(GeoType.WARD, Integer.valueOf(mapGeoShop.get("ward")));
                addressShop.setWard(geoWardShop.getName());
                Geo geoDistrictShop = geoManager.getGeoGHN_ID(GeoType.DISTRICT, Integer.valueOf(mapGeoShop.get("district")));
                addressShop.setDistrict(geoDistrictShop.getName());
                addressShop.setDistrictCode(Integer.parseInt(mapGeoShop.get("district")));
                Geo geoProvinceShop = geoManager.getGeoGHN_ID(GeoType.PROVINCE, Integer.valueOf(mapGeoShop.get("province")));
                addressShop.setProvince(geoProvinceShop.getName());
                addressShop.setProvinceCode(Integer.parseInt(mapGeoShop.get("province")));
                //Đăng kí tài khoản khách hàng (mua và bán)
                CreateShopDTO shopDTO = new CreateShopDTO();
                shopDTO.setWardCode(geoWardShop.getCode());
                shopDTO.setDistrict_id(geoDistrictShop.getGhn_id());
                shopDTO.setAddress(address.getAddress1());
                shopDTO.setAddressShop(addressShop);
                shopDTO.setPhone(NumberUtils.generateTelePhone());
                shopDTO.setName(t.getDescription());
                shopDTO.setDescription("Successful people do what unsuccessful people are not willing to do. Don’t wish it were easier; wish you were better.");
                shopDTO.setImageUrl(t.getIconUrl());

                shopRestController.createShop(dataRegister.getUserId(), shopDTO);

            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void test() {
        IndustrialProduct deck_4 = new IndustrialProduct();
        deck_4.setName("Thời Trang Nữ");
        deck_4.setDescription("Thời Trang Nữ");
        deck_4.setIconUrl("https://cf.shopee.vn/file/75ea42f9eca124e9cb3cde744c060e4d_tn");
        IndustrialProduct deck_2 = new IndustrialProduct();
        deck_2.setName("Thiết Bị Gia Dụng");
        deck_2.setDescription("Thiết Bị Gia Dụng");
        deck_2.setIconUrl("https://cf.shopee.vn/file/7abfbfee3c4844652b4a8245e473d857_tn");
        IndustrialProduct deck_3 = new IndustrialProduct();
        deck_3.setName("Thể Thao & Du Lịch");
        deck_3.setDescription("Thể Thao & Du Lịch");
        deck_3.setIconUrl("https://cf.shopee.vn/file/6cb7e633f8b63757463b676bd19a50e4_tn");
        IndustrialProduct deck_1 = new IndustrialProduct();
        deck_1.setName("Balo & Túi Ví Nam");
        deck_1.setDescription("Balo & Túi Ví Nam");
        deck_1.setIconUrl("https://cf.shopee.vn/file/18fd9d878ad946db2f1bf4e33760c86f_tn");
        IndustrialProduct deck_5 = new IndustrialProduct();
        deck_5.setName("Đồ Chơi");
        deck_5.setDescription("Đồ Chơi");
        deck_5.setIconUrl("https://cf.shopee.vn/file/ce8f8abc726cafff671d0e5311caa684_tn");
        IndustrialProduct deck_6 = new IndustrialProduct();
        deck_6.setName("Máy Ảnh & Máy Quay Phim");
        deck_6.setDescription("Máy Ảnh & Máy Quay Phim");
        deck_6.setIconUrl("https://cf.shopee.vn/file/ec14dd4fc238e676e43be2a911414d4d_tn");
        IndustrialProduct deck_7 = new IndustrialProduct();
        deck_7.setName("Mẹ & Bé");
        deck_7.setDescription("Mẹ & Bé");
        deck_7.setIconUrl("https://cf.shopee.vn/file/099edde1ab31df35bc255912bab54a5e_tn");
        IndustrialProduct deck_8 = new IndustrialProduct();
        deck_8.setName("Chăm Sóc Thú Cưng");
        deck_8.setDescription("Chăm Sóc Thú Cưng");
        deck_8.setIconUrl("https://cf.shopee.vn/file/cdf21b1bf4bfff257efe29054ecea1ec_tn");
        IndustrialProduct deck_9 = new IndustrialProduct();
        deck_9.setName("Voucher & Dịch Vụ");
        deck_9.setDescription("Voucher & Dịch Vụ");
        deck_9.setIconUrl("https://cf.shopee.vn/file/b0f78c3136d2d78d49af71dd1c3f38c1_tn");
        IndustrialProduct deck_10 = new IndustrialProduct();
        deck_10.setName("Giặt Giũ & Chăm Sóc Nhà Cửa");
        deck_10.setDescription("Giặt Giũ & Chăm Sóc Nhà Cửa");
        deck_10.setIconUrl("https://cf.shopee.vn/file/b0f78c3136d2d78d49af71dd1c3f38c1_tn");
        IndustrialProduct deck_11 = new IndustrialProduct();
        deck_11.setName("Túi Ví Nữ");
        deck_11.setDescription("Túi Ví Nữ");
        deck_11.setIconUrl("https://cf.shopee.vn/file/fa6ada2555e8e51f369718bbc92ccc52_tn");
        IndustrialProduct deck_12 = new IndustrialProduct();
        deck_12.setName("Giày Dép Nam");
        deck_12.setDescription("Giày Dép Nam");
        deck_12.setIconUrl("https://cf.shopee.vn/file/74ca517e1fa74dc4d974e5d03c3139de_tn");
        IndustrialProduct deck_13 = new IndustrialProduct();
        deck_13.setName("Giày Dép Nữ");
        deck_13.setDescription("Giày Dép Nữ");
        deck_13.setIconUrl("https://cf.shopee.vn/file/48630b7c76a7b62bc070c9e227097847_tn");
        try {
            deck_1 = productRestController.createIndustrialProduct(deck_1);
            deck_2 = productRestController.createIndustrialProduct(deck_2);
            deck_3 = productRestController.createIndustrialProduct(deck_3);
            deck_4 = productRestController.createIndustrialProduct(deck_4);
            deck_5 = productRestController.createIndustrialProduct(deck_5);
            deck_6 = productRestController.createIndustrialProduct(deck_6);
            deck_7 = productRestController.createIndustrialProduct(deck_7);
            deck_8 = productRestController.createIndustrialProduct(deck_8);
            deck_9 = productRestController.createIndustrialProduct(deck_9);
            deck_10 = productRestController.createIndustrialProduct(deck_10);
            deck_11 = productRestController.createIndustrialProduct(deck_11);
            deck_12 = productRestController.createIndustrialProduct(deck_12);
            deck_13 = productRestController.createIndustrialProduct(deck_13);


        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testIndustrialProduct() {
        for (IndustrialType type : IndustrialType.values()) {
            IndustrialProduct deck = new IndustrialProduct();
            deck.setName(type.getDescription());
            deck.setDescription(type.getDescription());
            deck.setIconUrl(type.getUrl());
            try {
                deck = productRestController.createIndustrialProduct(deck);
                if (type.equals(IndustrialType.ELECTRON)) {
                    for (TrademarkTypeElectron electron : TrademarkTypeElectron.values()) {
                        Trademark trademark = new Trademark();
                        trademark.setIndustrialId(deck.getId());
                        trademark.setName(electron.getDescription());
                        trademark.setDescription(electron.getDescription());
                        trademark.setIconUrl(electron.getUrl());
                        try {
                            trademark = productRestController.createTrademarkProduct(trademark);
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
                            trademark = productRestController.createTrademarkProduct(trademark);
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
                            trademark = productRestController.createTrademarkProduct(trademark);
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
                            trademark = productRestController.createTrademarkProduct(trademark);
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
                            trademark = productRestController.createTrademarkProduct(trademark);
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
                            trademark = productRestController.createTrademarkProduct(trademark);
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
                            trademark = productRestController.createTrademarkProduct(trademark);
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
            cartDetail = cartRestController.getCartDetail("1671610208305328");

        } catch (ServiceException e) {
            e.printStackTrace();
        }

        assertNotNull(cartDetail != null);
    }

    @Test
    public void testProduct() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Apple Watch Series 7 45mm GPS Sport Band");
        productDTO.setShopId(802541);
        productDTO.setTradeMarkId("1703772512359063");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/decaf47f7cb7a2df555dbff0deeed698");
        images.add("https://cf.shopee.vn/file/a5dc7787667f07f10c26456c04a51d34");
        images.add("https://cf.shopee.vn/file/5fa46c2de37a681744f74bc59ed5005e");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/c42123a453771f56de2c938a6ee8af6d");
        productDTO.setDescription("Trải nghiệm hoàn hảo trên màn hình lớn tràn viền\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //apple store
    @Test
    public void testProduct_802541_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple Watch Series 7 45mm GPS Sport Band");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/decaf47f7cb7a2df555dbff0deeed698");
        images.add("https://cf.shopee.vn/file/a5dc7787667f07f10c26456c04a51d34");
        images.add("https://cf.shopee.vn/file/5fa46c2de37a681744f74bc59ed5005e");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/c42123a453771f56de2c938a6ee8af6d");
        //thêm mô tả
        productDTO.setDescription("Trải nghiệm hoàn hảo trên màn hình lớn tràn viền\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple AirPods with Charging ");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/74f393af9467db98f825d50c3c71304c_tn");
        images.add("https://cf.shopee.vn/file/77fcdcf6e091441846fb0f9cae6439a5_tn");
        images.add("https://cf.shopee.vn/file/1553529b098197d0493e41cde92c011e_tn");
        images.add("https://cf.shopee.vn/file/cff5850e5abc59ab782c8c2f88ce45e7_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/a256a89d3b7b4b80224e768d9ff6fdff_tn");
        //thêm mô tả
        productDTO.setDescription("Với thời gian đàm thoại tối ưu, công nghệ đột phá và kết hợp cùng Hộp Sạc, AirPods đem đến trải nghiệm \n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple Watch Series 3 38mm GPS Sport Band");

        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/baa6bf0f0db1fe474a5ab815ec189f25_tn");
        images.add("https://cf.shopee.vn/file/4b1df878ee257bd895c0b6b0e8bdd5d2_tn");
        images.add("https://cf.shopee.vn/file/80c2d5fac406bff413bfda3c8f31eca2_tn");
        images.add("https://cf.shopee.vn/file/b229b6c671e963d5ed9f3954b860d02f_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/982a277b97e49732507d9d68341267c6_tn");
        //thêm mô tả
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple iPhone 14 128GB");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/39c9bb82bbf0dbe7d573ebec7c0bbfac_tn");
        images.add("https://cf.shopee.vn/file/8b299a01bed582fe9b285405833dff97_tn");
        images.add("https://cf.shopee.vn/file/ef7e86e59bc3f59667abe75b2cb523b4_tn");
        images.add("https://cf.shopee.vn/file/5d40817b8aa13d6685a825e06cc9d932_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/0982de1d517eed28495a9bbcaced5881_tn");
        //thêm mô tả
        productDTO.setDescription("IPhone 14. Với hệ thống camera kép tiên tiến nhất từng có trên iPhone. Chụp những bức ảnh tuyệt đẹp \n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple iPhone 14 128GB");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/3af324bc17f502e8b1898fd5a2b01844_tn");
        images.add("https://cf.shopee.vn/file/b7addff24c257ae552ee31f16a23af7c_tn");
        images.add("https://cf.shopee.vn/file/bdda15455bac30f5bc2b404752bbe3ba_tn");
        images.add("https://cf.shopee.vn/file/198720e37eeccfcdc98c6ea87f5decaa_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/93b3f6d4799b06f9279f817540daa4d1_tn");
        //thêm mô tả
        productDTO.setDescription("Điện thoại iPhone 14 Pro sở hữu trọng lượng 206g cùng thiết kế nhỏ gọn cho khả năng cầm nắm thoải mái. \n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple Macbook Air (2022) M2 chip, 13.6 inches, 8GB, 256GB SSD");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/b9245889043f87930168547032a5843e_tn");
        images.add("https://cf.shopee.vn/file/b09430e3c8c8dba7b6dc87d25cd8cb0e_tn");
        images.add("https://cf.shopee.vn/file/d3ebf604bcc0fcc5420cd0e680ef501a_tn");
        images.add("https://cf.shopee.vn/file/26b018e5decbecf17fb8f72117f8ea10_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/1cba156175a5ef0b0d356b52451b4c42_tn");
        //thêm mô tả
        productDTO.setDescription("Apple Macbook Air (2022) M2 chip  (năm SX 2022), 13.6 inches, 8GB, 256GB SSD\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple AirPods Pro");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a188dbde7bf8b549535fb5b2dc99902e_tn");
        images.add("https://cf.shopee.vn/file/dd89f8691815c4b874ffb3772fa0f62f_tn");
        images.add("https://cf.shopee.vn/file/883547817583605ccfb1fb925a1f950e_tn");
        images.add("https://cf.shopee.vn/file/89a436849bfefddcd542dfba60ec955a_tn");
        images.add("https://cf.shopee.vn/file/82037f8c1b7dfdcfb543072e8c2f5b85_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/5b0ee8b80d604dd93e2b77278b770928_tn");
        //thêm mô tả
        productDTO.setDescription("Tính năng Chủ Động Khử Tiếng Ồn tạo ra âm thanh sống động. Chế Độ Xuyên Âm giúp bạn nghe và kết nối \n" +
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
        createProductDTO1.setProduct(productDTO);
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple iPad Gen 9th 10.2-inch Wi-Fi 64GB");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/b46eef299808bc1751af1b5e9dce6cde_tn");
        images.add("https://cf.shopee.vn/file/5e1b81fa44eb632009b2eeec30edef2f_tn");
        images.add("https://cf.shopee.vn/file/ae538aa64c0ce480fd22bb5f0e8430d7_tn");
        images.add("https://cf.shopee.vn/file/4869283ee2d5359f3faadacf22bcc19b_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/38a51fee63a623600932f1aea78d76a9_tn");
        //thêm mô tả
        productDTO.setDescription("Mạnh mẽ. Dễ sử dụng. Đa năng. iPad mới có màn hình Retina tuyệt đẹp, chip A13 Bionic mạnh mẽ, \n" +
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
        createProductDTO1.setProduct(productDTO);
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple iPhone 11 64GB");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/3af324bc17f502e8b1898fd5a2b01844_tn");
        images.add("https://cf.shopee.vn/file/b7addff24c257ae552ee31f16a23af7c_tn");
        images.add("https://cf.shopee.vn/file/bdda15455bac30f5bc2b404752bbe3ba_tn");
        images.add("https://cf.shopee.vn/file/198720e37eeccfcdc98c6ea87f5decaa_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/93b3f6d4799b06f9279f817540daa4d1_tn");
        //thêm mô tả
        productDTO.setDescription("Lưu ý: Các sản phẩm sản xuất sau tháng 10/2020 sẽ không có củ sạc và tai nghe trong bộ sản phẩm.\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802541_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Apple Macbook Pro (2022) M2 chip, 13.3 inches, 8GB, 512GB SSD");
        //mã shop sửa lại
        productDTO.setShopId(802541);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512359063");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/6f65b108bcdcabae207d8255360e6f61_tn");
        images.add("https://cf.shopee.vn/file/7492878c607c83190a1c33f74fea076d_tn");
        images.add("https://cf.shopee.vn/file/decaf47f7cb7a2df555dbff0deeed698_tn");
        images.add("https://cf.shopee.vn/file/a5dc7787667f07f10c26456c04a51d34_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/a5e087409cca78fb6bdd281da177ff88_tn");
        //thêm mô tả
        productDTO.setDescription("Apple Macbook Pro (2022) M2 chip, 13.3 inches, 8GB, 512GB SSD\n" +
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
        createProductDTO1.setProduct(productDTO);
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //Vinfastore --------------------
    @Test
    public void testProduct_802555_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe máy điện VinFast Feliz S");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0a7642dc53b2a96509f5998a705b51ea_tn");
        images.add("https://cf.shopee.vn/file/4e1236dade3e74c798b01b64f990c2d5_tn");
        images.add("https://cf.shopee.vn/file/75cfbb02cbc1894ae434afd56db0e942_tn");
        images.add("https://cf.shopee.vn/file/843a3bd801e25b3a63f6df3d349249b1_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/953e2723509b9d4941e2a9b6daeb6597_tn");
        //thêm mô tả
        productDTO.setDescription("Giá niêm yết Feliz S: 29,900,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe máy điện VinFast Theon S");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a479458db484ed58d9fa8f0ffa64b8a2_tn");
        images.add("https://cf.shopee.vn/file/47606d82a6c1ddd096d1d8a326cc52fc_tn");
        images.add("https://cf.shopee.vn/file/254a7d52641844f889b7ec65cd4a70dd_tn");
        images.add("https://cf.shopee.vn/file/636d97db0e0982406e9a76f2036a7421_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/37c3fa447dd076c8503ed496bd97d37e_tn");
        //thêm mô tả
        productDTO.setDescription("Giá niêm yết Theon S: 69,900,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe máy điện VinFast Vento S 2022");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/c70249b922f372ac563b7f8c5cc65179_tn");
        images.add("https://cf.shopee.vn/file/097aa132cd572fe6e22f8adb703f5da9_tn");
        images.add("https://cf.shopee.vn/file/75cfbb02cbc1894ae434afd56db0e942_tn");
        images.add("https://cf.shopee.vn/file/140cd1faa9925d3787c8ded7a6fabda5_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/ec75dab542a10958575c26c15f99ac4e_tn");
        //thêm mô tả
        productDTO.setDescription("Giá niêm yết Vento S: 56,000,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe máy điện VinFast Klara S (2022)");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/cdece7d22791617fbbb8d7b5f3407136_tn");
        images.add("https://cf.shopee.vn/file/2ac450d37a7e1e8f74d797bcb56179b1_tn");
        images.add("https://cf.shopee.vn/file/df928df3782c04806254be40c4d55f50_tn");
        images.add("https://cf.shopee.vn/file/a5e661fc72d5478445e647f8c54fe225_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/3778e7fecc28f0879ed9655d61cc34e0_tn");
        //thêm mô tả
        productDTO.setDescription("Giá niêm yết Klara S (2022): 36,900,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +

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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe máy điện VinFast Ludo");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/3c8c8a8d155314743e1eab895bad2abc_tn");
        images.add("https://cf.shopee.vn/file/ef7633e1d0f1b2cc5e72d816ce098cf4_tn");
        images.add("https://cf.shopee.vn/file/e3f67fb013276465bfe16387a7755562_tn");
        images.add("https://cf.shopee.vn/file/96ade7c891c63a01f27b251a48d7a7d7_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/c71e0a97e0236c7ca7f1350ebad34f47_tn");
        //thêm mô tả
        productDTO.setDescription("Giá bán xe đã bao gồm VAT, đã bao gồm sạc và chưa bao gồm pin, lệ phí thuế trước bạ, làm giấy tờ, biển số.\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("VinFast - Xe máy điện Impes Red");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/13f47dcf9115a7d7666b7ea6b1f35650_tn");
        images.add("https://cf.shopee.vn/file/799f2bac92b3b08cc405d06684dd8bca_tn");
        images.add("https://cf.shopee.vn/file/4d12c80fbeaf66d918368b01a5017c8b_tn");
        images.add("https://cf.shopee.vn/file/681fcbc7ffc943236add3fb6dc0865d4_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/6a1357a8f75e74349acacd4418eaefaa_tn");
        //thêm mô tả
        productDTO.setDescription("Giá bán đã bao gồm VAT, bao gồm sạc và không bao gồm pin, phí đăng ký, đăng kiểm, biển số, lệ phí trước bạ.\n" +
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
        createProductDTO1.setProduct(productDTO);
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Motor Điện VinFast EVO 200");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0a7642dc53b2a96509f5998a705b51ea_tn");
        images.add("https://cf.shopee.vn/file/4e1236dade3e74c798b01b64f990c2d5_tn");
        images.add("https://cf.shopee.vn/file/75cfbb02cbc1894ae434afd56db0e942_tn");
        images.add("https://cf.shopee.vn/file/843a3bd801e25b3a63f6df3d349249b1_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/953e2723509b9d4941e2a9b6daeb6597_tn");
        //thêm mô tả
        productDTO.setDescription("Giá niêm yết EVO 200: 22,000,000 VNĐ (giá đã bao gồm VAT, 1 bộ sạc và không bao gồm pin).\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe motor điện VinFast Theon");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/bbcb49babefa093103ee24ef0cc9a63b_tn");
        images.add("https://cf.shopee.vn/file/8590194cd9414ce66134a5b14d26ec80_tn");
        images.add("https://cf.shopee.vn/file/11d547c80f64493da0c4fd1c80c00cdb_tn");
        images.add("https://cf.shopee.vn/file/ee486b342fcbb8c0caad2c2b46df1438_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/d43ee70b8d602964f17c9724b2b36e38_tn");
        //thêm mô tả
        productDTO.setDescription("\uD83D\uDD31 Thông tin chung:\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe máy điện VinFast Klara A2 - 2021");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/211d03dbff65d40fd94d57d54ba32a57_tn");
        images.add("https://cf.shopee.vn/file/7ec2baeef5092b2805e45b64ab75d35a_tn");
        images.add("https://cf.shopee.vn/file/f7edf6e9c2c736a779f1ac6ae277a899_tn");
        images.add("https://cf.shopee.vn/file/bb3f3a5b33da4e33e27b835d76c95362_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/8f80fc07aa046ab6ca62c29dcff620d3_tn");
        //thêm mô tả
        productDTO.setDescription("\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802555_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe máy điện VinFast Ludo Mint To Be - Phiên bản giới hạn");
        //mã shop sửa lại
        productDTO.setShopId(802555);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512375917");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/ee82a7088fbb45c8147feb6dbca080f6_tn");
        images.add("https://cf.shopee.vn/file/3dd12a5700320cd2770645d20477595b_tn");
        images.add("https://cf.shopee.vn/file/e06118487edeb7a0900bd7b4603bd55e_tn");
        images.add("https://cf.shopee.vn/file/075c3b4725104a87fad2d50e4fb4ecda_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/15b7bfa4253d613966fabffb7bc0c5e3_tn");
        //thêm mô tả
        productDTO.setDescription("➡️Giá bán xe đã bao gồm VAT, đã bao gồm sạc & pin & Không bao gồm lệ phí thuế trước bạ, làm giấy tờ, biển số.\n" +
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
        createProductDTO1.setProduct(productDTO);
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


        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //Yamaha
    @Test
    public void testProduct_802556_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Máy Yamaha NVX 155 ABS VVA");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/97269174e6606f9219cbed9155ce5b5d_tn");
        images.add("https://cf.shopee.vn/file/921543f18e57726d32bdf821bd32c600_tn");
        images.add("https://cf.shopee.vn/file/937fb199a71873b79d982a0e417cd8be_tn");
        images.add("https://cf.shopee.vn/file/9b96c2d057ef61e4218d342b99be548f_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/aa0643920f4780e3e916bdcb7850ccf6_tn");
        //thêm mô tả
        productDTO.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Máy Yamaha Latte Phiên Bản Chuẩn");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-55p67jeo9mjv00_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-s4sl9e1bq3kva9_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-lhelif1bq3kv3b_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/aa0643920f4780e3e916bdcb7850ccf6_tn");
        //thêm mô tả
        productDTO.setDescription("Yamaha Grande là mẫu xe tay ga tiết kiệm xăng số 1 Việt Nam với mức tiêu thụ chỉ 1,69 lít/100km, \n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Máy YAMAHA Janus ");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-diounf5ji8ivcf_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-yx2ubh5ji8ivc2_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-n8vynf5ji8iv2f_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-0uzojrmzonivd8_tn");
        //thêm mô tả
        productDTO.setDescription("Giá bán xe đã bao gồm thuế VAT, chưa bao gồm thuế trước bạ, biển số, \n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe máy Yamaha Janus Phiên Bản Giới Hạn");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/970938f58e68e37ddbeeaffe2ba7bcc6_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-e0z1qa0h9mjvb6_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-hdrwoc0h9mjv2b_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/73f1f6763965aabf62c0f7cc9398a8b3_tn");
        //thêm mô tả
        productDTO.setDescription("Yamaha Grande là mẫu xe tay ga tiết kiệm xăng số 1 Việt Nam với mức tiêu thụ chỉ 1,69 lít/100km, \n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Máy Yamaha Grande Phiên Bản Giới Hạn");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/4c5ea8bc9c8bbfbae25c96f83ca59fdf_tn");
        images.add("https://cf.shopee.vn/file/77d27b55eb2eda0d3b188f0b478471ac_tn");
        images.add("https://cf.shopee.vn/file/2c586c78f19bae7bc35f69a2ae5610bf_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/509cf4e0d80675cb95cae3ea41e6c3f4_tn");
        //thêm mô tả
        productDTO.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Máy Yamaha Exciter 150 Phiên Bản Gp và Rc");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/7803aa9c3e0d2c64d6f8f9bc5f4f2925_tn");
        images.add("https://cf.shopee.vn/file/020f9b4704b1366331c3ffa780cee8b7_tn");
        images.add("https://cf.shopee.vn/file/043df65b14e76f116a3f5e4bd1520f48_tn");

        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/b11e3fcd26b8d03818387c6e15ab41af_tn");
        //thêm mô tả
        productDTO.setDescription("Yamaha Grande là mẫu xe tay ga tiết kiệm xăng số 1 Việt Nam với mức tiêu thụ chỉ 1,69 lít/100km, \n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Máy Yamaha MT15  ");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/f804603ed99d44755cd288d69e181fb6_tn");
        images.add("https://cf.shopee.vn/file/c3f97d1cfcd6c61f9268e4df12245d62_tn");
        images.add("https://cf.shopee.vn/file/f8b5e3f82631338c344f32412cf2dc6d_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/54f2a3a01d31df919c1ee5ec924dc121_tn");
        //thêm mô tả
        productDTO.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
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
        createProductDTO1.setProduct(productDTO);
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Máy Yamaha Sirius Phanh Cơ");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/4c5ea8bc9c8bbfbae25c96f83ca59fdf_tn");
        images.add("https://cf.shopee.vn/file/77d27b55eb2eda0d3b188f0b478471ac_tn");
        images.add("https://cf.shopee.vn/file/2c586c78f19bae7bc35f69a2ae5610bf_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/509cf4e0d80675cb95cae3ea41e6c3f4_tn");
        //thêm mô tả
        productDTO.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe yamaha YZF-R15-V4 ");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-eis02rk4ohiv6a_tn");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-sus73rcrmuiv82_tn");

        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-eis02rk4ohiv6a_tn");
        //thêm mô tả
        productDTO.setDescription("Yamaha Grande là mẫu xe tay ga tiết kiệm xăng số 1 Việt Nam với mức tiêu thụ chỉ 1,69 lít/100km, \n" +
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
        createProductDTO1.setProduct(productDTO);
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802556_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Xe Máy Yamaha Sirius Vành Đúc");
        //mã shop sửa lại
        productDTO.setShopId(802556);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512376880");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/d8f4c895a58b8a01039ab6b829998190_tn");
        images.add("https://cf.shopee.vn/file/793bc35b35e52a4886a283fc7b1a0405_tn");
        images.add("https://cf.shopee.vn/file/69820f0835581834e1f684c357c8d13b_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/c67245663241a658918b11dc79445023_tn");
        //thêm mô tả
        productDTO.setDescription("Loại        Blue Core, 4 thì, 4 van, SOHC, làm mát bằng dung dịch\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //Inistree
    @Test
    public void testProduct_802544_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Phấn phủ bột kiềm dầu innisfree No Sebum Mineral Powder 5g");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/8a0c878f58e08c86a127040ac89bb63d");
        images.add("https://cf.shopee.vn/file/66057aee11843ae46ea38458f022453c");
        images.add("https://cf.shopee.vn/file/c69e636e738288be7fb3248253aa93b9");
        images.add("https://cf.shopee.vn/file/2f45bd462cd17aac30582d22946c545c");
        images.add("https://cf.shopee.vn/file/e74f80862cbd52df6a16e4d65a4c7a8c");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-vuwhm6d3salv21");
        productDTO.setDescription("PHẤN PHỦ BỘT KIỀM DẦU INNISFREE NO SEBUM MINERAL POWDER 5G\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem dưỡng da tay innisfree Jeju Life Perfumed Hand Cream 30ml");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0eae41afd7c614d5c7cf33e2f5de0a24");
        images.add("https://cf.shopee.vn/file/e55a17b5d0a73c919f0f1d509b237bb9");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/8c603c56d978cf5fe557545bb226baff");
        productDTO.setDescription("Kem dưỡng da tay innisfree Jeju Life Perfumed Hand Cream 30ml\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Mặt nạ innisfree My Real Squeeze Mask 20ml");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/7a965de5f7973b690724dbbeba4e98cb");
        images.add("https://cf.shopee.vn/file/24555648e8707398cc4b513c912dc9c3");
        images.add("https://cf.shopee.vn/file/259aa948870d92e272e741168fdf5298");
        images.add("https://cf.shopee.vn/file/0bd0302033765d5df371f3f9b8109f45");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-0e8fdlsl83iv4a");
        productDTO.setDescription("Mặt nạ innisfree My Real Squeeze Mask 20ml\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem dưỡng ẩm sáng da innisfree Cherry Blossom Tone Up Cream 50ml");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/2320796afe1a33e86d1621e0ccd9e60f");
        images.add("https://cf.shopee.vn/file/26d222e0e42ee310149142bf0acc694d");
        images.add("https://cf.shopee.vn/file/e26152fd98df94fff515a3fd816d00f5");
        images.add("https://cf.shopee.vn/file/798e1a8263f7dae0f0b807a9ae57925d");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-80ysxexje6kv7a");
        productDTO.setDescription("Kem dưỡng ẩm sáng da innisfree Cherry Blossom Tone Up Cream 50ml\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa rửa mặt dành cho da mụn innisfree Bija Trouble Facial Foam 150g");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/d97f9b0fdf880a3a9f72aab08869421e");
        images.add("https://cf.shopee.vn/file/253b6b4b1af4923fe61bd7bda237d4d3");
        images.add("https://cf.shopee.vn/file/7c5109c8b0298450ba1bb545853ff3ee");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-ia7mb1f4e6kv8b");
        productDTO.setDescription("Sữa rửa mặt dành cho da mụn innisfree Bija Trouble Facial Foam 150g\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Mặt nạ lột mụn đầu đen innisfree Jeju Volcanic Nose Pack 1 Miếng");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0b95f700dff9fba598b5074f6e0ac180");
        images.add("https://cf.shopee.vn/file/d627dae05cbae67486c615b5059482a3");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/ddb18f1f06bc188a31395525f5a0e49d");
        productDTO.setDescription("Mặt nạ lột mụn đầu đen innisfree Jeju Volcanic Nose Pack 1 Miếng\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem lót kiềm dầu làm mờ lỗ chân lông innisfree No Sebum Blur Primer 25ml");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/f9621851e40429c20b209966deced0df");
        images.add("https://cf.shopee.vn/file/0600e6c0f46ceda952453d3b223d9f51");
        images.add("https://cf.shopee.vn/file/0b25b1b9ce96ece6bbad343df283de23");
        images.add("https://cf.shopee.vn/file/babde95ab07ab6986ac4868d21d3d932");
        images.add("https://cf.shopee.vn/file/d011f3e709a41ab4b380f87a09bb5ed1");
        images.add("https://cf.shopee.vn/file/42670eb64085334b6308c9b6e7461a94");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/1c4200a6646886acac0750a6f0aea696");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa rửa mặt làm sáng và sạch da innisfree Brightening Pore Facial Cleanser 150ml");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/781b62e50f6a3de125ed6f0e32802523");
        images.add("https://cf.shopee.vn/file/f968e10c54259f170d3b53450eae90b7");
        images.add("https://cf.shopee.vn/file/98a998633ee1a1f18ff3367204a35d8c");
        images.add("https://cf.shopee.vn/file/7c129599dfe90b5c625516b180d575f1");
        images.add("https://cf.shopee.vn/file/7c1ca026bf38183aa2beee81ea61d15c");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-24oalix8e6kv05");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Mặt nạ tẩy tế bào da chết innisfree Green Barley Gommage Mask 120ml");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/585024f559681d38fac9464b2a38d1dd");
        images.add("https://cf.shopee.vn/file/cb09d6fa95cb1cc13643dca518a732cc");
        images.add("https://cf.shopee.vn/file/efd15cc5e928325c365160c9fe1af0d3");
        images.add("https://cf.shopee.vn/file/c4fbdda25c85a29c8ffc6c52f66b7993");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/585027f559681d38fac9464b2a38d1dd");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802544_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Nước cân bằng làm sạch tế bào da chết innisfree Green Barley Peeling Toner 250ml");
        productDTO.setShopId(802544);
        productDTO.setTradeMarkId("1703772512363815");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/8dbf984abddb1cfbd88fedd158b20c58");
        images.add("https://cf.shopee.vn/file/87f25622cf6e2307a98f70cf95361af1");
        images.add("https://cf.shopee.vn/file/7901c396a4c63a038992759fefdeab08");
        images.add("https://cf.shopee.vn/file/6ba9996ab0846e18d11dfc1d40cb8923");
        images.add("https://cf.shopee.vn/file/0301aeb6125d9c7af2a6c075a7aeb470");
        images.add("https://cf.shopee.vn/file/900ca46ca7f6f0a9d3110d64d522053d");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/beb93ed4a0df465a3f474891d2daeb5c");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
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
        createProductDTO1.setProduct(productDTO);
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
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //anessa
    @Test
    public void testProduct_802545_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sáp dưỡng ẩm Vaseline Pure Petrolium Jelly 50ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/8c4d0f06751b214338531cec8c83edae");
        images.add("https://cf.shopee.vn/file/7232e39b43a09ec0365443cb5fdc0269");
        images.add("https://cf.shopee.vn/file/ebdef43fcd3c2c5287ad4a916aaeb42a");
        images.add("https://cf.shopee.vn/file/b8d61b30c7074285c9fdd83a8c3b5113");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-spsp60j525kv6e");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sáp dưỡng ẩm Vaseline Pure Petrolium Jelly được xem là kem chống nẻ, kem dưỡng ẩm số 1 tại Mỹ, với lượng tiêu thụ lớn. Sản phẩm  được làm từ dầu khoáng tự nhiên, cô đặc lại dưới dạng sáp (jelly) mềm, tan ở nhiệt độ cơ thể, giúp cải thiện tình trạng bong tróc, khô nứt nẻ của da, tạo mối liên kết giữa các tế bào da khỏe mạnh và săn chắc, giúp da mềm mại, mịn màng.\n" +
                "\n" +
                "THÔNG TIN CHI TIẾT\n" +
                "\n" +
                "Công dụng:\n" +
                "\n" +
                "- Nhờ có khả năng dưỡng ẩm tuyệt vời, Vaseline sẽ là trợ thủ đắc lực giúp bạn giải quyết vùng da bị khô, bong tróc, nứt nẻ hoặc bị cháy nắng trong bất kỳ thời tiết nào.\n" +
                "\n" +
                "- Dưỡng môi ngày và đêm, giảm nứt khô môi do cơ thể thiếu nước hoặc tác động của thời tiết nóng, lạnh đồng thời cung cấp độ ẩm. \n" +
                "\n" +
                "- Loại bỏ da khô, chết lâu ngày, cải tạo da cho đôi môi sáng hơn, mềm mịn, không vết nứt.\n" +
                "\n" +
                "- Làm son lót nền, giúp cách li da môi với hóa chất chì của son môi màu lì trang điểm, đồng thời giúp son bám màu lâu hơn và môi mềm tự nhiên\n" +
                "\n" +
                "\n" +
                "\n" +
                "Đặc điểm riêng:\n" +
                "\n" +
                "- Vaseline cũng có tác dụng dưỡng móng, chỉ cần bôi một chút Vaseline sẽ giúp móng tay của chúng ta trông bóng, khoẻ hơn trông thấy. Ngoài ra, bạn cũng có thể thử dùng hỗn hợp kem đánh răng, Vaseline và nước cốt chanh khi cần tẩy màu móng cũ.\n" +
                "\n" +
                "- Ngoài ra sản phẩm còn có những tác dụng không ngờ tới như: dùng làm kem bôi khi tẩy lông, tẩy trang cho vùng mắt, rửa mặt, tẩy tế bào chết, dưỡng da, kem lót, hạn chế tóc khô và chẻ ngọn, tạo dáng cho lông mày, giữ mùi hương nước hoa\n" +
                "\n" +
                "- Thiết kế hũ nhỏ xinh dễ dàng mang theo khi đi làm, du lịch\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng:\n" +
                "\n" +
                "- Dùng hàng ngày, thoa đều lên phần da cần dưỡng ẩm hoặc da khô / bong tróc.\n" +
                "\n" +
                "- Dùng được cho toàn thân và môi.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ: India\n" +
                "\n" +
                "Nơi sản xuất: India\n" +
                "\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                "Bảo quản nơi khô ráo, thoáng mát\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Thương hiệu Vaseline ra đời từ năm 1870. Hiện nay thương hiệu Vaseline đang được quản lý bởi tập đoàn Unilever với các dòng sản phẩm: kem dưỡng da, nước hoa, chất làm sạch, chất khử mùi, sáp dưỡng ẩm... Bắt đầu nổi tiếng bằng sản phẩm sáp Vaseline do Robert Chesebrough tìm ra sau khi ông ghé thăm một mỏ dầu. Từ đó thương hiệu này đã làm một cuộc cách mạng và được sử dụng khắp mọi nơi trên toàn thế giới. Các sản phẩm chăm sóc da của thương hiệu Vaseline hiện có mặt ở 60 nước trên toàn thế giới và được bày bán ở hệ thống các siêu thị trên cả nước.\n" +
                "\n" +
                "Tại Việt Nam, bên cạnh VASELINE, Unilever còn nổi tiếng với những thương hiệu lớn như: St.Ives, Simple, Clear, Dove, Close-up, Omo, Surf và nhiều sản phẩm chất lượng cao khác,... Với hơn 500 công ty tại 90 quốc gia trên thế giới, Unilever luôn mang lại những sản phẩm tốt nhất cho người tiêu dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-spsp60j525kv6e");
        variant_1.setWeight(50.0);
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
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sáp Dưỡng Môi Hồng Xinh Vaseline Lip Therapy Rosy Lip 7g");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/34ddea973d10ad74298ca58246df041e");
        images.add("https://cf.shopee.vn/file/6baa5259cc7e016fbbddb081e214e12d");
        images.add("https://cf.shopee.vn/file/e8f604681c238e74c19dbdb47a431cdb");
        images.add("https://cf.shopee.vn/file/81ec6108dbbc20de33f8d43e6fc3ad7c");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-hjeoj3w725kvc3");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sáp Dưỡng Môi Hồng Xinh Vaseline Lip với được chiết xuất từ mỡ khoáng tinh khiết 100% - tức là loại mỡ khoáng đã được thanh lọc nên rất an toàn cho mọi loại da, có thể sử dụng cho đôi môi nhạy cảm. Chỉ cần 1 lớp mỏng để qua đêm bạn đã có được đôi môi mềm mượt, căng mọng đầy bất ngờ.\n" +
                "\n" +
                "THÔNG TIN CHI TIẾT\n" +
                "Công dụng chính:\n" +
                "- Son dưỡng môi sẽ lập tức làm mềm và mượt môi khô nứt nẻ và để lại một màu hồng nhẹ trên môi. \n" +
                "- Khóa ẩm giúp đôi môi môi mọng, khỏe, và giảm thiểu sự khó chịu.\n" +
                "- Son có hương hoa hồng nhẹ.\n" +
                "- Dùng như son lót, giúp cách li da môi với hóa chất son môi trang điểm, giúp son bám màu lâu hơn.\n" +
                "\n" +
                "*Điểm đặc biệt riêng:\n" +
                "- Vaseline cũng có tác dụng dưỡng móng, chỉ cần bôi một chút Vaseline sẽ giúp móng tay của chúng ta trông bóng, khoẻ hơn trông thấy. Ngoài ra, bạn cũng có thể thử dùng hỗn hợp kem đánh răng, Vaseline và nước cốt chanh khi cần tẩy màu móng cũ.\n" +
                "- Ngoài ra sản phẩm còn có những tác dụng dùng làm kem bôi khi tẩy lông, tẩy trang cho vùng mắt, rửa mặt, tẩy tế bào chết, dưỡng da, kem lót, hạn chế tóc khô và chẻ ngọn, tạo dáng cho lông mày, giữ mùi hương nước hoa,...\n" +
                "- Thiết kế hũ nhỏ xinh dễ dàng mang theo khi đi làm, du lịch,...\n" +
                "\n" +
                "Hướng dẫn sử dụng:\n" +
                "- Dùng hàng ngày, thoa đều lên phần da cần dưỡng ẩm hoặc da khô / bong tróc.\n" +
                "- Dùng được cho toàn thân và môi.\n" +
                "\n" +
                "Xuất xứ: Mỹ\n" +
                "Nơi sản xuất: Hàn Quốc\n" +
                "Ngày sản xuất: xem trên bao bì sản phẩm\n" +
                "Hạn sử dụng: 03 năm kể từ ngày sản xuất\n" +
                "Bảo quản nơi khô ráo, thoáng mát\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "Thương hiệu Vaseline ra đời từ năm 1870. Hiện nay thương hiệu Vaseline đang được quản lý bởi tập đoàn Unilever với các dòng sản phẩm: kem dưỡng da, nước hoa, chất làm sạch, chất khử mùi, sáp dưỡng ẩm... Bắt đầu nổi tiếng bằng sản phẩm sáp Vaseline do Robert Chesebrough tìm ra sau khi ông ghé thăm một mỏ dầu. Từ đó thương hiệu này đã làm một cuộc cách mạng và được sử dụng khắp mọi nơi trên toàn thế giới. Các sản phẩm chăm sóc da của thương hiệu Vaseline hiện có mặt ở 60 nước trên toàn thế giới và được bày bán ở hệ thống các siêu thị trên cả nước.\n" +
                "Tại Việt Nam, bên cạnh VASELINE, Unilever còn nổi tiếng với những thương hiệu lớn như: St.Ives, Simple, Clear, Dove, Close-up, Omo, Surf và nhiều sản phẩm chất lượng cao khác,... Với hơn 500 công ty tại 90 quốc gia trên thế giới, Unilever luôn mang lại những sản phẩm tốt nhất cho người tiêu dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-hjeoj3w725kvc3");
        variant_1.setWeight(7.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(550));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa tắm dưỡng da St.Ives 650ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/7f89513e18ab099bed44db67478036b2");
        images.add("https://cf.shopee.vn/file/a4a4be07b54ee2e2ed231af0692087aa");
        images.add("https://cf.shopee.vn/file/66cb3f19b3253d410e86715baa0bd5e2");
        images.add("https://cf.shopee.vn/file/0b97d1b55b31153c155cd6218cdef1c0");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-gk1ueiwf25kv30");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sữa tắm dưỡng da St.Ives là dòng sản phẩm rất được ưa chuộng của thương hiệu chăm sóc da St.Ives nổi tiếng xuất xứ từ Mỹ, với công thức được chiết xuất từ các thành phần thiên nhiên, giúp làm sạch nhẹ nhàng đồng thời bổ sung dưỡng chất nuôi dưỡng làn da khỏe mạnh và sáng mịn rạng ngời.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "\n" +
                "\n" +
                "1. Sữa tắm dưỡng da St.Ives Yến Mạch và Bơ: \n" +
                "\n" +
                "công thức dưỡng ẩm 100% từ thiên nhiên (lúa mạch và bơ shea) giúp làn da được làm sạch và dưỡng ẩm tối ưu, trả lại vẻ sáng ngời tự nhiên. Hương thơm ngọt ngào từ Yến Mạch và Bơ đánh thức giác quan của bạn và lưu lại lâu trên da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "2. Sữa tắm dưỡng da St.Ives Muối Biển:\n" +
                "\n" +
                " thành phần tẩy tế bào chết 100% từ thiên nhiên (muối biển và khoáng thiên nhiên) giúp nhẹ nhàng tẩy tế bào chết cho cơ thể, đồng thời cung cấp các dưỡng chất và vitamin giúp lấy lại vẻ sáng mịn và mềm mại vốn có của làn da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "3. Sữa tắm dưỡng da St.Ives Cam Chanh:\n" +
                "\n" +
                " hạt tẩy tế bào chết 100% chiết xuất từ thiên nhiên (Chanh đào và Cam quýt) giúp nhẹ nhàng tẩy sạch khuyết điểm trên làn da, giúp làn da sáng mịn, đều màu. Hương thơm Cam Chanh giúp đánh thức các giác quan, mang lại hương thơm dễ chịu trên da. Sữa tắm giàu độ ẩm với các bọt kem làm sạch và nhiều mùi hương tự nhiên thỏa mãn các giác quan.\n" +
                "\n" +
                "\n" +
                "\n" +
                "4. Sữa tắm dưỡng da St.Ives Hoa Hồng & Lô Hội:\n" +
                "\n" +
                "Công thức làm sạch 100% từ cánh hoa hồng thiên nhiên mang đến cảm giác tươi mới cho làn da, trả lại vẻ mịn màng, rạng ngời, êm ái như những cánh hoa hồng. Hương thơm nhẹ nhàng đặc biệt từ hoa hồng và lô hội đánh thức mọi giác quan. \n" +
                "\n" +
                "\n" +
                "\n" +
                "*Hướng dẫn sử dụng:\n" +
                "\n" +
                "- Cho sản phẩm trực tiếp lên da ướt hoặc bông tắm để tạo bọt, mát xa nhẹ nhàng toàn thân rồi tắm lại với nước\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Hướng dẫn bảo quản:\n" +
                "\n" +
                "- Chỉ sử dụng ngoài da.\n" +
                "\n" +
                "- Để xa tầm tay trẻ em.\n" +
                "\n" +
                "- Không sử dụng sản phẩm đã hết hạn.\n" +
                "\n" +
                "- Không để sản phẩm trực tiếp dưới nắng.\n" +
                "\n" +
                "- Rửa ngay với nước nếu sản phẩm rơi vào mắt.\n" +
                "\n" +
                "- Ngưng sử dụng và hỏi ý kiến bác sĩ da liễu nếu da bị kích ứng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ: Mỹ\n" +
                "\n" +
                "Nơi sản xuất: Mỹ\n" +
                "\n" +
                "NSX: Xem trên bao bì\n" +
                "\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "St.Ives là thương hiệu trực thuộc tập đoàn Unilever của Mỹ, ra đời từ năm 1980 và nổi tiếng với những sản phẩm chăm sóc sức khỏe làn da hiệu quả từ các thành phần thiên nhiên như: Sữa rửa mặt tẩy tế bào chết, Sữa dưỡng thể trẻ hóa da, Kem chống nắng, Mặt nạ,… St.Ives cam kết mang lại cho bạn những trải nghiệm chăm sóc da hiệu quả và hợp túi tiền với tiêu chí sử dụng nguyên liệu tươi tốt, chiết xuất 100% từ thiên nhiên, không có hóa chất, được chị em phụ nữ khắp thế giới biết đến và tin dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-gk1ueiwf25kv30");
        variant_1.setWeight(650.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1200));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Son dưỡng môi Vaseline Stick 4.8g");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a6ac84ddbc9ad5d941bd4fac54e6a132");
        images.add("https://cf.shopee.vn/file/9d66d36943b9ddcc5476a27b3682174e");
        images.add("https://cf.shopee.vn/file/009f35a4ce23f26ccbd376bd2278b49c");
        images.add("https://cf.shopee.vn/file/5f97f1ca9d657fb8a5d2109541c0a2c7");
        images.add("https://cf.shopee.vn/file/bc97faa6c3efa10fae10ad07f7a6fabd");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-0dmhj4n625kv89");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Son dưỡng môi Vaseline là một sản phẩm lý tưởng dành riêng cho việc dưỡng môi. Sản phẩm có hương thơm trái cây tự nhiên, nhẹ nhàng, thành phần chính làm từ mỡ khoáng tinh khiết 100%, mật ong, các vitamin và hoàn toàn lành tính. Đôi môi của bạn sẽ luôn mềm mịn, đầy quyến rũ. Son dưỡng Vaseline có thể được dùng như son lót, trước khi trang điểm môi, giúp cách li da môi với hóa chất son môi, giúp son bám màu lâu và căng mọng một cách tự nhiên.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Son dưỡng môi Vaseline Stick có 4 loại được ưa chuộng:\n" +
                "\n" +
                "- Son dưỡng môi Vaseline Mềm Mịn với thành phần Petroleum jelly cùng Vitamin E giúp cung cấp cho môi của bạn độ ẩm cần thiết để luôn khỏe mạnh, căng bóng tự nhiên.\n" +
                "\n" +
                "- Son dưỡng môi Vaseline Hồng Xinh với thành phần Petroleum jelly cùng chiết xuất dầu hạnh nhân và dầu hoa hồng giúp đôi môi mềm mại, mượt mà, sáng hồng nhẹ nhàng và tươi trẻ.\n" +
                "\n" +
                "- Son dưỡng môi Vaseline Lô Hội với thành phần Petroleum jelly cùng chiết xuất nha đam/ lô hội giúp đôi môi luôn mượt mà và căng đầy sức sống.\n" +
                "\n" +
                "- Son dưỡng môi Vaseline Bơ Cacao với thành phần Petroleum jelly cùng chiết xuất Cacao và bơ hạt mỡ giúp đôi môi luôn mượt mà và căng đầy sức sống.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN CHI TIẾT\n" +
                "\n" +
                "Công dụng chính:\n" +
                "\n" +
                "- Son dưỡng môi sẽ lập tức làm mềm và mượt môi khô nứt nẻ và để lại một màu hồng nhẹ trên môi. \n" +
                "\n" +
                "- Khóa ẩm giúp đôi môi môi mọng, khỏe, và giảm thiểu sự khó chịu.\n" +
                "\n" +
                "- Dùng như son lót, giúp cách li da môi với hóa chất son môi trang điểm, giúp son bám màu lâu hơn.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Cách sử dụng:\n" +
                "\n" +
                "- Sử dụng cho môi khi thấy môi khô căng và nứt nẻ. Thích hợp sử dụng dưỡng môi hàng ngày.\n" +
                "\n" +
                "- Sử dụng dưỡng môi khi trang điểm. Dùng như son lót, giúp cách li da môi với hóa chất son môi trang điểm, giúp son bám màu lâu hơn.\n" +
                "\n" +
                "- Dùng dưỡng môi ngày và đêm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ: Hàn Quốc\n" +
                "\n" +
                "Nơi sản xuất: Hàn Quốc\n" +
                "\n" +
                "Ngày sản xuất: xem trên bao bì sản phẩm\n" +
                "\n" +
                "Hạn sử dụng: 03 năm kể từ ngày sản xuất\n" +
                "\n" +
                "Bảo quản nơi khô ráo, thoáng mát\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Thương hiệu Vaseline ra đời từ năm 1870. Hiện nay thương hiệu Vaseline đang được quản lý bởi tập đoàn Unilever với các dòng sản phẩm: kem dưỡng da, nước hoa, chất làm sạch, chất khử mùi, sáp dưỡng ẩm... Bắt đầu nổi tiếng bằng sản phẩm sáp Vaseline do Robert Chesebrough tìm ra sau khi ông ghé thăm một mỏ dầu. Từ đó thương hiệu này đã làm một cuộc cách mạng và được sử dụng khắp mọi nơi trên toàn thế giới. Các sản phẩm chăm sóc da của thương hiệu Vaseline hiện có mặt ở 60 nước trên toàn thế giới và được bày bán ở hệ thống các siêu thị trên cả nước.\n" +
                "\n" +
                "Tại Việt Nam, bên cạnh VASELINE, Unilever còn nổi tiếng với những thương hiệu lớn như: St.Ives, Simple, Clear, Dove, Close-up, Omo, Surf và nhiều sản phẩm chất lượng cao khác,... Với hơn 500 công ty tại 90 quốc gia trên thế giới, Unilever luôn mang lại những sản phẩm tốt nhất cho người tiêu dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-0dmhj4n625kv89");
        variant_1.setWeight(5.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(400));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa dưỡng thể St.Ives Yến Mạch và Bơ 621ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/c2a7605ccd67fe5d3895db20854b8ee3");
        images.add("https://cf.shopee.vn/file/6113afdf420198b157715f520fa8d51c");
        images.add("https://cf.shopee.vn/file/d73f0d1eeacba35ac2e60f30f7e086c9");
        images.add("https://cf.shopee.vn/file/7acf49da1d4ca4cf03da6827b17f37bc");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-z9cijlvj25kvf6");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sữa dưỡng thể toàn thân trẻ hóa da St.Ives với 100% thành phần thiên nhiên kết hợp cùng công thức thẩm thấu nhanh và không nhờn, sản phẩm giúp tăng cường độ ẩm, phục hồi da tươi khỏe suốt cả ngày. Cùng hương thơm quyến rũ, sữa dưỡng thể ST. IVES Body Lotion giúp bạn trở lên ngọt ngào, quyến rũ và làm tan chảy cảm xúc của bất kì ai.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Sữa Dưỡng Thể ST.IVES Yến Mạch Và Bơ với công thức dưỡng ẩm 100% từ thiên nhiên ( yến mạch và bơ hạt mỡ)  thẩm thấu nhanh. Làn da được phục hồi, trả lại vẻ sáng ngời tự nhiên. Sử dụng mỗi ngày để duy trì nước trên bề mặt da cho da mềm mịn và sáng hơn.\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Thành phần và công dụng:\n" +
                "\n" +
                "- Bột yến mạch: làm mềm da khô ráp, tẩy sạch bụi bẩn trên da\n" +
                "\n" +
                "- Bơ hạt mỡ (Shea Butter): chứa Vitamin A, E, F và các axit béo giúp cung cấp nước cho da khô\n" +
                "\n" +
                "- Dưỡng ẩm từ Glycerin\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Hướng dẫn sử dụng:\n" +
                "\n" +
                "- Thoa đều sản phẩm lên toàn thân mỗi tối và sáng.\n" +
                "\n" +
                "- Sử dụng mỗi ngày để duy trì độ ẩm cho da, nên dùng sản phẩm ngay sau khi tắm để đạt hiệu quả dưỡng da tối ưu.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ: Mỹ\n" +
                "\n" +
                "Nơi sản xuất: Mỹ\n" +
                "\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                "Bảo quản nơi khô ráo, thoáng mát\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "St.Ives là thương hiệu trực thuộc tập đoàn Unilever của Mỹ, ra đời từ năm 1980 và nổi tiếng với những sản phẩm chăm sóc sức khỏe làn da hiệu quả từ các thành phần thiên nhiên như: Sữa rửa mặt tẩy tế bào chết, Sữa dưỡng thể trẻ hóa da, Kem chống nắng, Mặt nạ,… St.Ives cam kết mang lại cho bạn những trải nghiệm chăm sóc da hiệu quả và hợp túi tiền với tiêu chí sử dụng nguyên liệu tươi tốt, chiết xuất 100% từ thiên nhiên, không có hóa chất, được chị em phụ nữ khắp thế giới biết đến và tin dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-z9cijlvj25kvf6");
        variant_1.setWeight(621.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1300));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa rửa mặt tẩy tế bào da chết St.Ives 170g (Tươi Mát Trái Mơ)");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/8a1bd8da830e0dae7e1bb71dd24aac2d");
        images.add("https://cf.shopee.vn/file/58bd970751d38167ae056049f498595b");
        images.add("https://cf.shopee.vn/file/9a02979bc4e1a354ef0b47728c63419e");
        images.add("https://cf.shopee.vn/file/7489213dcbf9d1a682a002427853ae35");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-ulkt0k9h25kvd0");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Bao bì sản phẩm thay đổi theo lô nhập hàng.\n" +
                "Sữa Rửa Mặt Tẩy Tế Bào Chết đến từ thương hiệu St.Ives giúp nhẹ nhàng loại bỏ lớp tế bào chết sần sùi, mang lại cho bạn làn da sáng mịn, mềm mại và rạng rỡ. Sữa rửa mặt được chiết xuất từ thành phần tự nhiên với các mùi hương dịu nhẹ, nhẹ nhàng làm sạch lỗ chân lông.\n" +
                "\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM                                                                                                                                                                                    \n" +
                "\n" +
                "1. Sữa rửa mặt tẩy tế bào chết ngừa mụn St.Ives chiết xuất Trái Mơ: Nhẹ nhàng làm sạch bụi bẩn, lấy đi những tế bào chết là nguyên nhân làm bít lỗ chân lông gây ra mụn và các vấn đề về da. Giúp tẩy da chết, làm sạch sâu mà không gây kích ứng da, cho bạn làn da tươi mới, mịn màng và tràn đầy sức sống, ngăn ngừa sản sinh mụn.\n" +
                "\n" +
                "\n" +
                "\n" +
                "2. Sữa rửa mặt tẩy tế bào chết tươi mát St.Ives chiết xuất Trái Mơ: Giúp làn da sáng mịn, ngăn ngừa sản sinh mụn. Sau khi sử dụng bạn sẽ cảm nhận được làn da mềm mại và tươi mát.\n" +
                "\n" +
                "\n" +
                "\n" +
                "3. Sữa rửa mặt tẩy tế bào chết St.Ives Cafe và Dừa: Bổ sung độ ẩm và làm mềm da, tăng cường dưỡng chất cho da thêm sáng khỏe, hạn chế mẫn cảm, sưng đỏ do mụn gây ra, giúp da khỏe mạnh, mịn màng, trắng sáng rạng rỡ hơn, sạch mụn.\n" +
                "\n" +
                "\n" +
                "\n" +
                "4. Sữa rửa mặt tẩy tế bào chết ngừa mụn St.Ives Trà Xanh: Nhẹ nhàng làm sạch mụn đầu đen và làm dịu da ửng đỏ, tổn thương do mụn. Thanh lọc da, làm giảm kích ứng và mẩn đỏ do mụn gây ra, giúp da khỏe mạnh, mịn màng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "5. Sữa rửa mặt tẩy tế bào chết Bơ và Mật Ong St.Ives Soft Skin Avocado & Honey Scrub 170g là giải pháp tuyệt vời giúp làm sạch da một cách nhẹ nhàng nhất, nhưng cũng đồng thời loại bỏ tạp chất và bả nhờn trên da sau một ngày dài, mang đến cho bạn cảm giác nhẹ nhàng, tươi mới.\n" +
                "\n" +
                "\n" +
                "\n" +
                "6. Sữa rửa mặt tẩy tế bào chết ngừa mụn St.Ives chiết xuất Hoa Hồng & Lô Hội: Nhẹ nhàng lấy đi lớp tế bào chết cứng đầu, mang lại cho bạn làn da mịn màng, sáng xinh như những cánh hoa hồng. Sản phẩm có chứa 100% bột óc chó tự nhiên cùng bộ cánh hoa hồng nghiền mịn, tất cả thành phần được làm từ thiên nhiên an toàn cho mọi làn da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thành phần:\n" +
                "\n" +
                "- 100% chiết xuất từ thành phần tự nhiên, nuôi dưỡng làn da. \n" +
                "\n" +
                "- Chứa salicylic acid giúp ngăn ngừa mụn và làm thu nhỏ lỗ chân lông\n" +
                "\n" +
                "- Giúp tẩy da chết, làm sạch sâu mà không gây kích ứng da\n" +
                "\n" +
                "- Không chứa paraben (chất bảo quản gây nguy cơ ung thư)\n" +
                "\n" +
                "- Không chứa sulfate\n" +
                "\n" +
                "\n" +
                "\n" +
                "HƯỚNG DẪN SỬ DỤNG\n" +
                "\n" +
                "Bước 1. Bóp một lượng vừa đủ Sữa rửa mặt tẩy tế bào da chết St.Ives lên đầu ngón tay của bạn và mát xa lên vùng da ẩm ướt.\n" +
                "\n" +
                "Bước 2. Trải theo chuyển động tròn nhỏ, tạo áp lực nhẹ nhàng để đánh thức sự lưu thông tự nhiên của làn da của bạn.\n" +
                "\n" +
                "Bước 3. Thoa đều khắp khuôn mặt, ngay sát chân tóc và hai bên mũi.\n" +
                "\n" +
                "Bước 4. Khi bạn đã hoàn tất, rửa sạch và lau khô bằng khăn sạch.\n" +
                "\n" +
                "Bước 5. Sử dụng 3 đến 4 lần một tuần (tùy thuộc vào làn da) để có kết quả như mong đợi.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ: Mỹ\n" +
                "\n" +
                "Nơi sản xuất: Mỹ\n" +
                "\n" +
                "Hạn sử dụng: 36 tháng kể từ ngày sản xuất\n" +
                "\n" +
                "Bảo quản nơi khô ráo, thoáng mát\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "St.Ives là thương hiệu trực thuộc tập đoàn Unilever của Mỹ, ra đời từ năm 1980 và nổi tiếng với những sản phẩm chăm sóc sức khỏe làn da hiệu quả từ các thành phần thiên nhiên như: Sữa rửa mặt tẩy tế bào chết, Sữa dưỡng thể trẻ hóa da, Kem chống nắng, Mặt nạ,… St.Ives cam kết mang lại cho bạn những trải nghiệm chăm sóc da hiệu quả và hợp túi tiền với tiêu chí sử dụng nguyên liệu tươi tốt, chiết xuất 100% từ thiên nhiên, không có hóa chất, được chị em phụ nữ khắp thế giới biết đến và tin dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-ulkt0k9h25kvd0");
        variant_1.setWeight(170.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(800));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa dưỡng thể St.Ives Yến Mạch và Bơ/ Vitamin E và Bơ/ Collagen 621ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/583194d52f1e4cb4d9b46e0d2a5f9b66");
        images.add("https://cf.shopee.vn/file/82df64bdfc9dc9db438744ef8818a71e");
        images.add("https://cf.shopee.vn/file/2df88c4165debf28595c2be9f428b4ca");
        images.add("https://cf.shopee.vn/file/c2a7605ccd67fe5d3895db20854b8ee3");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-b54ifgmi25kva5");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sữa dưỡng thể toàn thân trẻ hóa da St.Ives với 100% thành phần thiên nhiên kết hợp cùng công thức thẩm thấu nhanh và không nhờn, sản phẩm giúp tăng cường độ ẩm, phục hồi da tươi khỏe suốt cả ngày. Cùng hương thơm quyến rũ, sữa dưỡng thể ST. IVES Body Lotion giúp bạn trở lên ngọt ngào, quyến rũ và làm tan chảy cảm xúc của bất kì ai.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "1. Sữa dưỡng thể St.Ives Yến Mạch & Bơ với công thức dưỡng ẩm 100% từ thiên nhiên ( yến mạch và bơ hạt mỡ)  thẩm thấu nhanh. Làn da được phục hồi, trả lại vẻ sáng ngời tự nhiên. Sử dụng mỗi ngày để duy trì nước trên bề mặt da cho da mềm mịn và sáng hơn.\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Thành phần và công dụng:\n" +
                "\n" +
                "- Bột yến mạch: làm mềm da khô ráp, tẩy sạch bụi bẩn trên da.\n" +
                "\n" +
                "- Bơ hạt mỡ (Shea Butter): chứa Vitamin A, E, F và các axit béo giúp cung cấp nước cho da khô.\n" +
                "\n" +
                "- Dưỡng ẩm từ Glycerin.\n" +
                "\n" +
                "\n" +
                "\n" +
                "2. Sữa dưỡng thể St.Ives Vitamin E & Bơ với 100% thành phần thiên nhiên (vintamin E chống lão hóa) công thức thẩm thấu nhanh và không nhờn tăng cường vitamin E chống lão hóa, ngăn ngừa khô da, cải thiện làn da tươi khỏe trông thấy.\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Thành phần và công dụng:\n" +
                "\n" +
                "- Quả bơ xanh nổi tiếng với khả năng cung cấp độ ẩm.\n" +
                "\n" +
                "- Vitamin E giúp dưỡng ẩm và chống lão hóa\n" +
                "\n" +
                "- Thành phần dưỡng ẩm từ thiên nhiên (glycerin)\n" +
                "\n" +
                "\n" +
                "\n" +
                "3. Sữa dưỡng thể St.Ives Trẻ Hóa Da Collagen với công thức dưỡng ẩm 100% từ thiên nhiên (Collagen, proteins và glycerin) thẩm thấu nhanh vào da, giúp tái sinh làn da, da lấy lại độ đàn hồi, căng tràn đầy sức sống chỉ trong 7 ngày.\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Thành phần và công dụng:\n" +
                "\n" +
                "- Collagen và các sợi đàn hồi  protein\n" +
                "\n" +
                "- Bơ hạt mỡ (Shea Butter)\n" +
                "\n" +
                "- Dưỡng ẩm từ Glycerin giúp tái tạo độ đàn hồi cho da, cho làn da trẻ trung, khỏe mịn\n" +
                "\n" +
                "\n" +
                "\n" +
                "HƯỚNG DẪN SỬ DỤNG:\n" +
                "\n" +
                "- Thoa đều sản phẩm lên toàn thân mỗi tối và sáng.\n" +
                "\n" +
                "- Sử dụng mỗi ngày để duy trì độ ẩm cho da, nên dùng sản phẩm ngay sau khi tắm để đạt hiệu quả dưỡng da tối ưu.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ: Mỹ\n" +
                "\n" +
                "Nơi sản xuất: Mỹ\n" +
                "\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                "Bảo quản nơi khô ráo, thoáng mát\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "St.Ives là thương hiệu trực thuộc tập đoàn Unilever của Mỹ, ra đời từ năm 1980 và nổi tiếng với những sản phẩm chăm sóc sức khỏe làn da hiệu quả từ các thành phần thiên nhiên như: Sữa rửa mặt tẩy tế bào chết, Sữa dưỡng thể trẻ hóa da, Kem chống nắng, Mặt nạ,… St.Ives cam kết mang lại cho bạn những trải nghiệm chăm sóc da hiệu quả và hợp túi tiền với tiêu chí sử dụng nguyên liệu tươi tốt, chiết xuất 100% từ thiên nhiên, không có hóa chất, được chị em phụ nữ khắp thế giới biết đến và tin dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-b54ifgmi25kva5");
        variant_1.setWeight(621.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1400));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa tắm Monsavon chiết xuất sữa và hoa vani 1000ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/2e43b840d95c90ae148db432437c41e2");
        images.add("https://cf.shopee.vn/file/0d39cd671db710245278da40d0951ebf");
        images.add("https://cf.shopee.vn/file/069748d6364b8b15d40ddb792db8cbc8");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-xmi0jko335kv55");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sữa tắm Monsavon chiết xuất sữa và hoa vani nâng niu làn da đến mềm mịn tuyệt vời với công thức giữ ẩm vượt trội cùng tinh chất sữa. Hương hoa quyến rũ từ Pháp giúp trôi đi bao căng thẳng , mang đến sự thư giãn cho cơ thể bạn sau một ngày dài làm việc mệt mỏi.\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Công dụng:\n" +
                "\n" +
                "- Sản phẩm tạo bọt nhẹ nhàng, lấy đi bụi bẩn đồng thời bổ sung dưỡng chất và độ ẩm để làn da bạn luôn mịn màng, đặc biệt không bị nhờn rít sau khi sử dụng.\n" +
                "\n" +
                "- Hương thơm dịu nhẹ, đem đến những phút giây thư giãn đồng thời lưu hương lâu trên da giúp phái đẹp quyến rũ, gợi cảm hơn.\n" +
                "\n" +
                "- Thiết kế dạng chai có vòi chắc chắn, tiện lợi giúp bạn dễ dàng sử dụng trong thời gian dài.\n" +
                "\n" +
                "- Với tinh chất sữa và chiết xuất hoa va ni tự nhiên, Monsavon ngọt ngào đánh thức mọi giác quan, mang lại làn da mềm mại, mịn màng với hương thơm từ sữa và vani.\n" +
                "\n" +
                "- Không chứa paraben (chất bảo quản gây nguy cơ ung thư) Không chứa chất tạo màu.\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Hướng dẫn sử dụng:\n" +
                "\n" +
                "Làm ướt cơ thể và cho một lượng sữa tắm vào lòng bàn tay hoặc bông tắm để tạo bọt. Mát xa da nhẹ nhàng và tắm sạch lại với nước. Sử dụng hàng ngày.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Ấn Độ\n" +
                "\n" +
                "Hạn sử dụng: 2 năm kể từ ngày sản xuất\n" +
                "\n" +
                "Bảo quản nơi khô ráo, thoáng mát\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Monsavon là thương hiệu mỹ phẩm của Pháp có từ năm 1925, nay thuộc công ty đa quốc gia Unilever. Sữa tắm Monsavon được hàng triệu phụ nữ Pháp và châu Âu yêu dùng bởi mùi hương đặc biệt quyến rũ. Với slogan “Thức tỉnh mọi giác quan\", sữa tắm Monsavon được sản xuất theo một quy trình khoa học nhất và đạt chuẩn về chất lượng đủ để chị em thể hiện sự quyến rũ, gợi cảm của phụ nữ theo một cách làm đẹp thật đơn giản, ít tốn kém và cực an toàn cho làn da.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-xmi0jko335kv55");
        variant_1.setWeight(1000.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1500));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Combo 3 Xà bông tắm Camay Naturel (125gx3)");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/53d1e53fda091d604979bbaa209aa89e");
        images.add("https://cf.shopee.vn/file/91bb86536a6d0336d064fb4a58e2ad46");
        images.add("https://cf.shopee.vn/file/a2d30f0fecf28bbe4a7ba54973a1da71");
        images.add("https://cf.shopee.vn/file/e32ed5e13c71965c69629e0bb7a869fe");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/6356cd91d2dbf85bc71086e5e204d6f8");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\\n\" +\n" +
                "                \"Kem dưỡng dạng gel sữa dành cho da mụn với tác động kép Vichy Normaderm Phytosolution Double-Correction Daily Care 50ml có thành phần hoạt tính hoàn toàn từ thiên nhiên, an toàn cho mọi loại da kể cả da nhạy cảm. Kết cấu kem mỏng nhẹ, thẩm thấu nhanh vào da, tác động trực tiếp vào nguyên nhân gây nên mụn trứng cá, làm giảm mụn, mụn trứng cá và  các đốm nâu sau mụn. Sản phẩm cũng cung cấp nước, độ ẩm, tái tạo củng cố hàng rào bảo vệ da, cải thiện sắc tố da mang đến làn da sáng mịn, đều màu. Lần đầu tiên, bạn sẽ tìm thấy một dòng kem hỗ trợ giảm mụn không hề nặng nề cho da, giúp việc loại bỏ mụn dễ dàng hơn bao giờ hết.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Loại sản phẩm\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Kem dưỡng dạng keo dành cho da mụn\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Loại da phù hợp\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Mọi loại da, nhất là da mụn, da nhạy cảm.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Độ an toàn\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Thành phần hoàn toàn từ thiên nhiên \\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Không parapen, không alcohol\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Được kiểm nghiệm da liễu\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Không gây kích ứng, không gây mụn\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Độ PH [5,5]\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- An toàn cho da nhạy cảm.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Thành phần:\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- 60% Nước khoáng Vichy: Củng cố khả năng phòng vệ tự nhiên của lớp hàng rào bảo vệ da, tăng cường sức đề kháng và các đặc tính chống oxy hóa tự nhiên\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Natural Hyaluronic Acid (Chiết xuất lúa mì): Giữ ẩm cho da, giảm tình trạng mất nước qua da\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Bifidus Probiotic (chiết xuất từ men vi sinh) : Củng cố và tái cấu trúc hàng rào bảo vệ da và tình trạng khô da\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"-  Natural Salicylic Acid (Winter Green Leaf): Giảm sừng nang lông tuyến bã\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Phyco Saccharide (Chiết xuất từ tảo): Ức chế sự tích tụ Lipid ở nang lông, giảm sản xuất bã nhờn, nguyên nhân hình thành mụn\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Vitamin Cg (dẫn xuất từ Vitamin C): Giảm mụn, đốm nâu sau mụn trứng cá\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Công dụng\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Tác động chuyên biệt với mụn và các vấn đề về da:\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Giảm mụn, ngăn mụn tái phát\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Giảm thâm hiệu quả\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Tác động đối với tổng thể làn da:\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Cải thiện đáng kể tình trạng da và sắc da, cho da sáng mịn đều màu\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Dưỡng ẩm 24h, giúp da trở nên trong suốt bóng mượt\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Thu nhỏ lỗ chân lông\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Tái tạo và củng cố hàng rào bảo vệ da\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Hiệu quả:\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Sau 8 tuần sử dụng:\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Giảm mụn cám 37%\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Giảm mụn 42%\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Giảm đốm thâm sâu mụn trứng cá 62%\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Thu nhỏ lỗ chân lông 8%\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Da sáng mịn đều màu hơn 26%\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Hướng dẫn sử dụng\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Sử dụng sáng và tối, sau bước làm sạch da mặt \\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Lấy một lượng sản phẩm vừa đủ cỡ hạt đậu, chấm đều lên 5 điểm: trán, mũi, cằm và 2 bên má rồi nhẹ nhàng massage theo hình tròn, hướng lên trên (tránh thoa lên vùng mắt)\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"- Có thể sử dung làm lớp nền trang điểm   \\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"THÔNG TIN THƯƠNG HIỆU\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Xuất xứ thương hiệu: Pháp\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Nơi sản xuất: Pháp\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Hạn sử dụng: 3 năm kể từ ngày sản xuất \\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Ngày sản xuất: In trên bao bì\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"Thành phần: Xem chi tiết trên bao bì\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"\\n\" +\n" +
                "                \"#Vichy #skincare #serum");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/6356cd91d2dbf85bc71086e5e204d6f8");
        variant_1.setWeight(375.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(800));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Combo 2 chai Sữa tắm dưỡng da St.Ives Yến mạch và Bơ 473ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/9ae8b42f96be97bb7028098f5c2815dd");
        images.add("https://cf.shopee.vn/file/84977258dede3882ffdf04ddf2a90271");
        images.add("https://cf.shopee.vn/file/4777e7902c4b12a49568deb24f14fae5");
        images.add("https://cf.shopee.vn/file/361cd9a8acaa0cc981386fb50d538bee");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-x64lcszn25kve8");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "BỘ SẢN PHẨM BAO GỒM\n" +
                "02 x Sữa tắm dưỡng da St.Ives Yến mạch và Bơ 473ml \n" +
                "\n" +
                "THÔNG TIN CHI TIẾT\n" +
                "Sữa tắm dưỡng da St.Ives là dòng sản phẩm rất được ưa chuộng của thương hiệu chăm sóc da St.Ives nổi tiếng xuất xứ từ Mỹ, với công thức được chiết xuất từ các thành phần thiên nhiên, giúp làm sạch nhẹ nhàng đồng thời bổ sung dưỡng chất nuôi dưỡng làn da khỏe mạnh và sáng mịn rạng ngời.\n" +
                "\n" +
                "Sữa tắm dưỡng da St.Ives Yến Mạch và Bơ với công thức dưỡng ẩm 100% từ thiên nhiên (lúa mạch và bơ shea) giúp làn da được làm sạch và dưỡng ẩm tối ưu, trả lại vẻ sáng ngời tự nhiên. Hương thơm ngọt ngào từ Yến Mạch và Bơ đánh thức giác quan của bạn và lưu lại lâu trên da.\n" +
                "\n" +
                "*Công dụng:\n" +
                "- Bột yến mạch: làm mềm da khô ráp, tẩy sạch bụi bẩn trên da\n" +
                "- Bơ hạt mỡ (Shea Butter): chứa Vitamin A, E, F và các axit béo giúp cung cấp nước cho da khô\n" +
                "- Dưỡng ẩm và giúp da mịn màng từ tinh dầu của quả mơ\n" +
                "- Dưỡng ẩm từ Glycerin \n" +
                "\n" +
                "*Hướng dẫn sử dụng:\n" +
                "- Cho sản phẩm trực tiếp lên da ướt hoặc bông tắm để tạo bọt, mát xa nhẹ nhàng toàn thân rồi tắm lại với nước\n" +
                "\n" +
                "*Hướng dẫn bảo quản:\n" +
                "- Chỉ sử dụng ngoài da.\n" +
                "- Để xa tầm tay trẻ em.\n" +
                "- Không sử dụng sản phẩm đã hết hạn.\n" +
                "- Không để sản phẩm trực tiếp dưới nắng.\n" +
                "- Rửa ngay với nước nếu sản phẩm rơi vào mắt.\n" +
                "- Ngưng sử dụng và hỏi ý kiến bác sĩ da liễu nếu da bị kích ứng.\n" +
                "\n" +
                "Xuất xứ: Mỹ\n" +
                "Nơi sản xuất: Mỹ\n" +
                "NSX: Xem trên bao bì\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "St.Ives là thương hiệu trực thuộc tập đoàn Unilever của Mỹ, ra đời từ năm 1980 và nổi tiếng với những sản phẩm chăm sóc sức khỏe làn da hiệu quả từ các thành phần thiên nhiên như: Sữa rửa mặt tẩy tế bào chết, Sữa dưỡng thể trẻ hóa da, Kem chống nắng, Mặt nạ,… St.Ives cam kết mang lại cho bạn những trải nghiệm chăm sóc da hiệu quả và hợp túi tiền với tiêu chí sử dụng nguyên liệu tươi tốt, chiết xuất 100% từ thiên nhiên, không có hóa chất, được chị em phụ nữ khắp thế giới biết đến và tin dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-x64lcszn25kve8");
        variant_1.setWeight(946.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1450));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //VICHY
    @Test
    public void testProduct_802546_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem dưỡng dạng gel sữa dành cho da mụn với tác động kép Vichy 50ml");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/34bd68dd6f2491b9fcf33ea9b4cb9ba2");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-i0rqp9z5vbivf0");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-qv7m1695vbiv57");
        images.add("https://cf.shopee.vn/file/9da4590526313f1c327868b7c8056158");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-yg9m1r8wo3kvaf");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Kem dưỡng dạng gel sữa dành cho da mụn với tác động kép Vichy Normaderm Phytosolution Double-Correction Daily Care 50ml có thành phần hoạt tính hoàn toàn từ thiên nhiên, an toàn cho mọi loại da kể cả da nhạy cảm. Kết cấu kem mỏng nhẹ, thẩm thấu nhanh vào da, tác động trực tiếp vào nguyên nhân gây nên mụn trứng cá, làm giảm mụn, mụn trứng cá và  các đốm nâu sau mụn. Sản phẩm cũng cung cấp nước, độ ẩm, tái tạo củng cố hàng rào bảo vệ da, cải thiện sắc tố da mang đến làn da sáng mịn, đều màu. Lần đầu tiên, bạn sẽ tìm thấy một dòng kem hỗ trợ giảm mụn không hề nặng nề cho da, giúp việc loại bỏ mụn dễ dàng hơn bao giờ hết.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại sản phẩm\n" +
                "\n" +
                "- Kem dưỡng dạng keo dành cho da mụn\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại da phù hợp\n" +
                "\n" +
                "- Mọi loại da, nhất là da mụn, da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Độ an toàn\n" +
                "\n" +
                "- Thành phần hoàn toàn từ thiên nhiên \n" +
                "\n" +
                "- Không parapen, không alcohol\n" +
                "\n" +
                "- Được kiểm nghiệm da liễu\n" +
                "\n" +
                "- Không gây kích ứng, không gây mụn\n" +
                "\n" +
                "- Độ PH [5,5]\n" +
                "\n" +
                "- An toàn cho da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thành phần:\n" +
                "\n" +
                "- 60% Nước khoáng Vichy: Củng cố khả năng phòng vệ tự nhiên của lớp hàng rào bảo vệ da, tăng cường sức đề kháng và các đặc tính chống oxy hóa tự nhiên\n" +
                "\n" +
                "- Natural Hyaluronic Acid (Chiết xuất lúa mì): Giữ ẩm cho da, giảm tình trạng mất nước qua da\n" +
                "\n" +
                "- Bifidus Probiotic (chiết xuất từ men vi sinh) : Củng cố và tái cấu trúc hàng rào bảo vệ da và tình trạng khô da\n" +
                "\n" +
                "-  Natural Salicylic Acid (Winter Green Leaf): Giảm sừng nang lông tuyến bã\n" +
                "\n" +
                "- Phyco Saccharide (Chiết xuất từ tảo): Ức chế sự tích tụ Lipid ở nang lông, giảm sản xuất bã nhờn, nguyên nhân hình thành mụn\n" +
                "\n" +
                "- Vitamin Cg (dẫn xuất từ Vitamin C): Giảm mụn, đốm nâu sau mụn trứng cá\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng\n" +
                "\n" +
                "Tác động chuyên biệt với mụn và các vấn đề về da:\n" +
                "\n" +
                "- Giảm mụn, ngăn mụn tái phát\n" +
                "\n" +
                "- Giảm thâm hiệu quả\n" +
                "\n" +
                "\n" +
                "\n" +
                "Tác động đối với tổng thể làn da:\n" +
                "\n" +
                "- Cải thiện đáng kể tình trạng da và sắc da, cho da sáng mịn đều màu\n" +
                "\n" +
                "- Dưỡng ẩm 24h, giúp da trở nên trong suốt bóng mượt\n" +
                "\n" +
                "- Thu nhỏ lỗ chân lông\n" +
                "\n" +
                "- Tái tạo và củng cố hàng rào bảo vệ da\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hiệu quả:\n" +
                "\n" +
                "Sau 8 tuần sử dụng:\n" +
                "\n" +
                "- Giảm mụn cám 37%\n" +
                "\n" +
                "- Giảm mụn 42%\n" +
                "\n" +
                "- Giảm đốm thâm sâu mụn trứng cá 62%\n" +
                "\n" +
                "- Thu nhỏ lỗ chân lông 8%\n" +
                "\n" +
                "- Da sáng mịn đều màu hơn 26%\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "\n" +
                "- Sử dụng sáng và tối, sau bước làm sạch da mặt \n" +
                "\n" +
                "- Lấy một lượng sản phẩm vừa đủ cỡ hạt đậu, chấm đều lên 5 điểm: trán, mũi, cằm và 2 bên má rồi nhẹ nhàng massage theo hình tròn, hướng lên trên (tránh thoa lên vùng mắt)\n" +
                "\n" +
                "- Có thể sử dung làm lớp nền trang điểm   \n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp\n" +
                "\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất \n" +
                "\n" +
                "Ngày sản xuất: In trên bao bì\n" +
                "\n" +
                "Thành phần: Xem chi tiết trên bao bì\n" +
                "\n" +
                "#Vichy #skincare #serum");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-yg9m1r8wo3kvaf");
        variant_1.setWeight(50.0);

        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(750));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802546_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem Chống Nắng UVA+UVB Chống Bụi Mịn Vichy Spf50+CapitalSoleilMattifying 3in150ml");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/3dd9fabcfa51e0c07f5f73d34dfaf0d6");
        images.add("https://cf.shopee.vn/file/aa3bb44f887b954868601a2db3d6e141");
        images.add("https://cf.shopee.vn/file/1ce89e601be1889e574639eb85bd83e5");
        images.add("https://cf.shopee.vn/file/23e08d45fbf4833cfc30fb33cc88fdf4");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-dfjtz9nyo3kv3f");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "VÌ SAO BẠN SẼ THÍCH?\n" +
                "Tia UV là một trong những nguyên nhân chính gây ra các dấu hiệu lão hóa sớm trên da và làm suy giảm khả năng đề kháng của da. Tia UV, đặc biệt là tia UVA có bước sóng dài, hiện diện trong bất kỳ thời tiết nào. Vì vậy kem chống nắng chính là bước bảo vệ da cực kỳ cần thiết và quan trọng trong quy trình dưỡng da hằng ngày.\n" +
                "\n" +
                "Ngoài công nghệ màng lọc Mexoryl độc quyền giúp bảo vệ da tối ưu trước tia UVA và UVB, kem chống nắng Capital Soleil Mattifying 3 trong 1 với thành phần chính từ đất sét xanh, men vi sinh và nước khoáng núi lửa Vichy cùng công nghệ thấm hút dầu MỚI giúp kiểm soát dầu hiệu quả\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM:\n" +
                "Thành Phần:\n" +
                "\n" +
                "- Đất sét xanh: giúp thấm hút và kiểm soát dầu nhờn trên da ngay lập tức, cho lớp nền mịn lỳ cả ngày dài\n" +
                "\n" +
                "- Men vi sinh Probiotic Bifidus và nước khoáng núi lửa Vichy: giúp củng cố, tái cấu trúc và nuôi dưỡng hàng rào bảo vệ da\n" +
                "\n" +
                "- Công nghệ thấm hút dầu MỚI: Chỉ hút dầu nhờn trên da, không hút nước, đảm bảo độ ẩm tự nhiên trên da\n" +
                "\n" +
                "- Đã được kiểm nghiệm da liễu an toàn cho da, kể cả da nhạy cảm\n" +
                "\n" +
                "- Kết cấu thấm nhanh vào da ko để lại cảm giác nhờn rít\n" +
                "\n" +
                "- Không paraben, không chất bảo quản\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng:\n" +
                "\n" +
                "- Hiệu quả đã được kiểm nghiệm da liễu\n" +
                "\n" +
                "- Chống tia uva và uvb tối ưu\n" +
                "\n" +
                "- Thanh lọc da, giảm 21% bụi mịn tiếp xúc trên da sau khi rửa mặt\n" +
                "\n" +
                "- Giảm 23% bóng dầu, da thoáng mịn trong suốt 12h\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại Da: Da Dầu, Da Mụn\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn bảo quản\n" +
                "\n" +
                "- Sử dụng buổi sáng, lấy 1 lượng bằng 2 hạt bắp chấm lên 5 điểm: trán, mũi, 2 má và cằm đồng thời thoa vùng cổ\n" +
                "\n" +
                "- Sử dụng sản phẩm sau bước dưỡng da, và 20 phút trước khi tiếp xúc với ánh nắng\n" +
                "\n" +
                "- Nếu đi bơi đi biển, hoạt động nhiều thì nên thoa lại sản phẩm sau mỗi 2h\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ NSX\n" +
                "\n" +
                "NSX: In trên bao bì\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp\n" +
                "\n" +
                "Hướng dẫn bảo quản: Nơi thoáng mát, tránh ánh nắng trực tiếp");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-dfjtz9nyo3kv3f");
        variant_1.setWeight(150.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(950));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test

    public void testProduct_802546_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Dưỡng chất giàu khoáng chất giúp da sáng mịn và căng mượt Vichy Mineral 89 30ml");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-31m125ba2mjvcd");
        images.add("https://cf.shopee.vn/file/aa3bb44f887b954868601a2db3d6e141");
        images.add("https://cf.shopee.vn/file/a09474b567004a17f00dd6e340ec4da1");
        images.add("https://cf.shopee.vn/file/fdb9d37f751fe7d2575faee4a0770bbe");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-z2cux8sqs3kv88");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "VÌ SAO BẠN SẼ THÍCH?\n" +
                "\n" +
                "Dưỡng chất khoáng cô đặc giúp phục hồi và bảo vệ da Vichy Mineral 89 30ml chứa đến 89% nước khoáng Vichy cô đặc với 15 khoáng chất quý báu kết hợp hoàn hảo cùng 0.5% Hyaluronic acid từ lòng núi lửa giúp củng cố hàng rào bảo vệ da, tái tạo và phục hồi, cho da mịn màng, căng mượt và tràn đầy sức sống.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Sản phẩm phù hợp với những làn da nhạy cảm nhất, phục hồi chuyên sâu: hỗ trợ quá trình miễn dịch của da, phục hồi sau cái liệu trình thẩm mỹ không xâm lấn.Sau một tuần sử dụng, da dần lấy độ căng mịn, rạng rỡ, lỗ chân lông thu nhỏ lại, các dấu hiệu lão hóa cũng được cải thiện.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Loại sản phẩm:\n" +
                "\n" +
                "- Tinh chất khoáng cô đặc giúp phục hồi và bảo vệ da Vichy Mineral 89 chứa 89% nước khoáng cô đặc cùng 0.5% Hyaluronic Acid\n" +
                "\n" +
                "- Giải pháp phục hồi cho làn da bị tổn hại do các yếu tố môi trường\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại da phù hợp:\n" +
                "\n" +
                "- Da yếu sau các liệu trình thẩm mỹ xâm lấn\n" +
                "\n" +
                "- Da bị mẫn đỏ, bong tróc, bỏng rát, châm chích, ngứa\n" +
                "\n" +
                "- Da khô do thiếu nước, cần được cấp ẩm\n" +
                "\n" +
                "- Da nhạy cảm\n" +
                "\n" +
                "\n" +
                "\n" +
                "Độ an toàn:\n" +
                "\n" +
                "- Không paraben, không hương liệu, không alcohol\n" +
                "\n" +
                "- An toàn cho da nhạy cảm, được kiểm nghiệm bỏi 179 bác sĩ da liễu và 1630 phụ nữ trên toàn thế giới\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thành phần:\n" +
                "\n" +
                "- 89% nước khoáng cô đặc chứa 15 khoáng chất quý từ nguồn khoáng núi lửa Vichy\n" +
                "\n" +
                "- 0.5% Hyaluronic acid từ lòng núi lửa\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng:\n" +
                "\n" +
                "- Phục hồi da đang bị mẫn đỏ, nhạy cảm, viêm da tiếp xúc\n" +
                "\n" +
                "- Bảo vệ da trước và phục hồi da sau khi thực hiện các thủ thuật thẩm mỹ không xâm lấn (Peel da, lăn kim, phi kim, laser)\n" +
                "\n" +
                "- Củng cố hàng rào bảo vệ da, tái tạo và phục hồi, cho da mịn màng, căng mượt và tràn đầy sức sống\n" +
                "\n" +
                "- Thu nhỏ lỗ chân lông, cải thiện các dấu hiệu lão hóa\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "\n" +
                "- Sử dụng sáng và tối, sau bước làm sạch và làm dịu da cùng nước khoáng Vichy.\n" +
                "\n" +
                "- Lấy một lượng sản phẩm vừa đủ cỡ hạt đậu, chấm đều lên 5 điểm: trán, mũi, cằm và 2 bên má rồi nhẹ nhàng massage theo hình tròn, hướng lên trên.\n" +
                "\n" +
                "- Có thể kết hợp sử dụng thêm các dưỡng chất cùng dòng nếu muốn. \n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ NSX\n" +
                "\n" +
                "NSX: In trên bao bì\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp\n" +
                "\n" +
                "Hướng dẫn bảo quản: Nơi thoáng mát, tránh ánh nắng trực tiếp");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-z2cux8sqs3kv88");
        variant_1.setWeight(30.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(650));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802546_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Bộ kem chống nắng có màu ngăn sạm da, giảm thâm nám VICHY Capital Soleil Anti-Daskspot");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/fc25730eee602ed3fe6410d9f0ab019a");
        images.add("https://cf.shopee.vn/file/aa3bb44f887b954868601a2db3d6e141");
        images.add("https://cf.shopee.vn/file/39a21c86a4664f4a7f89ff4153c05369");
        images.add("https://cf.shopee.vn/file/0a76319fb2eab221bf937afdfe9a6506");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-k04v2e63s3kv8c");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "VÌ SAO BẠN SẼ THÍCH?\n" +
                "\n" +
                "Bộ sản phẩm gồm:\n" +
                "\n" +
                "- 01 x Kem chống nắng ngăn sạm da, giảm thâm nám Vichy capital Soleil Anti Darkspot SPF 50+ Chống Tia UVA + UVB 50ml\n" +
                "\n" +
                "- 01 x Dưỡng chất Mineral 89 15ml\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thông tin chi tiết:\n" +
                "\n" +
                "\n" +
                "\n" +
                "1. Kem chống nắng ngăn sạm da, giảm thâm nám Vichy capital Soleil Anti Darkspot 50ml là kem chống nắng đa tác dụng (3 trong 1): chống nắng + trang điểm làm đều màu da, giảm bóng dầu + dưỡng trắng da, giảm thâm nám. Sản phẩm áp dụng công nghệ tiên tiến với 3 màng lọc, giúp chống nắng, ngăn lão hóa, ngừa ung thư và bảo vệ da tối ưu nhất trước các tia cực tím (UVA, UVB). Nếu bạn vừa muốn làn da được bảo vệ tối ưu, vừa muốn thêm chức năng làm sáng da và che phủ tự nhiên thì đây chính là lựa chọn lý tưởng dành cho bạn. Sản phẩm phù hợp với mọi loại da, đặc biệt là da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HDSD:\n" +
                "\n" +
                "- Sử dụng buổi sáng, lấy 1 lượng bằng 2 hạt bắp chấm lên 5 điểm: trán, mũi, 2 má và cằm đồng thời thoa vùng cổ.\n" +
                "\n" +
                "- Thoa từ trong ra ngoài và từ trên xuống dưới. Sử dụng sau bước kem dưỡng da.\n" +
                "\n" +
                "- Nếu hoạt động đi bơi đi biển, hoạt động nhiều thì nên thoa lại sau mỗi 2 tiếng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "2. Khoáng chất cô đặc phục hồi và bảo vệ da Vichy Mineral 89 Fortifying And Pluming Daily Booster chứa đến 89% nước khoáng Vichy cô đặc với 15 khoáng chất quý báu kết hợp hoàn hảo cùng Hyaluronic Acid từ lòng núi lửa giúp củng cố hàng rào bảo vệ da, tái tạo và phục hồi, cho da mịn màng, căng mượt và tràn đầy sức sống.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HDSD:\n" +
                "\n" +
                "- Rửa mặt sạch\n" +
                "\n" +
                "- Lấy một lượng vừa đủ (khoảng bằng hạt đậu) chấm đều lên mặt\n" +
                "\n" +
                "- Massage nhẹ nhàng để sản phẩm thẩm thấu vào da\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ NSX\n" +
                "\n" +
                "NSX: In trên bao bì\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp\n" +
                "\n" +
                "Hướng dẫn bảo quản: Nơi thoáng mát, tránh ánh nắng trực tiếp\n" +
                "\n" +
                "Lưu ý: Bao bì sản phẩm có thể thay đổi theo từng đợt nhập");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-k04v2e63s3kv8c");
        variant_1.setWeight(65.0);

        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1050));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802546_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Bộ kem chống nắng chống ô nhiễm, bụi mịn, căng mượt da Vichy Capital Soleil Mattifying");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-w761gt6u2mjvbe");
        images.add("https://cf.shopee.vn/file/aa3bb44f887b954868601a2db3d6e141");
        images.add("https://cf.shopee.vn/file/3dd9fabcfa51e0c07f5f73d34dfaf0d6");
        images.add("https://cf.shopee.vn/file/0a76319fb2eab221bf937afdfe9a6506");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-fbkvelf7s3kvd5");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "VÌ SAO BẠN SẼ THÍCH?\n" +
                "\n" +
                "Bộ kem chống nắng chống ô nhiễm, bụi mịn và căng mượt da Vichy Capital Soleil Mattifying\n" +
                "\n" +
                "\n" +
                "\n" +
                "BỘ SẢN PHẨM BAO GỒM:\n" +
                "\n" +
                "01 x Kem chống nắng chống ô nhiễm và bụi mịn VICHY Capital Soleil Mattifying 50ml\n" +
                "\n" +
                "01 x Serum dưỡng chất giàu khoáng chất giúp da sáng mượt và căng mịn Vichy Mineral 89 15ml\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thông tin chi tiết\n" +
                "\n" +
                "1. Kem chống nắng chống ô nhiễm và bụi mịn Vichy Capital Soleil Mattifying\n" +
                "\n" +
                "Kết cấu dạng kem, áp dụng công nghệ tiên tiến giúp chống nắng, ngăn lão hóa. Sản phẩm chống nắng hiệu quả cùng khả năng thấm cực nhanh, khô ráo tức thì đem lại cảm giác thoải mái khi sử dụng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng:\n" +
                "\n" +
                "- Bảo vệ da khỏi những tác động của tia UV và các tác động từ môi trường ô nhiễm\n" +
                "\n" +
                "- Mang lại cảm giác khô thoáng, không nhờn rít trên da.\n" +
                "\n" +
                "- Sử dụng công nghệ tiên tiến giúp ngăn ngừa các dấu hiệu lão hoá trên da.\n" +
                "\n" +
                "- Có khả năng thay thế lớp lót trong trang điểm và kem che khuyết điểm\n" +
                "\n" +
                "- Giảm bóng dầu cho da, tệp với màu da sau khi sản phẩm thẩm thấu hoàn toàn vào da.\n" +
                "\n" +
                "- CHỐNG TIA UVA và UVB TỐI ƯU\n" +
                "\n" +
                "- THANH LỌC DA, GIẢM 21% BỤI MỊN TIẾP XÚC TRÊN DA SAU KHI RỬA MẶT*\n" +
                "\n" +
                "- GIẢM 23% BÓNG DẦU, DA THOÁNG MỊN TRONG SUỐT 12H\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại da phù hợp:\n" +
                "\n" +
                "- Dùng được cho mọi loại da, đặc biệt là da dầu mụn, da nhạy cảm\n" +
                "\n" +
                "\n" +
                "\n" +
                "Cách dùng:\n" +
                "\n" +
                "- Sử dụng buổi sáng, lấy 1 lượng bằng 2 hạt bắp chấm lên 5 điểm: trán, mũi, 2 má và cằm đồng thời thoa vùng cổ\n" +
                "\n" +
                "- Sử dụng sản phẩm sau bước dưỡng da, và 20 phút trước khi tiếp xúc với ánh nắng\n" +
                "\n" +
                "- Nếu đi bơi đi biển, hoạt động nhiều thì nên thoa lại sản phẩm sau mỗi 2h\n" +
                "\n" +
                "\n" +
                "\n" +
                "2. Serum dưỡng chất giàu khoáng chất giúp da sáng mượt và căng mịn Vichy Mineral 89 15ml\n" +
                "\n" +
                "Chứa đến 89% khoáng Vichy cô đặc với 15 khoáng chất quý báu kết hợp hoàn hảo cùng Hyaluronic acid từ lòng núi lửa giúp củng cố hàng rào bảo vệ da, tái tạo và phục hồi, cho da mịn màng, căng mượt và tràn đầy sức sống. \n" +
                "\n" +
                "Sản phẩm không gây kích ứng, dịu nhẹ cho làn da khi sử dụng, đặc biệt là những làn da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại da phù hợp:\n" +
                "\n" +
                "- Mọi loại da, dùng được cho cả da nhạy cảm\n" +
                "\n" +
                "\n" +
                "\n" +
                "Cách dùng\n" +
                "\n" +
                "- Sử dụng sáng và tối, sau bước làm sạch và làm dịu da cùng nước khoáng Vichy.\n" +
                "\n" +
                "- Lấy một lượng sản phẩm vừa đủ chấm đều lên 5 điểm: trán, mũi, cằm và 2 bên má. Nhẹ nhàng massage theo hình tròn.\n" +
                "\n" +
                "- Có thể kết hợp sử dụng thêm các dưỡng chất cùng dòng nếu muốn.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ NSX\n" +
                "\n" +
                "NSX: In trên bao bì\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp\n" +
                "\n" +
                "Hướng dẫn bảo quản: Nơi thoáng mát, tránh ánh nắng trực tiếp\n" +
                "\n" +
                "Lưu ý: Bao bì sản phẩm có thể thay đổi theo từng đợt nhập");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-fbkvelf7s3kvd5");
        variant_1.setWeight(65.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1050));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802546_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem chống nắng SPF 50 UVA+UVB Vichy Capital Soleil Mattifying Dry Touch Face Fluid 50ml");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/2a07c5ee28a6d133c4f1293c19b1cdc2");
        images.add("https://cf.shopee.vn/file/aa3bb44f887b954868601a2db3d6e141");
        images.add("https://cf.shopee.vn/file/050202ba46153c56c7440b678f1b79c4");
        images.add("https://cf.shopee.vn/file/ea1940b7a74b28f385a4a1250f48d37a");
        images.add("https://cf.shopee.vn/file/5519e72fffe1b40a188247a4bc91a133");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-gdrs6enuo3kv43");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "\n" +
                "VÌ SAO BẠN SẼ THÍCH?\n" +
                "\n" +
                "Kem chống nắng không màu không gây nhờn rít Vichy Capital Soleil Dry Touch Face Fluid SPF50 UVB+UVA 50ml giúp bảo vệ da trước tác hại của tia UVB và tia UVA. Dạng kem không màu, không gây bết dính hay nhờn rít, thông thoáng lỗ chân lông mà vẫn cung cấp đủ độ ẩm cho da sau khi thoa.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại sản phẩm\n" +
                "\n" +
                "- Kem chống nắng không màu không gây nhờn rít SPF50 chống tia UVA &amp; UVB.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại da phù hợp\n" +
                "\n" +
                "- Phù hợp với mọi loại da, kể cả da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Độ an toàn\n" +
                "\n" +
                "- 100% dễ chịu.\n" +
                "\n" +
                "- Không Paraben.\n" +
                "\n" +
                "- Không kích ứng da*.\n" +
                "\n" +
                "- An toàn với da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thành phần\n" +
                "\n" +
                "- Màng lọc Mexoryl SX - XL® độc quyền giúp bảo vệ da trước tác hại của tia UVB (gây bỏng rát), và tia UVA (cả 2 tia ngắn và dài gây biến đổi chất lượng da và lão hóa sớm).\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng\n" +
                "\n" +
                "- Bảo vệ da toàn diện với chỉ số SPF50, chống tia UVA (cả 2 tia ngắn và dài gây biến đổi chất lượng da và lão hóa sớm) + UVB (gây bỏng rát) toàn diện ngăn sạm da, lão hóa sớm và ung thư da.\n" +
                "\n" +
                "- Dạng kem không màu thẩm thấu vào da, không gây bết dính hay nhờn rít, không để lại vệt trắng trên da, không gây bít tắt lỗ chân lông.\n" +
                "\n" +
                "- Da vẫn có cảm giác dưỡng ẩm sau khi thoa.\n" +
                "\n" +
                "- Lâu trôi khi sử dụng dưới nước.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "\n" +
                "- Sử dụng buổi sáng như kem lót nền trang điểm, sau bước dưỡng da. \n" +
                "\n" +
                "- Lấy 1 lượng bằng 2 hạt bắp chấm lên 5 điểm: Trán, mũi, 2 má &amp; cằm đồng thời thoa vùng cổ. Thoa từ trong ra ngoài và từ trên xuống dưới.\n" +
                "\n" +
                "- Nếu hoạt động đi bơi đi biển, hoạt động nhiều thì nên thoa lại sau mỗi 2 tiếng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi chuyên gia da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                "NSX: In trên bao bì\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-gdrs6enuo3kv43");
        variant_1.setWeight(50.0);

        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(950));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802546_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Tinh Chất Giúp Cải Thiện&Ngăn Ngừa Thâm Nám Đốm Nâu Vichy Liftactiv B3 Dark Spots");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-ehuxaa3k2mjv0b");
        images.add("https://cf.shopee.vn/file/aa3bb44f887b954868601a2db3d6e141");
        images.add("https://cf.shopee.vn/file/d71ab760f7cfccc96c3caef4855dd804");
        images.add("https://cf.shopee.vn/file/ceee67ad5c4499720ea719a921423454");
        images.add("https://cf.shopee.vn/file/5fe1fa0e4e9a192ebef2f6aecbbcddac");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-6fpjbe5zo3kve6");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "\n" +
                "VÌ SAO BẠN SẼ THÍCH?\n" +
                "\n" +
                "\n" +
                "Serum Vichy Liftactiv B3 Dark Spots 30ml với sự kết hợp giữa 13% phức hợp thành phần hoạt tính cao: Niacinamide [B3], thành phần peel da và AHA cho làn da tươi sáng đều màu sau 8 tuần sử dụng. Đây chính là giải pháp giúp da bạn cải thiện và ngăn ngừa đốm nâu, thâm nám toàn diện.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Loại sản phẩm: Serum dưỡng sáng, hỗ trợ giảm mụn, cải thiện và ngăn ngừa đốm nâu thâm nám\n" +
                "\n" +
                "Loại da phù hợp: Mọi loại da, ngay cả làn da nhạy cảm\n" +
                "\n" +
                "\n" +
                "\n" +
                "Độ an toàn:\n" +
                "\n" +
                "- An toàn và phù hợp cho da nhạy cảm \n" +
                "\n" +
                "- Không kích ứng, không chất tẩy trắng, không silicon\n" +
                "\n" +
                "- An toàn &amp; cao cấp với thiết kế vòi đặc biệt\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thành phần: \n" +
                "\n" +
                "13% phức hợp gồm Niacinamide [B3], Glycolid Acid và Peeling Actives.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng:\n" +
                "\n" +
                "- Cải thiện và ngăn ngừa đốm nâu, thâm nám toàn diện\n" +
                "\n" +
                "- Giúp da tươi sáng đều màu sau 8 tuần sử dụng\n" +
                "\n" +
                "- Giảm mật độ và kích thước đốm nâu, thâm nám\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng:\n" +
                "\n" +
                "1. Cho sản phẩm ra tay, vỗ nhẹ khắp mặt giúp sản phẩm thẩm thấu triệt để\n" +
                "\n" +
                "2. Sử dụng kết hợp kem dưỡng cùng dòng giúp tăng hiệu quả làm sáng da\n" +
                "\n" +
                "3. Lưu ý sử dụng 2 lần, sáng và tối; sử dụng kết hợp kem chống nắng vào buổi sáng\n" +
                "\n" +
                "Bảo quản nơi thoáng mát\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ NSX\n" +
                "\n" +
                "NSX: In trên bao bì\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp\n" +
                "\n" +
                "Hướng dẫn bảo quản: Nơi thoáng mát, tránh ánh nắng trực tiếp");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-6fpjbe5zo3kve6");
        variant_1.setWeight(50.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(750));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802546_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem dưỡng ẩm và cung cấp nước dạng gel Vichy Aqualia Thermal Cream-Gel 50ml");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/dd19c29cf1488370ae2a4f1ddab92931");
        images.add("https://cf.shopee.vn/file/ae088e40b0cabdd2cd02201094f639b6");
        images.add("https://cf.shopee.vn/file/57b7011eee99cbec975c7c9c54ca070b");
        images.add("https://cf.shopee.vn/file/c42d425747c59a32ae4bc1c5d58e907e");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-xjlafwbwo3kvd4");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "VÌ SAO BẠN SẼ THÍCH?\n" +
                "\n" +
                "Gel dưỡng ẩm giúp da dịu mát cho da thường, da hỗn hợp &amp; da nhạy cảm Vichy Aqualia Thermal Rehydrating Gel Cream 50ml sở hữu thế hệ dưỡng ẩm mới với cơ chế Isotonic bù nước, bù khoáng cùng 97% thành phần thiên nhiên (Khoáng núi lửa Vichy, Solium PCA, Đường Mannose, HA) giúp cấp nước sâu cho làn da ẩm mượt suốt 48h bất chấp mọi điều kiện thời tiết. Công thức mới với thành phần tự nhiên hơn, hiệu quả hơn và an toàn hơn cho làn da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại sản phẩm\n" +
                "\n" +
                "- Gel dưỡng ẩm giúp da dịu mát cho da thường, da hỗn hợp &amp; da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Loại da phù hợp\n" +
                "\n" +
                "- Da thường, da hỗn hợp &amp; da nhạy cảm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Độ an toàn\n" +
                "\n" +
                "- Không paraben.\n" +
                "\n" +
                "- Không gây dị ứng*.\n" +
                "\n" +
                "- Không gây nhân mụn trứng cá.\n" +
                "\n" +
                "- Thành phần tự nhiên dịu nhẹ cho da.\n" +
                "\n" +
                "- An toàn cho cả da nhạy cảm, dễ đỏ / kích ứng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thành phần\n" +
                "\n" +
                "- Cơ chế Isotonic bù nước, bù khoáng:\n" +
                "\n" +
                "+ Lấy cảm hứng từ Cơ chế Isotonic bù khoáng và nước cho vận động viên.\n" +
                "\n" +
                "+ Là cơ chế sử dụng [NƯỚC + KHOÁNG + ĐƯỜNG] theo tỷ lệ tương tự như tỷ lệ trong môi trường sinh lý của tế bào.\n" +
                "\n" +
                "- 97% thành phần thiên nhiên:\n" +
                "\n" +
                "+ [Nước] HYALURONIC ACID: giữ ẩm giúp da trông căng mịn hơn.\n" +
                "\n" +
                "+ [Khoáng] SODIUM PCA: giữ nước và khóa ẩm trên bề mặt da.\n" +
                "\n" +
                "+ [Khoáng] VICHY THERMAL WATER: chứa 15 khoáng chất làm dịu, củng cố và tăng cường sức khỏe làn da.\n" +
                "\n" +
                "+ [Đường] MANOSE: nguồn gốc từ bột gỗ tự nhiên, cung cấp độ ẩm và giảm tình trạng mất nước trên da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng\n" +
                "\n" +
                "- Cấp nước sâu cho làn da ẩm mượt suốt 48h.\n" +
                "\n" +
                "- Kết cấu sản phẩm dạng gel mỏng nhẹ, cho cảm giác mát, dễ chịu trên da, không có cảm giác nhờn dầu.\n" +
                "\n" +
                "- Hương thơm dịu nhẹ, dễ chịu.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hiệu quả\n" +
                "\n" +
                "- Ngay lập tức\n" +
                "\n" +
                "+ Da trông tươi tắn 94%\n" +
                "\n" +
                "+ Da trông mềm mại hơn 91%\n" +
                "\n" +
                "- 1 tháng\n" +
                "\n" +
                "+ Da trông ẩm mượt 91%\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "\n" +
                "- Sử dụng buổi sáng, sau bước thoa tinh chất.\n" +
                "\n" +
                "- Lấy lượng kem bằng hạt bắp chấm lên 5 điểm: trán, mũi, cằm và hai bên má rồi thoa từ trong ra ngoài và từ trên xuống dưới.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi chuyên gia da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                "NSX: In trên bao bì sản phẩm\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-xjlafwbwo3kvd4");
        variant_1.setWeight(50.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(750));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802546_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Bộ sản phẩm serum khoáng phục hồi chuyên sâu Vichy Mineral 89");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22110-j9dace1s2mjvf5");
        images.add("https://cf.shopee.vn/file/aa3bb44f887b954868601a2db3d6e141");
        images.add("https://cf.shopee.vn/file/a32b16bb5257c2a4ba34011a52419857");
        images.add("https://cf.shopee.vn/file/cbfb396714935f0b0d9c4cd53de93bc3");
        images.add("https://cf.shopee.vn/file/3ba1821a06453bf61b8e33eefd77f08a");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-hqy4rvi4s3kv77");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "VÌ SAO BẠN SẼ THÍCH?\n" +
                "\n" +
                "Bộ sản phẩm bao gồm:\n" +
                "\n" +
                "- 01 x Xịt khoáng dưỡng da Vichy Mineralizing Thermal Water 50ml\n" +
                "\n" +
                "- 01 x Kem Chống Nắng Dành Cho Da Dầu Mụn, Giảm Các Dấu Hiệu Lão Hóa Và Kiềm Soát Bóng Dầu Vichy Spf50+ Capital Soleil Mattifying 3-in-1 3ml(3g)\n" +
                "\n" +
                "- 01 x Dưỡng Chất (Serum) Khoáng núi lửa cô đặc Vichy Mineral 89 15ml\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thông tin chi tiết:\n" +
                "\n" +
                "\n" +
                "\n" +
                "1.  Xịt Khoáng Dưỡng Da Vichy Mineralizing Thermal Water 50ml được làm giàu tự nhiên với 15 khoáng chất quý hiếm nước khoáng Vichy Thermal Spa đã được công nhận bởi tác dụng làm dịu, củng cố và làm đẹp da tuyệt vời của nó, được kiểm chứng bởi 34 thí nghệm trên hơn 600 người thuộc mọi loại da, thậm chí với làn da nhạy cảm nhất và dưới sự giám sát của chuyên gia da liễu.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HDSD:\n" +
                "\n" +
                "- Xịt khắp mặt\n" +
                "\n" +
                "- Vỗ nhẹ\n" +
                "\n" +
                "- Thấm khô, có thể sử dụng nhiều lần trong ngày\n" +
                "\n" +
                "\n" +
                "\n" +
                "2. Kem Chống Nắng Dành Cho Da Dầu Mụn, Giảm Các Dấu Hiệu Lão Hóa Và Kiềm Soát Bóng Dầu Vichy Spf50+ Capital Soleil Mattifying 3-in-1 là kem chống nắng dành cho da dầu giúp kiểm soát bóng nhờn và bảo vệ da trước tác hại từ ánh nắng &amp; ô nhiễm, ngăn chặn các tác nhân gây lão hóa sớm. Sản phẩm có công thức chống thấm nước thích hợp dùng hàng ngày và cả những hoạt động ngoài trời, để bạn luôn tự tin tỏa sáng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng:\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Kết cấu dạng kem gel, thẩm thấu tức thì, mang lại cảm giác khô thoáng, không để lại vệt trắng.\n" +
                "\n" +
                "- Kiểm soát bã nhờn &amp; mồ hôi giúp mang đến một cảm giác “sạch” cho làn da đến 9h.\n" +
                "\n" +
                "- Bảo vệ da trước những tác hại từ ánh nắng &amp; ô nhiễm: lão hóa sớm, đốm nâu, kích ứng ánh nắng.\n" +
                "\n" +
                "- Độ chống nắng cao nhất SPF 50+ bảo vệ da tối ưu dưới ánh nắng.\n" +
                "\n" +
                "- Chống nắng phổ rộng với nhiều màng lọc tia UV bảo vệ da trước ngay cả tia UVA dài.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng:\n" +
                "\n" +
                "- Thoa kem trước khi tiếp xúc với ánh nắng 20 phút.\n" +
                "\n" +
                "- Lấy một lượng sản phẩm vừa đủ và chấm 5 điểm trên mặt (trán, mũi, cằm và hai bên má) sau đó thoa sản phẩm theo chiều từ trong ra ngoài và trên xuống dưới.\n" +
                "\n" +
                "\n" +
                "\n" +
                "3. Vichy Mineral 89 15ml chứa đến 89% khoáng Vichy cô đặc với 15 khoáng chất quý báu kết hợp hoàn hảo cùng Hyaluronic acid từ lòng núi lửa giúp củng cố hàng rào bảo vệ da, tái tạo và phục hồi, cho da mịn màng, căng mượt và tràn đầy sức sống. Sau một tuần sử dụng, da dần lấy độ căng mịn, rạng rỡ, lỗ chân lông thu nhỏ lại, các dấu hiệu lão hóa cũng được cải thiện.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng\n" +
                "\n" +
                "- Sử dụng sáng và tối, sau bước làm sạch và làm dịu da cùng nước khoáng Vichy.\n" +
                "\n" +
                "- Lấy một lượng sản phẩm vừa đủ cỡ hạt đậu, chấm đều lên 5 điểm: trán, mũi, cằm và 2 bên má rồi nhẹ nhàng massage theo hình tròn, hướng lên trên.\n" +
                "\n" +
                "- Có thể kết hợp sử dụng thêm các dưỡng chất cùng dòng nếu muốn. \n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ NSX\n" +
                "\n" +
                "NSX: In trên bao bì\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp\n" +
                "\n" +
                "Hướng dẫn bảo quản: Nơi thoáng mát, tránh ánh nắng trực tiếp\n" +
                "\n" +
                "Lưu ý: Bao bì sản phẩm có thể thay đổi theo từng đợt nhập");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-hqy4rvi4s3kv77");
        variant_1.setWeight(68.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1150));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);
        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802546_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Dưỡng chất làm sáng da ban đêm Vichy Liftactiv Specialist Glyco-C");
        productDTO.setShopId(802546);
        productDTO.setTradeMarkId("1703772512365458");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/6eaf61e2c70c94cc82b0502c4ea11c95");
        images.add("https://cf.shopee.vn/file/aa3bb44f887b954868601a2db3d6e141");
        images.add("https://cf.shopee.vn/file/cb4a4fab154d657583295e1337214669");
        images.add("https://cf.shopee.vn/file/e3762c65f7a32a5d9fb2a0b7539f2835");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-t96boa5yo3kvc3");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Dưỡng chất làm sáng da ban đêm Vichy Liftactiv Specialist GlycoC 2ml x10\n" +
                "\n" +
                "Với nguyên chất phức hợp 5 thành phần chống o.xy hóa rất mạnh mẽ, hiệu quả da trẻ trung tươi trẻ sau 10 ngày. Công thức chứa 15% Vitamin C nguyên chất giúp ngăn ngừa và cải thiện các dấu hiệu lão hóa trên da cho làn da sáng màu & mịn màng. Sử dụng sản phẩm khi làn da: xỉn màu, thiếu sức sống và nhiều vết thâm nám dưới tác động có hại của môi trường & căng thẳng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thành Phần Chính:\n" +
                "\n" +
                "- 10% PHỨC HỢP GLYCOLIC ACID (4% Glycolic Acid, 1% Citric Acid, 5% HEPE): Loại bỏ tế bào c.hết, làm sáng da và đều màu da, làm mờ đốm nâu và giảm thâm nám.\n" +
                "\n" +
                "- Vitamin Cg: Cải thiện và ngăn ngừa các đốm thâm nám.\n" +
                "\n" +
                "- Hyaluronic Acid: Giữ nước và cải thiện độ ẩm của da làm da căng mọng hơn.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Công dụng:\n" +
                "\n" +
                "- Cải thiện đốm thâm, cân bằng tone da, mang lại kết cấu đồng đều nhờ thành phần 10% phức hợp Glycolic Acid và Vitamin CG\n" +
                "\n" +
                "- Củng cố hệ thống phòng vệ tự nhiên của làn da nhờ thành phần Hyaluronic Acid và Nước khoáng núi lữa Vichy\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng dẫn sử dụng:\n" +
                "\n" +
                "- Nhỏ 4 - 5 giọt vào lòng bàn tay sạch, khô. \n" +
                "\n" +
                "- Vỗ nhẹ bằng đầu ngón tay khắp mặt. Tránh vùng da quanh mắt. \n" +
                "\n" +
                "- Sử dụng 1-2 lần mỗi ngày.\n" +
                "\n" +
                "- Nên sử dụng thêm sản phẩm chống nắng với chỉ số chống nắng SPF15 trở lên trước khi ra ngoài trời. Để cho kết quả tốt nhất, sử dụng sau dưỡng chất khoáng cô đặc Mineral 89 & trước sản phẩm Liftactiv serum ngăn ngừa lão hóa.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Lưu ý: Màu của sản phẩm có thể thay đổi theo thời gian. Tránh để sản phẩm tiếp xúc trực tiếp với ánh nắng mặt trời.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Độ an toàn:\n" +
                "\n" +
                "- Không chứa hương liệu, không paraben\n" +
                "\n" +
                "- Không gây kích ứng da\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "\n" +
                "\n" +
                "Vichy là thương hiệu nổi tiếng của Pháp được thành lập vào năm 1931 tại thành phố cùng tên Vichy bởi bác sĩ da liễu Haller và nhà kinh doanh Guerin. Với nhiều năm kinh nghiệm, viện nghiên cứu Vichy đã sáng tạo ra nhiều phương thức mới và hiệu quả để chăm sóc sức khỏe làn da. Tất cả sản phẩm Vichy được kiểm nghiệm một cách toàn diện và khắt khe dưới sự giám sát của chuyên gia da liễu về độ an toàn và dịu nhẹ cho da, đặc biệt đối với làn da nhạy cảm. Ngoài ra, các sản phẩm của Vichy đã được chứng minh không gây dị ứng, không gây mụn, chứa nước khoáng Vichy giàu khoáng chất có tác dụng củng cố và tăng cường tái tạo da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HSD: 3 năm kể từ NSX\n" +
                "\n" +
                "NSX: In trên bao bì\n" +
                "\n" +
                "Xuất xứ thương hiệu: Pháp\n" +
                "\n" +
                "Nơi sản xuất: Pháp\n" +
                "\n" +
                "Hướng dẫn bảo quản: Nơi thoáng mát, tránh ánh nắng trực tiếp\n" +
                "\n" +
                "Chú ý: Bao bì thay đổi tùy theo từng đợt nhập hàng");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-t96boa5yo3kvc3");
        variant_1.setWeight(20.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(650));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //ansaa
    @Test
    public void testProduct_802545_11() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa chống nắng dịu nhẹ cho da nhạy cảm Anessa Perfect UV Sunscreen Mild Milk 60ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/f5f700ed95b6e570d12d0a151ac339eb");
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-zn93bgddk3kv65");
        images.add("https://cf.shopee.vn/file/0844aecb437ab6c0a24d6175b14589b5");
        images.add("https://cf.shopee.vn/file/53e3907cebbd2f08a1cd8e0ed135d616");
        images.add("https://cf.shopee.vn/file/173459730be947ffd5c6b3f354a79339");
        images.add("https://cf.shopee.vn/file/02a56558f69f577c7825fc2726ce5619");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/f5f700ed95b6e570d12d0a151ac339eb");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sữa chống nắng dịu nhẹ cho da nhạy cảm và trẻ em với kết cấu mỏng nhẹ, kiềm dầu, mịn mượt & nâng tông (phù hợp mọi loại da, đặc biệt là da nhạy cảm thường đến thiên dầu)\n" +
                "\n" +
                " Thành phần và công dụng:\n" +
                "\n" +
                " • Kết cấu dạng sữa dịu nhẹ & ráo mịn phù hợp cho cả da nhạy cảm và da trẻ em từ 1 tuổi.\n" +
                "\n" +
                "  • Chỉ số chống nắng cao SPF 50+, PA++++ giúp bảo vệ da tối ưu khỏi tác hại của tia UV.\n" +
                "\n" +
                "  • Bảo vệ da khỏi tổn thương do tia UV & Bụi Mịn trong không khí.\n" +
                "\n" +
                "  • Công nghệ SmoothPROTECT giúp chống UV hoàn hảo từ mọi góc độ cùng kết cấu mịn mượt như lụa.\n" +
                "\n" +
                "  • Công nghệ Aqua Booster EX & Very Water Resistant giúp chống trôi do MA SÁT, NƯỚC, MỒ HÔi & chống trôi trong nước đến 80'.\n" +
                "\n" +
                "  • Hạt Cleansing Powder giúp trung hòa và chống lại tác hại của bụi mịn trong không khí.\n" +
                "\n" +
                "  • 50% thành phần dưỡng da được nâng cấp với Chiết xuất Mẫu đơn, Trà Xanh Kyoto Uji, Super Hyaluronic Acid, Collagen) giúp dưỡng da, tăng độ đàn hồi, chống oxi hoá.\n" +
                "\n" +
                "  • Thành phần 5 không (Cồn-Màu-Mùi-Paraben-Dầu khoáng) an toàn cho làn da nhạy cảm nhất.\n" +
                "\n" +
                "  • Dùng cho mặt và toàn thân. Phù hợp mọi loại da. Thích hợp sử dụng hàng ngày và trong các hoạt động ngoài trời. \n" +
                "\n" +
                "  • Có thể dùng làm lớp lót trang điểm và dễ dàng làm sạch với sữa rửa mặt.\n" +
                "\n" +
                " Loại da phù hợp: Phù hợp với mọi loại da, đặc biệt là da nhạy cảm thiên DẦU, DA DẦU MỤN, da đang điều trị và trẻ em từ 1 tuôỉ\n" +
                "\n" +
                " Hướng dẫn sử dụng:\n" +
                "\n" +
                " • Lắc thật kỹ trước khi sử dụng. \n" +
                "\n" +
                " • Dùng sau bước dưỡng da buổi sáng, lấy một lượng vừa đủ ra tay và dàn đều lên toàn bộ khuôn mặt và vùng da cần được bảo vệ. Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn lau.\n" +
                "\n" +
                " • Lượng sử dụng: 2mg/1cm2 da\n" +
                "\n" +
                " • Thích hợp sử dụng hàng ngày và trong các hoạt động ngoài trời.\n" +
                "\n" +
                " • Có thể dùng làm lớp lót trang điểm bảo vệ da.\n" +
                "\n" +
                " • Thích hợp sử dụng cho mặt & toàn thân.\n" +
                "\n" +
                " • Dễ dàng làm sạch với sữa rửa mặt.\n" +
                "\n" +
                " Thông tin dị ứng (nếu có)\n" +
                "\n" +
                " • Tránh tiếp xúc với mắt. Nếu có, rửa ngay bằng nước lạnh hoặc nước ấm • Không sử dụng cho vùng da bị tổn thương như trầy xước, sưng tấy và chàm • Ngưng sử dụng khi có biểu hiện kích ứng và tham khảo ý kiến bác sĩ da liễu.\n" +
                "\n" +
                " Hướng dẫn bảo quản:\n" +
                "\n" +
                " • Bảo quản tránh ánh sáng trực tiếp, nơi có nhiệt độ cao hoặc ẩm ướt. Để xa tầm tay trẻ em\n" +
                "\n" +
                " Ngày sản xuất: Xem trên bao bì sản phẩm\n" +
                "\n" +
                " Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                " Xuất xứ thương hiệu: Nhật Bản\n" +
                "\n" +
                " Nơi sản xuất: Nhật Bản\n" +
                "\n" +
                " Thông tin thương hiệu: ANESSA trực thuộc Tập đoàn Shiseido - tập đoàn mỹ phẩm lớn nhất Nhật Bản với lịch sử phát triển hàng thế kỉ. Shiseido bắt đầu nghiên cứu về UV từ 1915 và ra mắt thương hiệu chống nắng ANESSA vào 1992. ANESSA thừa hưởng hơn 100 năm nghiên cứu và phát triển sản phẩm chống UV. Tới năm 2022, ANESSA là thương hiệu chống nắng số 1 Nhật Bản suốt 21 năm liên tiếp.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/f5f700ed95b6e570d12d0a151ac339eb");
        variant_1.setWeight(60.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_12() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sữa chống nắng bảo vệ hoàn hảo Anessa Perfect UV Sunscreen Skincare Milk 60ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-8uz9qu6ck3kvc7");
        images.add("https://cf.shopee.vn/file/52f524ea884f2c11a0cdf0aab9e4081d");
        images.add("https://cf.shopee.vn/file/1e3ea9c5c63df6759f23f6841ebf6932");
        images.add("https://cf.shopee.vn/file/0d2c7ea900f4a62f334c28cbaca81258");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/fc7d182f29d8a5eda35c90ef8aff663d");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Sữa chống nắng dưỡng da kiềm dầu bảo vệ hoàn hảo giúp chống UV & bụi mịn hoàn hảo dưới mọi điều kiện sinh hoạt, kể cả thời tiết khắc nghiệt nhất (phù hợp với làn da thiên dầu)\n" +
                "\n" +
                " Thành phần và Công dụng:\n" +
                "\n" +
                " • Công nghệ AUTO BOOSTER độc quyền giúp lớp màng chống UV trở nên bền vững hơn khi gặp NHIỆT ĐỘ CAO-ĐỘ ẨM-MỒ HÔI-NƯỚC & CHỐNG TRÔI DO MA SÁT. \n" +
                "\n" +
                "  • Chỉ số chống nắng cao với SPF 50+, PA++++ giúp bảo vệ làn da khỏi tác hại của tia UV.\n" +
                "\n" +
                " • Công nghệ Very Water Resistant giúp chống trôi trong nước đến 80'.\n" +
                "\n" +
                " • Chống bụi mịn PM2.5 và chống dính cát\n" +
                "\n" +
                " • 50% thành phần dưỡng da gồm: Super HA, Collagen, Glycerin (giúp giữ ẩm nâng cơ), Rễ Hoa Hồng Vàng (chống giãn mao mạch), Chiết Xuất Trà Xanh Uji Kyoto (chống oxy hóa), Rễ Cam Thảo (giảm viêm, ngừa mụn) giúp chống lão hóa sớm do UV & dưỡng da mịn mượt.\n" +
                "\n" +
                " • Kết cấu sữa mỏng nhẹ, kiềm dầu vượt trội nhưng vẫn duy trì độ ẩm và dưỡng da ráo mịn cả ngày.\n" +
                "\n" +
                " • Có thể dùng làm kem lót trang điểm và dễ dàng làm sạch với sữa rửa mặt. \n" +
                "\n" +
                " • Dùng cho mặt & toàn thân. Thích hợp sử dụng hàng ngày và trong các hoạt động ngoài trời.\n" +
                "\n" +
                " • Hương hoa và trái cây nhẹ nhàng, dễ chịu có thể làm dịu mùi mồ hôi với công nghệ cảm biến mùi hương\n" +
                "\n" +
                " • Đã được kiểm định bởi chuyên gia da liễu.\n" +
                "\n" +
                " Loại da phù hợp: • Phù hợp với làn da thiên DẦU.\n" +
                "\n" +
                " Hướng dẫn sử dụng:\n" +
                "\n" +
                " • Lắc thật kỹ trước khi sử dụng. \n" +
                "\n" +
                " • Dùng sau bước dưỡng da buổi sáng, lấy một lượng vừa đủ ra tay và dàn đều lên toàn bộ khuôn mặt và vùng da cần được bảo vệ. Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn lau.\n" +
                "\n" +
                " • Lượng sử dụng: 2mg/1cm2 da\n" +
                "\n" +
                " • Thích hợp sử dụng hàng ngày và trong các hoạt động ngoài trời.\n" +
                "\n" +
                " • Có thể dùng làm lớp lót trang điểm bảo vệ da.\n" +
                "\n" +
                " • Thích hợp sử dụng cho mặt & toàn thân.\n" +
                "\n" +
                " • Dễ dàng làm sạch với sữa rửa mặt.\n" +
                "\n" +
                " Thông tin dị ứng (nếu có)\n" +
                "\n" +
                " • Tránh tiếp xúc với mắt. Nếu có, rửa ngay bằng nước lạnh hoặc nước ấm • Không sử dụng cho vùng da bị tổn thương như trầy xước, sưng tấy và chàm • Ngưng sử dụng khi có biểu hiện kích ứng và tham khảo ý kiến bác sĩ da liễu.\n" +
                "\n" +
                " Hướng dẫn bảo quản:\n" +
                "\n" +
                " • Bảo quản tránh ánh sáng trực tiếp, nơi có nhiệt độ cao hoặc ẩm ướt. Để xa tầm tay trẻ em\n" +
                "\n" +
                " Ngày sản xuất: Xem trên bao bì sản phẩm\n" +
                "\n" +
                " Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "\n" +
                " Xuất xứ thương hiệu: Nhật Bản\n" +
                "\n" +
                " Nơi sản xuất: Nhật Bản\n" +
                "\n" +
                " Thông tin thương hiệu: ANESSA trực thuộc Tập đoàn Shiseido - tập đoàn mỹ phẩm lớn nhất Nhật Bản với lịch sử phát triển hàng thế kỉ. Shiseido bắt đầu nghiên cứu về UV từ 1915 và ra mắt thương hiệu chống nắng ANESSA vào 1992. ANESSA thừa hưởng hơn 100 năm nghiên cứu và phát triển sản phẩm chống UV. Tới năm 2022, ANESSA là thương hiệu chống nắng số 1 Nhật Bản suốt 21 năm liên tiếp.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/fc7d182f29d8a5eda35c90ef8aff663d");
        variant_1.setWeight(60.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_13() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Bộ 2 Sữa chống nắng dưỡng da kiềm dầu bảo vệ hoàn hảo Anessa Perfect UV Sunscreen Skincare Milk SPF50+ PA++++ 60mlx2");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/97f9c5b62e9d8c9e81a4a153b8baac59");
        images.add("https://cf.shopee.vn/file/b14cc417a99591fce926ec3887f4ae39");
        images.add("https://cf.shopee.vn/file/20e2ba9b48622ca91da5777b8213b5a7");
        images.add("https://cf.shopee.vn/file/d50266972721ac6d51cba7662e035a46");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-y8j2xjcfk3kv74");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Bộ sản phẩm gồm:\n" +
                "02 Sữa chống nắng dưỡng da kiềm dầu bảo vệ hoàn hảo Anessa Perfect UV Sunscreen Skincare Milk SPF50+ PA++++ 60ml\n" +
                "Sữa chống nắng dưỡng da kiềm dầu bảo vệ hoàn hảo giúp chống UV & bụi mịn hoàn hảo dưới mọi điều kiện sinh hoạt, kể cả thời tiết khắc nghiệt nhất (phù hợp với làn da thiên dầu)\n" +
                "Thành phần và Công dụng:\n" +
                "• Công nghệ AUTO BOOSTER độc quyền giúp lớp màng chống UV trở nên bền vững hơn khi gặp NHIỆT ĐỘ CAO-ĐỘ ẨM-MỒ HÔI-NƯỚC & CHỐNG TRÔI DO MA SÁT.\n" +
                " • Chỉ số chống nắng cao với SPF 50+, PA++++ giúp bảo vệ làn da khỏi tác hại của tia UV.  \n" +
                "• Công nghệ Very Water Resistant giúp chống trôi trong nước đến 80'.\n" +
                "• Chống bụi mịn PM2.5 và chống dính cát\n" +
                "• 50% thành phần dưỡng da gồm: Super HA, Collagen, Glycerin (giúp giữ ẩm nâng cơ), Rễ Hoa Hồng Vàng (chống giãn mao mạch), Chiết Xuất Trà Xanh Uji Kyoto (chống oxy hóa), Rễ Cam Thảo (giảm viêm, ngừa mụn) giúp chống lão hóa sớm do UV & dưỡng da mịn mượt.\n" +
                "• Kết cấu sữa mỏng nhẹ, kiềm dầu vượt trội nhưng vẫn duy trì độ ẩm và dưỡng da ráo mịn cả ngày.\n" +
                "• Có thể dùng làm kem lót trang điểm và dễ dàng làm sạch với sữa rửa mặt. \n" +
                "• Dùng cho mặt & toàn thân. Thích hợp sử dụng hàng ngày và trong các hoạt động ngoài trời.\n" +
                "• Hương hoa và trái cây nhẹ nhàng, dễ chịu có thể làm dịu mùi mồ hôi với công nghệ cảm biến mùi hương\n" +
                "• Đã được kiểm định bởi chuyên gia da liễu.\n" +
                "Loại da phù hợp: • Phù hợp với làn da thiên DẦU.\n" +
                "Hướng dẫn sử dụng:\n" +
                "• Lắc thật kỹ trước khi sử dụng. \n" +
                "• Dùng sau bước dưỡng da buổi sáng, lấy một lượng vừa đủ ra tay và dàn đều lên toàn bộ khuôn mặt và vùng da cần được bảo vệ. Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn lau.\n" +
                "• Lượng sử dụng: 2mg/1cm2 da\n" +
                "• Thích hợp sử dụng hàng ngày và trong các hoạt động ngoài trời.\n" +
                "• Có thể dùng làm lớp lót trang điểm bảo vệ da.\n" +
                "•  Thích hợp sử dụng cho mặt & toàn thân.\n" +
                "• Dễ dàng làm sạch với sữa rửa mặt.\n" +
                "Thông tin dị ứng (nếu có)\n" +
                "• Tránh tiếp xúc với mắt. Nếu có, rửa ngay bằng nước lạnh hoặc nước ấm • Không sử dụng cho vùng da bị tổn thương như trầy xước, sưng tấy và chàm • Ngưng sử dụng khi có biểu hiện kích ứng và tham khảo ý kiến bác sĩ da liễu.\n" +
                "Hướng dẫn bảo quản:\n" +
                "• Bảo quản tránh ánh sáng trực tiếp, nơi có nhiệt độ cao hoặc ẩm ướt. Để xa tầm tay trẻ em\n" +
                "Ngày sản xuất: Xem trên bao bì sản phẩm\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "Xuất xứ thương hiệu: Nhật Bản\n" +
                "Nơi sản xuất: Nhật Bản\n" +
                "Thông tin thương hiệu: ANESSA trực thuộc Tập đoàn Shiseido - tập đoàn mỹ phẩm lớn nhất Nhật Bản với lịch sử phát triển hàng thế kỉ. Shiseido bắt đầu nghiên cứu về UV từ 1915 và ra mắt thương hiệu chống nắng ANESSA vào 1992. ANESSA thừa hưởng hơn 100 năm nghiên cứu và phát triển sản phẩm chống UV. Tới năm 2022, ANESSA là thương hiệu chống nắng số 1 Nhật Bản suốt 21 năm liên tiếp.\n" +
                "Lưu ý: Bao bì thay đổi tùy đợt nhập hàng");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-y8j2xjcfk3kv74");
        variant_1.setWeight(120.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1500));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_14() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem Chống Nắng Shiseido Anessa Perfect UV Sunscreen Skincare Milk SPF50+/PA+++ 60ml");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-gy9kejhsiykve2");
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-2adtudhsiykv53");
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-ecukojhsiykvc9");
        images.add("https://cf.shopee.vn/file/sg-11134201-22120-icewqchsiykv74");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-3myteehsiykvd4");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Kem chống nắng anessa Perfect UV Sunscreen Skincare Milk SPF50+/PA++++ 60ml\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Dung tích: 60ml\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "NSX: In trên bao bì sản phẩm\n" +
                "Xuất xứ thương hiệu: Nhật Bản\n" +
                "Nơi sản xuất: Nhật Bản\n" +
                "\n" +
                "\uD835\uDC0B\uD835\uDC22\uD835\uDC29\uD835\uDC2C\uD835\uDC2D\uD835\uDC22\uD835\uDC1C\uD835\uDC24 \uD835\uDC0E\uD835\uDC1F\uD835\uDC1F\uD835\uDC22\uD835\uDC1C\uD835\uDC1A\uD835\uDC25 \uD835\uDC12\uD835\uDC2D\uD835\uDC28\uD835\uDC2B\uD835\uDC1E xin giới thiệu sản phẩm Kem chống nắng anessa Perfect UV Sunscreen Skincare Milk SPF50+/PA++++\n" +
                " - phiên bản nâng cấp mới nhất đến từ thương hiệu chống nắng Anessa đứng số 1 Nhật Bản 20 năm liền, ứng dụng những công nghệ tiên tiến nhất về chống nắng giúp mang đến cho làn da bạn sự chăm sóc tối ưu để đẩy lùi những dấu hiệu lão hóa bởi UV, giúp bạn hoàn toàn có thể tự tin về một làn da sáng khỏe rạng rỡ.\n" +
                "\n" +
                "\n" +
                "ƯU THẾ NỔI BẬT: Kem chống nắng anessa Perfect UV Sunscreen Skincare Milk SPF50+/PA++++\n" +
                "\n" +
                "✔️Bộ ba công nghệ độc quyền giúp lớp màng chống UV trở nên mạnh mẽ hơn khi gặp NƯỚC - MỒ HÔI - NHIỆT ĐỘ CAO:\n" +
                "\n" +
                "✔️Công nghệ Thermo Booster: các phân tử chống nắng hình thành liên kết chặt chẽ hơn khi gặp nhiệt độ cao (do ánh mặt trời, thời tiết nắng nóng).\n" +
                "\n" +
                "✔️Công nghệ Aqua Booster EX giúp giúp lớp màng chống UV trở nên mạnh mẽ hơn khi gặp NƯỚC - MỒ HÔI và giúp chống trôi do ma sát.\n" +
                "\n" +
                "✔️Công nghệ Very Water-Resistant cho khả năng chống trôi trong nước tối đa, lên đến 80 phút liên tục dưới nước.\n" +
                "\n" +
                "\uD83C\uDF1ECông thức Kem chống nắng anessa Perfect UV Sunscreen Skincare Milk SPF50+/PA++++\n" +
                "chứa 50% thành phần dưỡng da giúp ngăn ngừa lão hóa do tia UV hiệu quả:\n" +
                "\n" +
                "✔️Kem chống nắng anessa cấp ẩm và tăng độ đàn hồi với Super Hyaluronic Acid, Collagen, Glycerin.\n" +
                "\n" +
                "✔️Chống oxi hóa với chiết xuất Trà Xanh Uji Kyoto và hoa Nhung Tuyết.\n" +
                "\n" +
                "✔️Chống giãn mao mạch với chiết xuất hoa vàng Rosaceae/Tormentilla.\n" +
                "\n" +
                "✔️Kết cấu sữa mỏng nhẹ, thấm nhanh mà không gây nhờn rít hay bóng dầu, mang đến làn da mịn mượt và khô ráo nhẹ tênh. \n" +
                "\n" +
                "✔️Có thể dùng làm lớp lót trang điểm.\n" +
                "\n" +
                "✔️Dễ dàng làm sạch với sữa rửa mặt / xà phòng.\n" +
                "\n" +
                "✔️Hương thơm nhẹ nhàng, thanh mát từ Cam Quýt.\n" +
                "\n" +
                "✔️Không chất tạo màu\n" +
                "\n" +
                "\n" +
                "HƯỚNG DẪN SỬ DỤNG:\n" +
                "\n" +
                "Lắc đều trước khi sử dụng kem chống nắng anessa\n" +
                "Dùng sau bước dưỡng da, thoa đều khắp vùng da cần bảo vệ.\n" +
                "Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn.\n" +
                "Dễ dàng làm sạch với sữa rửa mặt.\n" +
                " \n" +
                "*** Bao bì sản phẩm có thể thay đổi tùy từng đợt nhập hàng\n" +
                "\n" +
                "#kem #chong #nang #anessa #kemchongnang #kemchongnanganessa");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-3myteehsiykvd4");
        variant_1.setWeight(60.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_15() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("KEM CHỐNG NẮNG ANESSA MẪU MỚI NHẤT 2022 60ML skinker");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-qfqzyqrcgxive1");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-s7x78vrcgxiv43");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-0m0yjzrcgxiv22");
        images.add("https://cf.shopee.vn/file/sg-11134201-22100-w0exr2rcgxiv4a");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-w0exr2rcgxiv4a");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "THÔNG TIN SẢN PHẨM: ...\n" +
                "\n" +
                " Công dụng - Kết cấu dạng sữa mỏng nhẹ, khô ráo, dễ dàng thẩm thấu qua da, không gây nhờn rít. - Chỉ số chống nắng cao với SPF 50+, PA++++, bảo vệ da tối ưu.\n" +
                " - Công nghệ chống nắng 360° ngăn chặn tác hại của tia UV trên mọi bề mặt da và mọi góc độ. - Công nghệ độc quyền Aqua Booster, ngăn trôi trong nước & mồ hôi rất cao, lên đến 80 phút trong hồ bơi.\n" +
                "-Công nghệ “chống ma sát” độc đáo lần đầu tiên có trong sữa chống nắng, càng ma sát, lớp chống nắng mịn mượt sẽ không bị bong ra và gia tăng khả năng chống nắng, bảo vệ da hơn. \n" +
                "- ngăn cát dính vào da. - 50% chiết xuất dưỡng da (chiết xuất hoa hồng, collagen, lô hội và super Hyaluronic Acid) giúp da mịn mượt, gia tăng độ đàn hồi, ngăn oxi hoá và kiểm soát bóng dầu.\n" +
                " - Công thức với mùi hương thanh mát từ Cam, Quýt dễ chịu. - Có thể dùng làm lớp lót trang điểm và dễ dàng làm sạch với sữa rửa mặt. 　　　\n" +
                " Độ an toàn - Không chất tạo màu. \n" +
                "- Được kiểm định bởi chuyên gia da liễu.\n" +
                "Hướng dẫn sử dụng : \n" +
                " - Lắc đều trước khi sử dụng.\n" +
                " - Dùng sau bước dưỡng da, thoa đều khắp vùng da cần bảo vệ. - Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn. - Dễ dàng làm sạch với sữa rửa mặt. \n" +
                "Lưu ý - Tránh tiếp xúc với mắt.     \n" +
                " Nếu có, rửa ngay bằng nước lạnh hoặc nước ấm. \n" +
                "- Bảo quản tránh ánh sáng trực tiếp, nơi có nhiệt độ cao hoặc ẩm ướt. \n" +
                "- Để xa tầm tay trẻ em. \n" +
                "- Không sử dụng cho vùng da bị tổn thương.\n" +
                " - Ngưng dùng ngay khi có biểu hiện kích ứng và tham khảo ý kiến bác sĩ da liễu:\n" +
                "#anessa\n" +
                "#kemchongnanganessa\n" +
                "#kcnanessa\n" +
                "#anessachongnang\n" +
                "#kemchongnangnhatban\n" +
                "#kemchongnanganessanhatban\n" +
                "#anessa60ml\n" +
                "#kemanessa\n" +
                "#anessa");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22100-w0exr2rcgxiv4a");
        variant_1.setWeight(60.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(950));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_16() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Gel Chống Nắng Anessa Dưỡng Sáng Nâng Tông & Hiệu Chỉnh Sắc Da Brightening UV SPF50+ PA++++ 90g");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/83533f21be5ae4ee41f35df9b6c2d9d8");
        images.add("https://cf.shopee.vn/file/a1ec2f0a772c0360e951ef06b1b2b027");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/1410b6fcc7997b8d2d51bc07299a6787");
        productDTO.setDescription("\n" +
                "MÔ TẢ SẢN PHẨM\n" +
                "Gel Chống Nắng Anessa Dưỡng Sáng Nâng Tông & Hiệu Chỉnh Sắc Da Brightening UV SPF50+ PA++++ 90g\n" +
                "\n" +
                "Gel Chống Nắng Anessa Dưỡng Sáng Nâng Tông & Hiệu Chỉnh Sắc Da Brightening UV SPF50+ PA++++ 90g với kết cấu gel ẩm mượt, mát mịn giúp duy trì độ ẩm cho làn da, ngăn ngừa các dấu hiệu lão hóa da do tia UV, bảo vệ da trước tác hại của bụi mịn (phù hợp làn da thiên khô, xỉn màu)\n" +
                "\n" +
                "Thành Phần Chính và Công Dụng:\n" +
                "\n" +
                "- Chống nắng mạnh mẽ với SPF50+ PA++++ & công nghệ Water Resistant giúp chống trôi trong nước đến 40'.\n" +
                "\n" +
                "- Chống bụi mịn PM2.5.\n" +
                "\n" +
                "- m-Tranexamic Acid giúp ngăn ngừa đốm nâu, thâm nám và tình trạng thô ráp, xỉn màu do tác động của tia UV.\n" +
                "\n" +
                "- Glycyrrhizic Acid giúp phục hồi da tổn thương do UV, dưỡng da mịn mượt.\n" +
                "\n" +
                "- 50% thành phần dưỡng da gồm: Super HA, Collagen, Glycerin (giúp giữ ẩm nâng cơ), Hoa Hồng Vàng (chống giãn mao mạch), Chiết Xuất Trà Xanh Uji Kyoto (chống oxy hóa) giúp chống lão hóa sớm do UV.\n" +
                "\n" +
                "- Kết cấu gel mỏng nhẹ, mát mịn duy trì độ ẩm đến 8 giờ.\n" +
                "\n" +
                "- Sản phẩm có sắc hồng tím oải hương giúp nâng tông và hiệu chỉnh sắc da, làn da sẽ trở nên đều màu, rạng rỡ và sáng mịn. trị mụn\n" +
                "\n" +
                "- Có thể dùng làm lớp lót trang điểm và dễ dàng làm sạch với sữa rửa mặt.\n" +
                "\n" +
                "- Hương hoa và trái cây nhẹ nhàng, dễ chịu có thể làm dịu mùi mồ hôi với công nghệ cảm biến mùi hương.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng Dẫn Sử Dụng:\n" +
                "\n" +
                "- Thoa trực tiếp lên vùng da cần bảo vệ.\n" +
                "\n" +
                "- Dùng cho mặt. Thích hợp sử dụng hàng ngày.\n" +
                "\n" +
                "- Có thể thoa lai sau mỗi 4 giờ khi tiếp xúc với nước như bơi lội.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hạn sử dụng: 3 năm từ ngày sản xuất\n" +
                "\n" +
                "Xuất xứ Nhật Bản\n" +
                "\n" +
                "#anessa        #gelchốngnắng #nângtông\n" +
                "Để bật chế độ hỗ trợ đọc màn hình, nhấn Ctrl+Alt+Z Để tìm hiểu thêm về các phím tắt, nhấn Ctrl+dấu gạch chéo\n" +
                " \n" +
                " \n" +
                " \t\t\n" +
                "MÔ TẢ SẢN PHẨM\n" +
                "Gel Chống Nắng Anessa Dưỡng Sáng Nâng Tông & Hiệu Chỉnh Sắc Da Brightening UV SPF50+ PA++++ 90g\n" +
                "\n" +
                "Gel Chống Nắng Anessa Dưỡng Sáng Nâng Tông & Hiệu Chỉnh Sắc Da Brightening UV SPF50+ PA++++ 90g với kết cấu gel ẩm mượt, mát mịn giúp duy trì độ ẩm cho làn da, ngăn ngừa các dấu hiệu lão hóa da do tia UV, bảo vệ da trước tác hại của bụi mịn (phù hợp làn da thiên khô, xỉn màu)\n" +
                "\n" +
                "Thành Phần Chính và Công Dụng:\n" +
                "\n" +
                "- Chống nắng mạnh mẽ với SPF50+ PA++++ & công nghệ Water Resistant giúp chống trôi trong nước đến 40'.\n" +
                "\n" +
                "- Chống bụi mịn PM2.5.\n" +
                "\n" +
                "- m-Tranexamic Acid giúp ngăn ngừa đốm nâu, thâm nám và tình trạng thô ráp, xỉn màu do tác động của tia UV.\n" +
                "\n" +
                "- Glycyrrhizic Acid giúp phục hồi da tổn thương do UV, dưỡng da mịn mượt.\n" +
                "\n" +
                "- 50% thành phần dưỡng da gồm: Super HA, Collagen, Glycerin (giúp giữ ẩm nâng cơ), Hoa Hồng Vàng (chống giãn mao mạch), Chiết Xuất Trà Xanh Uji Kyoto (chống oxy hóa) giúp chống lão hóa sớm do UV.\n" +
                "\n" +
                "- Kết cấu gel mỏng nhẹ, mát mịn duy trì độ ẩm đến 8 giờ.\n" +
                "\n" +
                "- Sản phẩm có sắc hồng tím oải hương giúp nâng tông và hiệu chỉnh sắc da, làn da sẽ trở nên đều màu, rạng rỡ và sáng mịn. trị mụn\n" +
                "\n" +
                "- Có thể dùng làm lớp lót trang điểm và dễ dàng làm sạch với sữa rửa mặt.\n" +
                "\n" +
                "- Hương hoa và trái cây nhẹ nhàng, dễ chịu có thể làm dịu mùi mồ hôi với công nghệ cảm biến mùi hương.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hướng Dẫn Sử Dụng:\n" +
                "\n" +
                "- Thoa trực tiếp lên vùng da cần bảo vệ.\n" +
                "\n" +
                "- Dùng cho mặt. Thích hợp sử dụng hàng ngày.\n" +
                "\n" +
                "- Có thể thoa lai sau mỗi 4 giờ khi tiếp xúc với nước như bơi lội.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Hạn sử dụng: 3 năm từ ngày sản xuất\n" +
                "\n" +
                "Xuất xứ Nhật Bản\n" +
                "\n" +
                "#anessa        #gelchốngnắng #nângtông\n" +
                "Bật chế độ hỗ trợ trình đọc màn hình\n" +
                "2 cộng tác viên đã tham gia vào tài liệu.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/1410b6fcc7997b8d2d51bc07299a6787");
        variant_1.setWeight(90.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_17() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("combo Sữa Chống Nắng Anessa Cho Da Nhạy Cảm Và Trẻ Em (SPF50 PA+++ 60ml + SPF35 PA+++ 90g)");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/593d04f2b850ddbff85969df6a28634f");
        images.add("https://cf.shopee.vn/file/sg-11134201-22090-s7p55tvaxxhvc5");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22090-e0v076laxxhvde");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Combo Sữa Chống Nắng Anessa Cho Da Nhạy Cảm Và Trẻ Em (SPF50 PA+++ 60ml + SPF35 PA+++ 90g)\n" +
                "\n" +
                "Combo Sữa Chống Nắng Anessa Cho Da Nhạy Cảm Và Trẻ Em (SPF50 PA+++ 60ml + SPF35 PA+++ 90g) là bộ sản phẩm chống nắng với chỉ số SPF lần lượt SPF50 PA+++ và SPF35 PA+++ có khả năng chống nắng cho da nhạy cảm và trẻ em.\n" +
                "\n" +
                "Thông tin chi tiết:\n" +
                "1. Sữa Chống Nắng Anessa Cho Da Nhạy Cảm Perfect UV Mild Milk SPF50+/PA++++\n" +
                "Sữa Chống Nắng Anessa Cho Da Nhạy Cảm Perfect UV Mild Milk SPF50+/PA++++ là sản phẩm giúp bảo vệ làn da bạn, đặc biệt là da nhạy cảm và trẻ em dưới ánh nắng mặt trời mà không làm khô rát làn da. Sản phẩm với chiết xuất thiên nhiên còn giúp dưỡng ẩm cho da, giúp da luôn căng mịn và đàn hồi.\n" +
                "\n" +
                "Thành phần chính và công dụng:\n" +
                "- Sữa Chống Nắng Anessa Cho Da Nhạy Cảm Perfect UV Mild Milk SPF50+/PA++++ dưỡng da và bảo vệ da tối đa với thành phần 5 Không cho da nhạy cảm: Không màu, không mùi, không cồn, không dầu khoáng và không paraben.\n" +
                "\n" +
                "- Công nghệ Smooth Protect: một công thức cấp độ nano trải đều trên da của bạn và bảo vệ đa hướng, giúp làn da nhạy cảm của bạn thoải mái dễ chịu hơn khi sử dụng.\n" +
                "\n" +
                "- Công nghệ Aqua Booster Ex: Lớp màng chống UV hoạt động mạnh mẽ hơn khi gặp nước, mồ hôi và ma sát - những tác nhân chính gây trôi chống nắng.\n" +
                "\n" +
                "- Beauty Up Effect: nó giúp làn da trần của bạn trông tươi sáng và phủ sương khi tiếp xúc với ánh nắng mặt trời.\n" +
                "\n" +
                "Đối tượng sử dụng:\n" +
                "Sữa Chống Nắng Anessa Cho Da Nhạy Cảm Perfect UV Mild Milk SPF50+/PA++++ có thể sử dụng cho mặt và cả cơ thể, phù hợp mọi loại da.\n" +
                "\n" +
                "Hướng dẫn sử dụng:\n" +
                "- Lắc đều trước khi sử dụng.\n" +
                "- Dùng sau bước dưỡng da, thoa đều khắp vùng da cần bảo vệ.\n" +
                "- Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn.\n" +
                "- Dễ dàng làm sạch với sữa rửa mặt.\n" +
                "2. Gel Chống Nắng Anessa Moisture UV Sunscreen Mild Dịu Nhẹ Cho Da Nhạy Cảm & Trẻ Em SPF35 PA+++ 90g\n" +
                "Gel Chống Nắng Anessa Moisture UV Sunscreen Mild Dịu Nhẹ Cho Da Nhạy Cảm & Trẻ Em SPF35 PA+++ 90g là sản phẩm giúp bảo vệ làn da bạn, đặc biệt là da nhạy cảm và trẻ em dưới ánh nắng mặt trời mà không làm khô rát làn da. Sản phẩm với chiết xuất thiên nhiên còn giúp dưỡng ẩm cho da, giúp da luôn căng mịn và đàn hồi.\n" +
                "\n" +
                "Thành phần chính và công dụng:\n" +
                "- Gel Chống Nắng Anessa Moisture UV Sunscreen Mild Dịu Nhẹ Cho Da Nhạy Cảm & Trẻ Em SPF35 PA+++ 90g với kết cấu dạng gel dịu nhẹ và ẩm mượt, phù hợp cho cả da nhạy cảm và da trẻ em từ 1 tuổi.\n" +
                "\n" +
                "- Chỉ số chống nắng SPF35 với PA+++: an toàn cho làn da, phù hợp trong môi trường không tiếp xúc trực tiếp với ánh nắng mặt trời thường xuyên.\n" +
                "\n" +
                "- Thành phần 5 Không cho da nhạy cảm: Không màu, không mùi, không cồn, không dầu khoáng và không paraben.\n" +
                "\n" +
                "- Chống trôi trong nước tới 40 phút.\n" +
                "\n" +
                "- Cleansing Powder trong công thức trung hòa và chống lại tác hại của các hạt siêu vi, bụi mịn trong không khí.\n" +
                "\n" +
                "- 50% thành phần dưỡng da được nâng cấp với Chiết xuất Mẫu đơn giúp da mịn mượt.\n" +
                "\n" +
                "- Chống lại tia UVA/ UVB - nguyên nhân gây lão hóa da và bình thành các sắc tố Melanin\n" +
                "\n" +
                "- Sản phẩm lý tưởng dành cho các hoạt động ngoài trời như đi biển, chơi thể thao hoặc bơi lội.\n" +
                "\n" +
                "- Chứa nhiều dưỡng chất dưỡng ẩm chuyên sâu: Super Hyaluronic Acid, Collagen, chiết xuất hoa tuyết nhung, nha đam, trà xanh, hoa anh đào.\n" +
                "\n" +
                "Đối tượng sử dụng:\n" +
                "Gel Chống Nắng Anessa Moisture UV Sunscreen Mild Dịu Nhẹ Cho Da Nhạy Cảm & Trẻ Em SPF35 PA+++ 90g phù hợp sử dụng cho da nhạy cảm.\n" +
                "\n" +
                "Hướng dẫn sử dụng:\n" +
                "- Sử dụng sau bước dưỡng da, thoa đều khắp vùng da cần bảo vệ.\n" +
                "\n" +
                "- Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn.\n" +
                "\n" +
                "- Dễ dàng làm sạch với sữa rửa mặt.\n" +
                "\n" +
                "Xuất xứ thương hiệu:\n" +
                "Nhật Bản\n" +
                "\n" +
                "Hạn sử dụng: 3 năm từ ngày sản xuất");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22090-e0v076laxxhvde");
        variant_1.setWeight(150.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1400));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_18() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem chống nắng Anessa MARVEL Bản giới hạn");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a894735fe38d0eca4d06f283da165919");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/7eb8f357fc37ca9bb1718ac66b00b70e");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "shop về thêm bản giới hạn 2022 cho kem chống nắng anessa với 3 phiên bản khác nhau\n" +
                "ữa chống nắng dưỡng da kiềm dầu bảo vệ hoàn hảo giúp chống UV & bụi mịn hoàn hảo dưới mọi điều kiện sinh hoạt, kể cả thời tiết khắc nghiệt nhất (phù hợp với làn da thiên dầu)\n" +
                " Thành phần và Công dụng:\n" +
                " • Công nghệ AUTO BOOSTER độc quyền giúp lớp màng chống UV trở nên bền vững hơn khi gặp NHIỆT ĐỘ CAO-ĐỘ ẨM-MỒ HÔI-NƯỚC & CHỐNG TRÔI DO MA SÁT. \n" +
                "  • Chỉ số chống nắng cao với SPF 50+, PA++++ giúp bảo vệ làn da khỏi tác hại của tia UV.\n" +
                " • Công nghệ Very Water Resistant giúp chống trôi trong nước đến 80'.\n" +
                " • Chống bụi mịn PM2.5 và chống dính cát\n" +
                " • 50% thành phần dưỡng da gồm: Super HA, Collagen, Glycerin (giúp giữ ẩm nâng cơ), Rễ Hoa Hồng Vàng (chống giãn mao mạch), Chiết Xuất Trà Xanh Uji Kyoto (chống oxy hóa), Rễ Cam Thảo (giảm viêm, ngừa mụn) giúp chống lão hóa sớm do UV & dưỡng da mịn mượt.\n" +
                " • Kết cấu sữa mỏng nhẹ, kiềm dầu vượt trội nhưng vẫn duy trì độ ẩm và dưỡng da ráo mịn cả ngày.\n" +
                " • Có thể dùng làm kem lót trang điểm và dễ dàng làm sạch với sữa rửa mặt. \n" +
                " • Dùng cho mặt & toàn thân. Thích hợp sử dụng hàng ngày và trong các hoạt động ngoài trời.\n" +
                " • Hương hoa và trái cây nhẹ nhàng, dễ chịu có thể làm dịu mùi mồ hôi với công nghệ cảm biến mùi hương\n" +
                " • Đã được kiểm định bởi chuyên gia da liễu.\n" +
                " Loại da phù hợp: • Phù hợp với làn da thiên DẦU.\n" +
                "\n");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/7eb8f357fc37ca9bb1718ac66b00b70e");
        variant_1.setWeight(90.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1100));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_19() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Kem Chống Nắng Shisheido Anessa 60ml nhật bản");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/6b99268c6694e27565052be18a02397c");
        images.add("https://cf.shopee.vn/file/4a055fcb16ce350fef0f335001f668df");
        images.add("https://cf.shopee.vn/file/dec0ee21831a88726dbc89a38f9ac8f0");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-96zmiqd99xkv93");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "(Quý chị em vui lòng Check thông tin sản ANESSA CHÍNH HÃNG qua phần mềm ICHECK trên APP STORE & MÃ VẠCH sản phẩm trên bao bì ),\n" +
                "THÔNG TIN SẢN PHẨM :...\n" +
                "Công dụng - Kết cấu dạng sữa mỏng nhẹ, khô ráo, dễ dàng thẩm thấu qua da, không gây nhờn rít. - Chỉ số chống nắng cao với SPF 50+, PA++++, bảo vệ da tối ưu. ...\n" +
                "- Công nghệ chống nắng 360° ngăn chặn tác hại của tia UV trên mọi bề mặt da và mọi góc độ. - Công nghệ độc quyền Aqua Booster, hỗ trợ giảm  trôi trong nước & mồ hôi rất cao, lên đến 80 phút trong hồ bơi. \n" +
                "- Công nghệ “hỗ trợ giảm ma sát” độc đáo lần đầu tiên có trong sữa chống nắng, càng ma sát, lớp chống nắng mịn mượt sẽ không bị bong ra bảo vệ da hơn. Tác dụng :\n" +
                "- hỗ trợ giảm cát dính vào da. - 50% chiết xuất dưỡng da (chiết xuất hoa hồng, collagen, lô hội và super Hyaluronic Acid) giúp da mịn mượt, gia tăng độ đàn hồi.\n" +
                " - Công thức với mùi hương thanh mát từ Cam, Quýt dễ chịu. - Có thể dùng làm lớp lót trang điểm và dễ dàng làm sạch với sữa rửa mặt. 　　　 \n" +
                "Độ an toàn - Không chất tạo màu. - Được kiểm định bởi chuyên gia da liễu. \n" +
                "Đối tượng sử dụng - Thích hợp với mọi loại da. \n" +
                "Hướng dẫn sử dụng - Lắc đều trước khi sử dụng. \n" +
                "- Dùng sau bước dưỡng da, thoa đều khắp vùng da cần bảo vệ. \n" +
                "- Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn.\n" +
                "- Dễ dàng làm sạch với sữa rửa mặt. \n" +
                "#anessa, #anessa-chong-nang, #kem-chong-nang-anessa\n" +
                "#kem-chong-nang, #kcn-anessa\n" +
                "#anessa-chong-nang-nhat-ban\n" +
                "#kem-chong-nang-nhat-ban\n" +
                "#kem_chống_nắng_anessa\n" +
                "#anessa_kem_chống_nắng\n" +
                "#anessa\n" +
                "#anessa_chống_nắng\n" +
                "#kem_chống_nắng_nhật_bản\n" +
                "#kem_anessa\n" +
                "#kem_chống_nắng\n" +
                "#kcn_anessa\n" +
                "#kem_chống_nắng_anessa_nhật_bản\n" +
                "#chống_nắng_anessa\n" +
                "#anessa_chống_nắng\n" +
                "#anesa_kem_chống_nắng_nhật\n" +
                "#anessa\n" +
                "#kemchongnanganessa\n" +
                "#kcnanessa\n" +
                "#anessachongnang\n" +
                "#kemchongnangnhatban\n" +
                "#kemchongnanganessanhatban\n" +
                "#anessa60ml\n" +
                "#kemanessa\n" +
                "#anessa");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-96zmiqd99xkv93");
        variant_1.setWeight(60.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(800));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802545_110() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Bộ sản phẩm kem chống nắng Anessa bảo vệ cho cả gia đình");
        productDTO.setShopId(802545);
        productDTO.setTradeMarkId("1703772512364935");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sắc đẹp").getId());
        productDTO.setIndustrialTypeName("Sắc đẹp");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/b14cc417a99591fce926ec3887f4ae39");
        images.add("https://cf.shopee.vn/file/ee60d03c04385b7741210aa3aca54ebd");
        images.add("https://cf.shopee.vn/file/099b271ea91b4ab32dd4696fd982a383");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-w9h6yncjk3kv92");
        productDTO.setDescription("MÔ TẢ SẢN PHẨM\n" +
                "Bộ sản phẩm gồm:\n" +
                "1. Sữa chống nắng dưỡng da kiềm dầu bảo vệ hoàn hảo Anessa Perfect UV Sunscreen Skincare Milk SPF50+ PA++++  60ml\n" +
                "• Công nghệ AUTO BOOSTER độc quyền giúp lớp màng chống UV trở nên bền vững hơn khi gặp NHIỆT ĐỘ CAO-ĐỘ ẨM-MỒ HÔI-NƯỚC & CHỐNG TRÔI DO MA SÁT. \n" +
                " • Chỉ số chống nắng cao với SPF 50+, PA++++ giúp bảo vệ làn da khỏi tác hại của tia UV.\n" +
                "• Công nghệ Very Water Resistant giúp chống trôi trong nước đến 80'.\n" +
                "• Chống bụi mịn PM2.5 và chống dính cát\n" +
                "• 50% thành phần dưỡng da gồm: Super HA, Collagen, Glycerin (giúp giữ ẩm nâng cơ), Rễ Hoa Hồng Vàng (chống giãn mao mạch), Chiết Xuất Trà Xanh Uji Kyoto (chống oxy hóa), Rễ Cam Thảo (giảm viêm, ngừa mụn) giúp chống lão hóa sớm do UV & dưỡng da mịn mượt.\n" +
                "• Kết cấu sữa mỏng nhẹ, kiềm dầu vượt trội nhưng vẫn duy trì độ ẩm và dưỡng da ráo mịn cả ngày.\n" +
                "• Hương hoa và trái cây nhẹ nhàng, dễ chịu có thể làm dịu mùi mồ hôi với công nghệ cảm biến mùi hương\n" +
                "• Phù hợp với làn da thiên DẦU.\n" +
                "2. Sữa chống nắng dịu nhẹ cho da nhạy cảm và trẻ em Anessa MIld MIlk SPF50+ PA++++ 60ml \n" +
                "Sữa chống nắng dịu nhẹ cho da nhạy cảm và trẻ em với kết cấu mỏng nhẹ, kiềm dầu, mịn mượt & nâng tông (phù hợp mọi loại da, đặc biệt là da nhạy cảm thường đến thiên dầu)\n" +
                "• Kết cấu dạng sữa dịu nhẹ & ráo mịn phù hợp cho cả da nhạy cảm và da trẻ em từ 1 tuổi.\n" +
                " • Chỉ số chống nắng cao SPF 50+, PA++++ giúp bảo vệ da tối ưu khỏi tác hại của tia UV.\n" +
                " • Bảo vệ da khỏi tổn thương do tia UV & Bụi Mịn trong không khí.\n" +
                " • Công nghệ SmoothPROTECT giúp chống UV hoàn hảo từ mọi góc độ cùng kết cấu mịn mượt như lụa.\n" +
                " • Công nghệ Aqua Booster EX & Very Water Resistant giúp chống trôi do MA SÁT, NƯỚC, MỒ HÔi & chống trôi trong nước đến 80'.\n" +
                " • Hạt Cleansing Powder giúp trung hòa và chống lại tác hại của bụi mịn trong không khí.\n" +
                " • 50% thành phần dưỡng da được nâng cấp với Chiết xuất Mẫu đơn, Trà Xanh Kyoto Uji, Super Hyaluronic Acid, Collagen) giúp dưỡng da, tăng độ đàn hồi, chống oxi hoá.\n" +
                " • Thành phần 5 không (Cồn-Màu-Mùi-Paraben-Dầu khoáng) an toàn cho làn da nhạy cảm nhất.\n" +
                "Loại da phù hợp: Phù hợp với mọi loại da, đặc biệt là da nhạy cảm thiên DẦU, DA DẦU MỤN, da đang điều trị và trẻ em từ 1 tuôỉ\n" +
                "3. Xịt chống nắng dưỡng da đa năng bảo vệ hoàn hảo  Anessa Perfect UV Sunscreen Skincare Spray SPF50+ PA++++ 60g\n" +
                " • Công nghệ AUTO BOOSTER độc quyền giúp lớp màng chống UV trở nên bền vững khi tiếp xúc ĐỘ ẨM-MỒ HÔI-NƯỚC & CHỐNG TRÔI DO MA SÁT. \n" +
                " • Chỉ số chống nắng cao với SPF 50+, PA++++ giúp bảo vệ làn da khỏi tác hại của tia UV.\n" +
                " • Công nghệ Very Water Resistant giúp chống trôi trong nước đến 80'.\n" +
                " • Chống bụi mịn PM2.5 và chống dính cát.\n" +
                " • 50% thành phần dưỡng da gồm: Super HA, Collagen, Glycerin (giúp giữ ẩm nâng cơ), Rễ Hoa Hồng Vàng (chống giãn mao mạch), Chiết Xuất Trà Xanh Uji Kyoto (chống oxy hóa), Rễ Cam Thảo (giảm viêm, ngừa mụn) giúp chống lão hóa sớm do UV & dưỡng da mịn mượt.\n" +
                " • Kết cấu dạng xịt mỏng nhẹ, khô ráo, thẩm thấu nhanh, không bết dính cùng hiệu quả kiềm dầu tối ưu giúp làn da sáng rạng rỡ cả ngày. Tiện lợi có thể sử dụng trước & sau khi makeup, xịt lên tóc và dễ dàng thoa lại trong ngày. \n" +
                " • Phù hợp với mọi loại da, sử dụng được cho da và tóc.\n" +
                "Hướng dẫn sử dụng:\n" +
                "• Lắc thật kỹ trước khi sử dụng. \n" +
                "• Dùng sau bước dưỡng da buổi sáng, lấy một lượng vừa đủ ra tay và dàn đều lên toàn bộ khuôn mặt và vùng da cần được bảo vệ. Để đạt hiệu quả cao nhất, nên thoa lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn lau.\n" +
                " • Đối với xịt chống nắng: xịt đều lên vùng da cần được bảo vệ. Để đạt hiệu quả cao nhất, nên xịt lại sau khi tiếp xúc nhiều với nước hoặc lau bằng khăn. \n" +
                "• Lượng sử dụng: 2mg/1cm2 da\n" +
                "• Thích hợp sử dụng hàng ngày và trong các hoạt động ngoài trời.\n" +
                "• Có thể dùng làm lớp lót trang điểm bảo vệ da.\n" +
                "• Thích hợp sử dụng cho mặt & toàn thân.\n" +
                "• Dễ dàng làm sạch với sữa rửa mặt.\n" +
                "Thông tin dị ứng (nếu có)\n" +
                "• Tránh tiếp xúc với mắt. Nếu có, rửa ngay bằng nước lạnh hoặc nước ấm • Không sử dụng cho vùng da bị tổn thương như trầy xước, sưng tấy và chàm • Ngưng sử dụng khi có biểu hiện kích ứng và tham khảo ý kiến bác sĩ da liễu.\n" +
                "Hướng dẫn bảo quản:\n" +
                "• Bảo quản tránh ánh sáng trực tiếp, nơi có nhiệt độ cao hoặc ẩm ướt. Để xa tầm tay trẻ em\n" +
                "Ngày sản xuất: Xem trên bao bì sản phẩm\n" +
                "Hạn sử dụng: 3 năm kể từ ngày sản xuất\n" +
                "Xuất xứ thương hiệu: Nhật Bản\n" +
                "Nơi sản xuất: Nhật Bản\n" +
                "Thông tin thương hiệu: ANESSA trực thuộc Tập đoàn Shiseido - tập đoàn mỹ phẩm lớn nhất Nhật Bản với lịch sử phát triển hàng thế kỉ. Shiseido bắt đầu nghiên cứu về UV từ 1915 và ra mắt thương hiệu chống nắng ANESSA vào 1992. ANESSA thừa hưởng hơn 100 năm nghiên cứu và phát triển sản phẩm chống UV. Tới năm 2022, ANESSA là thương hiệu chống nắng số 1 Nhật Bản suốt 21 năm liên tiếp.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-w9h6yncjk3kv92");
        variant_1.setWeight(120.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1600));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //shop Unicharm - Gian hàng Chính hãng
    @Test
    public void testProduct_802550_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Bộ 3 hộp Bông trang điểm (bông tẩy trang) Silcot 82 miếng/hộp");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/9eba107a9bc5eea09d5aa8d3f9f3f0a2");
        images.add("https://cf.shopee.vn/file/edeaff8c771377448faac3cfe5d1ec5f");
        images.add("https://cf.shopee.vn/file/d3ca1ac2477c73deab28e7982ef70a45");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/f75dc87f00be1b67290a4a1804ebbf7b");
        productDTO.setDescription("** Bộ 3 hộp Bông trang điểm Silcot (82 miếng/hộp) là sản phẩm chăm sóc da cao cấp bán chạy số 1 Nhật Bản trong hơn 10 năm liền. Được làm từ 100% sợi bông tự nhiên, bông trang điểm mềm xốp, êm ái và vô cùng dịu nhẹ với da. Sợi bông thấm được dàn đều cùng thiết kế dạng túi thông minh, hoàn toàn không để lại xơ bông trên da, đồng thời giúp tiết kiệm nước dưỡng da tối ưu nhờ kết cấu sợi bông đặc biệt, giúp bạn có được lớp trang điểm tự nhiên hơn, hoặc tẩy trang dễ dàng và nhanh chóng hơn. Bông trang điểm Silcot thích hợp dùng trong quy trình chăm sóc da cơ bản\n" +
                "\n" +
                "ĐẶC ĐIỂM NỔI BẬT:\n" +
                "\n" +
                "Công nghệ hiện đại: Bông tẩy trang Unicharm Silcot được sản xuất theo công nghệ tiên tiến, được các chuyên gia kiểm soát nghiêm ngặt về chất lượng thành phẩm đầu vào và đầu ra. Sản phẩm đảm bảo tuyệt đối an toàn cho làn da người sử dụng, đồng thời rất thân thiện với môi trường\n" +
                "\n" +
                "Đóng hộp tiện lợi: Bông tẩy trang được đóng hộp giấy khá đẹp mắt và tiện dụng, bạn có thể cất gọn và bảo quản dễ dàng khi chưa sử dụng hết. Sản phẩm là sự lựa chọn hoàn hảo cho các bạn gái để hỗ trợ trang điểm.\n" +
                "\n" +
                "CÔNG DỤNG:\n" +
                "\n" +
                "- Giúp tẩy trang và làm sạch mọi bụi bẩn trên da\n" +
                "\n" +
                "- Nhẹ nhàng tẩy sạch toàn bộ làn da kể cả vùng da nhạy cảm quanh mắt và môi.\n" +
                "\n" +
                "- Giúp lấy đi lớp trang điểm 1 cách dễ dàng nhưng vẫn giữ lại độ ẩm cần thiết cho da mà không gây kích ứng hay tổn hại da.\n" +
                "\n" +
                "\n" +
                "HƯỚNG DẪN SỬ DỤNG VÀ BẢO QUẢN:\n" +
                "\n" +
                "- Thấm miếng bông với nước hoa hồng, dung dịch dưỡng da hoặc nước tẩy trang.\n" +
                "\n" +
                "- Nhẹ nhàng vỗ nhẹ lên toàn bộ khuôn mặt.\n" +
                "\n" +
                "- Bảo quản nơi khô ráo, thoáng mát, tránh ánh nắng trực tiếp và nhiệt độ cao.\n" +
                "\n" +
                "- Để xa tầm tay trẻ em.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU:\n" +
                "\n" +
                "Bông trang điểm Silcot được sản xuất bởi Unicharm Nhật Bản, nhập khẩu trực tiếp và phân phối bởi Diana Unicharm Việt Nam. Là sản phẩm bán chạy số 1 tại thị trường Nhật Bản trong hơn 10 năm liên tiếp (theo số liệu của công ty nghiên cứu thị trường Intage), bông trang điểm Silcot đã đồng hành cùng phương pháp chăm sóc da thông thái của phụ nữ Nhật trong suốt những năm qua, và hiện nay, Silcot đã chính thức có mặt tại thị trường Việt Nam.\n" +
                "\n" +
                "\n" +
                "Xuất xứ: Nhật Bản.\n" +
                "\n" +
                "Ngày sản xuất: xem trên bao bì.\n" +
                "\n" +
                "HSD: 4 năm kể từ NSX.\n" +
                "\n" +
                "** bao bì thay đổi theo từng đợt nhập hàng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/f75dc87f00be1b67290a4a1804ebbf7b");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(910));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Khẩu trang Unicharm 3D Mask Super Fit siêu thoáng khí hộp 100 miếng");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/c52a13172ec39032cb15f660f5f14eef");
        images.add("https://cf.shopee.vn/file/59c99d9dfc1801324db61362fccb738a");
        images.add("https://cf.shopee.vn/file/sg-11134201-22090-5buc74ejoxhv8a");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/073d7b796d2f06fd80281126b2e53040");
        productDTO.setDescription("\"( Màng Lọc AIR-FIT )Với thiết kế thông minh, độc đáo cùng nhiều tính năng vượt trội, khẩu trang ngăn khói bụi Unicharm 3D Mask Superfit là sản phẩm cần có trong mỗi gia đình. Sản phẩm sở hữu thiết kế 3D ôm vừa vặn đường cong khuôn mặt, mang lại cảm giác thoải mái, dễ chịu và không gây bí thở. Đặc biệt, cấu trúc lọc đa lớp mật độ cao giúp sản phẩm ngăn chặn khói bụi, phấn hoa trong không khí, bảo vệ sức khỏe người sử dụng. Hãy để khẩu trang ngăn khói bụi Unicharm 3D Mask Superfit chăm sóc sức khỏe cho mọi thành viên trong gia đình bạn.\n" +
                "\n" +
                "*Xuất xứ : Nhật Bản\n" +
                "*Hạn sử dụng : 4 Năm kể từ NSX\n" +
                "*Số lượng : 100 miếng/ hộp.\n" +
                "\n" +
                "ĐẶC ĐIỂM NỔI BẬT\n" +
                "Thiết kế 3D thông minh\n" +
                "Thiết kế 3D thông minh cho phép sản phẩm ôm vừa vặn đường cong khuôn mặt, cho cảm giác thoải mái, dễ chịu, không gây bí thở mà vẫn đảm bảo giữ nhiệt và độ ẩm cho mũi và họng vào mùa đông.\n" +
                "\n" +
                "Cấu trúc lọc đa lớp: Cấu trúc lọc đa lớp tiên tiến giúp sản phẩm ngăn khói bụi, phấn hoa trong không khí, bảo vệ sức khỏe người sử dụng.\n" +
                "\n" +
                "Quai đeo co giãn: Quai đeo với chất liệu co giãn và mềm mại, không gây cảm giác đau hay khó chịu trong quá trình sử dụng.\n" +
                "\n" +
                "Thoải mái giao tiếp, không lem son môi: Với cấu trúc đặc thù, khẩu trang Unicharm 3D Mask cho bạn thoải mái giao tiếp, đồng thời giúp phái đẹp giữ son môi không bị phai lem vào khẩu trang.\n" +
                "\n" +
                "Màu sắc tinh tế: Khác với các loại khẩu trang khác trên thị trường, khẩu trang Unicharm 3D Mask chỉ có duy nhất 1 màu trắng, giúp dễ dàng nhận biết được độ bám bẩn để thay cái mới, đảm bảo vệ sinh cho người sử dụng.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "Công ty Cổ phần Diana Unicharm là một Công ty hàng đầu trong ngành hàng sản xuất các sản phẩm chăm sóc phụ nữ và trẻ em tại Việt Nam (chuyên sản xuất các mặt hàng từ giấy và bột giấy như băng vệ sinh, tã giấy cho trẻ em, khăn giấy ăn với các thương hiệu nổi tiếng như Diana, Bobby, Caryn, Libera…). Sử dụng thế mạnh là thành viên của tập đoàn hàng đầu thế giới Unicharm Nhật Bản, công ty Diana Unicharm luôn đi đầu áp dụng những công nghệ mới nhất trong lĩnh vực sản xuất các sản phẩm chăm sóc vệ sinh cá nhân để sáng tạo ra những sản phẩm có chất lượng cao nhất và phù hợp nhất, góp phần nâng cao chất lượng cuộc sống của người tiêu dùng Việt Nam. Khẩu trang Unicharm 3D Mask là sản phẩm nổi bật trong việc chăm sóc sức khỏe tối ưu cho người dùng.\n" +
                "\n" +
                "bao bì thay đổi từng đợt nhập hàng\".");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/073d7b796d2f06fd80281126b2e53040");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(780));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Bộ 3 hộp Bông trang điểm (bông tẩy trang) cao cấp Silcot Premium 66 miếng/hộp");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/28e66da08f66eb69821e6156338c4f05");
        images.add("https://cf.shopee.vn/file/96a5db1e21df9205190b30516a124329");
        images.add("https://cf.shopee.vn/file/24d0161b4c1ba99341c9552b59a17218");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/8facd61c27dc1033e615bd1f63983cf1");
        productDTO.setDescription("**Bộ 3 hộp Bông trang điểm cao cấp Silcot Premium (66 miếng/hộp) là sản phẩm chăm sóc da cao cấp bán chạy số 1 Nhật Bản trong hơn 10 năm liền. Được làm từ 100% sợi bông tự nhiên, bông trang điểm mềm xốp, êm ái và vô cùng dịu nhẹ với da. Sợi bông thấm được dàn đều cùng thiết kế dạng túi thông minh, hoàn toàn không để lại xơ bông trên da, đồng thời giúp tiết kiệm nước dưỡng da tối ưu nhờ kết cấu sợi bông đặc biệt, giúp bạn có được lớp trang điểm tự nhiên hơn, hoặc tẩy trang dễ dàng và nhanh chóng hơn. Bông trang điểm Silcot thích hợp dùng trong quy trình chăm sóc da chuyên sâu với các loại nước dưỡng. \n" +
                "\n" +
                "\n" +
                "\n" +
                "ĐẶC ĐIỂM NỔI BẬT:\n" +
                "\n" +
                "Công nghệ hiện đại: Bông tẩy trang Unicharm Silcot được sản xuất theo công nghệ tiên tiến, được các chuyên gia kiểm soát nghiêm ngặt về chất lượng thành phẩm đầu vào và đầu ra. Sản phẩm đảm bảo tuyệt đối an toàn cho làn da người sử dụng, đồng thời rất thân thiện với môi trường.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Đóng hộp tiện lợi: Bông tẩy trang được đóng hộp giấy khá đẹp mắt và tiện dụng, bạn có thể cất gọn và bảo quản dễ dàng khi chưa sử dụng hết. Sản phẩm là sự lựa chọn hoàn hảo cho các bạn gái để hỗ trợ trang điểm.\n" +
                "\n" +
                "\n" +
                "\n" +
                "CÔNG DỤNG:\n" +
                "\n" +
                "- Giúp tẩy trang và làm sạch mọi bụi bẩn trên da\n" +
                "\n" +
                "- Nhẹ nhàng tẩy sạch toàn bộ làn da kể cả vùng da nhạy cảm quanh mắt và môi.\n" +
                "\n" +
                "- Giúp lấy đi lớp trang điểm 1 cách dễ dàng nhưng vẫn giữ lại độ ẩm cần thiết cho da mà không gây kích ứng hay tổn hại da.\n" +
                "\n" +
                "\n" +
                "\n" +
                "HƯỚNG DẪN SỬ DỤNG:\n" +
                "\n" +
                "- Khi dưỡng da: Tẩm nước hoa hồng lên miếng bông rồi sử dụng.\n" +
                "\n" +
                "- Khi tẩy trang (có 2 cách tẩy trang):\n" +
                "\n" +
                "+ Dùng dung dịch tẩy trang massage mặt, sau đó sử dụng bông tẩy trang lau sạch\n" +
                "\n" +
                "+ Thấm dung dịch tẩy trang lên bông, sau đó dùng bông lau những vùng da cần tẩy trang\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU:\n" +
                "\n" +
                "Bông trang điểm Silcot được sản xuất bởi Unicharm Nhật Bản, nhập khẩu trực tiếp và phân phối bởi Diana Unicharm Việt Nam. Là sản phẩm bán chạy số 1 tại thị trường Nhật Bản trong hơn 10 năm liên tiếp (theo số liệu của công ty nghiên cứu thị trường Intage), bông trang điểm Silcot đã đồng hành cùng phương pháp chăm sóc da thông thái của phụ nữ Nhật trong suốt những năm qua, và hiện nay, Silcot đã chính thức có mặt tại thị trường Việt Nam.\n" +
                "\n" +
                "\n" +
                "\n" +
                "***Chi tiết bao bì sản phẩm có thể thay đổi tùy đợt nhập hàng\n" +
                "\n" +
                "\n" +
                "\n" +
                "Xuất xứ: Nhật Bản.\n" +
                "\n" +
                "Ngày sản xuất: xem trên bao bì.\n" +
                "\n" +
                "Hạn sử dụng: 4 năm kể từ ngày sản xuất..");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/8facd61c27dc1033e615bd1f63983cf1");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(880));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Băng vệ sinh Diana Sensi siêu mỏng cánh 20 miếng/gói");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/dbad6aec5ce5b4e61a11d409db7d7815");
        images.add("https://cf.shopee.vn/file/f54c00edf70e2554e04497ae50d324ed");
        images.add("https://cf.shopee.vn/file/bd0fdeec0931b9959f5d46b09a2915df");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/3398cafb86bfb15fe9b607900a2e7070");
        productDTO.setDescription("Băng Vệ Sinh Diana Sensi Siêu Mỏng Cánh 20 được trang bị bề mặt lụa mới siêu mềm và hệ thống thấm hút độc đáo giúp thấm hút cực nhanh, chống thấm ngược tuyệt đối. Sản phẩm có đường dẫn thấm ngăn tràn và chống co dúm, cho bạn gái thêm năng động, tự tin hơn trong những ngày \"đèn đỏ\".\n" +
                "Diana Sensi Siêu Mỏng Cánh là băng vệ sinh dùng ban ngày giúp bạn nữ luôn tự tin trong những ngày đèn đỏ.\n" +
                "\n" +
                "Tính năng của sản phẩm\n" +
                "Công nghệ độc đáo với sự trang bị của tấm thấm hút mới, thấm hút tuyệt đối những chất dịch đặc giúp cho làn da của bạn luôn khô ráo, không ướt dính.\n" +
                "Bề mặt vi sợi mềm mại hơn, đem đến cảm giác nhẹ dịu cho làn da, đảm bảo không gây mẫn cảm hay kích ứng da.\n" +
                "Lõi thấm kép Duo Compact mới giúp miếng băng mỏng hơn mà vẫn đảm bảo khả năng thấm hút tức thì.\n" +
                "Kiểu dáng thời trang độc đáo vừa văn ôm sát cơ thể giúp bạn thoải mái vận động.\n" +
                "Loại da phù hợp: Sản phẩm thích hợp với mọi loại da.\n" +
                "\n" +
                "Hạn sử dụng: 4 năm kể từ ngày sản xuất.\n" +
                "Xuất xứ: Việt Nam\n" +
                "bao bì thay đổi theo từng đợt nhập hàng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/8facd61c27dc1033e615bd1f63983cf1");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(800));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Bộ 6 băng vệ sinh Diana Hàng ngày Sensi Compact gói 20 miếng");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/6dba9fe03a4ef3a8e85595d067001b7a");
        images.add("https://cf.shopee.vn/file/e1ff371d363d1713e2639442ed4adcfc");
        images.add("https://cf.shopee.vn/file/83464da92b22f9b76c86ea81b9f1aa3d");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/cf789f12e7f67e4fc32811ece4d11972");
        productDTO.setDescription("\"MÔ TẢ SẢN PHẨM\n" +
                "\n" +
                "Băng vệ sinh Diana SENSI Hàng ngày Compact (gói 20 miếng) là sản phẩm băng vệ sinh mới và duy nhất trên thị trường với thiết kế COMPACT FOLDING - MIẾNG GẬP SIÊU GỌN, BỎ TÚI VỪA VẶN, kích thước gập chỉ bằng ½ miếng BVS Hàng ngày thông thường, mỏng nhẹ như không nhưng vẫn giữ nguyên độ dài 155m để đáp ứng nhu cầu cầu giữ sạch sẽ mọi lúc mọi nơi của con gái. \n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM \n" +
                "\n" +
                "•        Xuất xứ: Việt Nam\n" +
                "\n" +
                "•        Nơi sản xuất: Việt Nam \n" +
                "\n" +
                "•        Gói 20/40 miếng nhỏ gọn\n" +
                "\n" +
                "•        Chiều dài: 155mm\n" +
                "\n" +
                "•        Hạn sử dụng: 5 năm kể từ ngày sản xuất \n" +
                "\n" +
                "ĐẶC ĐIỂM NỔI BẬT\n" +
                "\n" +
                "•        Thiết kế Compact Folding mới lạ, miếng gập siêu gọn chỉ bằng hai ngón tay, dễ dàng mang theo mọi nơi mọi lúc.\n" +
                "\n" +
                "•        Siêu mỏng nhẹ, vừa vặn cho mọi hoạt động hàng ngày.\n" +
                "\n" +
                "•        Bề mặt siêu êm với hệ đa lỗ siêu khô thoáng cho da\n" +
                "\n" +
                "•        Rãnh dập độc đáo mang lại cảm giác tươi mới mỗi ngày\n" +
                "\n" +
                "•        Dùng hàng ngày \n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU \n" +
                "\n" +
                "Diana là thương hiệu của Công ty chuyên sản xuất các sản phẩm chăm sóc phụ nữ và trẻ em tại Việt Nam từ nguyên liệu giấy, bột giấy, như băng vệ sinh, tã giấy, khăn giấy lụa,... Với thế mạnh là thành viên của tập đoàn hàng đầu thế giới Unicharm Nhật Bản cùng dây chuyền sản xuất hiện đại bậc nhất, Diana luôn đi đầu áp dụng những công nghệ mới nhất trong lĩnh vực sản xuất các sản phẩm chăm sóc vệ sinh cá nhân để sáng tạo ra những sản phẩm có chất lượng cao phù hợp với nhu cầu của người tiêu dùng Việt Nam.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Lưu ý: BAO BÌ THAY ĐỒI THEO ĐỢT SẢN XUẤT (bao bì cũ và mới được update trên hình ảnh sản phẩm, khuyến mãi miếng tặng kèm sẽ tùy vào mỗi đợt nhập hàng)\n" +
                "\n" +
                "\n" +
                "\n" +
                "*Update theo bao bì mới nhất.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/cf789f12e7f67e4fc32811ece4d11972");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(810));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Bộ 6 gói khẩu trang 3DMask Virus Block size M + Tặng kèm 1 gói khẩu trang nẹp mũi Max block 5 miếng size M");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/3a06258668ef9ed77d3ae688bab74a63");
        images.add("https://cf.shopee.vn/file/134cdbe47146a6915e7155add111d391");
        images.add("https://cf.shopee.vn/file/bd98617204fae4229bf71cc67625e9c7");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/ba779c6192d24dded123d9063fce05d3");
        productDTO.setDescription("Với thiết kế thông minh, độc đáo cùng nhiều tính năng vượt trội, khẩu trang ngăn khói bụi Unicharm 3D Mask High Block là sản phẩm cần có trong mỗi gia đình. Sản phẩm sở hữu thiết kế 3D ôm vừa vặn đường cong khuôn mặt, mang lại cảm giác thoải mái, dễ chịu và không gây bí thở. Đặc biệt, cấu trúc lọc đa lớp mật độ cao giúp sản phẩm ngăn chặn khói bụi, phấn hoa trong không khí, bảo vệ sức khỏe người sử dụng. Hãy để khẩu trang ngăn khói bụi Unicharm 3D Mask Virus Block chăm sóc sức khỏe cho mọi thành viên trong gia đình bạn.\n" +
                "\n" +
                "*Xuất xứ : Nhật Bản.\n" +
                "*Hạn sử dụng : 4 năm kể từ ngày sản xuất.\n" +
                "*Số lượng : 5 cái.\n" +
                "\n" +
                "ĐẶC ĐIỂM NỔI BẬT\n" +
                "Hiệu quả ngăn chặn 99% virus phát tán trong không khí\n" +
                "Khẩu trang 3D Mask Virus Block với cấu trúc lọc đa lớp giúp ngăn khói bụi, phấn hoa và 99% virus phát tán trong không khí.\n" +
                "\n" +
                "Thiết kế 3D thông minh\n" +
                "Thiết kế 3D thông minh cho phép sản phẩm ôm vừa vặn đường cong khuôn mặt, cho cảm giác thoải mái, dễ chịu, không gây bí thở mà vẫn đảm bảo giữ nhiệt và độ ẩm cho mũi và họng vào mùa đông.\n" +
                "\n" +
                "Cấu trúc lọc đa lớp\n" +
                "Cấu trúc lọc đa lớp tiên tiến giúp sản phẩm ngăn khói bụi, phấn hoa trong không khí, bảo vệ sức khỏe người sử dụng.\n" +
                "\n" +
                "Quai đeo co giãn\n" +
                "Quai đeo với chất liệu co giãn và mềm mại, không gây cảm giác đau hay khó chịu trong quá trình sử dụng.\n" +
                "\n" +
                "Thoải mái giao tiếp, không lem son môi\n" +
                "Với cấu trúc đặc thù, khẩu trang Unicharm 3D Mask cho bạn thoải mái giao tiếp, đồng thời giúp phái đẹp giữ son môi không bị phai lem vào khẩu trang.\n" +
                "\n" +
                "Màu sắc tinh tế \n" +
                "Khác với các loại khẩu trang khác trên thị trường, khẩu trang Unicharm 3D Mask chỉ có duy nhất 1 màu trắng, giúp dễ dàng nhận biết được độ bám bẩn để thay cái mới, đảm bảo vệ sinh cho người sử dụng.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "Công ty Cổ phần Diana Unicharm là một Công ty hàng đầu trong ngành hàng sản xuất các sản phẩm chăm sóc phụ nữ và trẻ em tại Việt Nam (chuyên sản xuất các mặt hàng từ giấy và bột giấy như băng vệ sinh, tã giấy cho trẻ em, khăn giấy ăn với các thương hiệu nổi tiếng như Diana, Bobby, Caryn, Libera…). \n" +
                "Sử dụng thế mạnh là thành viên của tập đoàn hàng đầu thế giới Unicharm Nhật Bản, công ty Diana Unicharm luôn đi đầu áp dụng những công nghệ mới nhất trong lĩnh vực sản xuất các sản phẩm chăm sóc vệ sinh cá nhân để sáng tạo ra những sản phẩm có chất lượng cao nhất và phù hợp nhất, góp phần nâng cao chất lượng cuộc sống của người tiêu dùng Việt Nam. Khẩu trang Unicharm 3D Mask là sản phẩm nổi bật trong việc chăm sóc sức khỏe tối ưu cho người dùng.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/ba779c6192d24dded123d9063fce05d3");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(820));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Tấm đệm lót Caryn loại Siêu Thấm 10 miếng");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/2b25523dac167f8cfb4dc2ba6c0cb24a");
        images.add(" https://cf.shopee.vn/file/f41f3617693cc3b5ccbbc5f7bb752327");
        images.add("https://cf.shopee.vn/file/d164f84a6e2471ba9d2b05390e0eea82");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/c3c1be77096c7d54c06c371d9276a8f1");
        productDTO.setDescription("Tấm đệm lót siêu thấm Caryn 10 miếng hoặc gói 20 miếng\n" +
                "\n" +
                "Tấm Đệm Lót Siêu Thấm Caryn (10 Miếng) được dùng kèm tã dán, thích hợp cho người dùng đã mặc tã có nhu cầu tăng cường bảo vệ chống trào, giúp giữ vệ sinh giường nằm và xe lăn, là lựa chọn tốt nhất cho người dùng hạn chế khả năng đi lại.\n" +
                "\n" +
                "CÔNG DỤNG:\n" +
                "+ Với cấu trúc 3 lớp thoáng khí, sản phẩm mang lại cảm giác thoải mái cho người dùng mà vẫn yên tâm không lo trào ngoài.\n" +
                "\n" +
                "+ Thiết kế mở rộng 4 chiều giúp người dùng thoải mái xoay trở, chống trào lưng hiệu quả.\n" +
                "+ Công thức Nano bạc kháng khuẩn ngăn mùi đến từ Nhật Bản giúp hạn chế sự phát triển của vi khuẩn và khử mùi hôi hiệu quả.\n" +
                "+ Màng đáy PE ngăn tuyệt đối chất lỏng ra ngoài, giữ giường bệnh, xe lăn,... luôn sạch sẽ.\n" +
                "+ Dải keo dính cố định giúp định vị miếng lót, chống xê dịch trong quá trình sử dụng.\n" +
                "\n" +
                "\n" +
                "HƯỚNG DẪN SỬ DỤNG:\n" +
                "+ Trải phẳng miệng đệm lót\n" +
                "+ Bóc miếng giấy che dải keo dính ở mặt đáy, rồi cố định các góc vào mặt phẳng để chống xê dịch.\n" +
                "+ Để người dùng nằm / ngồi lên giữa tấm đệm lót. Điều chỉnh cho phù hợp với người dùng.\n" +
                "\n" +
                "Lưu ý:\n" +
                "- Dùng để trải giường bệnh hoặc xe lăn\n" +
                "- Thay đệm lót theo khoảng thời gian đều đặn và ngay sau khi tiêu bẩn\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU:\n" +
                "Đi đầu trong ngành hàng chăm sóc người lớn tuổi, Unicharm ra mắt sản phẩm tã giấy dành riêng cho người lớn Unicharm Caryn với nhiều ưu điểm nổi bật. Sở hữu cấu trúc rãnh thấm tăng cường cùng lõi bông siêu thấm hút, tã giấy Unicharm Caryn thấm hút chất lỏng tối đa và chống tràn tối ưu, giữ cho bề mặt da luôn khô khoáng, dễ chịu. Hơn thế, thiết kế của Caryn đảm bảo tã luôn chắc chắn ngay cả khi ngưởi sử dụng xoay trở, vận động, hạn chết tối đa tình trạng chất lỏng trào ra ngoài. Tã giấy người lớn Unicharm Caryn là sản phẩm chăm sóc sức khỏe lí tưởng cho người thân của bạn.\n" +
                "\n" +
                "Xuất xứ: Nhật Bản\n" +
                "\n" +
                "HSD: 4 năm từ ngày sản xuất.");

        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/c3c1be77096c7d54c06c371d9276a8f1");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(830));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Khăn ướt Caryn gói 100");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a1b4897a869130d60ab2b8c92a28d977 ");
        images.add("https://cf.shopee.vn/file/d164f84a6e2471ba9d2b05390e0eea82");
        images.add("https://cf.shopee.vn/file/d164f84a6e2471ba9d2b05390e0eea82");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/a1b4897a869130d60ab2b8c92a28d977");
        productDTO.setDescription("Khăn ướt Caryn 100 miếng/gói, Giấy ướt người lớn kháng khuẩn ngăn mùi công thức Nhật Bản\n" +
                "\n" +
                "Khăn ướt Caryn với công thức kháng khuẩn – ngăn mùi, ứng dụng công nghệ Nano Silver với các hạt phân tử Nano Bạc giúp tăng cường khả năng làm sạch khuẩn, chăm sóc cho làn da luôn sạch sẽ và khỏe mạnh.\n" +
                "\n" +
                "Thành phần không chứa parabel, an toàn dịu nhẹ cho làn da\n" +
                "\n" +
                "Được sản xuất trong môi trường vô trùng\n" +
                "\n" +
                "Nhờ hạt Polyme và hợp chất Cyclodextrins có hiệu quả khử mùi với Amoniac ( Với loại 15cc, 50cc)\n" +
                "\n" +
                "Theo kết quả kiểm nghiệm của cục Tiêu chuẩn đo lường chất lượng tháng 12/2017, trong cùng 1 môi trường thí nghiệm, với cùng 1 loại chất lỏng mô phỏng, băng thấm tiểu thấm được 2ml chất lỏng trong 1s trong khi BVS hàng ngày thông thường chỉ thấm được 0.6ml\n" +
                "\n" +
                "THÔNG TIN SẢN PHẨM\n" +
                "\n" +
                "Thương hiệu: Caryn\n" +
                "\n" +
                "Xuất xứ: Nhật Bản\n" +
                "\n" +
                "Chất liệu: Sản phẩm được làm từ spunlace dày, mềm và mịn,nước tinh khiết, không paraben, không alcohol\n" +
                "\n" +
                "Quy cách đóng gói: Gói 100 miếng\n" +
                "\n" +
                "Số lượng : 6 gói\n" +
                "\n" +
                "HSD: 3 năm từ ngày sản xuất.");

        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/a1b4897a869130d60ab2b8c92a28d977");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(780));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Băng đêm dạng quần Diana size M-L 2 chiếc/ gói");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/67cd6d2351560eb60b7bb57a56f2f8b2");
        images.add("https://cf.shopee.vn/file/878f07fabccd73bfc59cbd2df1e6c70d");
        images.add("https://cf.shopee.vn/file/67cd6d2351560eb60b7bb57a56f2f8b2");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/334852ad262192fac4cd23cf00f49dd7");
        productDTO.setDescription("Băng vệ sinh quần Diana chống tràn 360 size M/L, Băng vệ sinh Diana ban đêm dạng quần gói 2 miếng. \n" +
                "\n" +
                "Băng vệ sinh quần Diana chống tràn 360 size M/L với đột phá “2 trong 1”: Sử dụng Thiết kế quần lót nhỏ gọn vừa vặn với cơ thể kết hợp cùng Cấu trúc băng ban đêm bảo vệ an toàn khiến các nàng mặc thoải mái trong mọi tư thế ngủ, xóa tan nỗi lo tràn băng. \n" +
                "\n" +
                "Thông tin sản phẩm Băng vệ sinh Diana ban đêm dạng quần gói 2 miếng\n" +
                "- Thương hiệu: Diana\n" +
                "- Công ty chịu trách nhiệm sản xuất: Công ty cổ phần Diana Unicharm\n" +
                "  Địa chỉ công ty cổ phần Diana Unicharm: Khu Công nghiệp Vĩnh Tuy, đường Lĩnh Nam, phường Vĩnh Hưng, quận Hoàng Mai, Hà Nội.\n" +
                "- Xuất xứ: Việt Nam. Sản xuất theo dây chuyền công nghệ Nhật Bản\n" +
                "- Hạn sử dụng: 4 năm kể từ ngày sản xuất\n" +
                "- Số lượng: 2 chiếc/gói\n" +
                "- Size: M-L vòng hông 85cm-105cm \n" +
                "\n" +
                "Đặc điểm nổi bật của băng vệ sinh Diana chống tràn 360\n" +
                "- Diana Sensi ban đêm với thiết kế quần lót nhỏ gọn vừa vặn với cơ thể cùng Cấu trúc băng đêm bảo vệ an toàn mà vẫn thoải mái trong mọi tư thế ngủ.\n" +
                "- Diana ban đêm sử dụng một thao tác mặc vào, một thao tác thay ra thật dễ dàng.\n" +
                "- Vách chống tràn tự động nâng lên ôm khít cơ thể giúp băng vệ sinh Diana dạng quần ban đêm chống tràn trong mọi tư thế.\n" +
                "- Hệ rãnh thông minh thiết kế dẫn chất lỏng xuống lõi thấm nhanh chóng.\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "Diana là thương hiệu của công ty chuyên sản xuất các sản phẩm chăm sóc phụ nữ và trẻ em tại Việt Nam từ nguyên liệu giấy, bột giấy, như băng vệ sinh, tã giấy, khăn giấy lụa,... Với thế mạnh là thành viên của tập đoàn hàng đầu thế giới Unicharm Nhật Bản cùng dây chuyền sản xuất hiện đại bậc nhất, Diana luôn đi đầu trong việc ứng dụng công nghệ tiên tiến để sáng tạo những sản phẩm có chất lượng cao nhất, phù hợp với nhu cầu của người tiêu dùng Việt Nam.\n" +
                "Hạn sử dụng: 4 năm kể từ ngày sản xuất .");

        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/334852ad262192fac4cd23cf00f49dd7");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(870));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802550_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //
        productDTO.setName("Băng đêm dạng quần Diana size M-L 5 chiếc/gói");
        //
        productDTO.setShopId(802550);
        productDTO.setTradeMarkId("1703772512370223");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Sức khoẻ").getId());
        productDTO.setIndustrialTypeName("Sức khoẻ");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/32eb0e9976db06b99c050a93f617462a");
        images.add("https://cf.shopee.vn/file/9b01e433ad77c09eec59d4527f06ab9c");
        images.add("https://cf.shopee.vn/file/fbf0ca2196ab2df1c7ef256f9a346170");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/942802ac099ba50771e4e2f6fcaf7a6d");
        productDTO.setDescription("Băng vệ sinh quần Diana chống tràn 360 size M/L, Băng vệ sinh Diana ban đêm dạng quần gói 5 miếng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Băng vệ sinh quần Diana chống tràn 360 size M/L với đột phá “2 trong 1”: Sử dụng Thiết kế quần lót nhỏ gọn vừa vặn với cơ thể kết hợp cùng Cấu trúc băng ban đêm bảo vệ an toàn khiến các nàng mặc thoải mái trong mọi tư thế ngủ, xóa tan nỗi lo tràn băng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thông tin sản phẩm Băng vệ sinh Diana ban đêm dạng quần gói 5 miếng\n" +
                "\n" +
                "- Thương hiệu: Diana\n" +
                "\n" +
                "- Công ty chịu trách nhiệm sản xuất: Công ty cổ phần Diana Unicharm\n" +
                "\n" +
                "Địa chỉ công ty cổ phần Diana Unicharm: Khu Công nghiệp Vĩnh Tuy, đường Lĩnh Nam, phường Vĩnh Hưng, quận Hoàng Mai, Hà Nội.\n" +
                "\n" +
                "- Xuất xứ: Việt Nam. Sản xuất theo dây chuyền công nghệ Nhật Bản\n" +
                "\n" +
                "- Hạn sử dụng: 4 năm kể từ ngày sản xuất\n" +
                "\n" +
                "- Số lượng: 5 chiếc/gói\n" +
                "\n" +
                "- Size: M-L vòng hông 85cm-105cm\n" +
                "\n" +
                "\n" +
                "\n" +
                "Đặc điểm nổi bật của băng vệ sinh Diana chống tràn 360\n" +
                "\n" +
                "- Diana Sensi ban đêm với thiết kế quần lót nhỏ gọn vừa vặn với cơ thể cùng Cấu trúc băng đêm bảo vệ an toàn mà vẫn thoải mái trong mọi tư thế ngủ.\n" +
                "\n" +
                "- Diana ban đêm sử dụng một thao tác mặc vào, một thao tác thay ra thật dễ dàng.\n" +
                "\n" +
                "- Vách chống tràn tự động nâng lên ôm khít cơ thể giúp băng vệ sinh Diana dạng quần ban đêm chống tràn trong mọi tư thế.\n" +
                "\n" +
                "- Hệ rãnh thông minh thiết kế dẫn chất lỏng xuống lõi thấm nhanh chóng.\n" +
                "\n" +
                "\n" +
                "\n" +
                "THÔNG TIN THƯƠNG HIỆU\n" +
                "\n" +
                "Diana là thương hiệu của công ty chuyên sản xuất các sản phẩm chăm sóc phụ nữ và trẻ em tại Việt Nam từ nguyên liệu giấy, bột giấy, như băng vệ sinh, tã giấy, khăn giấy lụa,... Với thế mạnh là thành viên của tập đoàn hàng đầu thế giới Unicharm Nhật Bản cùng dây chuyền sản xuất hiện đại bậc nhất, Diana luôn đi đầu trong việc ứng dụng công nghệ tiên tiến để sáng tạo những sản phẩm có chất lượng cao nhất, phù hợp với nhu cầu của người tiêu dùng Việt Nam.\n" +
                "\n" +
                "Hạn sử dụng: 4 năm kể từ ngày sản xuất.");

        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/942802ac099ba50771e4e2f6fcaf7a6d");
        variant_1.setWeight(400.0);
        variant_1.setColor(ColorProduct.BLACK);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(860));

        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //
    @Test
    public void testProduct_802539_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Loa Bluetooth Di Động LG Xboomgo PL2 -Hàng Chính Hãng - Màu Xanh Đen");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/130d179b4f235ff4580ba66b83b7e128_tn");
        images.add("https://cf.shopee.vn/file/0b14812a590b981027095f3bd661ae2f_tn");
        images.add("https://cf.shopee.vn/file/833286c728da54eb8c04793cd8295e94_tn");
        images.add("https://cf.shopee.vn/file/ad784f34e319882a146d022ede733e5b_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/bfc0b4c253ee49a69471312e0d8cbe95_tn");
        //thêm mô tả
        productDTO.setDescription("Đặc điểm nổi bật\n" +
                "【KHỬ TIẾNG ỒN CHỦ ĐỘNG NÂNG CAO】Đắm chìm hơn bao giờ hết, ít tiếng ồn hơn trước\n" +
                "【GEL TAI KHÔNG GÂY DỊ ỨNG ĐẠT TIÊU CHUẨN Y TẾ】 Silicone không độc hại, không gây dị ứng.\n" +
                "【THIẾT KẾ PHÙ HỢP SLEEK】Sự vừa khít thoải mái và phong cách phù hợp với hình dạng của tai bạn\n" +
                "【ÂM THANH CỦA MERIDIAN TECHNOLOGY】Âm thanh Hi-Fi với cảm giác không gian thực.\n" +
                "【SÂN KHẤU ÂM THANH 3D】Một trải nghiệm cảm xúc ngập tràn\n" +
                "\n" +
                "Thông số kỹ thuât\n" +
                "Loại màn hình: Đèn báo LED\n" +
                "Phiên bản bluetooth: 5,2\n" +
                "Dịch vụ kết nối nhanh Google: Có\n" +
                "Sản phẩm-Loại pin: Lithium + ion\n" +
                "Sản phẩm-Dung lượng pin: 55 mAh *2\n" +
                "Sản phẩm-Thời gian sạc pin: trong vòng 1 giờ\n" +
                "Hộp sạc-Loại pin: Lithium + ion\n" +
                "Hộp sạc-Dung lượng pin: 390 mAh\n" +
                "Hộp sạc-Thời gian sạc pin: trong vòng 2 giờ\n" +
                "Cảm ứng: Có\n" +
                "Phụ kiện: Cáp sạc, Gel tai nghe bổ sung\n" +
                "\n" +
                "THÔNG TIN BẢO HÀNH \n" +
                "Thời hạn bảo hành: 12 tháng \n" +
                "Trung Tâm Thông Tin Khách Hàng / Customer Information Center: Quý khách vui lòng truy cập vào link \n" +
                "https://www.lg.com/vn/tro-giup/bao-hanh Hotline: 18001503 (Miễn phí cước gọi/Toll free)");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/130d179b4f235ff4580ba66b83b7e128_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.WHITE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("LG TONE Free FP5 - Màu Trắng - Hàng Chính Hãng");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/b44b74b2b109c9e4a4b28926233c376b_tn");
        images.add("https://cf.shopee.vn/file/658e0e34ce30129cfe26325c259ea9ea_tn");
        images.add("https://cf.shopee.vn/file/fd529b56da190c1a6ec8e01ac5c23157_tn");
        images.add("https://cf.shopee.vn/file/209cf517c5b5b20493e53cf720566c00_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/ab1e7e03f0c442729df6b8cdd314f4e4_tn");
        //thêm mô tả
        productDTO.setDescription("Với công nghệ Meridian\n" +
                "Âm thanh 5W\n" +
                "Âm trầm hành động kép\n" +
                "Thời lượng pin dài (10 tiếng)\n" +
                "Chống nước\n" +
                "Điều khiển bằng giọng nói\n" +
                "\n" +
                "Đơn giản mà thời trang\n" +
                "Thiết kế dáng tròn đẹp mắt với lớp phủ ngoài cao su dễ cầm nắm và vừa vặn trong lòng bàn tay.\n" +
                "\n" +
                "Trải nghiệm âm thanh tuyệt đỉnh\n" +
                "LG XBOOM Go PL2 với công nghệ Meridian mang đến âm thanh chất lượng đẳng cấp. Mỗi lần bật nhạc, \n" +
                "bạn sẽ được thưởng thức âm bass sâu lắng và những âm sắc phong phú.\n" +
                "\n" +
                "Đưa âm nhạc ra cuộc sống\n" +
                "Sound Boost khuếch đại công suất âm thanh và mở rộng trường âm thanh. \n" +
                "Chỉ cần nhấn nút để khuấy động không khí bữa tiệc.\n" +
                "\n" +
                "Thời lượng pin lâu\n" +
                "Hoạt động lâu hơn\n" +
                "Thời lượng pin dài tận 10 tiếng cho phép bạn thoải mái thưởng thức âm nhạc trên đường đi mà không phải lo sạc.\n" +
                "\n" +
                "Kết nối không dây nhiều loa cùng lúc\n" +
                "Nhân rộng âm nhạc\n" +
                "Liên kết không dây giữa hai loa LG XBOOM Go PL2 để nhân đôi âm thanh đầu ra. \n" +
                "Âm thanh lớn hơn còn có thể là gì — tiệc xôm hơn và vui vẻ hơn.\n" +
                "\n" +
                "Điều khiển bằng giọng nói\n" +
                "Thưởng thức âm nhạc và hơn thế nữa mà chỉ cần cất lời\n" +
                "Nhấn nút phát trong hai giây, sau đó nói để kích hoạt Google Assistant trên điện thoại Android™ hoặc Siri trên iOS. \n" +
                "Bạn có thể bật nhạc, bật phát thanh và các chức năng khác mà chỉ cần dùng lệnh bằng giọng nói.\n" +
                "\n" +
                "\n" +
                "KÍCH THƯỚC\n" +
                "Loa chính (Rộng x Cao x Dày/ mm): 126 x 82 x 80\n" +
                "Loa chính (Khối lượng tịnh / kg): 0.35\n" +
                "Kích thước thùng các tông (Rộng x Cao x Dày / mm): 160 x 134 x 113\n" +
                "Khối lượng thực tế (kg): 0.66\n" +
                "Sức chứa container - 20ft: 7560\n" +
                "Sức chứa container - 40ft: 15960\n" +
                "Sức chứa container - 40ft (HC): 18240\n" +
                "\n" +
                "AMPLI\n" +
                "Kênh: 1ch\n" +
                "Công suất ra (W): 5W\n" +
                "Loa trầm: 1,75inch x 2ea\n" +
                "Màng rung thụ động: Có\n" +
                "Trở kháng: 4\n" +
                "\n" +
                "NHẬP & XUẤT\n" +
                "Cổng vào - Aux vào (Φ3,5): Có\n" +
                "Nguồn-USB loại C: Có (cái)\n" +
                "\n" +
                "MÀN HÌNH\n" +
                "Loại: Đèn báo LED (bluetooth, bật, pin, Bộ cân bằng, Chế độ đa, chế độ đôi)\n" +
                "\n" +
                "CHẾ ĐỘ ÂM THANH\n" +
                "Bộ cân bằn - Kích âm: Có (mặc định)\n" +
                "Bộ cân bằn - Tiêu chuẩn: Có\n" +
                "\n" +
                "PIN TÍCH HỢP\n" +
                "Dung lượng pin: 3.7V, 1500mAh\n" +
                "Thời gian sạc pin: 4\n" +
                "Thời hạn pin: 10 giờ \n" +
                "\n" +
                "ĐIỆN NĂNG\n" +
                "Mức tiêu thụ điện năng - Chế độ bật (trạng thái đang sạc): 5W\n" +
                "Mức tiêu thụ điện năng - Chế độ chờ: 0,5W\n" +
                "\n" +
                "ĐỊNH DẠNG ÂM THANH\n" +
                "SBC: Có\n" +
                "AAC: Có\n" +
                "\n" +
                "THUẬN TIỆN\n" +
                "Đa điểm: Có\n" +
                "Kết nối không dây theo nhóm (Chế độ đôi): Có\n" +
                "Quản lý nâng cấp (FOTA): Có\n" +
                "Ứng dụng Bluetooth: Có (Android)\n" +
                "Điều khiển bằng giọng nói: Có (Trợ lý Google, Siri)\n" +
                "Chống nước/Chống bắn nước: Có (IPX5)\n" +
                "Bluetooth: Có\n" +
                "Loa điện thoại: Có\n" +
                "Khóa an ninh (trong kho): Có\n" +
                "\n" +
                "BỘ PHỤ KIỆN\n" +
                "Hướng dẫn Sử dụng - Hướng dẫn đơn giản: Có\n" +
                "Thẻ bảo hành: Có\n" +
                "Cáp USB-C: Có\n" +
                "Loại thùng các tông (Tip on / Offset / Flexo): Offset\n" +
                "\n" +
                "Thời hạn bảo hành: 12 tháng\n" +
                "Trung Tâm Thông Tin Khách Hàng / Customer Information Center: Quý khách vui lòng truy cập vào link https://www.lg.com/vn/tro-giup/bao-hanh Hotline: 18001503 (Miễn phí cước gọi/Toll free)\n" +
                "\n");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/b44b74b2b109c9e4a4b28926233c376b_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.BLUE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Loa Bluetooth Di Động LG Xboom Go PL5 -Hàng Chính Hãng - Màu Xanh Đen");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/2c9072cca05f6157cd2c253163cae63e_tn");
        images.add("https://cf.shopee.vn/file/4b5c813e74c84be9549e3af29746bacc_tn");
        images.add("https://cf.shopee.vn/file/e9487709861ad2aa06615324aa55aae4_tn");
        images.add("https://cf.shopee.vn/file/c00ab27037f40fb1038573974b79d2fd_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/7a2874d7d52e65631c4d0645b639d420_tn");
        //thêm mô tả
        productDTO.setDescription("\n" +
                "MÔ TẢ SẢN PHẨM\n" +
                "Với công nghệ Meridian\n" +
                "Âm thanh 20W\n" +
                "Âm trầm hành động kép\n" +
                "Thời lượng pin dài (18 tiếng)\n" +
                "Chế độ đèn nhiều màu\n" +
                "IPX5\n" +
                "\n" +
                "Diện mạo mới của âm thanh cân bằng\n" +
                "Đơn giản mà thời trang\n" +
                "Thiết kế dáng tròn đẹp mắt với lớp phủ ngoài cao su dễ cầm nắm và vừa vặn trong lòng bàn tay, \n" +
                "trong khi đèn loa siêu trầm dạng vòng tròn điểm thêm nét rực rỡ cho không gian của bạn.\n" +
                "\n" +
                "Trải nghiệm âm thanh tuyệt đỉnh\n" +
                "LG XBOOM Go PL5 với công nghệ Meridian mang đến âm thanh chất lượng đẳng cấp. Mỗi lần bật nhạc, \n" +
                "bạn sẽ được thưởng thức âm bass sâu lắng và những âm sắc phong phú.\n" +
                "\n" +
                "Đưa âm nhạc ra cuộc sống\n" +
                "Sound Boost khuếch đại công suất âm thanh và mở rộng trường âm thanh. Chỉ cần nhấn nút để khuấy động không khí bữa tiệc.\n" +
                "\n" +
                "Cảm nhận nhịp nhạc mạnh mẽ hơn\n" +
                "Màng rung thụ động tạo ra nhịp điệu táo bạo đủ mạnh để khuấy động bữa tiệc dù bạn đang ở đâu. Khi loa siêu trầm rung, \n" +
                "hệ thống đèn nhấp nháy dạng vòng tròn tạo ra những chuyển động rực rỡ, khiến không khí bữa tiệc càng hào hứng.\n" +
                "\n" +
                "Tận hưởng bất kể thời tiết\n" +
                "Với xếp hạng IPX5, bộ loa của bạn vẫn có thể hoạt động dù bị ướt, nên cứ tiếp tục quẩy thôi.\n" +
                "\n" +
                "Hoạt động lâu hơn\n" +
                "Thời lượng pin dài tận 18 tiếng cho phép bạn thoải mái thưởng thức âm nhạc trên đường đi mà không phải lo sạc.\n" +
                "\n" +
                "Thêm khí sắc cho âm nhạc\n" +
                "Đèn LED rực rỡ sắc màu đa dạng và thay đổi theo điệu nhạc, mang thêm cảm xúc cho âm nhạc của bạn.\n" +
                "\n" +
                "Nhân rộng âm nhạc\n" +
                "Kết nối không dây liên kết tới một trăm loa LG XBOOM Go PL5 để nhân lên âm thanh phát ra.\n" +
                " Âm thanh lớn hơn còn có thể là gì — tiệc xôm hơn và vui vẻ hơn.\n" +
                "\n" +
                "Âm thanh vòm Bluetooth sẵn sàng\n" +
                "Mang đến âm thanh chân thực hơn cho TV\n" +
                "Kết nối hai loa LG XBOOM Go PL5 với một TV LG và sử dụng chúng để phát âm thanh cho TV. \n" +
                "Thiết lập đơn giản, cho bạn âm thanh vòm sống động với mọi nội dung mà bạn xem.\n" +
                "\n" +
                "Điều khiển bằng giọng nói\n" +
                "Thưởng thức âm nhạc và hơn thế nữa mà chỉ cần cất lời\n" +
                "Nhấn nút phát trong hai giây, sau đó nói để kích hoạt Google Assistant trên điện thoại Android™ hoặc Siri trên iOS.\n" +
                " Bạn có thể bật nhạc, bật phát thanh và các chức năng khác mà chỉ cần dùng lệnh bằng giọng nói.\n" +
                "\n" +
                "KÍCH THƯỚC\n" +
                "Loa chính (Rộng x Cao x Dày/ mm):201 x 79 x 79\n" +
                "Loa chính (Khối lượng tịnh / kg): 0.62\n" +
                "Kích thước thùng các tông (Rộng x Cao x Dày / mm): 249 x 149 x 130\n" +
                "Khối lượng thực tế (kg): 0.99\n" +
                "Sức chứa container - 20ft: 5760\n" +
                "Sức chứa container - 40ft: 12240\n" +
                "Sức chứa container - 40ft (HC): 14280\n" +
                "\n" +
                "AMPLI\n" +
                "Kênh: 2ch\n" +
                "Công suất ra (W): 20W\n" +
                "Loa trầm: 1,75inch x 2ea\n" +
                "Màng rung thụ động: Có\n" +
                "Trở kháng: 4\n" +
                "\n" +
                "NHẬP & XUẤT\n" +
                "Cổng vào - Aux vào (Φ3,5): Có\n" +
                "Nguồn-USB loại C:  Có (cái)\n" +
                "\n" +
                "MÀN HÌNH\n" +
                "Loại: Đèn báo LED (bluetooth, bật, pin, Bộ cân bằng, Chế độ đa, chế độ đôi)\n" +
                "\n" +
                "CHẾ ĐỘ ÂM THANH\n" +
                "Bộ cân bằn - Kích âm: Có (mặc định)\n" +
                "Bộ cân bằn - Tiêu chuẩn: Có\n" +
                "\n" +
                "PIN TÍCH HỢP\n" +
                "Dung lượng pin: 3.8V, 3900mAh\n" +
                "Thời gian sạc pin: 4\n" +
                "Thời hạn pin: 18h \n" +
                "\n" +
                "ĐIỆN NĂNG\n" +
                "Mức tiêu thụ điện năng - Chế độ bật (trạng thái đang sạc): 10W\n" +
                "Mức tiêu thụ điện năng - Chế độ chờ: 0,5W\n" +
                "\n" +
                "ĐỊNH DẠNG ÂM THANH\n" +
                "SBC: Có\n" +
                "AAC: Có\n" +
                "\n" +
                "THUẬN TIỆN\n" +
                "Đa điểm: Có\n" +
                "Kết nối không dây theo nhóm (Chế độ đôi): Có\n" +
                "Quản lý nâng cấp (FOTA): Có\n" +
                "Ứng dụng Bluetooth: Có (Android/iOS)\n" +
                "Điều khiển bằng giọng nói: Có (Trợ lý Google, Siri)\n" +
                "Chống nước/Chống bắn nước: Có (IPX5)\n" +
                "Bluetooth: Có\n" +
                "Loa điện thoại: Có\n" +
                "Khóa an ninh (trong kho): Có\n" +
                "\n" +
                "BỘ PHỤ KIỆN\n" +
                "Hướng dẫn Sử dụng - Hướng dẫn đơn giản: Có\n" +
                "Thẻ bảo hành: Có\n" +
                "Cáp USB-C: Có\n" +
                "Loại thùng các tông (Tip on / Offset / Flexo): Offset\n" +
                "\n" +
                "Thời hạn bảo hành: 12 tháng\n" +
                "Trung Tâm Thông Tin Khách Hàng / Customer Information Center: Quý khách vui lòng truy cập vào link \n" +
                "https://www.lg.com/vn/tro-giup/bao-hanh Hotline: 18001503 (Miễn phí cước gọi/Toll free)\n" +
                "Để bật chế độ hỗ trợ đọc màn hình, nhấn Ctrl+Alt+Z Để tìm hiểu thêm về các phím tắt, nhấn Ctrl+dấu gạch chéo\n" +
                " \n" +
                " \n" +
                " \t\t\n");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/2c9072cca05f6157cd2c253163cae63e_tn");
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Loa thanh soundbar LG SL4");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/77275663b219cf34a569a14f05b20f60_tn");
        images.add("https://cf.shopee.vn/file/0ee7ca61f4fa5cc6a542672a61a33750_tn");
        images.add("https://cf.shopee.vn/file/bd21725c5cd4b99acfab4d8ff06a1c71_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/59cda0eb10838e3782e00798ecb1dfaf_tn");
        //thêm mô tả
        productDTO.setDescription("Loa trầm carbon cho âm thanh có độ trung thực cao\n" +
                "Loa thanh LG SL4 được thiết kế tạo ra âm thanh rõ ràng không bị méo tiếng. \n" +
                "Đặc biệt, màng loa carbon trong loa trầm mang lại âm thanh trong trẻo tuyệt vời.\n" +
                "\n" +
                "Công nghệ âm thanh tự thích nghi Adaptive Sound\n" +
                "Âm nhạc, phim ảnh và thậm chí cả tin tức đều có cấu hình âm thanh khác nhau. \n" +
                "Loa thanh LG SL4 có Công nghệ âm thanh tự thích nghi Adaptive Sound Control giúp xác định nội dung đang được phát và\n" +
                " tự động điều chỉnh chế độ âm thanh để tạo ra trải nghiệm âm thanh lý tưởng.\n" +
                "\n" +
                "Loa siêu trầm không dây\n" +
                "Cảm nhận nhịp điệu mạnh mẽ với loa siêu trầm không dây của Loa thanh LG SL4 - tiện lợi và dễ cài đặt, với công suất 200 watt.\n" +
                "\n" +
                "Đa kết nối\n" +
                "Loa thanh LG SL4Y có nhiều đầu vào thuận tiện khác nhau như HDMI, Cáp quang Optical và Bluetooth®.\n" +
                "\n" +
                "Bluetooth, phát trực tuyến mọi nội dung\n" +
                "Phát nhạc trực tiếp qua Bluetooth® từ điện thoại thông minh của bạn đến Loa thanh LG SL4.\n" +
                "\n" +
                "Thông số kỹ thuật\n" +
                "Năng lượng tiêu thụ của Loa thanh: 23W\n" +
                "Năng lượng tiêu thụ của Loa thanh ở chế độ chờ: <0.5W\n" +
                "Năng lượng tiêu thụ của Loa trầm: 33W\n" +
                "Năng lượng tiêu thụ của Loa trầm ở chế độ chờ: <0.5W\n" +
                "KÍCH THƯỚC(WXHXD MM)/TRỌNG LƯỢNG(KG)\n" +
                "Phù hợp với TV        Trên 40\"\n" +
                "Kích thước Loa Trầm (WxHxD)        6.8\" x 15.4\" x 10.3\"\n" +
                "Khối lượng tịnh Loa trầm        5.3Kg\n" +
                "Tổng Khối lượng        9.53Kg\n" +
                "Kích thước Loa Thanh (WxHxD)        35.1\" x 2.3\" x 3.4\"\n" +
                "Khối lượng tịnh Loa thanh        2.25Kg\n" +
                "\n" +
                "PHỤ KIỆN BAO GỒM\n" +
                "Điều khiển        Có\n" +
                "Cáp quang        Có\n" +
                "Thẻ bảo hành        Có\n" +
                "Pin        Có\n" +
                "Giá treo tường        Có\n" +
                "\n" +
                "THÔNG TIN BẢO HÀNH\n" +
                "Thời hạn bảo hành: 12 tháng\n" +
                "Trung Tâm Thông Tin Khách Hàng / Customer Information Center: Quý khách vui lòng truy cập vào link \n" +
                "https://www.lg.com/vn/tro-giup/bao-hanh Hotline: 18001503 (Miễn phí cước gọi/Toll free)");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/77275663b219cf34a569a14f05b20f60_tn");
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Laptop LG Gram 14ZD90Q-G.AX51A5 (14''| i5-1240P| 8GB | 256GB | WQXGA | Non-OS)");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/55d05ba6a052671dbf7e72e9c617b2a0_tn");
        images.add("https://cf.shopee.vn/file/b37c5eab18408bd42a54a5c89377f8ed_tn");
        images.add("https://cf.shopee.vn/file/6e172090b49b766869d5feb66e21232a_tn");
        images.add("https://cf.shopee.vn/file/f45f226fdf07c88391739207ed6efc45_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22110-u0ioj1vbnfjv32_tn");
        //thêm mô tả
        productDTO.setDescription("Khách hàng sau khi mua sản phẩm laptop LG gram vui lòng thực hiện các bước sau:\n" +
                "\n" +
                "+ Bước 1: Truy cập vào trang - https://quatangLG.com - và điền đầy đủ thông tin theo yêu cầu.\n" +
                "+ Bước 2: Trong vòng  03 ngày làm việc (không bao gồm Thứ 7, Chủ Nhật và Ngày Lễ), bộ phận chăm sóc khách hàng \n" +
                "sẽ gọi điện xác nhận thông tin và xử lý yêu cầu.\n" +
                "- Thông tin hợp lệ: Khách hàng nhận được thông báo qua tin nhắn về thông tin quà tặng và đường link nhận quà.\n" +
                "- Thông tin không hợp lệ: Khách hàng nhận được cuộc gọi thông báo từ chối do thông tin không hợp lệ.\n" +
                "+ Bước 3: Khách hàng nhấn vào đường link trong tin nhắn và làm theo hướng dẫn để nhận quà tặng.\n" +
                "\n" +
                "Thời gian khuyến mại: Từ 15/07/2022 đến 31/12/2022.\n" +
                "------------------------------------------------------------------------------\n" +
                "Thông số kỹ thuật\n" +
                "- Năm: 2022 \n" +
                "- Kích thước: 14inch \n" +
                "- Tấm nền: ÍP\n" +
                "- Độ phân giải: WUXGA (1920 x 1200)\n" +
                "- Độ sáng: 30 nit\n" +
                "- Gam màu: DCI-P3 99% \n" +
                "- Độ tương phản: 1200:1 \n" +
                "\n" +
                "Hệ thống\n" +
                "- Bộ xử lý: Bộ xử lý Intel® Core™ Thế hệ 12 i7-1260P, L3 Cache 18MB i5-1240P , L3 Cache 12MB\n" +
                "- Đồ họa: Intel Iris Xe Graphics (i7,i5)/ Intel UHD Graphics (i3)\n" +
                "\n" +
                "Lưu trữ \n" +
                "- Bộ nhớ: 8/16/32GB LPDDR5 \n" +
                "- SSD: M.2 (2280)\n" +
                "- Khe cắm MMC: Micro SD \n" +
                "\n" +
                "Thông tin chung \n" +
                "- Pin: 72Wh Li-Ion \n" +
                "- Phụ kiện: bộ đổi nguồn AC & bộ điều hợp USB-C sang RJ45\n" +
                "- Tản nhiệt: làm mát mega 4.0\n" +
                "- Nút: nút nguồn có vân tay \n" +
                "\n" +
                "Mỏng, Gọn và Nhẹ\n" +
                "Siêu mỏng và siêu di động, LG gram là người bạn đồng hành trọng lượng nhẹ luôn di chuyển cùng với bạn\n" +
                "\n" +
                "Vươn tới hình ảnh tối ưu hóa \n" +
                "Đắm mình trong màn hình có tỷ lệ khung hình 16:10 lớn hơn 11% so với tỷ lệ 16:9. Vì vậy, bạn có thể cuộn ít hơn và\n" +
                " dành nhiều thời gian hơn cho sự hối hả của mình\n" +
                "\n" +
                "DCI-P3 99% (Thông thường) Phổ màu rộng \n" +
                "Đưa nội dung của bạn vào cuộc sống với gam màu rộng giúp bạn thấy màu sắc rực rỡ \n" +
                "\n" +
                "Tấm nền chống lóa mắt \n" +
                "Tấm nền chống lóa mắt ngăn chặn phản chiếu màn hình vào ban ngày hoặc khi ở ngoài trời. \n" +
                "Luôn hối hả bất cứ lúc nào từ bất cứ đâu.\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Với dòng sản phẩm cao cấp LG Gram 2020, LG áp dụng dịch vụ bảo hành tận nhà.\n" +
                "Thời gian bảo hành 12 tháng\n" +
                "https://www.lg.com/vn/tro-giup/bao-hanh \n" +
                "Trung Tâm Thông Tin Khách Hàng / Customer Information Center 18001503 (Miễn phí cước gọi/Toll free)");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/f45f226fdf07c88391739207ed6efc45_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.WHITE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        productVariantList.add(variant_1);

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName(" Màn Hình LG 27MP500-B 27'' IPS 75Hz FHD AMD FreeSync™ - Hàng Chính Hãng");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/4293faac1ae2a8e1613f8a680b8c2f2a_tn");
        images.add("https://cf.shopee.vn/file/44bca5800bf231db1d94b48b057302f7_tn");
        images.add("https://cf.shopee.vn/file/58fe597c57d8675f2a1d93d58263520e_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/a47a98bbb73162bba06d088522e84787_tn");
        //thêm mô tả
        productDTO.setDescription("Thông tin nổi bật:\n" +
                "Màn hình IPS Full HD\n" +
                "Màn hình sống động hiển thị ánh sáng xanh thấp\n" +
                "AMD FreeSync™\n" +
                "Chế độ đọc sách\n" +
                "Chống nháy\n" +
                "Điều khiển trên màn hình\n" +
                "\n" +
                "Màn hình IPS Full HD\n" +
                "Màu sắc chân thực ở góc rộng\n" +
                "Màn hình LG với công nghệ IPS làm nổi bật hiệu suất của màn hình tinh thể lỏng. Rút ngắn thời gian phản hồi, \n" +
                "cải thiện khả năng tái tạo màu sắc và người dùng có thể xem ở các góc rộng.\n" +
                "\n" +
                "Màn hình sống động hiển thị ánh sáng xanh thấp\n" +
                "Duy trì chất lượng hình ảnh, giảm hiển thị ánh sáng xanh\n" +
                "Công nghệ Live Color Low Blue Light của LG có tác dụng giảm sinh ra lượng ánh sáng xanh có hại. Ngoài ra, \n" +
                "công nghệ này còn giúp người dùng tránh bị mỏi mắt mà không ảnh hưởng xấu tới chất lượng hình ảnh, nhờ khả năng \n" +
                "làm giảm đáng kể tình trạng biến dạng màu.\n" +
                "\n" +
                "Chế độ đọc sách\n" +
                "Giúp mắt thoải mái hơn\n" +
                "Để giảm mỏi mắt và giúp mắt thoair mái hơn khi đọc văn bản trên màn hình, Chế độ Đọc sách sẽ điều chỉnh nhiệt độ màu và\n" +
                " độ sáng tương tự như khi đọc trên giấy.\n" +
                "\n" +
                "Chống nháy\n" +
                "Chăm Sóc Mắt\n" +
                "Chế độ Chống nháy giúp giảm chớp nháy không nhìn thấy được trên màn hình và mang lại môi trường làm việc thoải mái cho mắt bạn.\n" +
                "\n" +
                "Màn hình hầu như không có đường viền 3 cạnh\n" +
                "Hòa Mình với Nội Dung\n" +
                "Màn hình này có viền mỏng ở ba cạnh và không gây phân tâm nhờ hình ảnh chính xác đến kinh ngạc, \n" +
                "và khả năng điều chỉnh độ nghiêng thuận tiện cho phép bạn bố trí một môi trường làm việc tối ưu.\n" +
                "\n" +
                "\n" +
                "MÀN HÌNH\n" +
                "Kích thước (Inch): 27.0 inch\n" +
                "Kích thước (cm): 68.6 cm\n" +
                "Độ phân giải: 1920 x 1080\n" +
                "Kiểu tấm nền: IPS\n" +
                "Tỷ lệ màn hình: 16:9\n" +
                "Kích thước điểm ảnh: 0.3114 x 0.3114 mm\n" +
                "Độ sáng (Tối thiểu): 200 cd/m²\n" +
                "Độ sáng (Điển hình): 250 cd/m²\n" +
                "Gam màu (Điển hình): NTSC 72% (CIE1931)\n" +
                "Độ sâu màu (Số màu): 16.7M\n" +
                "Tỷ lệ tương phản (Tối thiểu): 600:1\n" +
                "Tỷ lệ tương phản (Điển hình): 1000:1\n" +
                "Thời gian phản hồi: 5ms (GtG nhanh hơn)\n" +
                "Góc xem (CR≥10)        178º(R/L), 178º(U/D)\n" +
                "Xử lý bề mặt: Chống lóa\n" +
                "\n" +
                "TÍNH NĂNG\n" +
                "Chống nháy: Có\n" +
                "Chế độ đọc sách: Có\n" +
                "Màu sắc yếu: Có\n" +
                "Super Resolution+:  Có\n" +
                "AMD FreeSync™: Có\n" +
                "Cân bằng tối: Có\n" +
                "Đồng bộ hành động kép: Có\n" +
                "Crosshair: Có\n" +
                "Tiết kiệm năng lượng thông minh: Có\n" +
                "Khác (TÍNH NĂNG)        Màu sống ánh sáng xanh dương thấp\n" +
                "\n" +
                "ỨNG DỤNG SW\n" +
                "Điều khiển trên màn hình (Trình quản lý màn hình LG): Có\n" +
                "\n" +
                "KẾT NỐI\n" +
                "HDMI™: Có (2ea)\n" +
                "HDMI (Độ phân giải Tối đa tại Hz)        1920 x 1080 at 75Hz\n" +
                "Tai nghe ra: Có\n" +
                "\n" +
                "NGUỒN\n" +
                "Loại        Hộp nguồn ngoài (Bộ sạc)\n" +
                "Ngõ vào AC: 100-240Vac, 50/60Hz\n" +
                "Mức tiêu thụ điện (Điển hình): 25.5W\n" +
                "Mức tiêu thụ điện (Tối đa): 28.0W\n" +
                "Công suất tiêu thụ (Energy Star): 20.3W\n" +
                "Mức tiêu thụ điện (Chế độ ngủ): Dưới 0,5W\n" +
                "Mức tiêu thụ điện (DC tắt): Dưới 0,3W\n" +
                "\n" +
                "ĐẶC ĐIỂM CƠ HỌC\n" +
                "Điều chỉnh vị trí màn hình: Nghiêng\n" +
                "Có thể treo tường: 75 x 75 mm\n" +
                "\n" +
                "KÍCH THƯỚC/KHỐI LƯỢNG\n" +
                "Kích thước tính cả chân đế (Rộng x Cao x Dày): 611.1 x 455.1 x 214.9 mm\n" +
                "Kích thước không tính chân đế (Rộng x Cao x Dày): 611.1 x 362.6 x 39.5 mm\n" +
                "Khối lượng tính cả chân đế: 4.8 kg\n" +
                "Khối lượng không tính chân đế: 4.3 kg\n" +
                "\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Thời gian bảo hành 24 tháng \n" +
                "https://www.lg.com/vn/tro-giup/bao-hanh \n" +
                "Trung Tâm Thông Tin Khách Hàng / Customer Information Center 18001503 (Miễn phí cước gọi/Toll free)");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/4293faac1ae2a8e1613f8a680b8c2f2a_tn");
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
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1200));
        productVariantList.add(variant_1);

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Smart FHD Tivi LG 43 Inch 43LM5750PTC ThinQ AI");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/77e46b525c2643a74239388dd0b43852_tn");
        images.add("https://cf.shopee.vn/file/460da44f61d187901056e2a221fd4a98_tn");
        images.add("https://cf.shopee.vn/file/71b0210a9b153296db7735188a556c50_tn");
        images.add("https://cf.shopee.vn/file/e2b886e3b35e8ccea6f0281814fd57d7_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/d02c97327d7ffa4b72fbf0bbac0dc437_tn");
        //thêm mô tả
        productDTO.setDescription("Trải nghiệm chuẩn mực Full HD\n" +
                "LG TV Full HD mang đến hình ảnh chính xác hơn với độ phân giải tuyệt đẹp với màu sắc sống động.\n" +
                "\n" +
                "Hình ảnh rõ nét hơn\n" +
                "LG TV FHD được gây ấn tượng với chất lượng hình ảnh rõ ràng, đẹp hơn gấp hai lần so với HD. \n" +
                "Và với các tính năng Dynamic Color và Active HDR, toàn bộ nội dung yêu thích của bạn sẽ trở nên trung thực và sống động hơn.\n" +
                "\n" +
                "Màu sắc tự nhiên nhất\n" +
                "Nâng cao hiệu suất xử lý và điều chỉnh màu sắc cho hình ảnh phong phú hơn, tự nhiên hơn. \n" +
                "Tận hưởng vẻ đẹp thiên nhiên với màu sắc trung thực trên màn hình TV của bạn.\n" +
                "\n" +
                "Đi sâu vào chi tiết\n" +
                "LG TV FHD mang đến màu sắc rực rỡ và chi tiết chính xác với Active HDR. Thưởng thức các bộ phim yêu thích với \n" +
                "chất lượng tương tự như bản gốc với nhiều định dạng HDR bao gồm HDR10 và HLG.\n" +
                "\n" +
                "Tối ưu hóa mọi trải nghiệm âm thanh\n" +
                "LG TV FHD có các tính năng Virtual Surround Plus và Dolby Audio giúp bạn đắm chìm trong trải nghiệm âm thanh phong phú và\n" +
                " chân thực hơn với mọi thể loại phim và chương trình truyền hình.\n" +
                "\n" +
                "Âm thanh lan tỏa khắp không gian của bạn\n" +
                "Bạn có thể trải nghiệm âm thanh đa chiều phong phú với loa có sẵn bên trong TV. \n" +
                "Nâng cao trải nghiệm xem của bạn với âm thanh đến từ mọi hướng.\n" +
                "\n" +
                "Trải nghiệm âm thanh điện ảnh\n" +
                "Trải nghiệm âm thanh chất lượng rạp hát rõ hơn, ngập tràn hơn tại gia đình với Dolby Audio trên TV.\n" +
                "\n" +
                "Thiết kế đơn giản nhưng tinh tế\n" +
                "Đường viền mỏng và kiểu cách hài hòa với thiết kế nội thất của bạn giúp nâng cấp trải nghiệm.\n" +
                "\n" +
                "Thông số kỹ thuật\n" +
                "Loại tivi: Smart Tivi43 inchFull HD\n" +
                "Hệ điều hành: webOS 4.5\n" +
                "Kích thước:: Ngang 97.7 cm - Cao 61.5 cm - Dày 18.7 cm\n" +
                "\n" +
                "Kết nối\n" +
                "Bluetooth:Có (kết nối loa bluetooth)\n" +
                "Kết nối Internet: Cổng LAN, Wifi\n" +
                "Cổng AV: Composite tích hợp trong Component\n" +
                "Cổng HDMI: 2 HDMI\n" +
                "Cổng xuất âm thanh:\n" +
                "- HDMI ARC\n" +
                "- Cổng Optical (Digital Audio Out)\n" +
                "- Cổng xuất âm thanh 3.5 mm\n" +
                "USB: 1 USB\n" +
                "\n" +
                "Thông tin bảo hành\n" +
                "Thời gian bảo hành: 24 tháng\n" +
                "Thông tin chi tiết xem tại: https://www.lg.com/vn/tro-giup/bao-hanh\n" +
                "\n" +
                "***Công lắp đặt:\n" +
                "\n" +
                "- Miễn phí cho nội thành HCM (Quận 1, 2, 3, 4, 5, 6, 7, 8, 10, 11, Tân Bình, Tân Phú, Phú Nhuận, Bình Thạnh, Gò Vấp,\n" +
                " Quận 9, 12, Thủ Đức, Bình Tân, Hóc Môn) và nội thành Hà Nội (Quận Ba Đình, Quận Bắc Từ Liêm, Quận Cầu Giấy, \n" +
                "Quận Hà Đông, Quận Hai Bà Trưng, Quận Hoàn Kiếm, Quận Hoàng Mai, Quận Long Biên, Quận Nam Từ Liêm, \n" +
                "Quận Tây Hồ, Quận Thanh Xuân, Quận Đống Đa)\n" +
                "- Chi phí vật tư: Nhân viên sẽ thông báo phí vật tư (ống đồng, dây điện v.v...) khi khảo sát lắp đặt (Bảng kê xem tại ảnh 2). \n" +
                "Khách hàng sẽ thanh toán trực tiếp cho nhân viên kỹ thuật sau khi việc lắp đặt hoàn thành - chi phí này sẽ không hoàn lại \n" +
                "trong bất cứ trường hợp nào.\n" +
                "- Quý khách hàng có thể trì hoãn việc lắp đặt tối đa là 7 ngày lịch kể từ ngày giao hàng thành công (không tính ngày Lễ). \n" +
                "Nếu nhân viên hỗ trợ không thể liên hệ được với Khách hàng quá 3 lần, hoặc Khách hàng trì hoãn việc lắp đặt quá thời hạn trên, \n" +
                "Dịch vụ lắp đặt sẽ được hủy bỏ.\n" +
                "- Đơn vị vận chuyển giao hàng cho bạn KHÔNG có nghiệp vụ lắp đặt sản phẩm.\n" +
                "- Thời gian bộ phận lắp đặt liên hệ (không bao gồm thời gian lắp đặt): trong vòng 24h kể từ khi nhận hàng (Trừ Chủ nhật/ Ngày Lễ). \n" +
                "Trong trường hợp bạn chưa được liên hệ sau thời gian này, vui lòng gọi lên hotline của Shopee (19001221) để được tư vấn.\n" +
                "- Tìm hiểu thêm về Dịch vụ lắp đặt:\n" +
                "help.shopee.vn/vn/s/article/Làm-thế-nào-để-tôi-có-thể-sử-dụng-dịch-vụ-lắp-đặt-tại-nhà-cho-các-sản-phẩm-tivi-điện-máy-lớn-\n" +
                "1542942683961\n" +
                "\n" +
                "- Quy định đổi trả: Chỉ đổi/trả sản phẩm, từ chối nhận hàng tại thời điểm nhận hàng trong trường hợp sản phẩm giao đến\n" +
                " không còn nguyên vẹn, thiếu phụ kiện hoặc nhận được sai hàng. Khi sản phẩm đã được cắm điện sử dụng và/hoặc lắp đặt, \n" +
                "và gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/77e46b525c2643a74239388dd0b43852_tn");
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

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Máy Lạnh LG Inverter 1 HP V10ENW1");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/7f1f2e0a2d8eab23bf96236ac312141f_tn");
        images.add("https://cf.shopee.vn/file/a3637e1cb76070bcbc9e8322936f2b7d_tn");
        images.add("https://cf.shopee.vn/file/6cf617e2e87dd34a6d8710c687a92fff_tn");
        images.add("https://cf.shopee.vn/file/d03e3368cb25b69506538a298e07d0f2_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/69a2413fc004a16acb9739ca145a503d_tn");
        //thêm mô tả
        productDTO.setDescription("\n" +
                "Đặc điểm nổi bật\n" +
                "Công suất 1 HP thích hợp sử dụng cho phòng có diện tích dưới 15m2\n" +
                "Công nghệ Inverter giúp máy vận hành êm, giảm ồn, tiết kiệm điện\n" +
                "Chế độ làm lạnh Jet Cool giúp căn phòng mát lạnh ngay tức thì\n" +
                "Chế độ thổi hướng gió dễ chịu tránh gió lạnh lùa trực tiếp vào cơ thể\n" +
                "Lớp phủ chống ăn mòn Gold Fin giúp tăng độ bền cho dàn tản nhiệt\n" +
                "Máy lạnh sử dụng Gas R32 làm lạnh sâu, thân thiện với môi trường\n" +
                "\n" +
                "Đặc điểm sản phẩm\n" +
                "Model: V10ENW1\n" +
                "Màu sắc: Trắng\n" +
                "Nhà sản xuất: LG\n" +
                "Xuất xứ: Thái Lan\n" +
                "Năm ra mắt: 2021\n" +
                "Loại máy lạnh:        1 chiều\n" +
                "Công suất: 1 HP\n" +
                "Công nghệ Inverter: Có\n" +
                "Làm lạnh nhanh: Powerful\n" +
                "Khử mùi: Tự động làm sạch\n" +
                "Chế độ gió: Làm lạnh nhanh, hướng gió dễ chịu\n" +
                "Độ ồn dàn lạnh: 33dB\n" +
                "Độ ồn dàn nóng: 50dB\n" +
                "Gas sử dụng: R-32\n" +
                "Phạm vi hiệu quả: Dưới 15m2\n" +
                "\n" +
                "Tiết kiệm năng lượng\n" +
                "Máy nén kép Inverter liên tục điều chỉnh tốc độ của máy nén để duy trì mức nhiệt độ mong muốn. Hơn thế nữa, \n" +
                "máy nén kép Dual Inverter Compressor™ với dải tần số hoạt động rộng hơn giúp tiết kiệm được nhiều hơn so với \n" +
                "máy nén thông thường.\n" +
                "\n" +
                "LÀM LẠNH NHANH\n" +
                "Điều LG DUALCOOL còn mang đến khả năng làm lạnh nhanh hơn tới 40% so với điều hòa thông thường. \n" +
                "Nhờ hiệu suất mạnh mẽ của máy nén \"kép\" DUAL Inverter được tích hợp đến 2 motor nén đặt lệch pha giúp máy nén \n" +
                "có thể hoạt động ổn định ở tốc độ cao hơn.\n" +
                "\n" +
                "BẢO HÀNH 10 NĂM MÁY NÉN\n" +
                "Với chế độ bảo hành 10 năm máy nén, giúp bạn luôn an tâm tận hưởng mọi tính năng ưu việt của điều hòa LG DUALCOOL\n" +
                "\n" +
                "Dàn tản nhiệt mạ vàng\n" +
                "Dàn tản nhiệt với lớp phủ đặc biệt màu vàng, giúp bảo vệ bề mặt dàn tản nhiệt, hạn chế quá trình ăn mòn, nâng cao tuổi thọ \n" +
                "sản phẩm.\n" +
                "\n" +
                "TỰ ĐỘNG LÀM SẠCH\n" +
                "\n" +
                "Chức năng tự động làm sạch giúp ngăn ngừa sự hình thành vi khuẩn và nấm mốc trong dàn lạnh , \n" +
                "giúp mang đến một môi trường trong lành hơn cho người sử dụng\n" +
                "\n" +
                "CHẾ ĐỘ VẬN HÀNH KHI NGỦ\n" +
                "Chế độ ngủ đêm sẽ tự động điều chỉnh giảm độ ồn động cơ vận hành xuống mức thấp nhất, \n" +
                "giúp bạn tận hưởng giấc ngủ ngon và sâu trong không gian yên tĩnh tuyệt đối.\n" +
                "Hướng gió dễ chịu\n" +
                "Chế độ hướng gió dễ chịu giúp bạn tránh khỏi luồng gió thổi trực tiếp vào cơ thể, đem đến cho bạn giấc ngủ thư giãn hơn\n" +
                "\n" +
                "Màng lọc giúp loại bỏ các tác nhân gây ô nhiễm\n" +
                "Hệ thống lọc của LG được thiết kế để bắt giữ các hạt bụi có kích thước trên 10μm cũng như các chất có khả năng\n" +
                " gây dị ứng trong không khí như mạt bụi giúp mang đến một môi trường trong lành hơn.\n" +
                "\n" +
                "Tiếng ồn thấp\n" +
                "Máy điều hòa không khí LG vận hành với mức âm thanh thấp, nhờ có thiết kế quạt nghiêng độc đáo của LG và\n" +
                " máy nén kép Dual Inverter Compressor™ giúp loại bỏ tiếng ồn không cần thiết và cho phép vận hành êm ái hơn.\n" +
                "\n" +
                "Thông tin bảo hành:\n" +
                "Thời gian bảo hành 24 tháng \n" +
                "https://www.lg.com/vn/tro-giup/bao-hanh \n" +
                "Trung Tâm Thông Tin Khách Hàng / Customer Information Center 18001503 (Miễn phí cước gọi/Toll free)\n" +
                "\n" +
                "Công lắp đặt:\n" +
                "- Miễn phí cho nội thành HCM (Quận 1, 2, 3, 4, 5, 6, 7, 8, 10, 11, Tân Bình, Tân Phú, Phú Nhuận, Bình Thạnh, Gò Vấp,\n" +
                " Quận 9, 12, Thủ Đức, Bình Tân, Hóc Môn) và nội thành Hà Nội (Quận Ba Đình, Quận Bắc Từ Liêm, Quận Cầu Giấy,\n" +
                " Quận Hà Đông, Quận Hai Bà Trưng, Quận Hoàn Kiếm, Quận Hoàng Mai, Quận Long Biên, Quận Nam Từ Liêm, Quận Tây Hồ,\n" +
                " Quận Thanh Xuân, Quận Đống Đa)\n" +
                "- Chi phí vật tư: Nhân viên sẽ thông báo phí vật tư (ống đồng, dây điện v.v...) khi khảo sát lắp đặt (Bảng kê xem tại ảnh 2). \n" +
                "Khách hàng sẽ thanh toán trực tiếp cho nhân viên kỹ thuật sau khi việc lắp đặt hoàn thành - chi phí này sẽ không hoàn lại\n" +
                " trong bất cứ trường hợp nào.\n" +
                "- Quý khách hàng có thể trì hoãn việc lắp đặt tối đa là 7 ngày lịch kể từ ngày giao hàng thành công (không tính ngày Lễ). \n" +
                "Nếu nhân viên hỗ trợ không thể liên hệ được với Khách hàng quá 3 lần, hoặc Khách hàng trì hoãn việc lắp đặt quá thời hạn trên, \n" +
                "Dịch vụ lắp đặt sẽ được hủy bỏ.\n" +
                "- Đơn vị vận chuyển giao hàng cho bạn KHÔNG có nghiệp vụ lắp đặt sản phẩm. \n" +
                "- Thời gian bộ phận lắp đặt liên hệ (không bao gồm thời gian lắp đặt): trong vòng 24h kể từ khi nhận hàng (Trừ Chủ nhật/ Ngày Lễ). \n" +
                "Trong trường hợp bạn chưa được liên hệ sau thời gian này, vui lòng gọi lên hotline của Shopee (19001221) để được tư vấn.\n" +
                "- Tìm hiểu thêm về Dịch vụ lắp đặt: \n" +
                "help.shopee.vn/vn/s/article/Làm-thế-nào-để-tôi-có-thể-sử-dụng-dịch-vụ-lắp-đặt-tại-nhà-cho-các-sản-phẩm-tivi-điện-máy-lớn-\n" +
                "1542942683961\n" +
                "\n" +
                "- Quy định đổi trả: Chỉ đổi/trả sản phẩm, từ chối nhận hàng tại thời điểm nhận hàng trong trường hợp sản phẩm giao đến \n" +
                "không còn nguyên vẹn, thiếu phụ kiện hoặc nhận được sai hàng. Khi sản phẩm đã được cắm điện sử dụng và/hoặc lắp đặt, \n" +
                "và gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/7f1f2e0a2d8eab23bf96236ac312141f_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.WHITE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1500));
        productVariantList.add(variant_1);

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Máy lọc không khí LG Puricare Pro AS40GWWJ1");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Điện tử").getId());
        productDTO.setIndustrialTypeName("Điện tử");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/cf1c11d40841d372c82be9168dbc2272_tn");
        images.add("https://cf.shopee.vn/file/b3300bf431726748ecd02f5162a39c3e_tn");
        images.add("https://cf.shopee.vn/file/e88f8cd5af82d3245ca62f457df2d203_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/03cf81eb69975a43dd8a9af1aaca32b4_tn");
        //thêm mô tả
        productDTO.setDescription("\n" +
                "Màn hình hiển thị thông minh\n" +
                "Màn hình hiển thị thông minh hiển thị mức độ hạt bụi trong nhà theo thời gian thực với cảm biến PM 1.0 và cảm biến mùi.\n" +
                "Cảm biến PM1.0 có thể cảm nhận được các hạt bụi mịn lên đến 1,0㎛. Cảm biến mùi phát hiện một số hợp chất gây mùi \n" +
                "trong không khí.\n" +
                "\n" +
                "Đèn hiển thị chất lượng không khí\n" +
                "Giờ đây bạn có thể dễ dàng kiểm tra chất lượng không khí từ xa hoặc vào ban đêm với đèn hiển thị chất lượng không khí \n" +
                "bằng 4 màu khác nhau.\n" +
                "*Mức chất lượng không khí và màu sắc đèn tương ứng có thể khác ở mỗi quốc gia.\n" +
                "\n" +
                "Hoạt động êm ái\n" +
                "Với động cơ Inverter thông minh, bạn có thể tận hưởng không khí đã được lọc sạch căn phòng vẫn rất yên tĩnh. \n" +
                "Chế độ hoạt động LowDecibel có thể giữ độ ồn thấp tới 20dB và vẫn lọc sạch được không khí.\n" +
                "\n" +
                "Hệ thống lọc thông minh\n" +
                "Hệ thống lọc 3 bước giúp loại bỏ các chất có hại như các hạt lớn, bụi mịn PM0.02, mạt cưa, formaldehyde, \n" +
                "SO2 & NO2, virus và vi khuẩn.\n" +
                "\n" +
                "Thiết kế độc đáo và tiện lợi\n" +
                "Máy lọc không khí LG có thiết kế tối ưu và hướng đến người dùng. Chiều cao của máy cho phép người lớn hoặc\n" +
                " trẻ em vận hành dễ dàng, và hình dạng tròn thanh lịch giúp tiết kiệm không gian.\n" +
                "\n" +
                "Dễ dàng thay thế bảo dưỡng\n" +
                "Khi cần thay bộ lọc, bạn sẽ nhận được cảnh báo trên màn hình. Ngoài ra, bạn có thể lấy ra tất cả các bộ lọc \n" +
                "cùng một lúc mà không làm đổ bụi ra khỏi bộ lọc và thay các bộ lọc khác nhau theo màu sắc một cách dễ dàng. \n" +
                "Với chức năng Khóa trẻ em, bạn có thể giữ cho máy lọc không khí an toàn với trẻ em.\n" +
                "\n" +
                "Thông số kỹ thuật\n" +
                "Mã sản phẩm        AS40GWWJ1\n" +
                "Công suất [W]        32\n" +
                "Màu sắc        Trắng\n" +
                "Kích thước (mm)        295 x 779 x 308\n" +
                "Trọng lượng (kg)        8.3CADR [m3/hr]        256\n" +
                "Diện tích sử dụng (m2)        32.8\n" +
                "Độ ồn (dB)        47/20\n" +
                "Chế độ hoạt động - Tự động        Có\n" +
                "Chế độ hoạt động - Auto        Có\n" +
                "Chế độ hoạt động - Ban đêm        Có\n" +
                "Hẹn giờ        Có\n" +
                "Bộ lọc        Màng lọc thô x 1 cái / Màng lọc HEPA x 1 cái / Màng lọc mùi x 1 cái\n" +
                "Loại màng lọc        E11\n" +
                "Bộ phát ion        Có\n" +
                "Cảm biến - Mùi        Có\n" +
                "Cảm biến - Bụi PM 1.0        Có\n" +
                "KhÓa trẻ em        Có\n" +
                "Wi-Fi (ThinQ)        Có\n" +
                "Phân tích thông minh        Có");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/cf1c11d40841d372c82be9168dbc2272_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.WHITE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testProduct_802539_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        //sửa tên sản phâm
        productDTO.setName("Máy giặt LG inverter 10.5kg (trắng) - T2350VS2W");
        //mã shop sửa lại
        productDTO.setShopId(802539);
        //mã thương hiệu
        productDTO.setTradeMarkId("1703772512358215");
        //sửa ngành hàng
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Ô tô").getId());
        productDTO.setIndustrialTypeName("Ô tô");
        //thêm link ảnh phụ
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/7b7b1c4b91085b9a5b91cb952c5c6a02_tn");
        images.add("https://cf.shopee.vn/file/086ecd6909ea6fb16bf5a1020be4b298_tn");
        images.add("https://cf.shopee.vn/file/1d44d0ee67965b58eaab8229f53bb0cb_tn");
        images.add("https://cf.shopee.vn/file/879ae34be26039dd0b531cd25e2db606_tn");
        productDTO.setImageUrls(images);
        //thêm link ảnh chính vô
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/6cb035e3a544fb0ebdf98f21da472434_tn");
        //thêm mô tả
        productDTO.setDescription("\n" +
                "Tiết kiệm năng lượng với công nghệ Smart Inverter™\n" +
                "Công nghệ Smart Inverter loại bỏ các hoạt động không cần thiết bằng cách điều khiển điện năng tiêu thụ một cách tối ưu.\n" +
                "\n" +
                "Chuyển động thông minh Smart Motion\n" +
                "Smart Motion là 3 chuyển động giặt do động cơ Smart Inverter tạo ra, để mang tới khả năng giặt tối ưu cho từng loại vải.\n" +
                " Sự kết hợp tối ưu giúp chăm sóc áo quần hoàn hảo.\n" +
                "\n" +
                "Giặt xoay chiều TurboDrum™\n" +
                "TurboDrum™ mang tới hiệu năng giặt mạnh mẽ và khả năng đánh bật cả các vết bẩn cứng đầu nhất nhờ xoáy nước mạnh mẽ, \n" +
                "tạo ra bởi việc xoay đảo hướng lồng giặt và mâm giặt ngược chiều nhau.\n" +
                "\n" +
                "Đấm nước Punch+3\n" +
                "Punch+3 tạo ra các dòng nước theo phương thẳng đứng, đưa đồ giặt lên và xuống liên tục, mang tới hiệu quả giặt đồng đều.\n" +
                "\n" +
                "Giặt sơ tự động\n" +
                "Chỉ với một chạm, vết bẩn khó giặt sẽ biến mất.\n" +
                "Hãy để đôi bàn tay của bạn nghỉ ngơi và để máy giặt làm công việc của chúng!\n" +
                "\n" +
                "Thác nước vòng cung\n" +
                "Các Thác nước vòng cung giúp hòa tan bột giặt nhanh chóng để dễ dàng thẩm thấu vào áo quần, \n" +
                "đồng thời giảm thiểu cặn bột giặt còn sót lại gây dị ứng và mẩn ngứa\n" +
                "\n" +
                "Bền Bỉ & Giảm Rung Ồn\n" +
                "Động cơ Smart Inverter không chỉ giảm rung và ồn tối đa mà còn tăng sự bền bỉ của động cơ. \n" +
                "Động cơ sẽ được bảo hành lên tới 10 năm.\n" +
                "\n" +
                "Chẩn đoán thông minh Smart Diagnosis™\n" +
                "Smart Diagnosis™ giúp chẩn đoán và khắc phục các sự cố sản phẩm, giảm số lần phải đến bảo dưỡng tốn chi phí và \n" +
                "không thuận tiện\n" +
                "\n" +
                "Thiết kế an toàn và thuận tiện\n" +
                "Thiết kế phong cách và bền bỉ, đảm bảo sự thuận tiện và an toàn, đồng thời mang đến những tính năng hiệu quả và đổi mới.\n" +
                "\n" +
                "Thông số kỹ thuật\n" +
                "Khối lượng giặt: 10.5kg\n" +
                "Màu sắc: Trắng\n" +
                "Kích thước (Dài x Cao x Sâu): 590 x 960 x 606\n" +
                "Công nghệ Smart Inverter™: Có\n" +
                "Chuyển động thông minh Smart Motion™: Có\n" +
                "Giặt xoay chiều TurboDrum™: Có\n" +
                "Loại máy giặt: Máy giặt lồng đứng\n" +
                "Trọng lượng: 39kg\n" +
                "\n" +
                "Thông tin bảo hành\n" +
                "Thời gian bảo hành: 24 tháng.\n" +
                "Thông tin chi tiết xem tại: https://www.lg.com/vn/tro-giup/bao-hanh.\n" +
                "\n" +
                "***Công lắp đặt:\n" +
                "- Miễn phí cho nội thành HCM (Quận 1, 2, 3, 4, 5, 6, 7, 8, 10, 11, Tân Bình, Tân Phú, Phú Nhuận, Bình Thạnh, Gò Vấp, \n" +
                "Quận 9, 12, Thủ Đức, Bình Tân, Hóc Môn) và nội thành Hà Nội (Quận Ba Đình, Quận Bắc Từ Liêm, Quận Cầu Giấy, \n" +
                "Quận Hà Đông, Quận Hai Bà Trưng, Quận Hoàn Kiếm, Quận Hoàng Mai, Quận Long Biên, Quận Nam Từ Liêm, Quận Tây Hồ,\n" +
                " Quận Thanh Xuân, Quận Đống Đa)\n" +
                "- Chi phí vật tư: Nhân viên sẽ thông báo phí vật tư (ống đồng, dây điện v.v...) khi khảo sát lắp đặt (Bảng kê xem tại ảnh 2). \n" +
                "Khách hàng sẽ thanh toán trực tiếp cho nhân viên kỹ thuật sau khi việc lắp đặt hoàn thành - chi phí này sẽ không hoàn lại\n" +
                " trong bất cứ trường hợp nào.\n" +
                "- Quý khách hàng có thể trì hoãn việc lắp đặt tối đa là 7 ngày lịch kể từ ngày giao hàng thành công (không tính ngày Lễ). \n" +
                "Nếu nhân viên hỗ trợ không thể liên hệ được với Khách hàng quá 3 lần, hoặc Khách hàng trì hoãn việc lắp đặt quá thời hạn trên, \n" +
                "Dịch vụ lắp đặt sẽ được hủy bỏ.\n" +
                "- Đơn vị vận chuyển giao hàng cho bạn KHÔNG có nghiệp vụ lắp đặt sản phẩm.\n" +
                "- Thời gian bộ phận lắp đặt liên hệ (không bao gồm thời gian lắp đặt): trong vòng 24h kể từ khi nhận hàng (Trừ Chủ nhật/ Ngày Lễ). \n" +
                "Trong trường hợp bạn chưa được liên hệ sau thời gian này, vui lòng gọi lên hotline của Shopee (19001221) để được tư vấn.\n" +
                "- Tìm hiểu thêm về Dịch vụ lắp đặt:\n" +
                "help.shopee.vn/vn/s/article/Làm-thế-nào-để-tôi-có-thể-sử-dụng-dịch-vụ-lắp-đặt-tại-nhà-cho-các-sản-phẩm-tivi-điện-máy-lớn-\n" +
                "1542942683961\n" +
                "\n" +
                "- Quy định đổi trả: Chỉ đổi/trả sản phẩm, từ chối nhận hàng tại thời điểm nhận hàng trong trường hợp sản phẩm giao đến\n" +
                " không còn nguyên vẹn, thiếu phụ kiện hoặc nhận được sai hàng. Khi sản phẩm đã được cắm điện sử dụng và/hoặc lắp đặt,\n" +
                " và gặp lỗi kĩ thuật, sản phẩm sẽ được hưởng chế độ bảo hành theo đúng chính sách của nhà sản xuất");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/7b7b1c4b91085b9a5b91cb952c5c6a02_tn");
        variant_1.setWeight(400.0);
        //sửa màu
        variant_1.setColor(ColorProduct.WHITE);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        // sửa giá tiền 1000
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        productVariantList.add(variant_1);

        createProductDTO1.setProductVariants(productVariantList);


        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //sachhaynhat.vn
    @Test
    public void testProduct_802772_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách The Magic Phép màu 142577882");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/7d6513b524af0365c85dee9cbdbd8e42");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/e3df9f4ffe00e063d06cfdfa081c31c1");
        productDTO.setDescription("Giới thiệu sách\n" +
                "\n" +
                "thông tin chi tiết\n" +
                "\n" +
                "Công ty phát hành : Công ty Cổ Phần Thiên Minh Book\n" +
                "\n" +
                "-Tác giả        Rhonda Byrne\n" +
                "\n" +
                "-Nhà xuất bản        : Nhà Xuất Bản Thể Giới\n" +
                "\n" +
                "-Loại bìa        Bìa cứng\n" +
                "\n" +
                "-Số trang        260 trang\n" +
                "\n" +
                "khổ sách : 13x19cm\n" +
                "\n" +
                "Năm xuất bản : 2020\n" +
                "\n" +
                "The Magic Phép màu 142577882\n" +
                "\n" +
                "Cuốn sách Trong suốt hơn 20 thế kỷ qua, những lời dạy trong các văn bản linh thiêng cổ xưa đã khiến hầu hết mọi người hiểu nhầm. Chỉ có một số ít người trong lịch sử có thể nhận ra rằng nó thật ra là một câu đố, và một khi đã giải được ẩn ý trong câu đố ấy -  để vén bức màn bí mật - một thế giới hoàn toàn mới sẽ xuất hiện trước mắt bạn.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Trong quyển sách phép màu, Rhonda Byrne đã tiết lộ bí mật đầy quyền năng này cho cả thế giới. Và sau đó, với hành trình 28 ngày nhiệm màu, cô ấy hướng dẫn chúng ta phương pháp ứng dụng kiến thức này vào cuộc sống thường nhật.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bất kể bạn là ai, bất kể bạn ở đâu, bất kể tình huống hiện tại của bạn là gì, phép màu sẽ thay đổi hoàn toàn cuộc sống của bạn!\n" +
                "\n" +
                "đây là một cuốn sách rất cần thiết cho các bạn");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/e3df9f4ffe00e063d06cfdfa081c31c1");
        variant_1.setWeight(25.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(350));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Tôi Tài Giỏi Bạn Cũng Thế (Tái Bản 2019 )");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/d47cb9e0f67e467a5fb13ffc742995aa");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/d47cb9e0f67e467a5fb13ffc742995aa");
        productDTO.setDescription("Công ty phát hành : Cty TGM\n" +
                "Tác giả : Adam Khoo\n" +
                "Ngày xuất bản : 02-2019\n" +
                "Kích thước : 16 x 24 cm\n" +
                "Nhà xuất bản : NXB Phụ Nữ\n" +
                "Loại bìa : Bìa mềm\n" +
                "Số trang : 276 trang\n" +
                "Khi bạn cầm trên tay quyển sách này, có nghĩa là bạn đã có chiếc chìa khóa đến sự thành công cùng bảng hướng dẫn sử dụng...\n" +
                "\n" +
                "Trong chúng ta, bất kỳ ai cũng muốn chính bản thân mình trở thành người tài giỏi, có thể giải quyết mọi vấn đề một cách hiệu quả nhất. Và để có được những điều đó quyển sách này sẽ giúp bạn bằng những hướng dẫn học tập chi tiết nhất.\n" +
                "\n" +
                "Tác giả không chỉ đơn thuần giải thích người khác đã thành công như thế nào, mà còn nói làm sao để họ làm được như thế để giúp người đọc khám phá ra tiềm năng của bản thân, và phát huy điều đó. Ngoài ra, sách còn cung cấp những phương pháp học thông minh (như áp dụng các công cụ học bằng cả não bộ như Sơ Đồ Tư Duy, phát triển trí nhớ siêu việt để ghi nhớ các sự kiện, con số một cách dễ dàng, thành thạo việc quản lý thời gian và xác định mục tiêu). Adam Khoo đã cho thấy, tài giỏi mang lại sự tự tin như thế nào và còn hướng dẫn bạn cách thức trở thành người tài giỏi. Qua đó độc giả sẽ lập được kế hoạch cho cuộc đời của chính mình.\n" +
                "\n" +
                "Tôi Tài Giỏi - Bạn Cũng Thế sẽ giúp tìm ra giải pháp tốt nhất cho mọi vấn đề, và giúp nhận ra cách thức để thành công. Tuy nhiên để làm một người tài giỏi thì người đọc cần đặt quyển sách xuống và thực thi ngay các kế hoạch. Đương nhiên, không phải chỉ chăm chỉ ngày một, ngày hai mà mỗi người phải thực hiện lâu dài, thậm chí cả đời thì mới đạt được những gì mình muốn.\n" +
                "\n" +
                "Thật sự đây là một quyển sách rất tuyệt vời và bổ ích, đem lại cho chúng ta những kinh nghiệm quý báu. Một quyển sách không - thể - thiếu trong tủ sách!\n" +
                "\n" +
                "“Thật không biết phải làm sao với con trai chúng tôi. Nó được gởi đi học thêm khắp nơi mà vẫn làm bài thi tệ hại. Chúng tôi tự hỏi sau này nó có làm nên trò trống gì không nữa”…\n" +
                "\n" +
                "Đó chính là những gì mà cha mẹ của Adam Khoo đã từng than vãn về sự kém cõi và kết quả thi cử thảm hại của cậu bé Adam nhiều năm về trước. May mắn thay, vào thời điểm tăm tối nhất trong đời, Adam đã tìm thấy và học tập theo công thức thành công của những người tài giỏi vượt bậc. Chính vì thế, từ một cậu học trò kém cỏi nhất trong số những học sinh kém, không những anh đã vươn lên để đạt đuợc kết quả xuất sắc trong các kỳ thi cuối cấp hai và cấp ba, anh còn đuợc xếp hạng trong số 1% sinh viên tài năng nhất của trường Đại học Quốc Gia Singapore (NUS)");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/d47cb9e0f67e467a5fb13ffc742995aa");
        variant_1.setWeight(21.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(350));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách - Truyện tranh tư duy cho bé,phát triển ngôn ngữ, PT trí tưởng tượng,rèn luyện khả năng quan sát,pt khả năng tư duy");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/dbbca2b146480594bd5ce1cbfa872120");
        images.add("https://cf.shopee.vn/file/d1044582f33209badc0f12d7f245b537");
        images.add("https://cf.shopee.vn/file/f58f4b1d153fd7888a4967bb9b69363e");
        images.add("https://cf.shopee.vn/file/2d20d73b7bdd8f8d3eb97f30b4eb19ec");
        images.add("https://cf.shopee.vn/file/cc1bc8f9db05354573070e0fa856f2fa");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/d1e62936a717e362c8b2c2a48aef01ce");
        productDTO.setDescription("THÔNG TIN CHI TIẾT\n" +
                "Công Ty phát hành : NS Đại Mai\n" +
                "Tác giả        Hải Minh\n" +
                "Ngày xuất bản        08-2019\n" +
                "Kích thước        17x24\n" +
                "Nhà xuất bản        Nhà Xuất Bản Phụ Nữ\n" +
                "Dịch Giả        Hải Minh ( biên soạn )\n" +
                "Loại bìa        Bìa cứng\n" +
                "Số trang        146 trang\n" +
                "GIỚI THIỆU SÁCH\n" +
                "Truyện tranh tư duy cho bé,phát triển ngôn ngữ, PT trí tưởng tượng,rèn luyện khả năng quan sát,pt khả năng tư duy\n" +
                "cuốn sách là những câu chuyện ngụ ngôn hay nhất , được thiết kế đọc truyện theo tư duy hình ảnh ,từng câu chuyện kết hợp với hình ảnh thay cho từ , để bé đoán hình thành câu chuyện đọc hay cho bé , giúp bé phát triển tư duy nhanh trí , khả năng tưởng tượng , rèn luyện cho bé");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/d1e62936a717e362c8b2c2a48aef01ce");
        variant_1.setWeight(22.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(450));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách - Combo 25 Chuyên Đề Ngữ Pháp Tiếng Anh Trọng Tâm Tập 1 và Tập 2 (Trọn Bộ 2 Tập)");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/fcb79729b9b5b41848772c1b7eecb328");
        images.add("https://cf.shopee.vn/file/ed5256ff782493bbef3abadd9364f517");
        images.add("https://cf.shopee.vn/file/c1aed5564d6d84443e5f34badd9d6af1");
        images.add("https://cf.shopee.vn/file/a8b358de96c0f60b2196dd48c62b37b6");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/980089e0069f23fbfd6ebafbee79d023");
        productDTO.setDescription("THÔNG TIN CHI TIẾT\n" +
                "Công ty phát hành        Huy Hoàng Bookstore\n" +
                "Ngày xuất bản        : 02-2019\n" +
                "Tác Giả        Trang Anh\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        308 trang\n" +
                "Khổ sách : 19x26cm\n" +
                "Nhà xuất bản        Nhà Xuất Bản Đại Học Sư Phạm\n" +
                "GIỚI THIỆU SÁCH\n" +
                "Combo 25 Chuyên Đề Ngữ Pháp Tiếng Anh Trọng Tâm Tập 1 và Tập 2 (Trọn Bộ 2 Tập)\n" +
                "Bộ sách ngữ pháp Tiếng Anh gồm 2 tập chắt lọc toàn bộ kiến thức ngữ pháp trọng tâm trong Tiếng Anh, phù hợp cho nhiều đối tượng: học sinh, sinh viên, các bạn muốn luyện thi các kì thi chuyên, kì thi quốc gia và cả những kì thi quốc tế và cả những đồng nghiệp, đồng môn tham khảo.\n" +
                "\n" +
                "Toàn bộ kiến thức trong sách được tác giả sắp xếp theo từng chuyên đề, hệ thống logic và diễn giải đơn giản, dễ hiểu. Hệ thống từ vựng đi kèm phong phú đủ để bạn vận dụng vào những cấu trúc tiếng Anh phù hợp. ‘Practice makes perfect’ – đây là slogan của cô giáo Trang Anh trong quá trình dạy học cho các bạn nên tác giả đã thiết kế rất nhiều dạng bài tập thực hành cho người học ghi nhớ, củng cố kiến thức. Sau 5 chuyên đề là có phần ôn tập để liên kết và hệ thống kiến thức với nhau nên người học không sợ quên kiến thức.\n" +
                "~~~*~~~\n" +
                "THÔNG TIN CHI TIẾT\n" +
                "Công ty phát hành: Huy Hoàng Book\n" +
                "Nhà Xuất bản: Đại Học Sư Phạm\n" +
                "Tác Giả: Trang Anh\n" +
                "Loại bìa: Bìa mềm\n" +
                "Kích Thước: 19 x 26.5 cm\n" +
                "Số Trang: 308 trang/tập 1; 363 trang/tập 2\n" +
                "Năm Xuất Bản: 01-2019 (Phiên Bản Tái Bản có sửa chữa, bổ sung)");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/980089e0069f23fbfd6ebafbee79d023");
        variant_1.setWeight(50.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(700));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Cẩm Nang Cấu Trúc Tiếng Anh");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/dd1bc094eb06947cba620041f264ffcd");
        images.add("https://cf.shopee.vn/file/3fdbc963a1ef667cebf51939436f85ae");
        images.add("https://cf.shopee.vn/file/ff7cb5ab8f3ecdb1da32939261ac8f79");
        images.add("https://cf.shopee.vn/file/e98c976f9d08974caafa55231f5e2a05");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/3b204b6a090682c8039c029aa1ef478d");
        productDTO.setDescription("GIỚI THIỆU SÁCH\n" +
                "Cẩm Nang Cấu Trúc Tiếng Anh (Trang Anh) -  (Kiến Thức Siêu Đầy Đủ - Áp Dụng Cực Dễ Dàng)\n" +
                "\n" +
                "Cuốn sách CẨM NANG CẤU TRÚC TIẾNG ANH gồm 25 phần, mỗi phần là một phạm trù kiến thức trong tiếng Anh được trình bày một cách ngắn gọn, đơn giản, cô đọng và hệ thống hoá dưới dạng sơ đồ, bảng biểu nhằm phát triển khả năng tư duy của người học và từ đó giúp người học nhớ kiến thức nhanh hơn và sâu hơn.\n" +
                "\n" +
                "Sau hầu hết các phần lí thuyết đều có 20-30 câu bài tập áp dụng để kiểm tra cũng như khắc sâu kiến thức cho người học.\n" +
                "\n" +
                "Tuy dày chưa đến 250 trang nhưng cuốn sách lại có thể bao trọn toàn bộ kiến thức từ đơn giản đến phức tạp cộng với cách tận dụng tối đa và áp dụng triệt để cách học tiếng Anh bằng sơ đồ tư duy.\n" +
                "\n" +
                "\"Kiến thức siêu đầy đủ - Áp dụng cực dễ dàng\", cuốn sách phù hợp với mọi trình độ, mọi đối tượng cũng như nhu cầu của người học.\n" +
                "\n" +
                "\" Tôi tự tin là người sẽ thay đổi quan niệm của người học bằng cách đơn giản hóa Tiếng Anh để người học nhận thấy học tiếng anh thật dễ dàng\" - Trang Anh (Lời tựa tác giả)\n" +
                "\n" +
                "~~~*~~~\n" +
                "THÔNG TIN CHI TIẾT\n" +
                "Công ty phát hành: Huy Hoang Bookstore\n" +
                "Nhà xuất bản: Nhà Xuất Bản Đại Học Sư Phạm\n" +
                "Tác giả: Trang Anh\n" +
                "Loại bìa: Bìa mềm\n" +
                "Kích thước: 19x26.5 cm\n" +
                "Số trang: 239\n" +
                "Ngày xuất bản: 04-2019");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/3b204b6a090682c8039c029aa1ef478d");
        variant_1.setWeight(40.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(600));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Giáo Trình Hán Ngữ Tập 1 Quyển Thượng (Phiên Bản Mới App)");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/33a4f93e215b7d9f99a0586d926efd83");
        images.add("https://cf.shopee.vn/file/e19f5d14a25ed944b73c6a8b019fe547");
        images.add("https://cf.shopee.vn/file/662f078f49171cee3e23f0422d6338e3");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/344f25943bcce98841ca1bb975c3f622");
        productDTO.setDescription("Thông Tin Chi Tiết\n" +
                "\n" +
                "Công ty phát hành : MCBOOKS\n" +
                "\n" +
                "Tác giả : Đại học ngôn ngữ bắc kinh\n" +
                "\n" +
                "Biên dịch : Trần thị thanh Liêm\n" +
                "\n" +
                "Kích thước ;19 x 27 cm\n" +
                "\n" +
                "Loại bìa :Bìa mềm\n" +
                "\n" +
                "Số trang 158 trang\n" +
                "\n" +
                "Năm xuất bản : 2022\n" +
                "\n" +
                "Nhà xuất bản : Nhà Xuất Bản Đại Học quốc gia Hà Nội\n" +
                "\n" +
                "Giới thiệu sách\n" +
                "\n" +
                "Giáo Trình Hán Ngữ Tập 1 - Quyển Thượng (Phiên Bản Mới - App)\n" +
                "\n" +
                "Bộ Giáo Trình Hán Ngữ gồm nhiều bài với nội dung từ dễ đến khó. Bắt đầu từ luyện tập ngữ âm cơ bản tiếng Hán, từ đó tiến hành giảng dạy kết cấu ngữ pháp, ngữ nghĩa và ngữ dụng, cuối cùng là giai đoạn giảng dạy ngữ đoạn, thông qua sự hiểu biết sâu hơn về ngữ pháp và vận dụng từ ngữ, để nâng cao hơn nữa khả năng biểu đạt thành đoạn văn của người học.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Mỗi bài học bao gồm các phần:\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bài khóa\n" +
                "\n" +
                "Từ mới\n" +
                "\n" +
                "Chú thích\n" +
                "\n" +
                "Ngữ pháp, ngữ âm\n" +
                "\n" +
                "Luyện tập\n" +
                "\n" +
                "Giáo Trình Hán Ngữ có 3300 từ mới. Phần bài khóa và nội dung đàm thoại có quan hệ chủ điểm đồng nhất với bài luyện đọc trong phần bài tập, trên cơ sở bảng từ mới của từng bài. Đặc biệt, cuốn sách học cùng app MCBooks ghi giọng đọc chuẩn giúp bạn luyện nghe và học phát âm hiệu quả.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Mục lục\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bài 1: Xin chào\n" +
                "\n" +
                "Bài 2: Tiếng Hán không khó lắm\n" +
                "\n" +
                "Bài 3: Ngày mai gặp lại\n" +
                "\n" +
                "Bài 4: Bạn đi đâu đấy\n" +
                "\n" +
                "Bài 5: Đây là thầy giáo Vương\n" +
                "\n" +
                "Bài 6: Tôi học tiếng Hán\n" +
                "\n" +
                "Bài 7: Bạn ăn gì\n" +
                "\n" +
                "Bài 8: Một cân táo bao nhiêu tiền II\n" +
                "\n" +
                "Bài 3: Ngày mai gặp lại\n" +
                "\n" +
                "Bài 4: Bạn đi đâu đấy IV\n" +
                "\n" +
                "Bài 9: Tôi đổi tiền nhân dân tệ\n" +
                "\n" +
                "Bài 10: Ông ấy sống ở đâu\n" +
                "\n" +
                "Bài 11: Chúng tôi đều là lưu học sinh V\n" +
                "\n" +
                "Bài 12: Bạn học ở đâu\n" +
                "\n" +
                "Bài 13: Đây không phải là thuốc Đông y\n" +
                "\n" +
                "Bài 14: Xe của bạn mới hay cũ\n" +
                "\n" +
                "Bài 15: Công ty của ông có bao nhiêu nhân viên\n" +
                "\n" +
                "Bảng từ vựng");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/344f25943bcce98841ca1bb975c3f622");
        variant_1.setWeight(45.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(650));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Đồng Dao Thơ Truyện cho bé tập nói");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/18ddc7ad793c41e9d46a465065cf73c7");
        images.add("https://cf.shopee.vn/file/58e8f352cec3493be28613f3c3ae45f5");
        images.add("https://cf.shopee.vn/file/6a91327dd9f3bf9bf7a0285dbe5c450c");
        images.add("https://cf.shopee.vn/file/70509ba76f79ad69a3f4f016220cdff9");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/4eb5e10c0cf2de04d697e293199cf01f");
        productDTO.setDescription("Thông tin chi tiết \n" +
                "Công ty phát hành : ĐẠI MAI BOOKS\n" +
                "-Tác giả        : Hải Minh\n" +
                "-Ngày xuất bản        2020\n" +
                "-Nhà xuất bản        :Nhà Xuất Bản Phụ Nữ việt nam\n" +
                "-Loại bìa        Bìa mềm\n" +
                "-Số trang        128 trang\n" +
                "Khổ sách 20x28cm\n" +
                "Giới thiệu sách\n" +
                "Đồng dao thơ-truyện cho bé tập nói , giúp bé phát triển ngôn ngữ, khả năng nghe hiểu, khả năng nhận biết,\n" +
                "\n" +
                "Ngay từ năm đầu đời, bé có thể bập bẹ những tiếng nói đầu tiên. Theo thời gian, ngôn ngữ của bé càng được hoàn thiện và phát triển. Đọc sách cho bé nghe để khuyến khích bé tập nói là một trong những phương pháp tối ưu giúp bé phát triển ngôn ngữ.\n" +
                "\n" +
                "Cuốn sách Đồng dao -Thơ -Truyện cho bé tập nói '' tuyển chọn những bài đồng dao, bài thơ và những câu chuyện phù hợp cho lứa tuổi tập nói, sách gồm có 3 phần.\n" +
                "\n" +
                "Đồng dao : là thơ ca truyền miệng dân gian, có nhịp có vần. Trẻ em nhỏ chưa hiểu lời thích đồng dao và chỉ cần mẹ đọc cho nghe, bé dậm chậm chân, vỗ vỗ tay theo nhịp. Bé lớn hơn càng thích đồng dao vì các bé có thể vừa hát đồng dao, vừa chơi những trò chơi gắn với đồng\n" +
                "\n" +
                "Phần Thơ : tuyển chọn những bài thơ hay, ngắn và nghộ nghĩnh.\n" +
                "\n" +
                "Phần Truyện : gồm những câu chuyện nhỏ dễ đọc, dễ hiểu, nội dung phong phú. Dung lượng chữ trong mỗi chuyện phù hợp với sự tiếp nhận của các bé trong độ tuổi tập nói.\n" +
                "\n" +
                "Cuốn sách có tranh minh họa sinh động, không chỉ giúp bé phát triển ngôn ngữ mà còn giúp bé hiểu thêm về cuộc sống quanh mình. Sách phù hợp với bé từ 0-6 tuổi");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/4eb5e10c0cf2de04d697e293199cf01f");
        variant_1.setWeight(20.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(350));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách 301 Câu Đàm Thoại Tiếng Hoa ( Trương văn giới lê khắc kiều lục ,khổ to )");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/9f41866760ddec818486b02c0dba9000");
        images.add("https://cf.shopee.vn/file/6f63bff561f02907717ad7e90f4187ec");
        images.add("https://cf.shopee.vn/file/4d3265212ed29c991ccff0d639c83eb4");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/9a21168b60a51de153d060aba7e4ab66");
        productDTO.setDescription("Giới thiệu sách : \n" +
                "công ty phát hành : Hải hà\n" +
                "Tác giả : Nhiều tác giả\n" +
                "Kích thước : 17x24cm\n" +
                "Số trang : 479 trang\n" +
                "Kích thước : 16x24 cm\n" +
                "nhà xuất bản : NXB Khoa Học Xã Hội\n" +
                "Hình thức : bìa mềm\n" +
                "Năm xuất bản : 2021\n" +
                "301 Câu Đàm Thoại Tiếng Hoa ( Trương văn giới- lê khắc kiều lục, khổ to )\n" +
                "Giáo Trình 301 Câu Đàm Thoại Tiếng Hoa được biên soạn công phu, có chú trọng đến những đặc điểm mà học viên cần lưu tâm khi mới học chữ Hán. Mục đích là để giúp học viên nhanh chóng nắm vững chữ Hán, nắm vững cách viết chính xác các nét cơ bản của chữ Hán và những quy tắc cơ bản khi viết chữ Hán nhằm giúp các bạn viết được chuẩn xác và đúng hơn các câu, từ của chữ Hán.\n" +
                "\n");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/9a21168b60a51de153d060aba7e4ab66");
        variant_1.setWeight(30.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(550));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách - Truyện tranh ngụ ngôn dành cho thiếu nhi song ngữ anh việt, những người bạn đường");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/bf9a77e2f00f1cd719f16e1012c74304");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/ea1f829e549b5fb9c3c9f86564b3c761");
        productDTO.setDescription("Thông tin chi tiết :\n" +
                "Công ty phát hành : Đại Mai\n" +
                "Tác giả : Nhiều tác giả \n" +
                "Hình thức:  bìa mềm \n" +
                "Số trang : 25 trang\n" +
                "Năm xuất bản : 2020\n" +
                "Nhà xuất bản : NXB Phụ Nữ Việt Nam\n" +
                "KHổ sách : 20x23cm\n" +
                "Giới thiệu sách:\n" +
                "Truyện tranh ngụ ngôn dành cho thiếu nhi song ngữ anh việt, những người bạn đường\n" +
                "Truyện tranh ngụ ngôn dành cho thiếu nhi ( song ngữ Anh - Việt ) là những câu chuyện nổi tiếng trong văn học dành cho thiếu nhi, sách được thiết kế và vẽ câu chuyện theo tranh , sách được thiết kế phần tiếng anh và tiếng việt , với sự kết hợp cả hai thứ tiếng , giúp các bạn nhỏ thích thú hơn trong từng câu chuyện hay nhất được chọn lọc , bạn nhỏ có thể vừa đọc truyện tiếng anh và tiếng việt , để nâng cao phần ngoại ngữ thêm cho bé , cuối mỗi cuốn truyện đều có phần câu hỏi theo tranh cho bé thích thú hơn khi đọc xong câu chuyện, Bộ sách này có 10 chủ đề các em nhỏ tìm cho đủ tập nhé. Chúc Các bạn thiếu nhi học tập tốt.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/ea1f829e549b5fb9c3c9f86564b3c761");
        variant_1.setWeight(26.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(300));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802772_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách - Truyện tranh ngụ ngôn dành cho thiếu nhi song ngữ anh việt , chuột nhà và chuột đồng");
        productDTO.setShopId(802772);
        productDTO.setTradeMarkId("1703772512384790");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/bbb966730a62fd9a220716815dabf8ec");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/bbb966730a62fd9a220716815dabf8ec");
        productDTO.setDescription("Thông tin chi tiết :\n" +
                "Công ty phát hành : Đại Mai\n" +
                "Tác giả : Nhiều tác giả \n" +
                "Số trang : 23 trang\n" +
                "Hình thức:  bìa mềm \n" +
                "Năm xuất bản : 2020\n" +
                "Nhà xuất bản : NXB Phụ Nữ Việt Nam\n" +
                "KHổ sách : 20x23cm\n" +
                "Giới thiệu sách:\n" +
                "Truyện tranh ngụ ngôn dành cho thiếu nhi song ngữ anh việt , chuột nhà và chuột đồng\n" +
                "Truyện tranh ngụ ngôn dành cho thiếu nhi ( song ngữ Anh - Việt ) là những câu chuyện nổi tiếng trong văn học dành cho thiếu nhi, sách được thiết kế và vẽ câu chuyện theo tranh , sách được thiết kế phần tiếng anh và tiếng việt , với sự kết hợp cả hai thứ tiếng , giúp các bạn nhỏ thích thú hơn trong từng câu chuyện hay nhất được chọn lọc , bạn nhỏ có thể vừa đọc truyện tiếng anh và tiếng việt , để nâng cao phần ngoại ngữ thêm cho bé , cuối mỗi cuốn truyện đều có phần câu hỏi theo tranh cho bé thích thú hơn khi đọc xong câu chuyện, Bộ sách này có 10 chủ đề các em nhỏ tìm cho đủ tập nhé. Chúc Các bạn thiếu nhi học tập tốt.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/bbb966730a62fd9a220716815dabf8ec");
        variant_1.setWeight(21.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(300));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //
    @Test
    public void testProduct_802773_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Nhà Giả Kim ( Paulo Coelho )");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a6882e15b20e5ecb22a0c87e14d1fde9");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/a6882e15b20e5ecb22a0c87e14d1fde9");
        productDTO.setDescription("Công ty phát hành        Nhã Nam\n" +
                "\n" +
                "Tác giả        Paulo Coelho\n" +
                "\n" +
                "Ngày xuất bản        10-2022\n" +
                "\n" +
                "Kích thước        13 x 20.5 cm\n" +
                "\n" +
                "Nhà xuất bản        Nhà Xuất Bản Văn Học\n" +
                "\n" +
                "Dịch Giả        Lê Chu Cầu\n" +
                "\n" +
                "Loại bìa        Bìa mềm\n" +
                "\n" +
                "Số trang        228\n" +
                "\n" +
                "SKU        2518407786529\n" +
                "\n" +
                "\n" +
                "\n" +
                "Nhà Giả Kim\n" +
                "\n" +
                "Tất cả những trải nghiệm trong chuyến phiêu du theo đuổi vận mệnh của mình đã giúp Santiago thấu hiểu được ý nghĩa sâu xa nhất của hạnh phúc, hòa hợp với vũ trụ và con người.\n" +
                "\n" +
                "Tiểu thuyết Nhà giả kim của Paulo Coelho như một câu chuyện cổ tích giản dị, nhân ái, giàu chất thơ, thấm đẫm những minh triết huyền bí của phương Đông. Trong lần xuất bản đầu tiên tại Brazil vào năm 1988, sách chỉ bán được 900 bản. Nhưng, với số phận đặc biệt của cuốn sách dành cho toàn nhân loại, vượt ra ngoài biên giới quốc gia, Nhà giả kim đã làm rung động hàng triệu tâm hồn, trở thành một trong những cuốn sách bán chạy nhất mọi thời đại, và có thể làm thay đổi cuộc đời người đọc.\n" +
                "\n" +
                "“Nhưng nhà luyện kim đan không quan tâm mấy đến những điều ấy. Ông đã từng thấy nhiều người đến rồi đi, trong khi ốc đảo và sa mạc vẫn là ốc đảo và sa mạc. Ông đã thấy vua chúa và kẻ ăn xin đi qua biển cát này, cái biển cát thường xuyên thay hình đổi dạng vì gió thổi nhưng vẫn mãi mãi là biển cát mà ông đã biết từ thuở nhỏ. Tuy vậy, tự đáy lòng mình, ông không thể không cảm thấy vui trước hạnh phúc của mỗi người lữ khách, sau bao ngày chỉ có cát vàng với trời xanh nay được thấy chà là xanh tươi hiện ra trước mắt. ‘Có thể Thượng đế tạo ra sa mạc chỉ để cho con người biết quý trọng cây chà là,’ ông nghĩ.”\n" +
                "\n" +
                "\n" +
                "\n" +
                "- Trích Nhà giả kim");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/a6882e15b20e5ecb22a0c87e14d1fde9");
        variant_1.setWeight(30.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(400));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Combo 3 cuốn Nuôi con không phải là cuộc chiến");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/ec04ad5084c74927eb5891882d1d7b5e");
        images.add("https://cf.shopee.vn/file/ac576096d7ebc99425cfe96dc360374f");
        images.add("https://cf.shopee.vn/file/1eacf246f5737e168c8a27edfb26bb19");
        images.add("https://cf.shopee.vn/file/076caf52a6d60207ff36533e92246d77");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/de14d4d318f7c66a67ae28b279da36ed");
        productDTO.setDescription("Công ty phát hành        Thái Hà\n" +
                "\n" +
                "Tác giả        Nhiều Tác Giả\n" +
                "\n" +
                "Ngày xuất bản        09-2018\n" +
                "\n" +
                "Kích thước        17x23x6 cm\n" +
                "\n" +
                "Nhà xuất bản        Nhà Xuất Bản Lao Động\n" +
                "\n" +
                "Loại bìa        Bìa mềm\n" +
                "\n" +
                "Số trang        1052\n" +
                "\n" +
                "SKU        5189538798889 GIỚI THIỆU SÁCH\n" +
                "\n" +
                "Nuôi Con Không Phải Là Cuộc Chiến 2 (Trọn Bộ 3 Tập )\n" +
                "\n" +
                "\n" +
                "\n" +
                "Mốc thai kì 40 tuần đến gần, lúc này cũng là lúc mà bản năng “làm tổ” của mẹ trỗi dậy. Nhiều cha mẹ, đặc biệt là các cha mẹ chờ đợi đứa con đầu lòng chắc hẳn không tránh khỏi cảm thấy lo lắng, bồn chồn hoặc tất bật mua sắm đồ dùng, giặt là quần áo và chuẩn bị tươm tất, chờ ngày được gặp mặt con yêu.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Đây cũng là lúc mà câu chuyện của chúng ta bắt đầu…\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bên cạnh những vật chất cơ bản như quần áo, tã, bình… thì những kiến thức khoa học về ăn, ngủ và an toàn cho bé cũng là những yếu tố hết sức cần thiết mà cha mẹ cần trang bị cho mình từ trước khi con chào đời. Khoa học về dinh dưỡng trẻ sơ sinh, về sữa non và dự trữ dinh dưỡng nơi cuống rốn, khoa học về giấc ngủ của trẻ và những điều kiện an toàn ăn và an toàn ngủ của bé sơ sinh là những thông tin thiết yếu mà cha mẹ thực sự cần trang bị đầy đủ để đón chào bé thơ.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Thời khắc Bé chào đời cũng là lúc mà nhịp sinh hoạt của gia đình xáo trộn, và lúc đó sự phục hồi sức khoẻ của ngƣời mẹ phụ thuộc rất nhiều vào cách tổ chức nếp sống, sinh hoạt cũng như bố trí việc ăn ngủ dành cho bé sơ sinh.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bộ sách này là sự giải đáp cho những câu hỏi mà chúng tôi nhận được từ các bậc cha mẹ trẻ trong 5 năm qua về những vấn đề của các em bé từ sơ sinh cho đến chập chững biết đi: con ăn ra sao, con ngủ bao nhiêu là đủ… Và bí quyết là gì để nuôi con khoẻ mạnh và khoa học nhất, để tăng thêm thành viên mới là tăng thêm niềm vui chứ không phải gánh nặng vất vả cho mọi ngƣời?\n" +
                "\n" +
                "\n" +
                "\n" +
                "Làm thế nào để con ngủ đủ theo nhu cầu lứa tuổi và có nền tảng tốt nhất cho sự phát triển thể chất và trí tuệ? Làm thế nào để bé thơ có thể tự học cách đưa mình vào giấc ngủ mà không cần phải chờ đến ti mẹ, không phải phụ thuộc vào cánh tay bế hay từ sự đung đưa của những chiếc võng và những lời ru đến lạc giọng của người lớn?\n" +
                "\n" +
                "\n" +
                "\n" +
                "Song hành cùng bạn, qua bộ sách này, chúng tôi cố gắng cung cấp đầy đủ và chi tiết hơn theo những mốc phát triển của bé cũng như lý giải những khúc mắc và giảm bớt những lo lắng, hoang mang không cần thiết cho những bậc ông bà - cha mẹ khi nuôi con trong những năm đầu đời. Chúng tôi mong rằng bộ sách này sẽ mang đến cho gia đình bạn sự an tâm, thƣ giãn và phong thái bình tĩnh khi nuôi và dạy bé thơ. Bởi, suy cho cùng, nuôi con không bao ");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/de14d4d318f7c66a67ae28b279da36ed");
        variant_1.setWeight(90.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(750));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Đắc Nhân Tâm( khổ lớn)");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/f6bea1f2e1aa442a0afd43f20f3f5e4c");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/b5bed61f6abdd57280fea3e658134b72");
        productDTO.setDescription("Công ty phát hành        First News - Trí Việt\n" +
                "\n" +
                "Tác giả        Dale Carnegie\n" +
                "\n" +
                "Ngày xuất bản         2022\n" +
                "\n" +
                "Kích thước        14.5 x 20.5 cm\n" +
                "\n" +
                "Nhà xuất bản        Nhà Xuất Bản Tổng hợp TP.HCM\n" +
                "\n" +
                "Loại bìa        Bìa mềm\n" +
                "\n" +
                "Số trang        320\n" +
                "\n" +
                "SKU        2436661537384\n" +
                "\n" +
                "Tại sao Đắc Nhân Tâm luôn trong Top sách bán chạy nhất thế giới?\n" +
                "\n" +
                "Bởi vì đó là cuốn sách mọi người đều nên đọc.\n" +
                "\n" +
                "Hiện nay có một sự hiểu nhầm đã xảy ra. Tuy Đắc Nhân Tâm là tựa sách hầu hết mọi người đều biết đến, vì những danh tiếng và mức độ phổ biến, nhưng một số người lại “ngại” đọc. Lý do vì họ tưởng đây là cuốn sách “dạy làm người” nên có tâm lý e ngại. Có lẽ là do khi giới thiệu về cuốn sách, người ta luôn gắn với miêu tả đây là “nghệ thuật đối nhân xử thế”, “nghệ thuật thu phục lòng người”… Những cụm từ này đã không còn hợp với hiện nay nữa, gây cảm giác xa lạ và không thực tế.\n" +
                "\n" +
                "Nhưng đâu phải thế, Đắc Nhân Tâm là cuốn sách không hề lỗi thời! \n" +
                "\n" +
                "Những vấn đề được chỉ ra trong đó đều là căn bản ứng xử giữa người với người. Nếu diễn giải theo từ ngữ bây giờ, có thể gọi đây là “giáo trình” giúp hiểu mình – hiểu người để thành công trong giao tiếp. Có ai sống mà không cần giao tiếp? Có bao nhiêu người ngày ngày mệt mỏi, khổ sở vì gặp phải các vấn đề trong giao tiếp? Vì thế, Đắc Nhân Tâm chính là cuốn sách dành cho mọi người. Con cái nên đọc – cha mẹ càng nên đọc, nhân viên nên đọc – sếp càng nên đọc, người quen nhau nên đọc – người lạ nhau càng nên đọc…. Và đó mới chính thật là lý do Đắc Nhân Tâm luôn lọt vào Top sách bán chạy nhất thế giới, dù đã ra đời cách đây gần 80 năm.\n" +
                "\n" +
                "Có lẽ sẽ có người vừa đọc vừa nghĩ, mấy điều trong sách này đơn giản mà, ai chẳng biết? Đúng thế, vì toàn bộ đều là những quy tắc, những cách cư xử căn bản giữa chúng ta với nhau thôi. Kiểu như “Không chỉ trích, oán trách hay than phiền”, “Thành thật khen ngợi và biết ơn người khác”, “Thật lòng làm cho người khác thấy rằng họ quan trọng”… Những điều này đúng thật là ai cũng biết, nhưng bạn có chắc bạn nhớ được và làm được những điều đơn giản đó? Vì vậy, cuốn sách mới ra đời, để giúp bạn thực hành.\n" +
                "\n" +
                "Nhưng có lẽ đa số người đọc sẽ thành thật gật gù đồng ý với từng trang sách. Ồ nếu như bình tâm suy xét lại mọi việc, thì trong bất cứ trường hợp nào mình cũng có thể cư xử đúng mực, không làm người khác tổn thương, giúp bầu không khí luôn thoải mái, và thế là cả hai bên đều vui vẻ, công việc cũng vì thế mà suôn sẻ, thành công hơn. Vậy chứ mà cũng không dễ, bởi “cái tôi” của mỗi người thường chiến thắng tâm trí trong đa số trường hợp. Để thỏa mãn nó, chúng ta hay mắc sai lầm không đáng. Đó cũng chính là lý do Đắc Nhân Tâm có mặt, để nhắc nhở và từng chút giúp ta uốn nắn chính “cái tôi” của mình.\n" +
                "\n" +
                "Đắc Nhân Tâm - Dale Carnegie\n" +
                "\n" +
                "Cuốn Sách Hay Nhất Của Mọi Thời Đại Đưa Bạn Đến Thành Công\n" +
                "\n" +
                "\n" +
                "\n" +
                "Đắc nhân tâm – How to win friends and Influence People của Dale Carnegie là quyển sách nổi tiếng nhất, bán chạy nhất và có tầm ảnh hưởng nhất của mọi thời đại. Tác phẩm đã được chuyển ngữ sang hầu hết các thứ tiếng trên thế giới và có mặt ở hàng trăm quốc gia. Đây là quyển sách duy nhất về thể loại self-help liên tục đứng đầu danh mục sách bán chạy nhất (best-selling Books) do báo The New York Times bình chọn suốt 10 năm liền. Riêng bản tiếng Anh của sách đã bán được hơn 15 triệu bản trên thế giới. Tác phẩm có sức lan toả vô cùng rộng lớn – dù bạn đi đến bất cứ đâu, bất kỳ quốc gia nào cũng đều có thể nhìn thấy. Tác phẩm được đánh giá là quyển sách đầu tiên và hay nhất, có ảnh hưởng làm thay đổi cuộc đời của hàng triệu người trên thế giới.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Không còn nữa khái niệm giới hạn Đắc Nhân Tâm là nghệ thuật thu phục lòng người, là làm cho tất cả mọi người yêu mến mình. Đắc nhân tâm và cái Tài trong mỗi người chúng ta. Đắc Nhân Tâm trong ý nghĩa đó cần được thụ đắc bằng sự hiểu rõ bản thân, thành thật với chính mình, hiểu biết và quan tâm đến những người xung quanh để nhìn ra và khơi gợi những tiềm năng ẩn khuất nơi họ, giúp họ phát triển lên một tầm cao mới. Đây chính là nghệ thuật cao nhất về con người và chính là ý nghĩa sâu sắc nhất đúc kết từ những nguyên tắc vàng của Dale Carnegie.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Quyển sách Đắc nhân tâm “Đầu tiên và hay nhất mọi thời đại về nghệ thuật giao tiếp và ứng xử”, quyển sách đã từng mang đến thành công và hạnh phúc cho hàng triệu người trên khắp thế giới\n" +
                "\n" +
                "#dacnhantam #vadatabooks #book #sachkinang");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/b5bed61f6abdd57280fea3e658134b72");
        variant_1.setWeight(45.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(650));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Giải Thích Ngữ Pháp Tiếng Anh ( Bài Tập & Đáp Án )");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/cfb91110741a5367728fa14fe4e3b055");
        images.add("https://cf.shopee.vn/file/131f37936cd4b0946074ffeb3368b2b4");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/d290cf92ddc6c4c11bb0e0641e7f90d6");
        productDTO.setDescription("Công ty phát hành        Zenbooks\n" +
                "\n" +
                "Tác giả: Nhiều tác giả\n" +
                "\n" +
                "Ngày xuất bản   2022\n" +
                "\n" +
                "Kích thước        17 x 24 cm\n" +
                "\n" +
                "Loại bìa        Bìa mềm\n" +
                "\n" +
                "Số trang        560\n" +
                "\n" +
                "SKU        7924944303061\n" +
                "\n" +
                "Nhà xuất bản        Nhà Xuất Bản Đà Nẵng\n" +
                "\n" +
                "\n" +
                "\n" +
                "GIẢI THÍCH NGỮ PHÁP TIẾNG ANH, tác giả Mai Lan Hương - Hà Thanh Uyên, là cuốn sách ngữ pháp đã được phát hành và tái bản rất nhiều lần trong suốt những năm vừa qua.\n" +
                "\n" +
                "Trong lần tái bản GIẢI THÍCH NGỮ PHÁP TIẾNG ANH năm 2020 này, nhằm nâng cao chất lượng sách và giúp người học trau dồi, củng cố và nâng cao kiến thức ngữ pháp tiếng Anh, chúng tôi chỉnh sửa, bổ sung và cập nhật một số kiến thức ngữ pháp mới.\n" +
                "\n" +
                "Ấn bản mới GIẢI THÍCH NGỮ PHÁP TIẾNG ANH gồm 5 chương, hệ thống hóa toàn diện kiến thức ngữ pháp tiếng Anh từ cơ bản đến nâng cao:\n" +
                "\n" +
                "Chương 1: Từ Loại (Parts of Speech)\n" +
                "\n" +
                "Chương 2: Thì và Sự Phối Hợp Thì (Tenses and Sequence of Tenses)\n" +
                "\n" +
                "Chương 3: Mệnh Đề (Clauses)\n" +
                "\n" +
                "Chương 4: Câu (Sentences)\n" +
                "\n" +
                "Chương 5: Từ Vựng Học (Word Study)\n" +
                "\n" +
                "Các chủ điểm ngữ pháp trong từng chương được trình bày rõ ràng, chi tiết, giải thích cặn kẽ các cách dùng và quy luật mà người học cần nắm vững. Sau mỗi chủ điểm ngữ pháp là phần bài tập đa dạng nhằm giúp người học củng cố phần lý thuyết.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/d290cf92ddc6c4c11bb0e0641e7f90d6");
        variant_1.setWeight(45.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(650));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Tuổi Trẻ Đáng Giá Bao Nhiêu");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/9605fd24ddec4d8c566cc7e9247b0829");
        images.add("https://cf.shopee.vn/file/68fab4cd1345cc34f45bdf8ddb1945eb");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/e558991a7192bb0ac88b798390c631ac");
        productDTO.setDescription("Công ty phát hành        Nhã Nam\n" +
                "Tác giả        Rosie Nguyễn\n" +
                "Ngày xuất bản        10-2020\n" +
                "Kích thước        14 x 20.5 cm\n" +
                "Nhà xuất bản        Nhà Xuất Bản Hội Nhà Văn\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        285\n" +
                "SKU        2431864615280\n" +
                "Tuổi Trẻ Đáng Giá Bao Nhiêu\n" +
                "\n" +
                "\"Bạn hối tiếc vì không nắm bắt lấy một cơ hội nào đó, chẳng có ai phải mất ngủ.\n" +
                "\n" +
                "Bạn trải qua những ngày tháng nhạt nhẽo với công việc bạn căm ghét, người ta chẳng hề bận lòng.\n" +
                "\n" +
                "Bạn có chết mòn nơi xó tường với những ước mơ dang dở, đó không phải là việc của họ.\n" +
                "\n" +
                "Suy cho cùng, quyết định là ở bạn. Muốn có điều gì hay không là tùy bạn.\n" +
                "\n" +
                "Nên hãy làm những điều bạn thích. Hãy đi theo tiếng nói trái tim. Hãy sống theo cách bạn cho là mình nên sống.\n" +
                "\n" +
                "Vì sau tất cả, chẳng ai quan tâm.\"\n" +
                "\n" +
                "Nhận định\n" +
                "\n" +
                "\"Tôi đã đọc quyển sách này một cách thích thú. Có nhiều kiến thức và kinh nghiệm hữu ích, những điều mới mẻ ngay cả với người gần trung niên như tôi.\n" +
                "\n" +
                "Tuổi trẻ đáng giá bao nhiêu? được tác giả chia làm 3 phần: HỌC, LÀM, ĐI.\n" +
                "\n" +
                "Nhưng tôi thấy cuốn sách còn thể hiện một phần thứ tư nữa, đó là ĐỌC.\n" +
                "\n" +
                "Hãy đọc sách, nếu bạn đọc sách một cách bền bỉ, sẽ đến lúc bạn bị thôi thúc không ngừng bởi ý muốn viết nên cuốn sách của riêng mình.\n" +
                "\n" +
                "Nếu tôi còn ở tuổi đôi mươi, hẳn là tôi sẽ đọc Tuổi trẻ đáng giá bao nhiêu? nhiều hơn một lần.\"\n" +
                "\n" +
                "(Đặng Nguyễn Đông Vy, tác giả, nhà báo)");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/e558991a7192bb0ac88b798390c631ac");
        variant_1.setWeight(25.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(350));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Combo Lẻ Tùy Chọn: Tô Bình Yên Vẽ Hạnh Phúc + Từ Điển Tiếng “Em”");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/d3a4c0b038ba267dbf030342f89817b0");
        images.add("https://cf.shopee.vn/file/d5da57bccdd0487bdf3bda3670c9b029");
        images.add("https://cf.shopee.vn/file/2595b06a1cb0d6ffdb92edc6d9cd73df");
        images.add("https://cf.shopee.vn/file/b25b945507431a4900d9bb1e13038f01");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-3ti0mb765ukv14");
        productDTO.setDescription("1. Tô Bình Yên Vẽ Hạnh Phúc\n" +
                "\n" +
                "Công ty phát hành        Skybooks\n" +
                "\n" +
                "Tác giả: Kulzsc\n" +
                "\n" +
                "Ngày xuất bản        2021\n" +
                "\n" +
                "Kích thước        24 x 19 cm\n" +
                "\n" +
                "Loại bìa        Bìa mềm\n" +
                "\n" +
                "Số trang        96\n" +
                "\n" +
                "SKU        4373072270180\n" +
                "\n" +
                "Nhà xuất bản        Nhà Xuất Bản Phụ Nữ Việt Nam\n" +
                "\n" +
                "\n" +
                "\n" +
                "TÔ BÌNH YÊN VẼ HẠNH PHÚC – KULZSC\n" +
                "\n" +
                "Sau thành công của cuốn sách đầu tay “Phải lòng với cô đơn” chàng họa sĩ nổi tiếng và tài năng Kulzsc đã trở lại với một cuốn sách vô cùng đặc biệt mang tên:\n" +
                "\n" +
                "\"Tô bình yên - vẽ hạnh phúc” – sắc nét phong cách cá nhân với một chút “thơ thẩn, rất hiền”.\n" +
                "\n" +
                "Không giống với những cuốn sách chỉ để đọc, “Tô bình yên – vẽ hạnh phúc” là một cuốn sách mà độc giả vừa tìm được “Hạnh phúc to to từ những điều nho nhỏ” vừa được thực hành ngay lập tức. Một sự kết hợp mới lạ đầy thú vị giữa thể loại sách tản văn và sách tô màu.\n" +
                "\n" +
                "Lật mở cuốn sách này, bạn sẽ thấy vô vàn những điều nhỏ bé dễ thương lan tòa nguồn năng lượng tích cực. Và kèm theo một list những điều tuyệt vời khiến bạn không thể bỏ lỡ:\n" +
                "\n" +
                "- Tận tình chỉ dẫn: 8 hướng dẫn tô màu đầy hứng thú từ Kulzsc\n" +
                "\n" +
                "- Tranh vẽ đơn giản, gần gũi. Nét vẽ đáng yêu, dễ thương\n" +
                "\n" +
                "- Chủ đề xoay quanh những điều bình yên trong cuộc sống: Đọc sách, đi dạo, dọn dẹp nhà cửa, trồng cây, ăn cơm mẹ nấu, nghe một bản nhạc hay, và nghĩ về nụ cười của một ai đó…\n" +
                "\n" +
                "- Mỗi bức tranh lại là những lời thủ thỉ, tâm tình của tác giả. Là những điều nhắn gửi nho nhỏ mong bạn bớt đi những xao động:\n" +
                "\n" +
                "“Em chọn hạnh phúc\n" +
                "\n" +
                "Em chọn bình yên\n" +
                "\n" +
                "Em chọn bên cạnh\n" +
                "\n" +
                "Những điều an yên”\n" +
                "\n" +
                "Hay đơn giản là những giãi bày ngắn gọn, thay nỗi lòng của một ai đó: \"Tớ biết cậu một mình vẫn ổn, nhưng có những chuyện, có ai đó cùng làm, vẫn hơn.\"\n" +
                "\n" +
                "Thông qua những hình vẽ đang đợi được lấp đầy bằng muôn vàn sắc màu ấy, Kulzsc sẽ giúp bạn - những người lớn cô đơn, những ai đang cần bổ sung vitamin\n" +
                "\n" +
                "hạnh phúc “truy tìm” những niềm vui bị bỏ quên hay tuyệt chiêu để đối phó với stress.\n" +
                "\n" +
                "\n" +
                "\n" +
                "2. Từ Điển Tiếng “Em”\n" +
                "\n" +
                "Công ty phát hành        Skybooks\n" +
                "\n" +
                "Tác giả: Khotudien\n" +
                "\n" +
                "Ngày xuất bản        10-2020\n" +
                "\n" +
                "Kích thước        12 x 18 cm\n" +
                "\n" +
                "Dịch Giả        Quote\n" +
                "\n" +
                "Loại bìa        Bìa mềm\n" +
                "\n" +
                "Số trang        280\n" +
                "\n" +
                "SKU        9507721326293\n" +
                "\n" +
                "Nhà xuất bản        Nhà Xuất Bản Phụ Nữ Việt Nam\n" +
                "\n" +
                "\n" +
                "\n" +
                "TỪ ĐIỂN TIẾNG “EM” – Định nghĩa về thế giới mới!\n" +
                "\n" +
                "Bạn sẽ bất ngờ, khi cầm cuốn “từ điển” xinh xinh này trên tay.\n" +
                "\n" +
                "Và sẽ còn ngạc nhiên hơn nữa, khi bắt đầu đọc từng trang sách…\n" +
                "\n" +
                "Dĩ nhiên là vì “Từ điển tiếng “Em” không phải là một cuốn từ điển thông thường rồi!\n" +
                "\n" +
                "Nói đến “từ điển”, xưa nay chúng ta đều nghĩ về một bộ sách đồ sộ, giải thích ý nghĩa, cách dùng, dịch, cách phát âm, và thường kèm theo các ví dụ về cách sử dụng từ đó.\n" +
                "\n" +
                "Tuy nhiên, với cuốn sách “Từ điển tiếng “em”, các bạn sẽ hết sức bất ngờ với những định nghĩa mới, bắt trend, sáng tạo, thông minh và vô cùng hài hước.\n" +
                "\n" +
                "Tiếng “em” [danh từ] ở đây là tiếng lòng của những người yêu sự thật, ghét sự giả dối\n" +
                "\n" +
                "Cô đơn [ tính từ ] không phải là không có ai bên cạnh, mà là người mình muốn ở cạnh lại không hề ở bên\n" +
                "\n" +
                "Sống lỗi [ động từ ] là cách sống của mấy bạn có người yêu cái là bỏ bê bạn bè liền hà\n" +
                "\n" +
                "Nhưng đây không chỉ là cuốn sách chỉ biết nói dăm ba câu chuyện về tình yêu.\n" +
                "\n" +
                "Còn nhiều hơn thế!\n" +
                "\n" +
                "Là những câu cửa miệng của giới trẻ thời nay; là hoạt động tưởng vô bổ nhưng cần thiết cho sự sống: ăn, ngủ, tắm, gội cũng được định nghĩa hết sức dí dỏ Và cũng không thiếu những “tật xấu, thói hư” nghĩ đến đã thấy “tức cái lồng ngực”\n" +
                "\n" +
                "Và bạn yên tâm, “tập đoàn” Kho Từ Điển điều hành bởi Thịt Kho – Hiệp Thị - 2 chủ xị cho ra đời cuốn sách nhỏ bé xíu xiu nhưng mới mẻ và mặn mà vô cùng này sẽ khiến bạn thoát mác “người tối cổ” cười cả ngày không chán, nhìn bạn bè quanh mình bằng ánh mắt dễ thương, tận hưởng cuộc đời với những định nghĩa hoàn toàn!!!\n" +
                "\n" +
                "\n" +
                "\n" +
                "3. Ở Tiệm Bánh Ngày Mai\n" +
                "\n" +
                "Công ty phát hành SkyComics \n" +
                "\n" +
                "Tác giả: Múc\n" +
                "\n" +
                "Ngày xuất bản        2022\n" +
                "\n" +
                "Kích thước 24 cm x 19 cm\n" +
                "\n" +
                "Loại bìa Bìa mềm\n" +
                "\n" +
                "Số trang 104\n" +
                "\n" +
                "Nhà xuất bản Nhà Xuất Bản Dân Trí\n" +
                "\n" +
                "Ở tiệm bánh Ngày Mai - Sắc màu nào tô điểm ngày mai?\n" +
                "\n" +
                "“Tại sao lại là tiệm bánh “Ngày Mai” thế?”\n" +
                "\n" +
                "“Vì nhìn xem, hôm nay mình đã chuẩn bị gì cho nó đâu nào!”\n" +
                "\n" +
                "Chuyển mình từ series truyện tranh đời thường gây nhung nhớ - “Chuyện vặt của Múc”, cô nàng Múc dễ thương hài hước đã trở lại với cuốn sách hoàn toàn mới, không phải về những mẩu chuyện nhí nhố hàng ngày chúng ta từng quen thuộc, mà là về một tiệm bánh đặc biệt mang tên “Ngày Mai”. Tiệm bánh này không hề có câu chuyện nào để kể, cũng chỉ có duy nhất hai màu đen trắng, chính là bởi nó vẫn đang chờ đợi bạn tô điểm lên bằng những gam màu rực rỡ!\n" +
                "\n" +
                "“Ở tiệm bánh Ngày Mai” là một cuốn sách tô màu, với những trang sách vẽ về “Ngày Mai” - tiệm bánh xinh xắn của bạn và Múc: nếu như Múc dựng xây cửa tiệm bằng nét bút thì bạn là người tạo nên màu sắc riêng của “Ngày Mai”. Tiệm bánh sẽ mở cửa vào buổi sớm hay ban trưa? Giữa sắc vàng rực rỡ của nắng hạ hay màu xanh êm ả của trời thu? Nào là góc bếp gọn gàng ngay ngắn, nào là khay bánh nóng hổi mới ra lò, nào những chiếc ghế xinh xắn bên khung cửa sổ…, tiệm bánh “Ngày Mai” trong mắt bạn sẽ mang những sắc màu gì?");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/sg-11134201-22120-3ti0mb765ukv14");
        variant_1.setWeight(45.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(650));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Think & Grow Rich Nghĩ Giàu Và Làm Giàu");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/1ad91c2a2cfa85c2b3059b1f22e913bb");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/39f1386415bcdadb7d0f6c1f400a644d");
        productDTO.setDescription("Công ty phát hành        First News - Trí Việt\n" +
                "Tác giả        Napoleon Hill\n" +
                "Ngày xuất bản        11-2017\n" +
                "Loại bìa        Bìa mềm\n" +
                "Kích thước        16 x 24 cm\n" +
                "Nhà xuất bản        Nhà Xuất Bản Tổng hợp TP.HCM\n" +
                "Dịch Giả        Việt Khương\n" +
                "Số trang        372\n" +
                "SKU        2005311986454\n" +
                "Think & Grow Rich - Nghĩ Giàu Và Làm Giàu là một trong những cuốn sách bán chạy nhất mọi thời đại. Đã hơn 60 triệu bản được phát hành với gần trăm ngôn ngữ trên toàn thế giới và được công nhận là cuốn sách tạo ra nhiều triệu phú hơn, một cuốn sách truyền cảm hứng thành công nhiều hơn bất cứ cuốn sách kinh doanh nào trong lịch sử.\n" +
                "\n" +
                "Tác phẩm này đã giúp tác giả của nó, Napoleon Hill, được tôn vinh bằng danh hiệu “người tạo ra những nhà triệu phú”. Đây cũng là cuốn sách hiếm hoi được đứng trong top của rất nhiều bình chọn theo nhiều tiêu chí khác nhau - bình chọn của độc giả, của giới chuyên môn, của báo chí.\n" +
                "\n" +
                "Lý do để Think and Grow Rich - Nghĩ giàu và Làm giàu có được vinh quang này thật hiển nhiên và dễ hiểu: Bằng việc đọc và áp dụng những phương pháp đơn giản, cô đọng này vào đời sống của mỗi cá nhân mà đã có hàng ngàn người trên thế giới trở thành triệu phú và thành công bền vững. Điều thú vị nhất là các bí quyết này có thể được hiểu và áp dụng bởi bất kỳ một người bình thường nào, hoạt động trong bất cứ lĩnh vực nào. Qua hơn 70 năm tồn tại, những đúc kết về thành công của Napoleon Hill đến nay vẫn không hề bị lỗi thời, ngược lại, thời gian chính là minh chứng sống động cho tính đúng đắn của những bí quyết mà ông chia sẻ.\n" +
                "\n" +
                "Sinh ra trong một gia đình nghèo vùng Tây Virginia, con đường thành công của Napoleon Hill cũng trải qua nhiều thăng trầm. Khởi đầu bằng chân cộng tác viên cho một tờ báo địa phương lúc 15 tuổi, đến năm 19 tuổi Hill trở thành nhà quản lý mỏ than trẻ tuổi nhất, sau đó bỏ ngang để theo đuổi ngành luật. Napoleon Hill còn kinh qua nhiều nghề như kinh doanh gỗ, xe hơi, viết báo về kinh doanh… Đó là những công việc ông từng nếm trải trước khi 25 tuổi! Song khác với những người thành đạt khác, ông cẩn thận phân tích từng sự kiện trọng đại trong đời mình, rút ra những bài học, rồi tiếp tục rút gọn chúng thành các nguyên tắc căn bản, tổ chức các nguyên tắc ấy thành triết lý sống và rèn luyện... Cơ hội đặc biệt đã đến với Hill trong một lần phỏng vấn để viết về chân dung Andrew Carnegie - ông “vua thép” huyền thoại của Mỹ đã đi lên từ hai bàn tay trắng. Từ lần phỏng vấn đó, Napoleon Hill có dịp tiếp cận với những con người thành đạt và có quyền lực nhất tại Mỹ để tìm hiểu và học hỏi những bí quyết thành công của họ, trong thế so sánh và kiểm chứng với những công thức thành công của Andrew Carnegie. Ông muốn qua đó có thể đúc kết và viết nên một cuốn sách ghi lại những bí quyết giúp biến các cá nhân bình thường thành những người thành công trong xã hội.\n" +
                "#vadata  #sáchtiếnghàn  #hàntổnghợp  #sáchngoạingữ  #sáchminna  #sáchthật   #sách  #sáchgiáotrình  #minnanonihongo  #sáchtiếngtrung  #giáotrìnhhánngữ  #sáchvănhọc  #sáchkinhdoanh  #sáchmẹbé  #sáchtiếngnhật  #sáchgiảmgiá  #Sáchtựhọc  #Sáchkỹnăng");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/39f1386415bcdadb7d0f6c1f400a644d");
        variant_1.setWeight(23.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(300));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Người Nam Châm Bí Mật Của Luật Hấp Dẫn (Tái Bản)");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/0e11983562b5f8f04574fb5ce9d95e2c");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/0e11983562b5f8f04574fb5ce9d95e2c");
        productDTO.setDescription("Công ty phát hành        Thái Hà\n" +
                "Tác giả        Jack Canfield, D.D Watkins\n" +
                "Ngày xuất bản        01-2018\n" +
                "Kích thước        13 x 20.5 cm\n" +
                "Nhà xuất bản        Nhà Xuất Bản Lao Động Xã Hội\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        216\n" +
                "SKU        5538890778952 GIỚI THIỆU SÁCH\n" +
                "Người Nam Châm - Bí Mật Của Luật Hấp Dẫn\n" +
                "\n" +
                "Cuốn sách viết về cách hoạt động của Luật hấp dẫn trong cuộc sống của bạn, từ đó bạn có thể hiểu nhiều hơn về bản thân mình - thực ra bạn là ai và tại sao bạn lại ở đây. Cuốn sách này chính là chìa khóa của bạn. Nó có thể mở cánh cửa tới tương lai như bạn mong ước và đưa bạn tới con đường có nhiều niềm vui, sung túc và giàu có hơn. Khi đọc cuốn sách này, bạn sẽ thấy mình được truyền cảm hứng vì bạn nhận ra rằng bạn có thể tạo ra cuộc sống mà bạn khao khát, và bạn sẽ được trao quyền khi sử dụng những công cụ, những chiến lược và những khái niệm cơ bản được chuyển tải trong những trang sách này.\n" +
                "#vadata #nguoinamcham");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/0e11983562b5f8f04574fb5ce9d95e2c");
        variant_1.setWeight(25.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(320));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Trọn Bộ Giáo Trình Tiếng Hàn Tổng Hợp Sơ Cấp 1 (SGK + SBT)");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/f2913e2ca341f4b2078f3505921bf5ee");
        images.add("https://cf.shopee.vn/file/54af4c63647dda86ff6169dd233523d8");
        images.add("https://cf.shopee.vn/file/2ac03f22692a78a69c59127f575e819a");
        images.add("https://cf.shopee.vn/file/9c386090ee709601d954175f6d31483e");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/337bf81a572e11cd6cd5eaefa08b2298");
        productDTO.setDescription("NĂM XUẤT BẢN: 2021\n" +
                "\n" +
                "NHÀ XUẤT BẢN: Đại học Quốc Gia Hà Nội\n" +
                "\n" +
                "SỐ TRANG: 378\n" +
                "\n" +
                "KÍCH THƯỚC        :19×27 cm\n" +
                "\n" +
                "Công ty phát hành: Mcbooks\n" +
                "\n" +
                "Tác giả: Nhiều tác giả\n" +
                "\n" +
                "Trong những năm gần đây, nhu cầu học tiếng Hàn và tìm hiểu về văn hóa Hàn Quốc đang có xu hướng tăng lên. Người học tiếng Hàn rất mong muốn có được một bộ giáo trình chuẩn, được biên soạn một cách tỉ mỉ và hữu ích nhất.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Giáo trình tiếng Hàn tổng hợp lần đầu tiên được trao bản quyền chính thức tại Việt Nam\n" +
                "\n" +
                "Hiểu được điều đó, ngày 4-7, Đại diện chính phủ Hàn Quốc, ngài Woo Hyeong Min – Trưởng đại diện Văn phòng Quỹ giao lưu Quốc tế HQ tại Việt Nam cùng GS.TS.NGƯT Mai Ngọc Chừ – Chủ tịch Hội Nghiên cứu khoa học về Hàn Quốc của Việt Nam (KRAV) đã trao quyết định cho phép Công ty Cổ phần sách MCBooks của Việt Nam được xuất bản cuốn sách này tại thị trường Việt Nam. Và đây chính là bộ giáo trình tiếng Hàn tổng hợp đầu tiên có bản quyền chính thức có mặt tại Việt Nam.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Cũng theo ông Woo Hyeong Min, Thực tế đây là bộ giáo trình chính thống mà tất cả người học tiếng Hàn tại Việt Nam cần sử dụng. Tuy nhiên nhiều năm qua bộ sách đến tay người dùng đều là sách lậu nên chưa đạt được kết quả mà chính phủ Hàn muốn mang tới Việt Nam. Do đó Chính phủ Hàn Quốc đã quyết  định tìm 1 đơn vị xuất bản uy tín của Việt Nam để giúp họ mang đến cho người học những cuốn sách tiếng Hàn chất lượng tốt với giá cả hợp lý.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bộ giáo trình được Hội nghiên cứu khoa học Hàn Quốc của Việt Nam biên soạn và hiệu đính.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Bộ giáo trình sẽ giúp nâng cao hiệu quả trong việc sử dụng ngôn ngữ Hàn sau tiếng mẹ đẻ và tiếng Anh, là nền tảng giúp người lao động Việt Nam nhận được những vị trí làm việc cao trong doanh nghiệp Hàn Quốc. Đây cũng là cách để thúc đẩy quan hệ hợp tác về văn hoá và giáo dục giữa hai nước trong thời gian tới.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Giới thiệu về cuốn Giáo trình tiếng Hàn tổng hợp dành cho người Việt Nam – Sơ cấp 1\n" +
                "\n" +
                "Giáo trình tiếng Hàn tổng hợp dành cho người Việt Nam – Sơ cấp 1 được biên soạn dành cho người  Việt Nam muốn học tiếng Hàn ở trình độ sơ cấp. Sách được biên soạn nhằm hướng tới việc sử dụng được trong các tiết học tiếng Hàn tại giảng đường đại học nên mỗi bài học được chia thành nhiều phần như: Lý thuyết cơ bản gồm từ vựng và cấu trúc ngữ pháp; phần luyện tập theo 4 kỹ năng giao tiếp như: Nghe – Nói – Đọc – Viết, phần luyện phát âm, phần tìm hiểu văn hóa…\n" +
                "\n" +
                "\n" +
                "\n" +
                "Các chủ đề trong sách được sắp xếp một cách có hệ thống và liên quan chặt chẽ với nhau. Giáo trình tiếng Hàn tổng hợp được biên soạn kèm theo một cuốn sách bài tập hỗ trợ giúp người học ôn tập kỹ hơn và có thể luyện tập nâng cao trong những trường hợp cần thiết.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Nội dung trong Giáo trình tiếng Hàn tổng hợp – Sơ cấp 1\n" +
                "\n" +
                "2 cuốn sách trong bộ Sơ cấp được xây dựng với 30 bài khóa và bảng chữ cái. Trong đó quyển 1 gồm phần bảng chữ cái và 15 bài đầu. Học hết quyển 1, bạn sẽ học đến quyển sơ cấp 2 với 15 bài còn lại.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Cấu trúc tổng thể của giáo trình được xây dựng theo các chủ đề; mỗi chủ đề được liên kết thống nhất với một hệ thống các từ vựng và cấu trúc ngữ pháp cơ bản, bài tập ứng dụng, kỹ năng và tìm hiểu văn hóa.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Trong mỗi bài học, nội dung học được chia thành các phần như: luyện từ vựng, luyện ngữ pháp cơ bản, luyện tập kỹ năng nghe – nói – đọc – viết, luyện phát âm, tìm hiểu văn hóa của đất nước Hàn Quốc xinh đẹp. Và kết thúc mỗi bài đều có thêm bảng từ mới xuất hiện trong bài học.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Phần cuối của sách, các tác giả đưa ra hệ thống từ mới được sắp xếp theo thứ tự của bảng chữ cái. Đây là những từ không xuất hiện trong phần ngữ pháp, từ vựng cơ bản; nhưng lại xuất hiện nhiều ở phần luyện tập của từng bài.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Học Giáo trình tiếng Hàn tổng hợp cùng hệ thống app MCBooks tiện dụng mọi lúc mọi nơi\n" +
                "\n" +
                "Các cuốn trong bộ Giáo trình tiếng Hàn tổng hợp đều được hỗ trợ kèm theo app MCBooks hiện đại. Tất cả những tài liệu cần thiết trong quá trình học bài khóa, bài nghe đều được tích hợp trên app MCBooks. Người học chỉ cần tải app, quét mã là có thể học tiếng Hàn, luyện tập phản xạ nghe – nói hay thực hành các bài tập trong sách giáo trình mọi lúc mọi nơi một cách dễ dàng, tiện dụng. Nếu người học khai thác triệt để tài liệu luyện nghe và bài tập luyện tập trong app, kết hợp song song với học giáo trình trong sách in thì chắc chắn việc học tập sẽ đạt hiệu quả cao hơn rất nhiều, nhanh chóng cải thiện khả năng nói tiếng Hàn và ngày càng yêu thích thứ ngôn ngữ tuyệt vời này.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Có thể nói, Giáo trình tiếng Hàn tổng hợp Sơ cấp 1 nằm trong bộ giáo trình được xây dựng hết sức công phu trên cơ sở những nghiên cứu sâu sắc môi trường học tiếng Hàn tại Việt Nam, cũng như nghiên cứu chiến lược học tiếng Hàn của người Việt. Giáo trình được biên soạn dựa trên những kinh nghiệm thực tế và nền tảng lý luận sư phạm được tích lũy từ những chuyên gia dạy tiếng Hàn hàng đầu. Vì vậy,  bộ Giáo trình tiếng Hàn tổng hợp phiên bản mới – Sơ cấp 1 là sự lựa chọn hoàn hảo cho những người yêu thích và muốn chinh phục thứ ngôn ngữ này");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/337bf81a572e11cd6cd5eaefa08b2298");
        variant_1.setWeight(420.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1000));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802773_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Cây Cam Ngọt Của Tôi");
        productDTO.setShopId(802773);
        productDTO.setTradeMarkId("1703772512385661");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/4dd18d2d7f32298b6886ac3557cc1bc9");
        images.add("https://cf.shopee.vn/file/483589092a58edfc94bcd3e824e00f2d");
        images.add("https://cf.shopee.vn/file/ff17278247de9ca6fb17b29675e1b877");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/414cdecac5e1b3dd08f34827bdbca1c7");
        productDTO.setDescription("Nhà Phát Hành  Nhã Nam\n" +
                "\n" +
                "Tác giả: José Mauro de Vasconcelos\n" +
                "\n" +
                "Dịch giả: Nguyễn Bích Lan , Tô Yến Ly\n" +
                "\n" +
                "Nhà xuất bản: Hội nhà văn\n" +
                "\n" +
                "Số trang: 244\n" +
                "\n" +
                "Kích thước: 14 x 20.5 cm\n" +
                "\n" +
                "Ngày phát hành: 2022\n" +
                "\n" +
                "Hơn nửa thế kỷ trôi qua kể từ lần đầu tiên cậu bé Zezé bước vào văn chương và ở lại đó cùng với cây cam ngọt ngào lẫn đắng chát của mình, độc giả tiếp tục khóc cười với cuốn tiểu thuyết thành công nhất của nhà văn José Mauro de Vasconcelos (1920 - 1984) - Cây cam ngọt của tôi (Nguyễn Bích Lan - Tô Yến Ly dịch, Nhã Nam và Nhà xuất bản Hội Nhà Văn).\n" +
                "\n" +
                "Cuốn sách xuất bản lần đầu năm 1968 ở Brazil, khi cơn sốt văn chương châu Mỹ Latin đang lan dần ra toàn thế giới.\n" +
                "\n" +
                "Nếu Hảo hán nơi trảng cát của Jorge Amado (1912 - 2002) là cuộc sống của những đứa trẻ mồ côi ở Salvador thì Cây cam ngọt của tôi là cuộc đời của những con người trong khu xóm nghèo ở thủ đô Rio de Janeiro.\n" +
                "\n" +
                "Ở đó, những người lớn tất tả trong cuộc mưu sinh bỏ quên chú bé Zezé nghịch ngợm chật vật trong thế giới tuổi thơ của mình. Trước một thế giới ảm đạm buồn chán, Zezé phải bày đủ trò quậy phá đến mức bị đòn roi.\n" +
                "\n" +
                "Zezé lấy trí tưởng tượng để làm vũ khí chống lại thế giới người lớn quay cuồng trong tiền bạc nhưng thiếu vắng hạnh phúc và ước mơ. Cậu đặt tên cho cây cam trong vườn là Minguinho và như hai người bạn, chúng trò chuyện với nhau, cùng nhau bước qua một tuổi thơ khốn khó nhưng không tuyệt vọng.\n" +
                "\n" +
                "Câu chuyện đơn giản nhuốm màu sắc tự truyện vẫn khiến cuốn sách gặt hái được thành công quốc tế, trở thành một tác phẩm mang tính giáo dục cao, dẫu tác giả không tuyên ngôn dưới bất kỳ hình thức nào.\n" +
                "\n" +
                "Tác phẩm được đưa vào chương trình tiểu học, chuyển thể thành phim và cách đây không lâu trở thành nguồn cảm hứng sáng tạo cho một ngôi sao K-pop.\n" +
                "\n" +
                "Thời đại bất an mà nhà văn sống là thời đại của những nhà độc tài, những cuộc cách mạng, nội chiến liên miên.\n" +
                "\n" +
                "\n" +
                "\n" +
                "Cho nên tác phẩm không chỉ là hành trình hướng thiện của một đứa trẻ mà còn là cuộc chiến thu nhỏ diễn ra ở chốn tận cùng, nơi con người chống lại sự tàn nhẫn của cuộc đời để bảo vệ sự ngây thơ của thế giới.");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/414cdecac5e1b3dd08f34827bdbca1c7");
        variant_1.setWeight(120.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(500));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    //
    @Test
    public void testProduct_802774_1() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách 300 Miếng Bóc Dán Thông Minh");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/1e4af2c55e7354b2dea34a1fb8615039");
        images.add("https://cf.shopee.vn/file/b4a134e6a2d3372bd40fa3125b0bc2e8");
        images.add("https://cf.shopee.vn/file/c9dd53569e97ad2897c7c7e2c353b96a");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/08cfa550ab697507891a62e41d69ae46");
        productDTO.setDescription("300 Miếng Bóc Dán Thông Minh - Bộ 6 quyển\n" +
                "\n" +
                "3-6 tuổi là giai đoạn bộ não của trẻ phát triển nhanh nhất để có thể nhận thức được màu sắc, hình khối, đồ vật, phát triển mạnh mẽ về cả ngôn ngữ, tư duy logic và khả năng sáng tạo... Cũng bởi thế mà trẻ cần có những \"công cụ\" phù hợp để có thể nâng cao được toàn diện những kỹ năng của mình. Bộ sách 300 miếng bóc dán thông minh là một \"thiết kế\" đặc biệt phù hợp cho trẻ ở độ tuổi này. Bộ sách gồm 6 cuốn, bao hàm tổng quát các lĩnh vực phát triển tư duy cần thiết, sẽ giúp trẻ:\n" +
                "\n" +
                "\n" +
                "- Phát huy tối đa khả năng nhận biết thế giới xung quanh, các hình khối, đồ vật.\n" +
                "- Nâng cao khả năng sáng tạo và tư duy logic, giúp bé tạo ra những \"tác phẩm\" độc đáo mang cá tính riêng.\n" +
                "- Nhận biết và phân biệt được chính xác giữa bộ phận cơ thể người và động vật\n" +
                "- Rèn luyện khả năng ngôn ngữ linh hoạt, biết vận dụng ngôn ngữ thích hợp trong mỗi tình huống.\n" +
                "- Rèn luyện thói quen sinh hoạt nề nếp và khoa học.\n" +
                "\n" +
                "\n" +
                "~~~~~~\n" +
                "Công ty phát hành        Đinh Tị\n" +
                "Tác giả        Ưu Ưu Thử\n" +
                "Ngày xuất bản        3/2021\n" +
                "Kích thước        24 x 17 cm\n" +
                "Nhà xuất bản        NXB Thanh Niên\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        108");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/08cfa550ab697507891a62e41d69ae46");
        variant_1.setWeight(150.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(300));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_2() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách 365 Chuyện Kể Trước Giờ Đi Ngủ (Combo 2 quyển)");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/1cb376e86d8e40a519aeb2c7f9d6da6b");
        images.add("https://cf.shopee.vn/file/c043637549dd90dba18a6df47fe7b12e");
        images.add("https://cf.shopee.vn/file/dbf944d9ac2fc1914a766033a91b539d");
        images.add("https://cf.shopee.vn/file/38c0458c8d186c7889e65aad13cec447");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/a22e03d0f31cd4597a490806ccad35e5");
        productDTO.setDescription("Mỗi buổi tối trước giờ đi ngủ, các bậc cha mẹ đừng quên đọc truyện cho con em mình. Những câu chuyện có nội dung hay, nhân vật lại vô cùng gần gũi, cuối mỗi truyện lại có mục. “Bài học nhỏ” - nhẹ nhàng mà sâu sắc chắc chắn sẽ giúp các em trở nên thông minh hơn, lương thiện hơn, biết quan tâm, yêu thương mọi người, yêu cuộc sống và có sức sáng tạo. Mỗi ngày cha mẹ cùng trẻ đọc một câu chuyện, những điều tiếp thu được sẽ là tài sản tinh thần to lớn giúp ích cho các em trong suốt cuộc đời.\n" +
                "***\n" +
                "365 Chuyện Kể Trước Giờ Đi Ngủ gồm 2 quyển:\n" +
                "- Những Câu Chuyện Phát Triển Chỉ Số Tình Cảm EQ\n" +
                "- Những Câu Chuyện Phát Triển Chỉ Số Thông Minh IQ\n" +
                " \n" +
                "-----\n" +
                "Công ty phát hành        Đinh Tị\n" +
                "Nhà xuất bản        Nhà Xuất Bản Thế Giới\n" +
                "Phiên bản        2017\n" +
                "Kích thước        13 x 20.5 cm\n" +
                "Tác giả        Ngọc Linh (biên soạn)\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        244 trang/ 1 quyển * 2 quyển\n" +
                "Ngày xuất bản        04-2017");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/a22e03d0f31cd4597a490806ccad35e5");
        variant_1.setWeight(300.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(700));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_3() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Đồng dao, thơ, truyện cho bé tập nói");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/e401dffd894cad277ca99a7baccf53db");
        images.add("https://cf.shopee.vn/file/81530dfac0159064aa25fc9009172d05");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/33752b698d9fc316fc29eb52683be053");
        productDTO.setDescription("Sách Đồng dao, thơ, truyện cho bé tập nói \n" +
                "\n" +
                "Ngay từ năm đầu đời, bé đã có thể bập bẹ những tiếng nói đầu tiên, theo thời gian ngôn ngữ của bé càng được hoàn thiện và phát triển. Đọc sách cho bé nghe để khuyến khích bé tập nói là một trong những phương pháp tối ưu và hữu dụng nhất giúp bé phát triển ngôn ngữ\n" +
                "\n" +
                "\n" +
                "ĐỒNG DAO: Là thơ ca truyền miệng dân gian, có nhịp, có vần. Trẻ em nhỏ chưa hiểu lời, thích đồng dao vì chỉ cần mẹ đọc cho nghe, vé dậm chậm chân, vỗ vỗ tay theo nhịp.Bé lớn hơn càng thích đồng dao vì các bé có thể vừa hát đồng dao, vừa chơi các trò chơi gắn với đồng dao...\n" +
                "\n" +
                "THƠ: Tuyển chọn những bài thơ hay, ngắn và ngộ nghĩnh theo chủ để:\n" +
                "\n" +
                "TRUYỆN: Gồm những câu truyện ngắn, nhỏ, dễ đọc, dễ hiểu nội dung phong phú, thông qua mỗi câu truyện là một bài học nho nhỏ, giúp bé rèn luyện tính cách tốt. Dung lượng chữ trong mỗi truyện phù hợp với sự tiếp nhận của các bé ở giai đoạn tập nói và phát triển ngôn ngữ.\n" +
                "\n" +
                "Những cuốn sách minh họa sinh động, không chỉ giúp bé phát triển ngôn ngữ đúng theo tiêu chí : PHÁT TRIỂN NGÔN NGỮ TỪ TRONG NÔI - mà còn giúp bé ngày càng hiểu hơn về thế giới xung quanh mình!\n" +
                "Sau mỗi phần đều có mục trò chơi rèn luyện tư duy và tập kể chuyện theo tranh!\n" +
                "\n" +
                "\n" +
                "Cty phát hành Đại Mai\n" +
                "Tác giả        Hải Minh, T-Books minh họa\n" +
                "Ngày xuất bản        01-2019\n" +
                "Kích thước        20,5x28,5\n" +
                "Nhà xuất bản        Nhà Xuất Bản Phụ Nữ\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        128");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/33752b698d9fc316fc29eb52683be053");
        variant_1.setWeight(150.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(320));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_4() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Ehon Kĩ Năng Sống Miu Miu Tự Lập Combo 6 quyển (16 tuổi)");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/d0a9b8b4086cf36a1f2fedbe71ea38a1");
        images.add("https://cf.shopee.vn/file/99ff514e0d654a6b3041827876bce01f");
        images.add("https://cf.shopee.vn/file/c3737d639b5605e4b1e4c67f71acd2f0");
        images.add("https://cf.shopee.vn/file/f5f40093428713af0c7b92501cee646e");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/2bb9a4138e7f56201cc45d89b3b8cb7e");
        productDTO.setDescription("Ehon Kĩ Năng Sống - Miu Miu Tự Lập - Combo 6 quyển (1-6 tuổi)\n" +
                "\n" +
                "Trọn bộ Miu Miu Tự Lập gồm 6 quyển:\n" +
                "- Miu Miu Tự Lập - Đánh Răng (1-6 tuổi)\n" +
                "- Miu Miu Tự Lập - Đi Tắm (1-6 tuổi)\n" +
                "- Miu Miu Tự Lập - Mặc Quần Áo (1-6 tuổi)\n" +
                "- Miu Miu Tự Lập - Đi Ị (1-6 tuổi)\n" +
                "- Miu Miu Tự Lập - Xúc Cơm Ăn (1-6 tuổi)\n" +
                "- Miu Miu Tự Lập - Thay Răng Sữa (1-6 tuổi)\n" +
                "\n" +
                "Miu Miu là bộ sách giúp con hình thành thói quen tự lập rất nên đọc ngay khi bé lên 2 tuổi, là những công việc đơn giản hàng ngày như đánh răng, tự xúc cơm ăn, tự con mặc quần áo. Từng công việc nhỏ thôi nhưng góp phần rất lớn đến việc hình thành tính cách độc lập của con sau này!\n" +
                "\n" +
                "Khi bé đọc sách Miu Miu, bé sẽ đồng nhất hoá mình là bạn Miu Miu và sẽ hành động y chang bạn Miu Miu vì bé cảm nhận thấy Miu Miu là nhân vật được ba mẹ yêu quý. Những bài học về kỹ năng sống đơn giản qua những trang sách Ehon mà bé học được từ bạn Miu Miu sẽ nuôi dưỡng tâm hồn bé.\n" +
                "\n" +
                "\n" +
                "-*-*-*-*-*-\n" +
                "Công ty phát hành        CÔNG TY CỔ PHẦN MUKI VIỆT NAM\n" +
                "Tác giả        Nhiều tác giả \n" +
                "Ngày xuất bản        02-2020\n" +
                "Kích thước        18 x 18 cm\n" +
                "Nhà xuất bản        NXB Phụ Nữ Việt Nam\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        28 trang/ 1 quyển * 6 quyển");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/2bb9a4138e7f56201cc45d89b3b8cb7e");
        variant_1.setWeight(450.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(720));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_5() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách - Ehon Kĩ Năng Sống - Miu Bé Nhỏ - Bộ 8 quyển (1-6 tuổi) ");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/f50c332fc5401d2e59a1c3b7be9718e3");
        images.add("https://cf.shopee.vn/file/86d0f1df1f3697311aae47ed94d30be7");
        images.add("https://cf.shopee.vn/file/089871b0a2907193fb6179c201e327d1");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/e8f280723c41f0fa3e68d0eedbc3b4f9");
        productDTO.setDescription("Ehon Kĩ Năng Sống - Miu Bé Nhỏ - Combo 8 quyển (1-6 tuổi) + Tặng ngay 1 bạn Miu nhồi bông xinh xắn số lượng có hạn:\n" +
                "\n" +
                "Duy nhất chỉ có 1000 Bản Đặc biệt do Thanh Hà Books độc quyền phát hành, các bé sẽ được tặng kèm 1 bạn Miu nhồi bông xinh xắn dễ thương.\n" +
                "\n" +
                "Chỉ có 1000 bạn Miu nhồi bông xinh xắn dễ thương tặng cho 1000 bộ đầu tiên. Do số lượng quà tặng có hạn, nên các ba mẹ hãy nhanh tay nhé!\n" +
                "\n" +
                "\n" +
                "GIỚI THIỆU:\n" +
                "\n" +
                "Trẻ nhỏ lớn lên cần rất nhiều sự giúp đỡ và giáo dục của ba mẹ, nếu ba mẹ lơ đãng không đồng hành cùng con trên chặng đường phát triển này thì rất có thể các bé sẽ trở thành những người xấu trong tương lai. Vậy nên trẻ nhỏ rất cần sự sát cánh của ba mẹ.\n" +
                "\n" +
                "Bộ sách này là những “bức tranh” vẽ lại khung cảnh hỗn loạn và không thiếu sự hài hước của bạn Miu và cũng chính là hình ảnh của rất nhiều các bạn nhỏ. Với màu sắc cuốn sách nổi bật, hình vẽ thú vị và có nét nguệch ngoạc “hơi trẻ con” tạo sự gần gũi với các em nhỏ. Qua bộ sách, bé sẽ học được cách giúp đỡ người khác, giải quyết được tình huống sao cho giống được bạn “Miu bé nhỏ”.\n" +
                "\n" +
                "Nhắc lại cho con về những hành động ấy và sẽ điều chỉnh dần dần thói quen xấu của con bố mẹ nhé!!!\n" +
                "\n" +
                "Trọn bộ 8 quyển bao gồm:\n" +
                "\n" +
                "- Đừng Khóc Nhè Nhé!\n" +
                "- Đừng Ăn Vạ Nhé!\n" +
                "- Đừng Thức Khuya Nhé!\n" +
                "- Đừng Mút Tay Nhé!\n" +
                "- Đừng Đánh Chừa Nhé!\n" +
                "- Đừng Xem Tivi Nhiều Nhé!\n" +
                "- Đừng Tè Bậy Nhé!\n" +
                "- Đừng Ị Đùn Nhé!\n" +
                "\n" +
                "Đặc biệt khi mua bộ sách này các bé sẽ được tặng kèm 1 chú gấu bông. Số lượng quà tặng có hạn nên ba mẹ hãy nhanh tay nhé!\n" +
                "\n" +
                "------\n" +
                "Công ty phát hành        CÔNG TY CỔ PHẦN MUKI VIỆT NAM\n" +
                "Tác giả        Nhiều tác giả \n" +
                "Ngày xuất bản        04-2020\n" +
                "Kích thước        20 x 20 cm\n" +
                "Nhà xuất bản        NXB Phụ Nữ Việt Nam\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        214 trang/ Bộ 8 quyển");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/e8f280723c41f0fa3e68d0eedbc3b4f9");
        variant_1.setWeight(750.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(1200));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_6() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách - Ehon Kích Thích Thị Giác - Song Ngữ - Black and White books ");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a7561af53961e0c05154ec38c73c4b4b");
        images.add("https://cf.shopee.vn/file/f18d564816c029d522591d206ac1574a");
        images.add("https://cf.shopee.vn/file/ce0c180287a256bcffbefe38009a8606");
        images.add("https://cf.shopee.vn/file/e11e16dad26136100912ae5362f1d944");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/6e0d8c108524f0e737d7d4b1aebca5ee");
        productDTO.setDescription("Ehon Kích Thích Thị Giác - Song Ngữ - Black and White books (Bộ 6 quyển) gồm các chủ đề:\n" +
                "\n" +
                "- Chúc Bé Ngủ Ngon\n" +
                "- Xin Chào Các Bạn Động Vật\n" +
                "- Xin Chào Các Bạn Hình Khối\n" +
                "- Xin Chào Các Bạn Sinh Vật Bé Nhỏ\n" +
                "- Xin Chào Các Bạn Sinh Vật Biển\n" +
                "- Xin Chào Các Bạn Phương Tiện Giao Thông\n" +
                "\n" +
                "Việc nâng cao khả năng ghi nhớ vốn từ tiếng anh là bước đệm vô cùng quan trọng, hiểu được điều này, Black and White book sẽ giúp bé ghi nhớ cực nhanh và làm quen với rất nhiều chủ đề gần gũi thân thuộc\n" +
                "Hình ảnh kèm theo sinh động, hấp dẫn theo từng chủ đề giúp bé dễ dàng nhận biết, tạo cho bé sự hào hứng khi bắt đầu bài học.\n" +
                "Việc ghi nhớ từ vựng chưa bao giờ dễ dàng hơn thế. Hãy để Black and White book trở thành người bạn đồng hành cùng bé yêu học tiếng anh ngay từ những từ ngữ đầu tiên.\n" +
                "\n" +
                "_______ _ _ _ _ _______\n" +
                "Công ty phát hành        CÔNG TY CỔ PHẦN MUKI VIỆT NAM\n" +
                "Tác giả        Kawa\n" +
                "Kích thước        20 x 20 cm\n" +
                "Nhà xuất bản        NXB Phụ Nữ Việt Nam\n" +
                "Loại bìa        Bìa mềm\n" +
                "Minh họa        Mai Anh Đỗ\n" +
                "Ngày xuất bản        07-2020\n" +
                "Số trang        24 trang/1q * 6 quyển");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/6e0d8c108524f0e737d7d4b1aebca5ee");
        variant_1.setWeight(600.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(900));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_7() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Ehon Âm Thanh Màu Sắc Quanh Bé 0 3 Tuổi ");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/a6b93b2cfd855cf6869cf1a41d7ffcad");
        images.add("https://cf.shopee.vn/file/7ff29e56e82b6c5072c91c5e02631f95");
        images.add("https://cf.shopee.vn/file/dc592433c429a5b2598eec1022211fa4");
        images.add("https://cf.shopee.vn/file/e64056ade75eed61ab06df95d11f2402");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/f5f952d2c6dd7c9ba8931a3d5fb542e8");
        productDTO.setDescription("Ehon Âm Thanh - Màu Sắc Quanh Bé  0 - 3 Tuổi (lẻ tùy chọn)\n" +
                "\n" +
                "A.        Ehon Âm Thanh (Bộ 3 quyển)\n" +
                " “Cáo Ken và Đồ Vật\" mang tên “Lộp bộp lộp bộp”, gợi ra những âm thanh vô cùng “bắt tai” khi bạn cáo Ken sử dụng các đồ vật thường ngày: Gõ cánh cửa sẽ phát ra tiếng “cốc cốc cốc”, chiếc ô hứng mưa sẽ kêu “lộp bộp lộp bộp”, máy ảnh kêu “tách tách”, còn ô tô khởi động kêu “bờ-rừn, bờ-rừn”,… Mỗi đồ vật đều có những tiếng kêu đặc trưng giúp trẻ phân biệt được mà không cần phải ngước mắt lên nhìn. \n" +
                "\n" +
                "Công ty phát hành        Wabooks\n" +
                "Tác giả        Fuku Mitsu\n" +
                "Ngày xuất bản        03-2019\n" +
                "Kích thước        20 x 20 cm\n" +
                "Nhà xuất bản        Nhà Xuất Bản Lao Động - Xã Hội\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        69\n" +
                "\n" +
                "\n" +
                "B. Ehon Âm Thanh Quanh Bé (0 - 6 tuổi) - Bộ 3 quyển\n" +
                "- Ehon Âm Thanh Quanh Bé - Tùng Tùng (0 - 6 tuổi)\n" +
                "- Ehon Âm Thanh Quanh Bé - Leng Keng (0 - 6 tuổi)\n" +
                "- Ehon Âm Thanh Quanh Bé - Ùm Bò (0 - 6 tuổi)\n" +
                "Nội dung đơn giản mà sâu sắc được thể hiện bằng những hình vẽ vô cùng đáng yêu ngộ nghĩnh cùng với những mảng màu lớn, chắc chắn sẽ khiến các bé vô cùng thích thú và hào hứng theo dõi. Hứa hẹn đây sẽ là một bộ sách gối đầu giường của bất kỳ bạn nhỏ nào đang trong độ tuổi lên 0 - 6 đấy. \n" +
                "Công ty phát hành        Wabooks\n" +
                "Tác giả        Fuku mitsu \n" +
                "Ngày xuất bản        2020\n" +
                "Kích thước        20 x 20 cm\n" +
                "Nhà xuất bản        NXB Lao Động Xã Hội\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        28 trang/ 1 quyển\n" +
                "\n" +
                "C. Ehon Nhận Biết - Chơi Cùng Các Âm Thanh (1-6 tuổi) - Bộ 4 quyển bao gồm:\n" +
                "1. Ehon Nhận Biết - Chơi Cùng Các Âm Thanh - Gâu Gâu, Quác Quác (1-6 tuổi)\n" +
                "2. Ehon Nhận Biết - Chơi Cùng Các Âm Thanh - Tu Tu Bíp Bíp (1-6 tuổi)\n" +
                "3. Ehon Nhận Biết - Chơi Cùng Các Âm Thanh - Ì Oạp Choang (1-6 tuổi)\n" +
                "4. Ehon Nhận Biết - Chơi Cùng Các Âm Thanh - Oa Oa Tõm Tõm (1-6 tuổi)\n" +
                "\n" +
                "Bộ sách \"Ehon Nhận Biết - Chơi Cùng Các Âm Thanh\" đã ra đời và làm bạn đồng hành cùng các bé, qua cuốn sách bé được làm quen với những âm thanh rất đỗi quen thuộc trong cuộc sống, bé sẽ cảm thấy hào hứng khi chính những âm thanh bình thường lại được thể hiện thú vị và sinh động qua cuốn sách như tiếng kêu của các loại động vật, âm thanh của các loại phương tiện, hay âm thanh của các hoạt động sinh hoạt hàng ngày....\n" +
                "Công ty phát hành        CÔNG TY CỔ PHẦN MUKI VIỆT NAM\n" +
                "Tác giả        Kawa\n" +
                "Minh họa        Minh Trang\n" +
                "Ngày xuất bản        07-2020\n" +
                "Kích thước        20 x 20 cm\n" +
                "Nhà xuất bản        NXB Phụ Nữ Việt Nam\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        28 trang/ 1q * 4\n" +
                "\n" +
                "D. Ehon Kể Chuyện - Tuti Tuti (1-6 tuổi) - Bộ 3 quyển bao gồm\n" +
                "\n" +
                "1. Ehon Kể Chuyện - Tuti Tuti - Cục Ị Thối, Úm Ba La (1-6 tuổi)\n" +
                "2. Ehon Kể Chuyện - Tuti Tuti - Bãi Tè, Roong Roong (1-6 tuổi)\n" +
                "3. Ehon Kể Chuyện - Tuti Tuti - Rắm, Bủm Bủm (1-6 tuổi)\n" +
                "Bộ sách Ehon Kể Chuyện Tuti - Tuti kể về các hoạt động bình thường của bé như \"đi ị\", \"tè dầm\", \"đánh rắm\", nhưng với một câu chuyện thú vị và độc đáo hơn. Các bé sẽ được học cách vệ sinh sao cho đúng cách và giữ vệ sinh cho mình. Bố mẹ có thể cùng đọc sách với trẻ, thông qua đó giáo dục trẻ nhận thức những điều ấy là không xấu và khuyến khích bé đi vệ sinh sao cho đúng\n" +
                "Màu sắc cuốn sách nổi bật, hình vẽ thú vị và có nét nguệch ngoạc “hơi trẻ con” tạo sự gần gũi với các em nhỏ, tạo sự thân mật hơn đối với trẻ\n" +
                "Công ty phát hành        CÔNG TY CỔ PHẦN MUKI VIỆT NAM\n" +
                "Tác giả        Kawa\n" +
                "Họa sĩ         Chây\n" +
                "Ngày xuất bản        07-2020\n" +
                "Kích thước        20 x 20 cm\n" +
                "Nhà xuất bản        NXB Phụ Nữ Việt Nam\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        28 trang/ 1q * 3\n" +
                "\n" +
                "E. Ehon Màu Sắc Quanh Ta (Bộ 4 quyển) (3-6 tuổi)\n" +
                "Giới thiệu bộ sách:\n" +
                "Trọn bộ Ehon “Màu sắc quanh ta” gồm 4 quyển:\n" +
                "- Cả Nhà Giống Nhau\n" +
                "- Vịt Con Goro Sặc Sỡ\n" +
                "- Màu Sắc Chơi Trốn Tìm\n" +
                "- Nếu Mặt Trăng Có Thể Đổi Màu\n" +
                "\n" +
                "Công ty phát hành        Wabooks\n" +
                "Tác giả        Heian Akira\n" +
                "Ngày xuất bản        02-2020\n" +
                "Kích thước        20 x 20 cm \n" +
                "Nhà xuất bản        Nhà xuất bản Lao Động Xã Hội\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        24 trang/ 1 quyển * 4 quyển\n" +
                "\n" +
                "F.  Ehon Nhận Biết - Đoán Dấu Chân Xinh - Tớ Là Ai? - Combo 3 quyển (Song ngữ Anh - Việt cho bé 1-6 tuổi)\n" +
                "Đoán dấu chân xinh BỘ SÁCH KÍCH THÍCH THỊ GIÁC & PHÁT TRIỂN TƯ DUY DÀNH CHO BÉ TỪ 0 - 6 TUỔI \n" +
                "Combo gồm 3 quyển, mỗi 1 quyển gồm 3 phần:\n" +
                "+ Mở đầu là \"Hướng dẫn cách đọc Picture Book khoa học cho bé\"\n" +
                "+ Phần 2: Truyện tranh song ngữ về dấu chân của các loài động vật\n" +
                "+ Phần 3: Hiểu biết vui\n" +
                "1. Tập 1:\n" +
                "Với tập đầu tiên này tác giả Ota Umme giới thiệu với các bé những đặc điểm dấu chân của các loài động vật quen thuộc như voi, mèo, gấu trúc,... \n" +
                "2. Tập 2: \n" +
                "Tiếp nối với tập 1, tập 2 mang đến cho các bé những dấu chân vô cùng đáng yêu của các bạn sư tử, bò sữa, cô bé,...\n" +
                "\"Chúa tể rừng xanh\n" +
                "Gầm vang khắp chốn\n" +
                "Dấu chân để lại\n" +
                "Biết tớ là ai? \n" +
                "- Tớ là Sư tử (À uồm! À uồm! À uồm!)\"\n" +
                "3. Tập 3:\n" +
                "Kết thúc bộ sách \"Đoán dấu chân xinh\" là cuốn sách tập 3 này với những dấu chân vô cùng đặc biệt và mới lạ của các bạn chuột túi,vịt, trẻ em,... \n" +
                "\"Bốn chân móng guốc\n" +
                "Nước đại tớ phi\n" +
                "Vang tiếng \"Hí! Hí!\"\n" +
                "Nhìn dấu chân đi\n" +
                "Tớ là ai thế?\n" +
                "- Tớ là Ngựa (Hí! Hí! Hí!)\"\n" +
                "\n" +
                "Công ty phát hành        CÔNG TY CỔ PHẦN MUKI VIỆT NAM\n" +
                "Tác giả        Ota Umme\n" +
                "Ngày xuất bản        08-2019\n" +
                "Kích thước        19 x 20.3 cm\n" +
                "Nhà xuất bản        NXB Phụ Nữ \n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        32 trang/ 1 quyển * 3 quyển");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/f5f952d2c6dd7c9ba8931a3d5fb542e8");
        variant_1.setWeight(500.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(750));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_8() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách 10 vạn câu hỏi vì sao (Bộ 5q) Huy Hoàng");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/9a119ba3c57e75e7683eea0e086a7609");
        images.add("https://cf.shopee.vn/file/41c3ca08f296d7cb731a27eec45b2531");
        images.add("https://cf.shopee.vn/file/45fdfeb35a7b1f92d342875e83878333");
        images.add("https://cf.shopee.vn/file/4f90082991919d81dfc5585232fe2f27");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/0437cbc06457377fe598e3fc9f2fe51b");
        productDTO.setDescription("10 vạn câu hỏi vì sao (Bộ 5q) - Huy Hoàng\n" +
                "\n" +
                "Đứng trước thế giới với bao kì diệu, mang trong mình sự tò mò, khát khao tìm hiểu, câu hỏi thường thấy nhất ở trẻ là \"Vì sao?\". \"Vì sao phải hít thở?\", \"Vì sao Vịt có thể bơi trên mặt nước?\"… Quả thực, những câu hỏi \"Vì sao?\" đó khiến người lớn chúng ta cũng khó mà trả lời để con trẻ hiểu được.\n" +
                "\n" +
                "Bộ sách 10 Vạn Câu Hỏi Vì Sao sẽ giúp các bậc phụ huynh tháo gỡ những khúc mắc của trẻ. Bộ sách gồm:\n" +
                "\n" +
                "10 Vạn Câu Hỏi Vì Sao - Thực Vật\n" +
                "10 Vạn Câu Hỏi Vì Sao - Động Vật\n" +
                "10 Vạn Câu Hỏi Vì Sao - Con Người\n" +
                "10 Vạn Câu Hỏi Vì Sao - Vũ Trụ Kỳ Bí\n" +
                "10 Vạn Câu Hỏi Vì Sao - Bí Ẩn Quanh Ta.\n" +
                "0-0-0-0\n" +
                "Công ty phát hành        Huy Hoàng Bookstore\n" +
                "Tác giả        Đức Anh\n" +
                "Ngày xuất bản        08-2016\n" +
                "Kích thước        13.5 x 20.5 cm\n" +
                "Nhà xuất bản        Nhà Xuất Bản  Dân Trí\n" +
                "Loại bìa        Bìa mềm\n" +
                "Số trang        962 trang/5 quyển");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/0437cbc06457377fe598e3fc9f2fe51b");
        variant_1.setWeight(500.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(850));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_9() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách 200 Miếng bóc dán thông minh 2-6 tuổi");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/4b794e7261dd70652efbd07df7a3e9cb");
        images.add("https://cf.shopee.vn/file/ccc3f54c09c48feab02e85e8113f0450");
        images.add("https://cf.shopee.vn/file/9a6c6eedbec48a9b0de1d0efc0643b36");
        images.add("https://cf.shopee.vn/file/ffc85be3a5609f9e26ed2a39c7e422d6");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/6fac2b24476ac714301113a01e902146");
        productDTO.setDescription("200 Miếng bóc dán thông minh 2-6 tuổi gồm 12 cuốn:\n" +
                "1.        200 Miếng bóc dán thông minh 2-6 tuổi - Nhận biết rau củ quả\n" +
                "2.        200 Miếng bóc dán thông minh 2-6 tuổi - Đồ chơi của bé\n" +
                "3.     200 Miếng bóc dán thông minh 2-6 tuổi - Bé học Toán\n" +
                "4.        200 Miếng bóc dán thông minh 2-6 tuổi - Bé học Tiếng Anh\n" +
                "5.        200 Miếng bóc dán thông minh 2-6 tuổi - Rèn luyện ngôn ngữ\n" +
                "6.        200 Miếng bóc dán thông minh 2-6 tuổi - Phát triển chỉ số Tình cảm EQ\n" +
                "7.        200 Miếng bóc dán thông minh 2-6 tuổi - Phát triển chỉ số Thông minh IQ\n" +
                "8.        200 Miếng bóc dán thông minh 2-6 tuổi - Bé làm quen với Toán\n" +
                "9.        200 Miếng bóc dán thông minh 2-6 tuổi - Khủng long\n" +
                "10.   200 Miếng bóc dán thông minh 2-6 tuổi - Bảng chữ cái\n" +
                "11.   200 Miếng bóc dán thông minh 2-6 tuổi - Phương tiện giao thông\n" +
                "12.        200 Miếng bóc dán thông minh 2-6 tuổi - Các loài động vật\n" +
                "\n" +
                "Đây là cuốn sách được tuyển chọn và những trò chơi dán hình, giúp cho đôi tay của các em thêm linh hoạt, khéo léo, nhận biết được các hình để bóc và dán cho đúng chỗ...\n" +
                "Trong sách có tặng kèm hơn 80 miếng bóc dán. Xin trân trọng giới thiệu.\n" +
                "-------\n" +
                "Công ty phát hành Đinh Tị\n" +
                "Tác giả Nhiều tác giả\n" +
                "Nhà xuất bản Nhà xuất bản Thanh Niên\n" +
                "Năm xuất bản 2018\n" +
                "Kích thước 25 x 26.3 cm\n" +
                "Số trang 25\n" +
                "Hình thức Bìa Mềm");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/6fac2b24476ac714301113a01e902146");
        variant_1.setWeight(300.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(450));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testProduct_802774_10() {
        List<IndustrialProduct> industrialProducts = null;
        try {
            industrialProducts = productRestController.getListIndustrial();

        } catch (ServiceException e) {
            e.printStackTrace();
        }
        //------1
        CreateProductDTO createProductDTO1 = new CreateProductDTO();
        Product productDTO = new Product();
        productDTO.setName("Sách Mê cung phát triển tư duy");
        productDTO.setShopId(802774);
        productDTO.setTradeMarkId("1703772512386568");
        productDTO.setIndustrialId(productManager.getIndustrialProduct("Nhà sách online").getId());
        productDTO.setIndustrialTypeName("Nhà sách online");
        List<String> images = new ArrayList<>();
        images.add("https://cf.shopee.vn/file/b0464e9f49a7141eb87a5b201ab9c03f");
        images.add("https://cf.shopee.vn/file/bbcf2b661eb2c4d817f3712a5d8c75d3");
        images.add("https://cf.shopee.vn/file/641992cf649024d9ef3c2c37dccfcfe5");
        images.add("https://cf.shopee.vn/file/873ac0740dd05eda747999e77cd9364f");
        images.add("https://cf.shopee.vn/file/74920d1dc7ed8ed537213970415ea2eb");
        images.add("https://cf.shopee.vn/file/dbe6365a4c7dc5b6a5e64339f7da1e5b");
        productDTO.setImageUrls(images);
        productDTO.setFeaturedImageUrl("https://cf.shopee.vn/file/d8a113e578ff642e30864c494b647997");
        productDTO.setDescription("Mê cung phát triển tư duy bao gồm:\n" +
                "\n" +
                "1. Mê cung phát triển tư duy 1\n" +
                "\n" +
                "Công ty phát hành    Pingbooks\n" +
                "Tác giả     Nhiều Tác Giả\n" +
                "Ngày xuất bản    10-2017\n" +
                "Kích thước     21.5 x 25 cm\n" +
                "Nhà xuất bản     Nhà Xuất Bản Phụ Nữ\n" +
                "Loại bìa     Bìa mềm\n" +
                "Số trang: 32 trang\n" +
                "\n" +
                "2. Mê cung phát triển tư duy 2\n" +
                "\n" +
                "Công ty phát hành    Pingbooks\n" +
                "Tác giả     Nhiều Tác Giả\n" +
                "Ngày xuất bản    10-2017\n" +
                "Kích thước     21.5 x 25 cm\n" +
                "Nhà xuất bản     Nhà Xuất Bản Phụ Nữ\n" +
                "Loại bìa     Bìa mềm\n" +
                "Số trang: 64 trang\n" +
                "\n" +
                "3. Mê cung phát triển tư duy 3\n" +
                "\n" +
                "Công ty phát hành    Pingbooks\n" +
                "Tác giả     Nhiều Tác Giả\n" +
                "Ngày xuất bản    10-2017\n" +
                "Kích thước     21.5 x 25 cm\n" +
                "Nhà xuất bản     Nhà Xuất Bản Phụ Nữ\n" +
                "Loại bìa     Bìa mềm\n" +
                "Số trang: 64 trang\n" +
                "\n" +
                "4. Mê cung phát triển tư duy 4\n" +
                "\n" +
                "Công ty phát hành    Pingbooks\n" +
                "Tác giả     Nhiều Tác Giả\n" +
                "Ngày xuất bản    10-2017\n" +
                "Kích thước     21.5 x 25 cm\n" +
                "Nhà xuất bản     Nhà Xuất Bản Phụ Nữ\n" +
                "Loại bìa     Bìa mềm\n" +
                "Số trang: 64 trang\n" +
                "\n" +
                "5. Mê cung phát triển tư duy - Du hành vũ trụ\n" +
                "\n" +
                "Công ty phát hành    Pingbooks\n" +
                "Tác giả     Nhiều Tác Giả\n" +
                "Ngày xuất bản    10-2017\n" +
                "Kích thước     21.5 x 25 cm\n" +
                "Nhà xuất bản     Nhà Xuất Bản Phụ Nữ\n" +
                "Loại bìa     Bìa mềm\n" +
                "Số trang: 64 trang\n" +
                "\n" +
                "6. Mê cung phát triển tư duy - Khám phá thế giới\n" +
                "\n" +
                "Công ty phát hành    Pingbooks\n" +
                "Tác giả     Nhiều Tác Giả\n" +
                "Ngày xuất bản    10-2017\n" +
                "Kích thước     21.5 x 25 cm\n" +
                "Nhà xuất bản     Nhà Xuất Bản Phụ Nữ\n" +
                "Loại bìa     Bìa mềm\n" +
                "Số trang: 64 trang\n" +
                "\n" +
                "Bộ sách Mê Cung Phát Triển Tư Duy đúng như tên gọi của 6 tập sách, giúp trẻ phát triển rất nhiều kĩ năng thông qua việc tìm đường đi trong mê cung:\n" +
                "\n" +
                "+ Khả năng quan sát\n" +
                "+ Khả năng phân tích\n" +
                "+ Rèn luyện tính kiên trì\n" +
                "+ Rèn luyện tinh thần trách nhiệm\n" +
                "\n" +
                "Ngoài ra, bộ sách Mê Cung Phát Triển Tư Duy còn có một số ưu điểm nổi bật:\n" +
                "Củng cố kiến thức về màu sắc. Sách được in màu hoàn toàn giúp trẻ nhận diện được màu sách một cách chính xác và đa dạng. Chủ đề mê cung đa dạng. Từ công viên, nông trại, động vật cho tới các vì sao, sân bay…Mở mang kiến thức về đời sống xung quanh cho trẻ.\n" +
                "\n" +
                "Là bộ sách tương tác tốt giữa cha mẹ và con cái. Bố mẹ có thể hướng dẫn cách chơi hoặc gợi ý cho trẻ khi trẻ cần sự giúp đỡ. Hỗ trợ tốt cho trẻ nhỏ đang trong giai đoạn “tìm hiểu” về thế giới xung quanh\n" +
                "Là bộ sách đa năng cho trẻ. Trẻ có thể tự chơi một mình hoặc chơi theo nhóm, những đứa trẻ sẽ học được cách giúp đỡ lẫn nhau. Và rất nhiều ưu điểm nữa, các mẹ hãy mua sách về để khám phá cùng các con nhé!\n" +
                "\n" +
                "______ _ _ _ _ ______\n" +
                "THANH HÀ BOOKS CAM KẾT\n" +
                "- 100% Sách chuẩn của các NXB, phát hành trong nước. Thanh Hà Books có đầy đủ Giấy tờ chứng minh nguồn gốc xuất xứ, gồm:\n" +
                "\uF0FC Hợp đồng với Nhà xuất bản, phát hành\n" +
                "\uF0FC Giấy bảo lãnh, ủy quyền phân phối chính hãng\n" +
                "\uF0FC Hóa đơn giá trị gia tăng\n" +
                "\uF0FC Chứng chỉ Phát hành xuất bản phẩm\n" +
                "- Sách mới 100%\n" +
                "- Đóng bọc rất cẩn thận bằng Hộp Bìa Carton do Thanh Hà Books sản xuất riêng + Nilon chống thấm nước hoặc Bóng khí chống sốc + Băng keo thương hiệu dán niêm phong\n" +
                "- Đội ngũ nhân sự > 50 người, tư vấn nhiệt tình, hết lòng phục vụ Quý Khách.\n" +
                "- Kho hàng có sẵn cực lớn\n" +
                "- Quy trình đóng gói chuyên nghiệp, xử lý đơn hàng nhanh\n" +
                "- Đảm bảo chất lượng sản phẩm 100% giống mô tả. \n" +
                "- Hình ảnh sản phẩm là ảnh thật do hệ thống Thanh Hà Books tự chụp bằng điện thoại Iphone và giữ bản quyền hình ảnh. (Nghiêm cấm các shop khác sao chép hình ảnh)\n" +
                "- Hoàn tiền nếu sản phẩm không giống với mô tả \n" +
                "- Chấp nhận đổi hàng khi sách không đúng\n" +
                "- Giao hàng trên toàn quốc, nhận hàng trả tiền\n" +
                " \n" +
                "Hỗ trợ đổi trả theo quy định của Shopee: \n" +
                "1. Điều kiện áp dụng (trong vòng 07 ngày kể từ khi nhận sản phẩm)\n" +
                " - Hàng hoá vẫn còn mới, chưa qua sử dụng \n" +
                "- Hàng hoá bị lỗi hoặc hư hỏng do vận chuyển hoặc do nhà sản xuất\n" +
                " 2. Trường hợp được chấp nhận:\n" +
                " - Không đúng sách mà khách hàng đặt\n" +
                "- Không đủ số lượng, không đủ bộ như trong đơn hàng \n" +
                "3. Trường hợp không đủ điều kiện áp dụng chính sách:\n" +
                " - Quá 07 ngày kể từ khi Quý khách nhận hàng\n" +
                " - Gửi lại hàng không đúng mẫu mã, không phải sản phẩm của THANH HÀ BOOKS\n" +
                " - Không thích, không hợp, đặt nhầm mã, nhầm sản phẩm,... \n" +
                "Do màn hình và điều kiện ánh sáng khác nhau, màu sắc thực tế của sản phẩm có thể chênh lệch khoảng 3-5%");
        createProductDTO1.setProduct(productDTO);
        List<ProductVariant> productVariantList = new ArrayList<>();
        ProductVariant variant_1 = new ProductVariant();
        variant_1.setImageUrl("https://cf.shopee.vn/file/d8a113e578ff642e30864c494b647997");
        variant_1.setWeight(360.0);
        DimensionMeasurement dimensionMeasurement = new DimensionMeasurement();
        dimensionMeasurement.setWidth(40);
        dimensionMeasurement.setHeight(10);
        dimensionMeasurement.setLength(100);
        variant_1.setDimension(dimensionMeasurement);
        variant_1.setRequiresShipping(true);
        variant_1.setPrice(MoneyCalculateUtils.getMoney(650));
        variant_1.setColor(ColorProduct.BLACK);
        productVariantList.add(variant_1);
        createProductDTO1.setProductVariants(productVariantList);

        try {
            productRestController.createProductVariant(createProductDTO1);

        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

}
