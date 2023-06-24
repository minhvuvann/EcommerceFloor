E-commerce Website

Đây là một trang web thương mại điện tử được phát triển bằng cách sử dụng Spring Boot RESTful API và ReactJS. Trang web này tương tự như Shopee, cung cấp các chức năng đặt hàng, thanh toán, quản lý sản phẩm và giao dịch.
Các tính năng

    Đăng nhập và xác thực người dùng: Người dùng có thể đăng ký tài khoản mới, đăng nhập và xác thực để sử dụng các chức năng của trang web.
    Trang chủ và tìm kiếm sản phẩm: Hiển thị danh sách sản phẩm phổ biến và cho phép người dùng tìm kiếm sản phẩm theo từ khóa.
    Danh mục sản phẩm: Hiển thị các danh mục sản phẩm để người dùng có thể dễ dàng tìm kiếm và duyệt qua các sản phẩm.
    Chi tiết sản phẩm: Hiển thị thông tin chi tiết về sản phẩm, bao gồm hình ảnh, mô tả và giá cả.
    Giỏ hàng: Cho phép người dùng thêm sản phẩm vào giỏ hàng và quản lý số lượng sản phẩm trong giỏ hàng.
    Thanh toán: Cung cấp giao diện thanh toán an toàn để người dùng có thể thanh toán cho các mặt hàng trong giỏ hàng.
    Quản lý đơn hàng: Người dùng và quản trị viên có thể xem và quản lý đơn hàng đã đặt, bao gồm xác nhận đơn hàng và cập nhật trạng thái giao hàng.
    Hỗ trợ trực tuyến: Cung cấp chức năng chat trực tuyến để người dùng có thể liên hệ và nhận hỗ trợ từ nhân viên dịch vụ khách hàng.

Công nghệ sử dụng

    Backend Framework: Spring Boot (Java)
    RESTful API Development: Spring MVC
    Database: MongoDB
    Frontend Framework: ReactJS
    User Interface: HTML, CSS, JavaScript
    Library:
      Spring data mongoDB.
      Lombok. 
      Jlf4j(Logger).
      Javax mail.
      Springfox(Swagger). 
      ResourceBundle.
      Spring boot  websocket.
      Spring boot security.
      Login Google(firebase).
      Search autoComplete.
      Formik.
    Giao tiếp với API: Axios hoặc Fetch API
    Quản lý trạng thái: Redux hoặc React Context API

Cài đặt và chạy

    Sao chép repository này hoặc tải xuống dưới dạng tệp tin ZIP.
    Mở terminal và di chuyển vào thư mục dự án frontend.
    Cài đặt các gói phụ thuộc bằng lệnh sau:

    Chạy ứng dụng frontend bằng lệnh sau:
      npm start

    Mở terminal và di chuyển vào thư mục dự án backend.
    Cấu hình cơ sở dữ liệu và kết nối trong file application.properties.
    Biên dịch và chạy ứng dụng backend bằng lệnh sau:
      mvn spring-boot:run

    Mở trình duyệt web và truy cập vào địa chỉ http://localhost:3000 để xem trang web.

Hướng dẫn sử dụng

    Mở trang web trong trình duyệt web.
    Đăng ký tài khoản mới hoặc đăng nhập với tài khoản hiện có.
    Duyệt qua các sản phẩm trên trang chủ hoặc sử dụng chức năng tìm kiếm để tìm sản phẩm cụ thể.
    Xem chi tiết sản phẩm và thêm vào giỏ hàng.
    Kiểm tra và chỉnh sửa giỏ hàng của bạn.
    Tiến hành thanh toán và cung cấp thông tin thanh toán.
    Xem lịch sử đơn hàng và trạng thái giao hàng.

