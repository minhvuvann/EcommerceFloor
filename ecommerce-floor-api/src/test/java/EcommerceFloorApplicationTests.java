import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import vn.mellow.ecom.ecommercefloor.EcommerceFloorApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = EcommerceFloorApplication.class)
@AutoConfigureMockMvc
class EcommerceFloorApplicationTests {

    @Test
    void contextLoads() {
    }

}
